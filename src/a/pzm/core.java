package a.pzm;
import java.io.Serializable;
final public class core implements Serializable{
	public static final int op_eof=0xfffff;
	public static final int op_stc=0x40;
	// - - - - -  - -  -- - - - - -  -- - - - - - - -  --  - -- - - -  - - - - - - - - - - - - - - -- 	
	public int core_id,program_counter,instruction,flags,loading_register=-1,call_stack_index,loop_stack_index;
	public int[]registers,call_stack,loop_stack_address,loop_stack_counter,ram,rom;
	public long meter_instructions,meter_frames;
	// - - - - -  - -  -- - - - - -  -- - - - - - - -  --  - -- - - -  - - - - - - - - - - - - - - -- 	
	public core(){}
	public core(final int register_array_size,final int call_stack_size,final int loop_stack_size,final int ram_size,final int rom_size){
		registers=new int[register_array_size];
		call_stack=new int[call_stack_size];
		loop_stack_address=new int[loop_stack_size];
		loop_stack_counter=new int[loop_stack_size];
		ram=new int[ram_size];
		rom=new int[rom_size];
	}
	// - - - - -  - -  -- - - - - -  -- - - - - - - -  --  - -- - - -  - - - - - - - - - - - - - - -- 	
	public void reset(){
		flags=program_counter=instruction=call_stack_index=loop_stack_index=0;
		loading_register=-1;
		if(registers!=null)for(int i=0;i<registers.length;i++)registers[i]=0;
		if(call_stack!=null)for(int i=0;i<call_stack.length;i++)call_stack[i]=0;
		if(loop_stack_address!=null)for(int i=0;i<loop_stack_address.length;i++)loop_stack_address[i]=0;
		if(loop_stack_counter!=null)for(int i=0;i<loop_stack_counter.length;i++)loop_stack_counter[i]=0;
		if(ram!=null)for(int i=0;i<ram.length;i++)ram[i]=0;
		if(rom!=null)for(int i=0;i<rom.length;i++)ram[i]=rom[i];//?
		if(rom!=null)instruction=rom[0];
	}
	private void calls_push(final int v){call_stack[call_stack_index++]=v;}
	private boolean loops_loop_is_done(){return--loop_stack_counter[loop_stack_index-1]==0;}
	private void loops_pop(){loop_stack_index--;}
	private int loops_address(){return loop_stack_address[loop_stack_index-1];}
	private int calls_pop(){return call_stack[--call_stack_index];}
	private void loops_push(final int addr,final int counter){
		loop_stack_address[loop_stack_index]=addr;
		loop_stack_counter[loop_stack_index]=counter;
		loop_stack_index++;
	}
	public void step(){
		meter_instructions++;
		if(loading_register!=-1){// load reg 2 instructions command
			registers[loading_register]=instruction;
			loading_register=-1;
			instruction=rom[++program_counter];
			return;
		}
		if((instruction&0xfffff)==0xfffff){// end of frame
			program_counter=0;
			instruction=rom[0];
			meter_frames++;
			return;
		}
		int in=instruction;// znxr ci.. rraaii rrddii
		final int izn=in&3;
		if((izn!=0&&(izn!=flags))){
			final int op=(in>>5)&127;//? &7 //i.. .... ....
			final int skp=op==0?2:1;//skp 2 for li
			program_counter+=skp;
			instruction=rom[program_counter];
			return;
		}
		in>>=2;// xr ci.. rraaii rrddii
		final int xr=in&0x3;
		final boolean invalid_opcode=(in&6)==6;
		if(!invalid_opcode&&(in&4)==4){//call
			final int imm16=in>>4;// .. ...... ......  (imm14)
			final int znx=flags|((xr&1)<<2);// nxt after ret
			final int stkentry=(znx<<16)|(program_counter+1);
			program_counter=imm16;
			instruction=rom[imm16];
			calls_push(stkentry);
			return;
		}
		boolean isnxt=false;
		boolean program_counter_has_been_set=false;
		if((xr&1)==1){// nxt
			isnxt=true;
			if(loops_loop_is_done()){
				loops_pop();
			}else{
				final int index_in_rom=loops_address();
				program_counter=index_in_rom;
				instruction=rom[index_in_rom];
				program_counter_has_been_set=true;
			}
		}
		boolean isret=false;//? nxt ret bug?
		if(!invalid_opcode&&!program_counter_has_been_set&&(xr&2)==2){// ret after loop complete
			final int stkentry=calls_pop();
			final int ipc=stkentry&0xffff;
			final int znx=(stkentry>>16);
			if((znx&4)==4){// nxt after previous call
				if(loops_loop_is_done()){
					loops_pop();
				}else{
					final int index_in_rom=loops_address();
					program_counter=index_in_rom;
					instruction=rom[index_in_rom];
					program_counter_has_been_set=true;
				}
			}
			if(!program_counter_has_been_set){
				program_counter=ipc;
				instruction=rom[ipc];
				program_counter_has_been_set=true;
			}
			isret=true;
		}
		in>>=3;// i.. rraaii rrddii
		final int op=in&7;
		in>>=3;// rraaii rrddii
		final int imm12=in;
		final int rai=in&0x3f;//? magicnum
		in>>=6;// rrddii
		final int rdi=in&0x3f;
		if(!invalid_opcode){
			if(op==0){//load
				if(rai!=0){//branch
					if(rai==1){//lp
						if(isnxt)throw new Error("unimplmeneted 1 op(x,y)");
						final int d=registers[rdi];
						loops_push(program_counter+1,d);	
					}else if(rai==2){//inc
						registers[rdi]++;	
						evaluate_zn_flags(registers[rdi]);
					}else if(rai==3){//neg
						final int d=registers[rdi];
						final int r=-d;
						registers[rdi]=r;
						evaluate_zn_flags(registers[rdi]);
					}else if(rai==4){//dac
						final int d=registers[rdi];
						try{
							b.b.pl("dac "+d);
//							ev(null,this,new Integer(d));// ev(x,this.dac,int)
						}catch(final Throwable t){
							throw new Error(t);
						}
					}else throw new Error("unimplemented ops(x)");
				}else{
					if(isret||isnxt){
						if(!program_counter_has_been_set){
							instruction=rom[++program_counter];
						}
						return;
					}
					loading_register=rdi;
				}
			}else if(op==1){// sub
				final int a=registers[rai];
				final int d=registers[rdi];
				final int r=a-d;
				evaluate_zn_flags(r);
				registers[rai]=r;
			}else if(op==2){//stc
				final int a=registers[rai]++;
				final int d=registers[rdi];
				ram[a]=d;
//				me.stc++;
			}else if(op==3){//shf and not
				if(rai==0){//not
					final int d=registers[rdi];
					final int r=~d;
					registers[rdi]=r;
				}else{//shf
					final int a=rai>31?rai-64:rai;//? !
					final int r;
					if(a<0)r=registers[rdi]<<-a;
					else r=registers[rdi]>>a;
					registers[rdi]=r;
					evaluate_zn_flags(r);
				}
			}else if(op==4){//skp
				if(imm12==0)throw new Error("unencoded op (rol x)");
				if(program_counter_has_been_set)throw new Error("unimplemented");
				final int index_in_rom=program_counter+imm12;
				program_counter=index_in_rom;
				instruction=rom[index_in_rom];
				program_counter_has_been_set=true;
			}else if(op==5){//add
				final int a=registers[rai];
				final int d=registers[rdi];
				final int r=a+d;
				evaluate_zn_flags(r);
				registers[rai]=r;
			}else if(op==6){//ldc
				final int a=registers[rai]++;
				final int d=ram[a];
				registers[rdi]=d;
				evaluate_zn_flags(d);
//				me.ldc++;
			}else if(op==7){//tx
				final int a=registers[rai];
				registers[rdi]=a;
			}
		}else{
			if(op==0){//free
			}else if(op==1){//skp
				if(imm12==0)throw new Error("unencoded op (rol x)");
				if(program_counter_has_been_set)throw new Error("unimplemented");
				final int index_in_rom=program_counter+imm12;
				program_counter=index_in_rom;
				instruction=rom[index_in_rom];
				program_counter_has_been_set=true;
			}else if(op==2){// free
			}else if(op==3){// free
			}else if(op==4){// free  
			}else if(op==5){// free
			}else if(op==6){// st
				final int d=registers[rdi];
				final int a=registers[rai];
				ram[a]=d;
//				meters_st++;
			}else if(op==7){// ld
				final int a=registers[rai];
				final int d=ram[a];
				registers[rdi]=d;
				evaluate_zn_flags(d);
//				meters_ld++;
			}else throw new Error();
		}
		if(!program_counter_has_been_set){
			final int index_in_rom=program_counter+1;
			program_counter=index_in_rom;
			instruction=rom[index_in_rom];
		}
	}
	private void evaluate_zn_flags(final int number_to_be_evaluated){
		if(number_to_be_evaluated==0){flags=1;return;}
		if((number_to_be_evaluated&(1<<16))==(1<<16)){flags=2;return;}//? .
		flags=3;
	}
	public boolean is_instruction_eof(){
		return loading_register==-1&&(instruction&op_eof)==op_eof;
	}
	private static final long serialVersionUID=1;
}