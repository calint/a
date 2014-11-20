package a.pz;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import a.x.jskeys;
import b.a;
import b.a_ajaxsts;
import b.xwriter;
final public class acore extends a{
	final static String filenmromsrc="pz.src";
	public core c=new core(16,8,8,32*1024,1*1024);
	public rom ro;
	public ram ra;
	public core_status sy;
	public registers re;
	public call_stack ca;
	public loop_stack lo;
	/**statusline*/public a st;
	/**coreid*/public a co;
	public crun_source_editor ec;
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
	
	public acore()throws Throwable{
		ec.src.from(getClass().getResourceAsStream(filenmromsrc));
		ajaxsts.set("idle");
		bits.set(0b1111110000);
		ro.ints=c.rom;
		ra.ints=c.ram;
		re.ints=c.register;
		lo.core=c;
	}
	@Override public void ev(xwriter x,a from,Object o) throws Throwable{
		if(o instanceof program){
			final program p=(program)o;
			p.write_to(ro);
			c.reset();
			x.xuo(ro);
			x_f(x,null);
		}else super.ev(x,from,o);
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
		c.reset();
//		c.copy_rom_to_ram();
		if(x==null)return;
		xfocusline(x);
		x.xu(sy).xuo(re).xuo(ca).xuo(lo);
		x.xu(st.set("refresh display"));
		x.flush();
		if(hasbit(bit_display)){
			x.xu(st.set("refreshing display")).flush();
			ra.x_rfh(x,s,ra.wi,ra.hi,0,0);
		}
		x.xu(st.set("reseted"));
	}
	public void x_n(final xwriter x,final String s)throws Throwable{
		if(c.running){c.running=false;return;}
		st.clr();
		ra.x=x;
		c.step();
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
		ro.focusline=c.program_counter;
		ro.xfocusline(x);
	}
	private long runms=1000;
	synchronized public void x_u(final xwriter x,final String s)throws Throwable{
		if(c.running)throw new Error("already running");
		c.running=true;
		if(x!=null)x.xu(st.set("running "+runms+" ms")).flush();
		long t0=System.currentTimeMillis();
		final long minstr=me.instr;
		final long mframes=me.frames;
		long dt=0;
		while(c.running){
			c.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(c.last_instruction_was_end_of_frame)
				ev(null,this);//refresh display
			if(dt>runms)
				break;
		}
		if(c.running){
			c.running=false;
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
				ra.x_rfh(x,s,ra.wi,ra.hi,0,0);
			}
		}
	}
	/**runtobreakpoint*/
	synchronized public void x_b(xwriter x,String s)throws Throwable{//? doesnotstopafterconnectionclose
		if(c.running)throw new Error("already running");
		c.running=true;
		if(x!=null)x.xu(st.set("running to breakpoint")).flush();
//		final long t0=System.currentTimeMillis();
//		final long instr0=me.instr;
		st.clr();
		while(c.running){
			boolean go=true;
			c.step();
//			final int srclno=lino.get(pc);
//			if(ec.isonbrkpt(srclno)){
//				st.set("breakpoint @ "+srclno);
//				go=false;
//			}
			if(!go)break;
		}
		if(c.running){
			c.running=false;
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
			ra.x_rfh(x,s,ra.wi,ra.hi,0,0);
		}
	}
	/**stepframe*/
	synchronized public void x_f(final xwriter x,final String s)throws Throwable{
		if(c.running)throw new Error("already running");
		if(x!=null)x.xu(st.set("running frame")).flush();
		c.running=true;
		final long instr0=me.instr;
		final long t0=System.currentTimeMillis();
		while(c.running){
			c.step();
			if(c.last_instruction_was_end_of_frame){
				c.last_instruction_was_end_of_frame=false;
				break;
			}
		}
		if(c.running){
			final long dt=System.currentTimeMillis()-t0;
			final long dinstr=me.instr-instr0;
			st.set("#"+me.frames+", "+strdatasize3((int)dinstr)+"i, "+dt+" ms");
			c.running=false;
		}
		if(x==null)return;
		xfocusline(x);
		x.xu(st).xu(sy).xu(re).xu(ca).xu(lo);
		if(hasbit(bit_display)){
			ra.x_rfh(x,s,ra.wi,ra.hi,0,0);
		}
	}
	
	
	public void snapshot_png_to(final OutputStream os)throws IOException{
		final int wi=ra.wi;
		final int hi=ra.hi;
		final BufferedImage bi=new BufferedImage(wi,hi,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<hi;i++){
			for(int j=0;j<wi;j++){
				final int d=c.ram[k++];
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
	public void logo_to(final xwriter x){
		final int con_wi=64;
		for(int k=0;k<con_wi;k++)x.p(Math.random()>.5?'-':' ');x.nl();
		x.pl("clare "+strdatasize(c.ram.length));
		for(int k=0;k<con_wi;k++)x.p(Math.random()>.5?'-':' ');x.nl();
	}
	static public void copyright_to(final xwriter x){
//		x.pl("(c) 1984 some rights reserved ltd");
	}
	public void schematics_to(final xwriter x){
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
		x.pl(strdatasize(ra.ints.length)+" 20b ram");
		x.pl(re.ints.length+" 20b registers");
		x.pl(ra.wi+" x "+ra.hi+" pixels display");//\n  12 bit rgb\n  20 bit free");
		x.pl("12b rgb color in 20b pixel");
		x.pl("256 sprites collision detection");
		x.pl(strdatasize2(ro.ints.length)+" 16b instructions");
		x.pl(c.loop_stack_address.length+" loops stack");
		x.pl(call_stack.size+" calls stack");
	}
	static public void instructions_table_to(final xwriter x){
		x.pl(":------:------:----------------------:");
		x.pl(": load : "+fld("x000",Integer.toHexString(program.opload))+" : next instr to reg[x] :");
		x.pl(": call : "+fld("..00",Integer.toHexString(program.opcall))+" : 2b + ..              :");
		x.pl(":  skp : "+fld("..00",Integer.toHexString(program.opskp))+" : pc+=..               :");
		x.pl(":  stc : "+fld("yx00",Integer.toHexString(program.opstc))+" : ram[x++]=y           :");
		x.pl(":   st : "+fld("yx00",Integer.toHexString(program.opst))+" : ram[x]=y             :");
 		x.pl(":   lp : "+fld("x000",Integer.toHexString(program.oplp))+" : loop x               :");
		x.pl(":  ldc : "+fld("yx00",Integer.toHexString(program.opldc))+" : y=ram[x++]           :");
		x.pl(":   ld : "+fld("yx00",Integer.toHexString(program.opld))+" : y=ram[x]             :");
		x.pl(":  shf : "+fld("xi00",Integer.toHexString(program.opshf))+" : r[x]>>=i             :");
		x.pl(":  shf : "+fld("xi00",Integer.toHexString(program.opshf))+" : r[x]<<=i             :");
		x.pl(":  not : "+fld("x000",Integer.toHexString(program.opshf))+" : r[x]=~r[x]           :");
		x.pl(":  inc : "+fld("x000",Integer.toHexString(program.opinc))+" : r[x]++               :");
		x.pl(":  neg : "+fld("x000",Integer.toHexString(program.opneg))+" : r[x]=-r[x]           :");
		x.pl(":  add : "+fld("yx00",Integer.toHexString(program.opadd))+" : r[y]+=r[x]           :");
		x.pl(":   tx : "+fld("yx00",Integer.toHexString(program.opset))+" : r[y]=r[x]            :");
//		x.pl(":  skp : "+fld("im00",Integer.toHexString(opskp))+" : pc+=imm8             :");
		x.pl(":  sub : "+fld("xy00",Integer.toHexString(program.opsub))+" : r[y]-=r[x]           :");
		x.pl(":  dac : "+fld("x000",Integer.toHexString(program.opdac))+" : sound.add block r[x] :");
		x.pl(":  ifz : ...1 : if z op              :");
		x.pl(":  ifn : ...2 : if n op              :");
		x.pl(":  ifp : ...3 : if p op              :");
		x.pl(":  nxt : ...4 : op nxt               :");
		x.pl(":  ret : ...8 : op ret               :");
		x.pl(":      : ...c : op nxt ret           :");
		x.pl(":------:------:----------------------:");
		x.pl(":      : ..18 : cr invalids          :");
		x.pl(": wait : "+fld("x000",Integer.toHexString(program.opwait))+" : wait                 :");
		x.pl(":notify: "+fld("x000",Integer.toHexString(program.opnotify))+" : notify               :");
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
