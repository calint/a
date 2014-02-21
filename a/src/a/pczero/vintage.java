package a.pczero;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import a.x.jskeys;
import b.a;
import b.path;
import b.xwriter;
final public class vintage extends a{
	static final long serialVersionUID=1;
	@SuppressWarnings("static-access")
	private void pramble(final xwriter x){
		for(int k=0;k<48;k++)x.p('-');x.nl();
		x.pl(" vintage 16 bit computing - znxrcis "+vintage.strdatasize(ram.size));
		for(int k=0;k<48;k++)x.p('-');x.nl();
		x.pl("   "+regs.size+"x16b regs");
		x.pl("   "+vintage.strdatasize2(rom.size)+" 16b code cache");
//		x.pl("   "++" x 16b bmp display");
		x.pl("   "+vintage.strdatasize(ram.size)+" x 16b ram in "+ram.width+"x"+ram.height+" display");
		x.pl("   "+loops.size+" loops stack");
		x.pl("   "+calls.size+" calls stack");
		x.pl("");
		prambleops(x);
	}
	private final static short opload=0x000;
	private final static short oplp=0x100;
	private final static short opinc=0x200;
	private final static short opneg=0x300;
	private final static short opdac=0x400;
	private final static short opwait=0x058;
	private final static short opnotify=0x078;
	private final static short opset=0xe0;
	private final static short opldc=0xc0;
	private final static short opadd=0xa0;
	private final static short opskp=0x80;
	private final static short opshf=0x60;
	private final static short opstc=0x40;
	private final static short opsub=0x20;
	private final static short opcall=0x10;
	public static void prambleops(final xwriter x){
//		x.pl("  op format znxrci              ");
//		x.el("text-align:left;border:1px solid red;display:inline-table");
		x.pl("pc ir");
		x.pl("|_______|______|____|____|  ");
		x.pl("|z n x r|c i 00|0000|0000|  ");
		x.pl("|_______|______|____|____|  ");
		x.pl("\\1 2 4 8\\. . ..\\....\\....   ");
		x.pl(" \\    n  \\      \\....\\....  ");
		x.pl("  \\z n x r\\c i ..\\yyyy\\xxxx ");
		x.pl("   \\e e t e a m             ");
		x.pl("    \\r g   t l m            ");
		x.pl("     \\o       l             ");
//		x.elend();
		x.nl();
		x.pl(":------:------:----------------------:");
		x.pl(": load : "+fld("x000",Integer.toHexString(opload))+" : next instr to reg[x] :");
		x.pl(": call : "+fld("..00",Integer.toHexString(opcall))+" : 2b + ..              :");
		x.pl(":  skp : "+fld("..00",Integer.toHexString(opskp))+" : pc+=..               :");
		x.pl(":  stc : "+fld("yx00",Integer.toHexString(opstc))+" : ram[x++]=y           :");
 		x.pl(":   lp : "+fld("x000",Integer.toHexString(oplp))+" : loop x               :");
		x.pl(":  ldc : "+fld("yx00",Integer.toHexString(opldc))+" : y=ram[x++]           :");
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
	public static void main(final String[]a)throws Throwable{
		final PrintStream out=System.out;
		final vintage x1=new vintage();
		x1.pth=path.get1(a.length>1?a[1]:filenmromsrc);
		x1.x_l(null,null);
		out.println(x1.sts);
		x1.x_c(null,null);
		out.println(x1.sts);
		x1.x_r(null,null);
		out.println(x1.sts);
		x1.x_u(null,null);
		out.println(x1.sts);
//		while(true){
//			x1.ax_rf(null,null);
//			out.println(x1.sts);
//		}
	}
	static final String filenmromsrc="pz.src";
	private path pth;
	public rom rom;
	public ram ram;
	public sys sys;
	public int pcr;
	int ir;
	int zn;
	public regs regs;
	public calls calls;
	public loops loops;
	public a sts;
	public a coreid;
	public srcviwr src;
	public int mode;//1:multicore
	public final Map<Integer,Integer>lino=new HashMap<Integer,Integer>();// bin->src
	private int lno;
	private int lnosrc;
	private final Map<Integer,String>callmap=new HashMap<Integer,String>();
	private final Map<String,Integer>labels=new HashMap<String,Integer>();
	private final Map<Integer,String>loadlabelmap=new HashMap<Integer,String>();
	private final Map<Integer,String>skplabelmap=new HashMap<Integer,String>();
	private final List<String>srclines=new ArrayList<String>(32);
	private int loadreg=-1;
	public long mtrinstr;
	public long mtrframes;
	long mtrldc;
	long mtrstc;
	public int dispbits=-1;
	public void setpth(final path p){pth=p;}
	public void to(final xwriter x)throws Throwable{
		if(mode==1){
			x.el(this);
//			x.p(coreid);
			rendpanel(x);
			src.to(x);
			x.elend();
			return;
		}
		if(pt()==null){
			x.el(this,"width:768px;color:#222;margin-left:auto;margin-right:auto;padding:0 4em 0 4em;display:block;border-right:0px dotted #666;border-left:0px dotted #666;box-shadow:0 0 17px rgba(0,0,0,.5);border-radius:1px");
			final String id=id();
			final jskeys jskeys=new jskeys(x);
			x.nl().script().nl();
			jskeys.add("cS","$x('"+id+" s')");
			jskeys.add("cL","$x('"+id+" l')");
			jskeys.add("cT","$x('"+id+" n')");
			jskeys.add("cR","$x('"+id+" r')");
			jskeys.add("cF","$x('"+id+" i')");
			jskeys.add("cG","$x('"+id+" g')");
			jskeys.add("cU","$x('"+id+" u')");
			jskeys.add("cO","$x('"+id+" c')");
			jskeys.add("cF","$x('"+id+" f')");
			jskeys.add("cD","$x('"+ram.id()+" rfh')");
			jskeys.add("cB","$x('"+id+" b')");
			jskeys.add("cK","alert('info')");
			x.scriptEnd();
			x.nl().style();
			x.nl().css(this,"text-align:center");
			x.nl().cssfont("tini","/ttf/tini.ttf");
			x.nl().css("*","white-space:pre");
			x.nl().css("*","-webkit-transition: all .4s ease-in-out;");
			x.nl().css("*","-moz-transition: all .4s ease-in-out;");
			x.nl().css("*","-o-transition: all .4s ease-in-out;");
			x.nl().css("*","transition: all .4s ease-in-out;");
			x.nl().css("a","color:blue");
			x.nl().css("a:focus,a:hover,a:active","color:red;text-shadow:0 0 .1em rgba(0,0,0,.5);");
			x.nl().styleEnd();
//			src.libgbrkpt="#8a8";
//			src.libgstep="#c88";
//			src.libghvr="#898";
			rom.libgstp="#c88";
//			x.style(this,"ol li.stp","cursor:pointer;background-color:#c88");
		}else
			x.el(this,"display:inline-block");
				
		if(mode==1){
			rendpanel(x);
			src.to(x);
			x.elend();
			return;
		}
		final boolean dispramble=(dispbits&8)==8;
		if(dispramble){
			x.pre();
			x.el("border:0px solid green;display:block;text-align:center");
			pramble(x);
			x.elend().nl();
		}
		final boolean dispram=(dispbits&1)==1;
		if(dispram){
			ram.to(x);
			x.style(ram,"border:1px solid #488");
			x.nl();
		}
		final boolean dispmenu=(dispbits&2)==2;
		if(dispmenu){
			x.ax(this,"l"," load");
			x.ax(this,"c"," compile");
			x.ax(this,"r"," reset");
			x.ax(this,"f"," frame");
			x.ax(this,"n"," step");
			x.ax(this,"g"," go");
			x.ax(this,"u"," run");
			x.ax(this,"s"," save");
			x.ax(this,"b"," run-to-break-point");
			x.nl();
		}
		final boolean disprom=(dispbits&4)==4;
		if(disprom){
			x.style("table.d tr td","padding-left:1em;text-align:left");
			x.style("table.d tr td:first-child","padding-left:0em");
			x.table("d");
			x.nl().tr().td();
		}
		if(mode==1)x.el("display:table;margin-left:auto;margin-right:auto");
		if(mode==0)x.el("display:table;margin-left:auto;text-align:right;margin-right:1em");
		rendpanel(x);
		if(disprom){
			x.nl().td();
			rom.to(x);
			x.nl().td();
		}
		src.to(x);
		if(disprom){
			x.nl().tableEnd();
		}
		x.elend();
	}
	void rendpanel(xwriter x)throws Throwable{
		x.pre();
		x.span(sts,"font-weight:bold").nl();
		x.p(sys).nl();
		x.p(regs);
		x.p(calls);
		x.p(loops);
		x.preEnd();
	}
	void rendrom(xwriter x){
		rom.to(x);
	}
	synchronized public void x_i(xwriter x,String s)throws Throwable{
		ir=rom.get(pcr);
	}
	boolean running;
	@SuppressWarnings("static-access")
	public void x_r(xwriter x,String s)throws Throwable{
		running=false;
		sts.set("reseting");
		if(x!=null)x.xu(sts).flush();
		zn=0;
		loadreg=-1;
		setpcr(0);
		calls.rst();
		loops.rst();
		regs.rst();
		ram.rst();
		rom.rst();
		mtrinstr=mtrframes=mtrldc=mtrstc=0;
		for(int i=0;i<rom.size;i++)
			ram.set(i,rom.get(i));
		ev(null,this);
		wait=notify=false;
		if(x==null)return;
		xfocusline(x);
		x.xu(sys).xuo(regs).xuo(calls).xuo(loops);
		x.xu(sts.set("refresh display"));
		x.flush();
		if((dispbits&1)==1&&mode==0){
			x.xu(sts.set("refreshing display")).flush();
			ram.x_rfh(x,s);
		}
		x.xu(sts.set("reseted"));
	}
	public void x_s(final xwriter x,final String s)throws Throwable{
		src.txt.to(pth);
		sts.set("saved "+pth.name());
		if(x==null)return;
		x.xu(sts);
	}
	synchronized public void x_l(final xwriter x,final String s)throws Throwable{
		if(pth!=null&&pth.exists()){
			src.txt.from(pth);
			sts.set("loaded "+pth.name());
		}else{
			final InputStream is=getClass().getResourceAsStream(filenmromsrc);
			src.txt.from(is);
			sts.set("loaded default");
		}
		if(x==null)return;
		x.xuo(src);
		x.xu(sts);
		x.xuo(rom);
	}
	public void x_n(final xwriter x,final String s)throws Throwable{
		if(running){
			running=false;
			return;
		}
		sts.clr();
		ram.x=x;
		final int srclineno=lino.get(pcr);
		final String srcline=srclines.get(srclineno-1);
		step();
		ram.x=null;
		if(x==null)return;
		x.xu(sts.set(srcline));
		x.xuo(sys).xuo(regs).xuo(calls).xuo(loops);
		xfocusline(x);
	}
	synchronized public void x_g(final xwriter x,final String s)throws Throwable{
		while(true){
			x_n(x,s);
			if(x!=null)x.flush();
			Thread.sleep(1000);
		}
	}
	public void xfocusline(xwriter x){
		final boolean disprom=(dispbits&4)==4;
		if(disprom){
			rom.focusline=pcr;
			rom.xfocusline(x);
		}
		if(lino.isEmpty())return;
		src.focusline=lino.get(pcr);
		src.xfocusline(x);
	}
	private long runms=1000;
	synchronized public void x_u(final xwriter x,final String s)throws Throwable{
		if(running)throw new Error("already running");
		running=true;
		if(x!=null)x.xu(sts.set("running "+runms+" ms")).flush();
		long t0=System.currentTimeMillis();
		final long minstr=mtrinstr;
		final long mframes=mtrframes;
		long dt=0;
		while(running){
			step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(wasrerun)
				ev(null,this);//refresh display
			if(dt>runms)
				break;
		}
		if(running){
			running=false;
			final long dminstr=mtrinstr-minstr;
			final long dmframes=mtrframes-mframes;
			if(dt==0)dt=1;
			sts.set(strdatasize3((long)dminstr*1000/dt)+"ips, "+strdatasize3((long)dmframes*1000/dt)+"fps");
			if(x==null)return;
			x.xu(sts).xu(regs).xu(calls).xu(loops);
			rom.xfocusline(x);
			x.flush();
			if((dispbits&1)==1&&mode==0){
				ram.x_rfh(x,s);
			}
		}
	}
	synchronized public void x_b(xwriter x,String s)throws Throwable{//? doesnotstopafterconnectionclose
		if(running)throw new Error("already running");
		running=true;
		if(x!=null)x.xu(sts.set("running to breakpoint")).flush();
		final long t0=System.currentTimeMillis();
		final long instr0=mtrinstr;
		sts.clr();
		while(running){
			boolean go=true;
			step();
			final int srclno=lino.get(pcr);
			if(src.isonbrkpt(srclno)){
				sts.set("breakpoint @ "+srclno);
				go=false;
			}
			if(!go)break;
		}
		if(running){
			running=false;
			final long dt=System.currentTimeMillis()-t0;
			final long dinstr=mtrinstr-instr0;
			final int l=lino.get(rom.focusline);
			sts.set(dinstr+" instr, "+dt+" ms, "+l);
			if(x==null)return;
			x.xu(sts);
			x.xu(sys);
			x.xu(regs);
			x.xu(calls);
			x.xu(loops);
			xfocusline(x);
			ram.x_rfh(x,s);
		}
	}
	synchronized public void x_f(final xwriter x,final String s)throws Throwable{
		if(running)throw new Error("already running");
		if(x!=null)x.xu(sts.set("running frame")).flush();
		running=true;
		final long instr0=mtrinstr;
		final long t0=System.currentTimeMillis();
		while(running){
			step();
			if(wasrerun){
				wasrerun=false;
				break;
			}
		}
		if(running){
			final long dt=System.currentTimeMillis()-t0;
			final long dinstr=mtrinstr-instr0;
			sts.set("#"+mtrframes+", "+strdatasize3((int)dinstr)+"i, "+dt+" ms");
			running=false;
		}
		if(x==null)return;
		xfocusline(x);
		x.xu(sts).xu(sys).xu(regs).xu(calls).xu(loops);
		if((dispbits&1)==1&&mode==0){
			ram.x_rfh(x,s);
		}
	}
	
//	private int snapshotn;
	public void snapshot(final OutputStream os)throws IOException{
		final BufferedImage bi=new BufferedImage(ram.width,ram.height,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<ram.height;i++){
			for(int j=0;j<ram.width;j++){
				final short d=ram.get(k++);
				final int b= (d    &0xf)*0xf;
				final int g=((d>>4)&0xf)*0xf;
				final int r=((d>>8)&0xf)*0xf;
//				final int a=(d>>12)&0xf;
				final int a=0xff;
				final int argb=(a<<24)+(r<<16)+(g<<8)+b;
				bi.setRGB(j,i,argb);
			}
		}
		ImageIO.write(bi,"png",os);
//		
//		
//		final byte[]pixels=((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
	}
	synchronized public void x_c(final xwriter x,final String s)throws Throwable{
		if(x!=null)x.xu(sts.set("compiling")).flush();
		callmap.clear();
		labels.clear();
		lino.clear();
		loadlabelmap.clear();
		skplabelmap.clear();
		srclines.clear();
		final Scanner sc=new Scanner(src.txt.toString());
		try{
		lno=0;
		lnosrc=0;
		lino.clear();
		while(sc.hasNextLine()){
			try{
			String ln=sc.nextLine().trim();
			lnosrc++;
			srclines.add(ln);
			if(ln.startsWith("#")){
				if(ln.charAt(1)!=coreid.toString().charAt(0))
					continue;
				else
					ln=ln.substring(2).trim();
			}
			{int i=ln.indexOf("//");if(i!=-1)ln=ln.substring(0,i).trim();}
			if(ln.length()==0)continue;
			if(ln.startsWith(". ")){
				final String[]ss=ln.split(" ");
				for(int i=1;i<ss.length;i++){
					romwrite(Integer.parseInt(ss[i],16));
				}
				continue;
			}
			int ir=0;
			if(ln.startsWith("ifz ")){
				ir+=1;
				ln=ln.substring(4).trim();
			}else if(ln.startsWith("ifn ")){
				ir+=2;
				ln=ln.substring(4);
				ln=ln.trim();
			}else if(ln.startsWith("ifp ")){
				ir+=3;
				ln=ln.substring(4).trim();
			}
			boolean more=true;
			while(more){
				if(ln.endsWith("ret")){
					ir+=(1<<3);
					ln=ln.substring(0,ln.length()-"ret".length()).trim();
					more=true;
				}else if(ln.endsWith("nxt")){
					ir+=(1<<2);
					ln=ln.substring(0,ln.length()-"nxt".length()).trim();
					more=true;
				}else
					more=false;
			}
//			ln=ln.trim();
			if(ln.length()==0){// nxt ret
				romwrite(ir);
				continue;				
			}
			{final int i0=ln.indexOf("=*");
			if(i0!=-1){
				final String dest=ln.substring(0,i0).trim();
				String tokens=ln.substring(i0+"=*".length()).trim();
				if(tokens.endsWith("++")){
					tokens=tokens.substring(0,tokens.length()-"++".length()).trim();
				}else
					throw new Error("only d=*a++ supported");
				final int rdi=rifor(dest);
				final int rai=rifor(tokens);
				ir+=opldc;
				ir+=(rai<<8);
				ir+=(rdi<<12);
				romwrite(ir);
				continue;
			}}
			{final int i0=ln.indexOf(":=");
			if(i0!=-1){
				final String dest=ln.substring(0,i0).trim();
				final String tokens=ln.substring(i0+":=".length()).trim();
				final int regd=rifor(dest);
				final int rega=rifor(tokens);
				ir+=opset;
				ir+=(rega<<8);
				ir+=(regd<<12);
				romwrite(ir);
				continue;
			}}
			{final int i0=ln.indexOf("-=");
			if(i0!=-1){
				final String dest=ln.substring(0,i0).trim();
				final String tokens=ln.substring(i0+"-=".length()).trim();
				final int rega=rifor(dest);
				final int regd=rifor(tokens);
				ir+=opsub;
				ir+=(rega<<8);
				ir+=(regd<<12);
				romwrite(ir);
				continue;
			}}
			{final int i0=ln.indexOf("+=");
			if(i0!=-1){
				final String dest=ln.substring(0,i0).trim();
				final String tokens=ln.substring(i0+"+=".length()).trim();
				final int rega=rifor(dest);
				final int regd=rifor(tokens);
				ir+=opadd;
				ir+=(rega<<8);
				ir+=(regd<<12);
				romwrite(ir);
				continue;
			}}
			{final int i0=ln.indexOf("++");
			if(i0!=-1){
				final String dest=ln.substring(0,i0);
				final int rdi=rifor(dest);
				ir+=opinc;
				ir+=(rdi<<12);
				romwrite(ir);
				continue;
			}}
			{final int i0=ln.indexOf("<<=");
			if(i0!=-1){
				final String dest=ln.substring(0,i0);
				final String tokens=ln.substring(i0+"<<=".length()).trim();
				final int rdi=rifor(dest);
				int shf=Integer.parseInt(tokens,16);
				shf=-shf;
				final int rai=shf&0xf;
				ir+=opshf;
				ir+=(rai<<8);
				ir+=(rdi<<12);
				romwrite(ir);
				continue;
			}}
			{final int i0=ln.indexOf(">>=");
			if(i0!=-1){
				final String dest=ln.substring(0,i0);
				final String tokens=ln.substring(i0+">>=".length()).trim();
				final int rdi=rifor(dest);
				int shf=Integer.parseInt(tokens,16);
				final int im4=shf&0xf;
				ir+=opshf;
				ir+=(im4<<8);
				ir+=(rdi<<12);
				romwrite(ir);
				continue;
			}}
			if(ln.indexOf("..")!=-1){
				ir+=0xffff;
				romwrite(ir);
				continue;
			}
			final int i0=ln.indexOf('=');
			if(i0!=-1){
				final String regname=ln.substring(0,i0);
				final int rdi=rifor(regname);
				final String v=ln.substring(i0+1);
				ir+=(rdi<<12);
				romwrite(ir);
				if(v.startsWith(":")){
					final String lbl=v.substring(1).trim();
					loadlabelmap.put(lno,lbl);//. addtoloadls
					romwrite(0);
				}else
					romwrite(Integer.parseInt(v,16));
				continue;
			}
			if(ln.endsWith(":")){//? align(1<<2)
				final String lbl=ln.substring(0,ln.length()-1).toLowerCase();
				if(labels.containsKey(lbl))
					throw new Error("line "+lnosrc+": redefined location of '"+lbl+"' from "+lino.get(labels.get(lbl)));
				labels.put(lbl,lno);
				continue;
			}
			if(ln.indexOf(' ')==-1){
				final String lbl=ln.toLowerCase();
				callmap.put(lno,lbl);
				ir+=opcall;
				romwrite(ir);
				continue;
			}
			final String[]tkns=ln.split(" ");
			int of=0;
			if("lp".equals(tkns[of])){
				final int rdi=rifor(tkns[++of]);
				ir+=oplp;
				ir+=(rdi<<12);
			}else if("stc".equals(tkns[of])){
				final int rai=rifor(tkns[++of]);
				final int rdi=rifor(tkns[++of]);
				ir+=opstc;
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("ldc".equals(tkns[of])){
				final int rai=rifor(tkns[++of]);
				final int rdi=rifor(tkns[++of]);
				ir+=opldc;
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("inc".equals(tkns[of])){
				final int rdi=rifor(tkns[++of]);
				ir+=opinc;
				ir+=(rdi<<12);
			}else if("shf".equals(tkns[of])){
				final int rdi=rifor(tkns[++of]);
				final int rai=Integer.parseInt(tkns[++of],16)&0xf;
				ir+=opshf;
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("not".equals(tkns[of])){
				final int rdi=rifor(tkns[++of]);
				final int rai=0;
				ir+=opshf;
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("add".equals(tkns[of])){
				final int rai=rifor(tkns[++of]);
				final int rdi=rifor(tkns[++of]);
				ir+=opadd;
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("load".equals(tkns[of])){
				final int rdi=rifor(tkns[++of]);
				ir+=opload;
				ir+=(rdi<<12);
				romwrite(ir);
				final String v=tkns[of+1];
				if(v.startsWith(":")){
					final String lbl=v.substring(1).trim();
					loadlabelmap.put(lno,lbl);//. addtoloadls
					ir=0;
				}else
					ir=Integer.parseInt(v,16);
			}else if("skp".equals(tkns[of])){
				of++;
				if(!tkns[of].startsWith(":"))throw new Error("line "+lnosrc+": ie skp :label");
				final String label=tkns[of].substring(1);
				ir+=opskp;
				skplabelmap.put(lno,label);
			}else if("tx".equals(tkns[of])){
				final int rdi=rifor(tkns[++of]);
				final int rai=rifor(tkns[++of]);
				ir+=opset;
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("wait".equals(tkns[of])){
				ir+=opwait;
			}else if("notify".equals(tkns[of])){
				ir+=opnotify;
				final int rdi=Integer.parseInt(tkns[++of],16);
				ir+=(rdi<<12);
			}else if("neg".equals(tkns[of])){
				ir+=opneg;
				final int rdi=rifor(tkns[++of]);
				ir+=(rdi<<12);
			}else if("sub".equals(tkns[of])){
				ir+=opsub;
				final int rai=rifor(tkns[++of]);
				final int rdi=rifor(tkns[++of]);
				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else if("dac".equals(tkns[of])){
				ir+=opdac;
//				final int rai=rifor(tkns[++of]);
				final int rdi=rifor(tkns[++of]);
//				ir+=(rai<<8);
				ir+=(rdi<<12);
			}else 
				throw new Error("line "+lnosrc+": unknown op "+tkns[of]);
			romwrite(ir);
		}catch(final Throwable t){
			throw new Error("line "+lnosrc+": "+t);
		}
			//? what is lp x nxt
		}}finally{sc.close();}
		//link calls
		for(Iterator<Map.Entry<Integer,String>>i=callmap.entrySet().iterator();i.hasNext();){
			final Map.Entry<Integer,String>me=i.next();
			final Integer ln=me.getKey();
			final String lbl=me.getValue();
			final Integer n=labels.get(lbl);
			final Integer l=lino.get(ln);
			if(l==null)
				throw new Error("line "+ln+"  not found in lino");
			if(n==null)
				throw new Error("line "+lino.get(ln)+": label not found '"+lbl+"'");
			int instr=rom.get(ln);
			int instr1=instr+(n<<6);//znxr ci.. .... ....
			rom.set(ln,instr1);
		}
		//link imm loads
		for(Iterator<Map.Entry<Integer,String>>i=loadlabelmap.entrySet().iterator();i.hasNext();){
			final Map.Entry<Integer,String>me=i.next();
			final Integer ln=me.getKey();
			final String lbl=me.getValue();
			final Integer n=labels.get(lbl);
			if(n==null)
				throw new Error("linker: can not find label '"+lbl+"' load");
			rom.set(ln,n);
		}
		//link skips
		for(Iterator<Map.Entry<Integer,String>>i=skplabelmap.entrySet().iterator();i.hasNext();){
			final Map.Entry<Integer,String>me=i.next();
			final Integer ln=me.getKey();
			final String lbl=me.getValue();
			final Integer n=labels.get(lbl);
			if(n==null)
				throw new Error("linker: can not find label '"+lbl+"' for skp");
			final int i0=rom.get(ln);
			final int skp=n-ln;
			final int i1=i0+(skp<<8);//znxr ci.. .... ....
			rom.set(ln,i1);
		}
		sts.set(lno+" x 16b ops");
		if(x==null)return;
		x.xu(sts);
		x.flush();
		x.xuo(rom);
	}		
	private void romwrite(final int i){
		rom.set(lno,i);
		lino.put(lno,lnosrc);
		lno++;
	}
	private int rifor(final String s){
		if(s.length()>1)throw new Error("line "+lnosrc+": variable name '"+s+"' invalid. valid variable names a through p");
		final int i=s.charAt(0)-'a';
		if(i>15)throw new Error("line "+lnosrc+": variable name '"+s+"' invalid. valid variable names a through p");		
		return i;
	}
	boolean wait;
	public boolean notify;
	public void step(){
		if(wait){
			if(notify){
				synchronized(this){wait=notify=false;}
				setpcr(pcr+1);
			}else{
				return;
			}
		}
		mtrinstr++;
//		if(pcr>=rom.size)throw new Error("program out of bounds");
//		ir.set(rom.get(pcr.toint()));
//		rom.focusline=pcr;
		if(loadreg!=-1){
			regs.setr(loadreg,(short)ir);
			loadreg=-1;
			setpcr(pcr+1);
			return;
		}
		if(ir==0xffff){//? move
			wasrerun=true;
			setpcr(0);
			mtrframes++;
			try{ev(null,this,null);}catch(Throwable t){throw new Error(t);}
			return;
		}
		
		int in=ir;// znxr ci.. .rai .rdi
		final int izn=in&3;
		if((izn!=0&&(izn!=zn))){
			final int op=(in>>5)&127;//? &7 //i.. .... ....
			final int skp=op==0?2:1;// ifloadopskip2
//			if(skp!=1)throw new Error();
			setpcr(pcr+skp);
			return;
		}
		in>>=2;// xr ci.. .rai .rdi
		final int xr=in&0x3;
		final boolean rcinvalid=(in&6)==6;
		if(!rcinvalid&&(in&4)==4){//call
			final int imm10=in>>4;// .. .... ....
			final int znx=zn+((xr&1)<<2);// nxt after ret
			final int stkentry=(znx<<12)|(pcr+1);
			setpcr(imm10);
			calls.push(stkentry);
			return;			
		}
		boolean isnxt=false;
		boolean ispcrset=false;
		if((xr&1)==1){// nxt
			isnxt=true;
			if(loops.nxt()){
				loops.pop();
			}else{
				setpcr(loops.adr());
				ispcrset=true;
			}
		}
		boolean isret=false;
		if(!rcinvalid&&!ispcrset&&(xr&2)==2){// ret after loop complete
			final int stkentry=calls.pop();
			final int ipc=stkentry&0xfff;
			final int znx=(stkentry>>12);
			if((znx&4)==4){// nxt after previous call
				if(loops.nxt()){
					loops.pop();
				}else{
					setpcr(loops.adr());
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
						final short d=regs.get(rdi);
						loops.push(pcr+1,d);	
					}else if(rai==2){//inc
						regs.inc(rdi);	
					}else if(rai==3){//neg
						final int d=regs.get(rdi);
						final int r=-d;
						regs.setr(rdi,(short)r);
					}else if(rai==4){//dac
						final short d=regs.get(rdi);
						try{
							ev(null,this,new Short(d));
						}catch(final Throwable t){
							throw new Error(t);
						}
					}else throw new Error("unimplemented ops(x)");
				}else{
					if(isret||isnxt){
						if(!ispcrset){
							setpcr(pcr+1);
						}
						return;
					}
					loadreg=rdi;
				}
			}else if(op==1){// sub
				final int a=regs.get(rai);
				final int d=regs.get(rdi);
				final int r=a-d;
				zneval(r);				
				regs.setr(rai,(short)r);
			}else if(op==2){//stc
				final int d=regs.get(rdi);
				final int a=regs.getinc(rai);
				ram.set(a,d);
				mtrstc++;
			}else if(op==3){//shf and not
				if(rai==0){//not
					final short d=regs.get(rdi);
					final int r=~d;
					regs.setr(rdi,(short)r);
				}else{//shf
//					if(rai==0)throw new Error("unimplmented op");
					final int a=rai>7?rai-16:rai;
					final int r;
					if(a<0)
						r=regs.get(rdi)<<-a;
					else
						r=regs.get(rdi)>>a;
					regs.setr(rdi,(short)r);
					zneval(r);
				}
			}else if(op==4){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(ispcrset)throw new Error("unimplemented");
				setpcr(pcr+imm8);
				ispcrset=true;
			}else if(op==5){//add
				{final short a=regs.get(rai);
				final short d=regs.get(rdi);
				final int r=a+d;
				zneval(r);
				regs.setr(rai,(short)r);}
			}else if(op==6){//ldc
				final short a=regs.getinc(rai);
				final short d=ram.get(a);
				regs.setr(rdi,d);
				zneval(d);
				mtrldc++;
			}else if(op==7){//tx
				{final short a=regs.get(rai);
				regs.setr(rdi,a);}
			}
		}else{
			if(op==0){//free
			}else if(op==1){//skp
				if(imm8==0)throw new Error("unencoded op (rol x)");
				if(ispcrset)throw new Error("unimplemented");
				setpcr(pcr+imm8);
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
			}else if(op==6){// free
			}else if(op==7){// free
			}else throw new Error();
		}
		if(!ispcrset)
			setpcr(pcr+1);
	}
	private void setpcr(final int i){
		pcr=i;
		ir=rom.get(i);
	}
//	private String tknsnxtret(final int i){
//		final StringBuilder sb=new StringBuilder();
//		if((i&4)==4)sb.append(" nxt");
//		if((i&8)==8)sb.append(" ret");
//		return sb.toString();
//	}
	private void zneval(int i){
		if(i==0){zn=1;return;}
		if(i<0){zn=2;return;}
		zn=3;
	}
	String zntkns(){
		if(zn==0){return "";}
		if(zn==1){return "z";}
		if(zn==2){return "n";}
		if(zn==3){return "p";}
		throw new Error();
	}
//	private String regnm(final int ri){return ""+(char)('a'+ri);}
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
	public boolean wasrerun;
}
