package a.pz;
final public class core{
	final public int K=1024,nregs=16,ncalls=8,nloops=8,nram=64*K,nrom=2*K;

	public int id;
	public boolean running,stopped,wait,notify,last_instruction_was_end_of_frame;
	public int program_counter,instruction_register,zn_flags,loading_register=-1;
	public int[]register=new int[nregs];
	public int[]call_stack=new int[ncalls];
	public int call_stack_index;
	public int[]loop_stack_address=new int[nloops];
	public int[]loop_stack_counter=new int[nloops];
	public int loop_stack_index;
	public int[]ram=new int[nram];
	public int[]rom=new int[nrom];
	
	public void reset(){
		running=wait=notify=stopped=false;
		zn_flags=program_counter=instruction_register=call_stack_index=loop_stack_index=0;
		loading_register=-1;
		for(int i=0;i<register.length;i++)register[i]=0;
		for(int i=0;i<call_stack.length;i++)call_stack[i]=0;
		for(int i=0;i<loop_stack_address.length;i++)loop_stack_address[i]=0;
		for(int i=0;i<loop_stack_counter.length;i++)loop_stack_counter[i]=0;
		for(int i=0;i<ram.length;i++)ram[i]=0;
		for(int i=0;i<rom.length;i++)ram[i]=rom[i];
	}
	public void calls_push(final int v){call_stack[call_stack_index++]=v;}
	public boolean loops_loop_is_done(){return--loop_stack_counter[loop_stack_index-1]==0;}
	public void loops_pop(){loop_stack_index--;}
	public int loops_address(){return loop_stack_address[loop_stack_index-1];}
	public int calls_pop(){return call_stack[--call_stack_index];}
	public void loops_push(final int addr,final int counter){
		loop_stack_address[loop_stack_index]=addr;
		loop_stack_counter[loop_stack_index]=counter;
		loop_stack_index++;
	}
	public void step(){
		if(wait){
			if(notify){
				synchronized(this){wait=notify=false;}
				program_counter_set(program_counter+1);
			}else{
				return;
			}
		}
//		me.instr++;
//		if(pcr>=rom.size)throw new Error("program out of bounds");
		if(loading_register!=-1){// load reg 2 instructions command
			register[loading_register]=instruction_register;
			loading_register=-1;
			program_counter_set(program_counter+1);
			return;
		}
		if(instruction_register==-1){// end of frame
			last_instruction_was_end_of_frame=true;
			program_counter_set(0);
//			me.frames++;
//			try{ev(null);}catch(Throwable t){throw new Error(t);}
			return;
		}
		int in=instruction_register;// znxr ci.. .rai .rdi
		final int izn=in&3;
		if((izn!=0&&(izn!=zn_flags))){
			final int op=(in>>5)&127;//? &7 //i.. .... ....
			final int skp=op==0?2:1;// ifloadopskip2
			program_counter_set(program_counter+skp);
			return;
		}
		in>>=2;// xr ci.. .rai .rdi
		final int xr=in&0x3;
		final boolean rcinvalid=(in&6)==6;
		if(!rcinvalid&&(in&4)==4){//call
			final int imm10=in>>4;// .. .... ....
			final int znx=zn_flags|((xr&1)<<2);// nxt after ret
			final int stkentry=(znx<<12)|(program_counter+1);
			program_counter_set(imm10);
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
				program_counter_set(loops_address());
				program_counter_has_been_set=true;
			}
		}
		boolean isret=false;
		if(!rcinvalid&&!program_counter_has_been_set&&(xr&2)==2){// ret after loop complete
			final int stkentry=calls_pop();
			final int ipc=stkentry&0xfff;
			final int znx=(stkentry>>12);
			if((znx&4)==4){// nxt after previous call
				if(loops_loop_is_done()){
					loops_pop();
				}else{
					program_counter_set(loops_address());
					program_counter_has_been_set=true;
				}
			}
			if(!program_counter_has_been_set){
				program_counter_set(ipc);
				program_counter_has_been_set=true;
			}
			isret=true;
		}
		in>>=3;// i.. .rai .rdi
		final int op=in&7;
		in>>=3;// .rai .rdi
		final int imm8=in;
		final int rai=in&0xf;
		in>>=4;// .rdi
		final int rdi=in&0xf;
		if(!rcinvalid){
			if(op==0){//load
				if(rai!=0){//branch
					if(rai==1){//lp
						if(isnxt)throw new Error("unimplmeneted 1 op(x,y)");
						final int d=register[rdi];
						loops_push(program_counter+1,d);	
					}else if(rai==2){//inc
						register[rdi]++;	
					}else if(rai==3){//neg
						final int d=register[rdi];
						final int r=-d;
						register[rdi]=r;
					}else if(rai==4){//dac
						final int d=register[rdi];
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
							program_counter_set(program_counter+1);
						}
						return;
					}
					loading_register=rdi;
				}
			}else if(op==1){// sub
				final int a=register[rai];
				final int d=register[rdi];
				final int r=a-d;
				evaluate_zn_flags(r);
				register[rai]=r;
			}else if(op==2){//stc
				final int d=register[rdi];
				final int a=register[rai];
				register[a]=a;
//				me.stc++;
			}else if(op==3){//shf and not
				if(rai==0){//not
					final int d=register[rdi];
					final int r=~d;
					register[rdi]=r;
				}else{//shf
					final int a=rai>7?rai-16:rai;
					final int r;
					if(a<0)r=register[rdi]<<-a;
					else r=register[rdi]>>a;
					register[rdi]=r;
					evaluate_zn_flags(r);
				}
			}else if(op==4){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(program_counter_has_been_set)throw new Error("unimplemented");
				program_counter_set(program_counter+imm8);
				program_counter_has_been_set=true;
			}else if(op==5){//add
				final int a=register[rai];
				final int d=register[rdi];
				final int r=a+d;
				evaluate_zn_flags(r);
				register[rai]=r;
			}else if(op==6){//ldc
				final int a=register[rai]++;
				final int d=ram[a];
				register[rdi]=d;
				evaluate_zn_flags(d);
//				me.ldc++;
			}else if(op==7){//tx
				final int a=register[rai];
				register[rdi]=a;
			}
		}else{
			if(op==0){//free
			}else if(op==1){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(program_counter_has_been_set)throw new Error("unimplemented");
				program_counter_set(program_counter+imm8);
				program_counter_has_been_set=true;
			}else if(op==2){// wait
				if(!wait){// first time
					synchronized(this){// atomic wait mode
						wait=true;
						notify=false;
					}
					return;
				}
				// after notify
				synchronized(this){wait=notify=false;}
			}else if(op==3){// notify
				final int imm4=(instruction_register>>12);
				b.b.pl("notify "+imm4);
//				try{ev(null,this,new Integer(imm4));}catch(Throwable t){throw new Error(t);}
			}else if(op==4){// free  
			}else if(op==5){// sub
			}else if(op==6){// st
				final int d=register[rdi];
				final int a=register[rai];
				ram[a]=d;
//				me.stc++;
			}else if(op==7){// ld
				final int a=register[rai];
				final int d=ram[a];
				register[rdi]=d;
				evaluate_zn_flags(d);
//				me.ldc++;
			}else throw new Error();
		}
		if(!program_counter_has_been_set)
			program_counter_set(program_counter+1);
	}
	private void program_counter_set(final int index_in_rom){
		program_counter=index_in_rom;
		instruction_register=rom[index_in_rom];
	}
	private void evaluate_zn_flags(final int number_to_be_evaluated){
		if(number_to_be_evaluated==0){zn_flags=1;return;}
		if((number_to_be_evaluated&(1<<16))==(1<<16)){zn_flags=2;return;}//? .
//		if(i<0){zn=2;return;}
		zn_flags=3;
	}

}