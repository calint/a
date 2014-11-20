package a.pz;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import a.x.jskeys;
import b.a;
import b.a_ajaxsts;
import b.xwriter;
final public class zn extends a{
	final static String filenmromsrc="pz.src";
	public rom ro;
	public ram ra;
	public sys sy;
	public regs re;
	public calls ca;
	public loops lo;
	/**statusline*/public a st;
	/**coreid*/public a co;
	public edcrun ec;
	/**theme*/public a th;
	public a bits;
	public final static int bit_logo=1;
	public final static int bit_schematics=2;
	public final static int bit_pramble=4;
	public final static int bit_instructions_table=8;
	public final static int bit_display=16;
	public final static int bit_menu=32;
	public final static int bit_panels=64;
	public final static int bit_rom=128;
	public final static int bit_edasm=256;
	public final static int bit_edcrn=512;
	/**builtinajaxstatus*/public a_ajaxsts ajaxsts;
	public metrics me;

	private boolean running;
	boolean stopped;
	int pc;
	int ir;
	int zn;
	private int loadreg=-1;
	private boolean wait;
	private boolean notify;
	private boolean last_instruction_was_end_of_frame;

	final static int opload=0x000;
	final static int oplp=0x100;
	final static int opinc=0x200;
	final static int opneg=0x300;
	final static int opdac=0x400;
	final static int opwait=0x058;
	final static int opnotify=0x078;
	final static int opset=0xe0;
	final static int opldc=0xc0;
	final static int opadd=0xa0;
	final static int opskp=0x80;
	final static int opshf=0x60;
	final static int opstc=0x40;
	final static int opsub=0x20;
	final static int opcall=0x10;
	final static int opst=0x0d8;//?
	final static int opld=0x0f8;//?
	final static int opnxt=4;
	final static int opret=8;
	
	private void reset(){
		running=false;
		zn=0;
		loadreg=-1;
		setpcr(0);
		ca.rst();
		lo.rst();
		re.rst();
		ra.rst();
		me.rst();
		wait=notify=stopped=false;
	}
	private void copy_rom_to_ram(){
		for(int i=0;i<rom.size;i++)
			ra.set(i,ro.get(i));
	}
	public void snapshot_png_to(final OutputStream os)throws IOException{
		final int wi=ram.wi;
		final int hi=ram.hi;
		final BufferedImage bi=new BufferedImage(wi,hi,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<hi;i++){
			for(int j=0;j<wi;j++){
				final int d=ra.get(k++);
				final int b= (d    &0xf)*0xf;
				final int g=((d>>4)&0xf)*0xf;
				final int r=((d>>8)&0xf)*0xf;
//				final int a=(d>>12)&0xf;
				final int a=0xff;
				final int argb=(a<<24)+(r<<16)+(g<<8)+b;
				bi.setRGB(j,i,argb);
			}
//			k+=0;//skip
		}
		ImageIO.write(bi,"png",os);
//		
//		
//		final byte[]pixels=((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
	}
	private void step(){
		if(wait){
			if(notify){
				synchronized(this){wait=notify=false;}
				setpcr(pc+1);
			}else{
				return;
			}
		}
		me.instr++;
//		if(pcr>=rom.size)throw new Error("program out of bounds");
		if(loadreg!=-1){// load reg 2 instructions command
			re.setr(loadreg,ir);
			loadreg=-1;
			setpcr(pc+1);
			return;
		}
		if(ir==-1){// end of frame
			last_instruction_was_end_of_frame=true;
			setpcr(0);
			me.frames++;
			try{ev(null);}catch(Throwable t){throw new Error(t);}
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
			ca.push(stkentry);
			return;
		}
		boolean isnxt=false;
		boolean ispcrset=false;
		if((xr&1)==1){// nxt
			isnxt=true;
			if(lo.nxt()){
				lo.pop();
			}else{
				setpcr(lo.adr());
				ispcrset=true;
			}
		}
		boolean isret=false;
		if(!rcinvalid&&!ispcrset&&(xr&2)==2){// ret after loop complete
			final int stkentry=ca.pop();
			final int ipc=stkentry&0xfff;
			final int znx=(stkentry>>12);
			if((znx&4)==4){// nxt after previous call
				if(lo.nxt()){
					lo.pop();
				}else{
					setpcr(lo.adr());
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
						final int d=re.get(rdi);
						lo.push(pc+1,d);	
					}else if(rai==2){//inc
						re.inc(rdi);	
					}else if(rai==3){//neg
						final int d=re.get(rdi);
						final int r=-d;
						re.setr(rdi,r);
					}else if(rai==4){//dac
						final int d=re.get(rdi);
						try{
							ev(null,this,new Integer(d));// ev(x,this.dac,int)
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
				final int a=re.get(rai);
				final int d=re.get(rdi);
				final int r=a-d;
				zneval(r);
				re.setr(rai,r);
			}else if(op==2){//stc
				final int d=re.get(rdi);
				final int a=re.getinc(rai);
				ra.set(a,d);
				me.stc++;
			}else if(op==3){//shf and not
				if(rai==0){//not
					final int d=re.get(rdi);
					final int r=~d;
					re.setr(rdi,r);
				}else{//shf
					final int a=rai>7?rai-16:rai;
					final int r;
					if(a<0)r=re.get(rdi)<<-a;
					else r=re.get(rdi)>>a;
					re.setr(rdi,r);
					zneval(r);
				}
			}else if(op==4){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(ispcrset)throw new Error("unimplemented");
				setpcr(pc+imm8);
				ispcrset=true;
			}else if(op==5){//add
				{final int a=re.get(rai);
				final int d=re.get(rdi);
				final int r=a+d;
				zneval(r);
				re.setr(rai,r);}
			}else if(op==6){//ldc
				final int a=re.getinc(rai);
				final int d=ra.get(a);
				re.setr(rdi,d);
				zneval(d);
				me.ldc++;
			}else if(op==7){//tx
				final int a=re.get(rai);
				re.setr(rdi,a);
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
				try{ev(null,this,new Integer(imm4));}catch(Throwable t){throw new Error(t);}
			}else if(op==4){// free  
			}else if(op==5){// sub
			}else if(op==6){// st
				final int d=re.get(rdi);
				final int a=re.get(rai);
				ra.set(a,d);
				me.stc++;
			}else if(op==7){// ld
				final int a=re.get(rai);
				final int d=ra.get(a);
				re.setr(rdi,d);
				zneval(d);
				me.ldc++;
			}else throw new Error();
		}
		if(!ispcrset)
			setpcr(pc+1);
	}
	private void setpcr(final int i){
		pc=i;
		ir=ro.get(i);
	}
	private void zneval(final int i){
		if(i==0){zn=1;return;}
		if((i&(1<<16))==(1<<16)){zn=2;return;}//? .
//		if(i<0){zn=2;return;}
		zn=3;
	}


	@Override public void ev(xwriter x,a from,Object o) throws Throwable{
		if(o instanceof program){
			final program p=(program)o;
			p.write_to(ro);
			x.xuo(ro);
			x_r(x,null);
			x_f(x,null);
		}else super.ev(x,from,o);
	}
	
	public zn()throws Throwable{
		ec.src.from(getClass().getResourceAsStream(filenmromsrc));
		ajaxsts.set("idle");
		bits.set(0b1111110000);
	}
	public void to(final xwriter x)throws Throwable{
		x.div(this);
		final String id=id();
		if(pt()==null){
			x.style();
			switch(th.toint()){
			case 0:x
				//width:50em;
				.css("body","box-shadow:0 0 17px rgba(0,0,0,.5);text-align:center;line-height:1.4em;margin-left:auto;margin-right:auto;padding:3em 4em 0 8em")
				.css(ec.src,"width:24em;min-width:24em;height:1024em;min-height:1024em;resize:none;line-height:1.4em")
				.css(".border","border:1px dotted red")
				.css(".float","float:left")
				.css(".textleft","text-align:left")
				.css(".floatclear","clear:both")
				.css(".panel","padding-left:.5em;padding-right:.5em")
				.css(".stp","background-color:#020")
				.css(".brk","background-color:#060")
				.css(".laycent","display:table;margin-left:auto;margin-right:auto")
				.css(ajaxsts,"position:fixed;bottom:0;right:0")
				;
				break;
			case 1:x
				.css("html","background:#111;color:#080")
				.css("body","text-align:center;line-height:1.4em;width:80em;margin-left:auto;margin-right:auto;padding:3em 4em 0 8em;display:block;box-shadow:0 0 17px rgba(0,0,0,.5)")
				.css("a","color:#008")
				.css(ec.src,"width:24em;min-width:24em;height:1024em;min-height:1024em;resize:none;line-height:1.4em")
				.css(".border","border:1px dotted red")
				.css(".float","float:left")
				.css(".textleft","text-align:left")
				.css(".floatclear","clear:both")
				.css(".panel","padding-left:.5em;padding-right:.5em")
				.css(ajaxsts,"position:fixed;bottom:0;right:0")
				.css(".stp","background-color:#020")
				.css(".brk","background-color:#060")
				.css(".laycent","display:table;margin-left:auto;margin-right:auto")
				;
				break;
			}
			x.style_();
			ajaxsts.to(x);
			try(final jskeys jskeys=new jskeys(x)){
				jskeys.add("cS","$x('"+id+" s')");//? x.axstr(id,func,param):"$x('..','... ...');
				jskeys.add("cL","$x('"+id+" l')");
				jskeys.add("cT","$x('"+id+" n')");
				jskeys.add("cR","$x('"+id+" r')");
				jskeys.add("cF","$x('"+id+" i')");
				jskeys.add("cG","$x('"+id+" g')");
				jskeys.add("cU","$x('"+id+" u')");
				jskeys.add("cO","$x('"+id+" c')");
				jskeys.add("cF","$x('"+id+" f')");
				jskeys.add("cD","$x('"+ra.id()+" rfh')");
				jskeys.add("cB","$x('"+id+" b')");
				jskeys.add("cK","alert('info')");
			}
		}
		if(hasbit(bit_logo)){
			logo_to(x);
			copyright_to(x);
		}
		if(hasbit(bit_schematics))schematics_to(x);
		if(hasbit(bit_pramble)){
			x.nl();
			pramble_to(x);
			x.nl();
		}
		if(hasbit(bit_instructions_table))instructions_table_to(x);
		if(hasbit(bit_display))x.r(ra);
		if(hasbit(bit_menu))
			x.div()
				.ax(this,"r"," reset")
				.ax(this,"f"," frame")
				.ax(this,"n"," step")
				.ax(this,"g"," go")
				.ax(this,"u"," bench")
//				.ax(this,"be"," bench")
//				.ax(this,"s"," save")
//				.ax(this,"b"," run-to-break-point")
			.div_();
		x.div("laycent");
		if(hasbit(bit_panels))x.div(null,"float panel").span(st,"font-weight:bold").r(sy).r(re).r(ca).r(lo).div_();
		if(hasbit(bit_rom))x.r(ro);
		if(hasbit(bit_edcrn))x.r(ec);
		x.div_();
		x.div(null,"floatclear").div_();
		if(pt()==null)x.p("theme: ").inptxt(th,this);
		x.div_();
	}
	boolean hasbit(final int bit){return(bits.toint()&bit)==bit;}
	synchronized public void x_(xwriter x,String s)throws Throwable{x.xuo(this);}
	/**reset*/public void x_r(xwriter x,String s)throws Throwable{
		reset();
		copy_rom_to_ram();
		if(x==null)return;
		xfocusline(x);
		x.xu(sy).xuo(re).xuo(ca).xuo(lo);
		x.xu(st.set("refresh display"));
		x.flush();
		if(hasbit(bit_display)){
			x.xu(st.set("refreshing display")).flush();
			ra.x_rfh(x,s,ram.wi,ram.hi,0,0);
		}
		x.xu(st.set("reseted"));
	}
	public void x_n(final xwriter x,final String s)throws Throwable{
		if(running){running=false;return;}
		st.clr();
		ra.x=x;
		step();
		if(x==null)return;
		x.xuo(sy).xuo(re).xuo(ca).xuo(lo);
		xfocusline(x);
	}
	/**go*/
	synchronized public void x_g(final xwriter x,final String s)throws Throwable{
		while(true){
			x_n(x,s);
			if(x!=null)x.flush();
			Thread.sleep(500);
		}
	}
	private void xfocusline(xwriter x){
		if(!hasbit(bit_rom))return;
		ro.focusline=pc;
		ro.xfocusline(x);
	}
	private long runms=1000;
	synchronized public void x_u(final xwriter x,final String s)throws Throwable{
		if(running)throw new Error("already running");
		running=true;
		if(x!=null)x.xu(st.set("running "+runms+" ms")).flush();
		long t0=System.currentTimeMillis();
		final long minstr=me.instr;
		final long mframes=me.frames;
		long dt=0;
		while(running){
			step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(last_instruction_was_end_of_frame)
				ev(null,this);//refresh display
			if(dt>runms)
				break;
		}
		if(running){
			running=false;
			final long dminstr=me.instr-minstr;
			final long dmframes=me.frames-mframes;
			if(dt==0)dt=1;
			st.set(strdatasize3((long)dminstr*1000/dt)+"ips, "+strdatasize3((long)dmframes*1000/dt)+"fps");
			if(x==null)return;
			x.xu(st).xu(re).xu(ca).xu(lo);
			ro.xfocusline(x);
			x.flush();
			final int b=bits.toint();
			if((b&1)==1){
				ra.x_rfh(x,s,ram.wi,ram.hi,0,0);
			}
		}
	}
	/**runtobreakpoint*/
	synchronized public void x_b(xwriter x,String s)throws Throwable{//? doesnotstopafterconnectionclose
		if(running)throw new Error("already running");
		running=true;
		if(x!=null)x.xu(st.set("running to breakpoint")).flush();
//		final long t0=System.currentTimeMillis();
//		final long instr0=me.instr;
		st.clr();
		while(running){
			boolean go=true;
			step();
//			final int srclno=lino.get(pc);
//			if(ec.isonbrkpt(srclno)){
//				st.set("breakpoint @ "+srclno);
//				go=false;
//			}
			if(!go)break;
		}
		if(running){
			running=false;
//			final long dt=System.currentTimeMillis()-t0;
//			final long dinstr=me.instr-instr0;
//			final int l=lino.get(ro.focusline);
//			st.set(dinstr+" instr, "+dt+" ms, "+l);
			if(x==null)return;
			x.xu(st);
			x.xu(sy);
			x.xu(re);
			x.xu(ca);
			x.xu(this.lo);
			xfocusline(x);
			ra.x_rfh(x,s,ram.wi,ram.hi,0,0);
		}
	}
	/**stepframe*/
	synchronized public void x_f(final xwriter x,final String s)throws Throwable{
		if(running)throw new Error("already running");
		if(x!=null)x.xu(st.set("running frame")).flush();
		running=true;
		final long instr0=me.instr;
		final long t0=System.currentTimeMillis();
		while(running){
			step();
			if(last_instruction_was_end_of_frame){
				last_instruction_was_end_of_frame=false;
				break;
			}
		}
		if(running){
			final long dt=System.currentTimeMillis()-t0;
			final long dinstr=me.instr-instr0;
			st.set("#"+me.frames+", "+strdatasize3((int)dinstr)+"i, "+dt+" ms");
			running=false;
		}
		if(x==null)return;
		xfocusline(x);
		x.xu(st).xu(sy).xu(re).xu(ca).xu(lo);
		if(hasbit(bit_display)){
			ra.x_rfh(x,s,ram.wi,ram.hi,0,0);
		}
	}
	static public void logo_to(final xwriter x){
		final int con_wi=64;
		for(int k=0;k<con_wi;k++)x.p(Math.random()>.5?'-':' ');x.nl();
		x.pl("clare "+strdatasize(ram.size));
		for(int k=0;k<con_wi;k++)x.p(Math.random()>.5?'-':' ');x.nl();
	}
	static public void copyright_to(final xwriter x){
//		x.pl("(c) 1984 some rights reserved ltd");
	}
	static public void schematics_to(final xwriter x){
		x.pl("|_______|______|____|____|  ");
		x.pl("|z n x r|c i 00|0000|0000|  ");
		x.pl("|_______|______|____|____|  ");
		x.pl("\\1 2 4 8\\. . ..\\....\\....   ");
		x.pl(" \\    n  \\      \\....\\....  ");
		x.pl("  \\z n x r\\c i ..\\yyyy\\xxxx ");
		x.pl("   \\e e t e a m             ");
		x.pl("    \\r g   t l m            ");
		x.pl("     \\o       l             ");
	}
	public void pramble_to(final xwriter x){
		x.pl(strdatasize(ram.size)+" 20b ram");
		x.pl(re.size+" 20b registers");
		x.pl(ram.wi+" x "+ram.hi+" pixels display");//\n  12 bit rgb\n  20 bit free");
		x.pl("12b rgb color in 20b pixel");
		x.pl("256 sprites collision detection");
		x.pl(strdatasize2(rom.size)+" 16b instructions");
		x.pl(loops.size+" loops stack");
		x.pl(calls.size+" calls stack");
	}
	static public void instructions_table_to(final xwriter x){
		x.pl(":------:------:----------------------:");
		x.pl(": load : "+fld("x000",Integer.toHexString(opload))+" : next instr to reg[x] :");
		x.pl(": call : "+fld("..00",Integer.toHexString(opcall))+" : 2b + ..              :");
		x.pl(":  skp : "+fld("..00",Integer.toHexString(opskp))+" : pc+=..               :");
		x.pl(":  stc : "+fld("yx00",Integer.toHexString(opstc))+" : ram[x++]=y           :");
		x.pl(":   st : "+fld("yx00",Integer.toHexString(opst))+" : ram[x]=y             :");
 		x.pl(":   lp : "+fld("x000",Integer.toHexString(oplp))+" : loop x               :");
		x.pl(":  ldc : "+fld("yx00",Integer.toHexString(opldc))+" : y=ram[x++]           :");
		x.pl(":   ld : "+fld("yx00",Integer.toHexString(opld))+" : y=ram[x]             :");
		x.pl(":  shf : "+fld("xi00",Integer.toHexString(opshf))+" : r[x]>>=i             :");
		x.pl(":  shf : "+fld("xi00",Integer.toHexString(opshf))+" : r[x]<<=i             :");
		x.pl(":  not : "+fld("x000",Integer.toHexString(opshf))+" : r[x]=~r[x]           :");
		x.pl(":  inc : "+fld("x000",Integer.toHexString(opinc))+" : r[x]++               :");
		x.pl(":  neg : "+fld("x000",Integer.toHexString(opneg))+" : r[x]=-r[x]           :");
		x.pl(":  add : "+fld("yx00",Integer.toHexString(opadd))+" : r[y]+=r[x]           :");
		x.pl(":   tx : "+fld("yx00",Integer.toHexString(opset))+" : r[y]=r[x]            :");
//		x.pl(":  skp : "+fld("im00",Integer.toHexString(opskp))+" : pc+=imm8             :");
		x.pl(":  sub : "+fld("xy00",Integer.toHexString(opsub))+" : r[y]-=r[x]           :");
		x.pl(":  dac : "+fld("x000",Integer.toHexString(opdac))+" : sound.add block r[x] :");
		x.pl(":  ifz : ...1 : if z op              :");
		x.pl(":  ifn : ...2 : if n op              :");
		x.pl(":  ifp : ...3 : if p op              :");
		x.pl(":  nxt : ...4 : op nxt               :");
		x.pl(":  ret : ...8 : op ret               :");
		x.pl(":      : ...c : op nxt ret           :");
		x.pl(":------:------:----------------------:");
		x.pl(":      : ..18 : cr invalids          :");
		x.pl(": wait : "+fld("x000",Integer.toHexString(opwait))+" : wait                 :");
		x.pl(":notify: "+fld("x000",Integer.toHexString(opnotify))+" : notify               :");
		x.pl(":  rrn : ffff : rerun                :");
		x.pl(":------:------:----------------------:");
	}
	
	private static final long serialVersionUID=1;







	public final static String fld(final String def,final String s){
		final String s1=s.length()>def.length()?s.substring(s.length()-def.length()):s;
		final int a=def.length()-s1.length();
		if(a<0)return s1;
		final String s2=def.substring(0,a)+s1;
		return s2;
	}
	private static String strdatasize2(final int i){
		final StringBuilder sb=new StringBuilder();//! final StringBuilder sb=;
		int x=i;
		final int megs=(x>>20);
		if(megs>0){
			x-=(megs<<20);
			sb.append(megs).append("m");			
		}
		final int kilos=(x>>10);
		if(kilos>0){
			x-=(kilos<<10);
			sb.append(kilos).append("k");
		}
		if(x>0){
			sb.append(x);
		}
		return sb.toString();
	}
	public static String strdatasize3(final long i){
		final StringBuilder sb=new StringBuilder();//! final StringBuilder sb=;
		long x=i;
		final long megs=(x>>20);
		if(megs>0){
			x-=(megs<<20);
			sb.append(megs).append(" m");
			return sb.toString();
		}
		final long kilos=(x>>10);
		if(kilos>0){
			x-=(kilos<<10);
			sb.append(kilos).append(" k");
			return sb.toString();
		}
		if(x>0){
			sb.append(x).append(" ");
		}
		return sb.toString();
	}
	public static String strdatasize(final int i){
		final StringBuilder sb=new StringBuilder();//! final StringBuilder sb=;
		int x=i;
		final int megs=(x>>20);
		if(megs>0){
			x-=(megs<<20);
			sb.append(megs).append("m");			
		}
		final int kilos=(x>>10);
		if(kilos>0){
			x-=(kilos<<10);
			sb.append(kilos).append("k");
		}
		if(x>0){
			sb.append(x).append("b");
		}
		return sb.toString();
	}
}
