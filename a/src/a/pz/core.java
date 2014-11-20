package a.pz;
final public class core{
	final public int K=1024,nregs=16,ncalls=8,nloops=8,nram=64*K,nrom=2*K;

	public boolean running,stopped,wait,notify,last_instruction_was_end_of_frame;
	public int pc,ir,zn,loadreg=-1;
	public int[]regs=new int[nregs];
	public int[]calls=new int[ncalls];
	public int calls_i;
	public int[]loopas=new int[nloops];
	public int[]loopcs=new int[nloops];
	public int loops_i;
	public int[]ram=new int[nram];
	public int[]rom=new int[nrom];
	
	public void reset(){
		running=wait=notify=stopped=false;
		zn=pc=ir=calls_i=loops_i=0;
		loadreg=-1;
		for(int i=0;i<regs.length;i++)regs[i]=0;
		for(int i=0;i<calls.length;i++)calls[i]=0;
		for(int i=0;i<loopas.length;i++)loopas[i]=0;
		for(int i=0;i<loopcs.length;i++)loopcs[i]=0;
		for(int i=0;i<ram.length;i++)ram[i]=0;
		for(int i=0;i<rom.length;i++)ram[i]=rom[i];
	}
	public void regs_set(final int ri,int d){regs[ri]=d;}
	public void calls_push(final int v){calls[calls_i++]=v;}
	public boolean loops_nxt(){return--loopcs[loops_i-1]==0;}
	public void loops_pop(){loops_i--;}
	public int loops_address(){return loopas[loops_i-1];}
	public int calls_pop(){return calls[--calls_i];}
	public void loops_push(final int addr,final int counter){
		loopas[loops_i]=addr;
		loopcs[loops_i]=counter;
		loops_i++;
	}
	public void step(){
		if(wait){
			if(notify){
				synchronized(this){wait=notify=false;}
				setpcr(pc+1);
			}else{
				return;
			}
		}
//		me.instr++;
//		if(pcr>=rom.size)throw new Error("program out of bounds");
		if(loadreg!=-1){// load reg 2 instructions command
			regs_set(loadreg,ir);
			loadreg=-1;
			setpcr(pc+1);
			return;
		}
		if(ir==-1){// end of frame
			last_instruction_was_end_of_frame=true;
			setpcr(0);
//			me.frames++;
//			try{ev(null);}catch(Throwable t){throw new Error(t);}
			return;
		}
		int in=ir;// znxr ci.. .rai .rdi
		final int izn=in&3;
		if((izn!=0&&(izn!=zn))){
			final int op=(in>>5)&127;//? &7 //i.. .... ....
			final int skp=op==0?2:1;// ifloadopskip2
			setpcr(pc+skp);
			return;
		}
		in>>=2;// xr ci.. .rai .rdi
		final int xr=in&0x3;
		final boolean rcinvalid=(in&6)==6;
		if(!rcinvalid&&(in&4)==4){//call
			final int imm10=in>>4;// .. .... ....
			final int znx=zn|((xr&1)<<2);// nxt after ret
			final int stkentry=(znx<<12)|(pc+1);
			setpcr(imm10);
			calls_push(stkentry);
			return;
		}
		boolean isnxt=false;
		boolean ispcrset=false;
		if((xr&1)==1){// nxt
			isnxt=true;
			if(loops_nxt()){
				loops_pop();
			}else{
				setpcr(loops_address());
				ispcrset=true;
			}
		}
		boolean isret=false;
		if(!rcinvalid&&!ispcrset&&(xr&2)==2){// ret after loop complete
			final int stkentry=calls_pop();
			final int ipc=stkentry&0xfff;
			final int znx=(stkentry>>12);
			if((znx&4)==4){// nxt after previous call
				if(loops_nxt()){
					loops_pop();
				}else{
					setpcr(loops_address());
					ispcrset=true;
				}
			}
			if(!ispcrset){
				setpcr(ipc);
				ispcrset=true;
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
						final int d=regs[rdi];
						loops_push(pc+1,d);	
					}else if(rai==2){//inc
						regs[rdi]++;	
					}else if(rai==3){//neg
						final int d=regs[rdi];
						final int r=-d;
						regs[rdi]=r;
					}else if(rai==4){//dac
						final int d=regs[rdi];
						try{
							b.b.pl("dav event");
//							ev(null,this,new Integer(d));// ev(x,this.dac,int)
						}catch(final Throwable t){
							throw new Error(t);
						}
					}else throw new Error("unimplemented ops(x)");
				}else{
					if(isret||isnxt){
						if(!ispcrset){
							setpcr(pc+1);
						}
						return;
					}
					loadreg=rdi;
				}
			}else if(op==1){// sub
				final int a=regs[rai];
				final int d=regs[rdi];
				final int r=a-d;
				zneval(r);
				regs[rai]=r;
			}else if(op==2){//stc
				final int d=regs[rdi];
				final int a=regs[rai];
				regs[a]=a;
//				me.stc++;
			}else if(op==3){//shf and not
				if(rai==0){//not
					final int d=regs[rdi];
					final int r=~d;
					regs[rdi]=r;
				}else{//shf
					final int a=rai>7?rai-16:rai;
					final int r;
					if(a<0)r=regs[rdi]<<-a;
					else r=regs[rdi]>>a;
					regs[rdi]=r;
					zneval(r);
				}
			}else if(op==4){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(ispcrset)throw new Error("unimplemented");
				setpcr(pc+imm8);
				ispcrset=true;
			}else if(op==5){//add
				final int a=regs[rai];
				final int d=regs[rdi];
				final int r=a+d;
				zneval(r);
				regs[rai]=r;
			}else if(op==6){//ldc
				final int a=regs[rai]++;
				final int d=ram[a];
				regs[rdi]=d;
				zneval(d);
//				me.ldc++;
			}else if(op==7){//tx
				final int a=regs[rai];
				regs[rdi]=a;
			}
		}else{
			if(op==0){//free
			}else if(op==1){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(ispcrset)throw new Error("unimplemented");
				setpcr(pc+imm8);
				ispcrset=true;
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
				final int imm4=(ir>>12);
				b.b.pl("core notify "+imm4);
//				try{ev(null,this,new Integer(imm4));}catch(Throwable t){throw new Error(t);}
			}else if(op==4){// free  
			}else if(op==5){// sub
			}else if(op==6){// st
				final int d=regs[rdi];
				final int a=regs[rai];
				ram[a]=d;
//				me.stc++;
			}else if(op==7){// ld
				final int a=regs[rai];
				final int d=ram[a];
				regs[rdi]=d;
				zneval(d);
//				me.ldc++;
			}else throw new Error();
		}
		if(!ispcrset)
			setpcr(pc+1);
	}
	private void setpcr(final int i){
		pc=i;
		ir=rom[i];
	}
	private void zneval(final int i){
		if(i==0){zn=1;return;}
		if((i&(1<<16))==(1<<16)){zn=2;return;}//? .
//		if(i<0){zn=2;return;}
		zn=3;
	}

}