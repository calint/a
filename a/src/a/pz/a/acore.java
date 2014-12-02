package a.pz.a;
import static b.b.pl;
import static b.b.stacktrace;
import a.pz.core;
import a.pz.a.crun_source_editor.block;
import a.pz.a.crun_source_editor.xbin;
import a.pz.bas.call;
import a.pz.bas.program;
import a.pz.bas.assembly.add;
import a.pz.bas.assembly.inc;
import a.pz.bas.assembly.ld;
import a.pz.bas.assembly.ldc;
import a.pz.bas.assembly.li;
import a.pz.bas.assembly.lp;
import a.pz.bas.assembly.shf;
import a.pz.bas.assembly.stc;
import a.pz.bas.assembly.sub;
import a.pz.bas.assembly.tx;
import b.a;
import b.xwriter;
final public class acore extends a{
	public core cor=new core(16,8,8,32*1024,1*1024);
	public rom ro;
	public display ra;
	public core_status sy;
	public registers re;
	public call_stack ca;
	public loop_stack lo;
	/**statusline*/public a st;
	/**coreid*/public a co;
	public crun_source_editor ec;
	/**theme*/public a th;
	/**speakerleft*/public speaker sl;
	/**speakerright*/public speaker sr;
	public a bi;
	public final static int bit_show_logo=1;
	public final static int bit_show_schematics=2;
	public final static int bit_show_pramble=4;
	public final static int bit_show_instructions_table=8;
	public final static int bit_show_screen=16;
	public final static int bit_menu=32;
	public final static int bit_show_panel=64;
	public final static int bit_show_rom=128;
	public final static int bit_edasm=256;
	public final static int bit_show_source_editor=512;
	/**builtinajaxstatus*/public a ajaxsts;
	/**disassembled*/public a di;
	/**programtoc*/public a toc;
//	public metrics me;
	
	public acore()throws Throwable{
		ec.src.from(getClass().getResourceAsStream("rom"));
		ajaxsts.set("idle");
		bi.set(0b1111110000);
		ro.ints=cor.rom;
		ra.ints=cor.ram;
		re.ints=cor.registers;
		lo.core=cor;
		ec.x_f3(null,null);
	}
	@Override public void ev(xwriter x,a from,Object o) throws Throwable{
		pl("ev");
		if(o instanceof block){
			for(int i=0;i<cor.rom.length;i++)cor.rom[i]=0;
			final xbin b=new xbin(cor.rom);
			((block)o).binary_to(b);
			x_r(x,"");
			if(x==null)return;
			x.xu(ro);
		}else if(o instanceof program){
			final program p=(program)o;//? oishereinstanceofprogram
			final xwriter y=new xwriter();
			p.typedefs.values().forEach(e->y.p(e.name()).spc().p(e.source_location_line()).nl());
			p.defines.values().forEach(e->y.p(e.name()).spc().p(e.source_location_line()).nl());
			p.labels.values().forEach(e->y.p(e.name()).spc().p(e.source_location_line()).nl());
			p.structs.values().forEach(e->y.p(e.name()).spc().p(e.source_location_line()).nl());
			p.functions.values().forEach(e->y.p(e.name()).spc().p(e.source_location_line()).nl());
			toc.set(y.toString());
			p.zap(cor.rom);
			di.set(p.toString());
			cor.reset();
			x_f(x,null);
			if(x==null)return;
			x.xu(ro,di,toc);
		}else super.ev(x,from,o);
	}	
	public void to(final xwriter x)throws Throwable{
//		final String id=id();
		if(pt()==null){
			x.title("clare")
			.style()
				.css("html","color:#111")
				.css("body","padding:7em 0 0 11em;width:200em;text-align:center;line-height:1.4em;margin-left:auto;margin-right:auto;box-shadow:0 0 17px rgba(0,0,0,.5)")
				.css(".border","border:1px dotted red")
				.css(".float","float:left")
				.css(".textleft","text-align:left")
				.css(".fontbold","font-weight:bold")
				.css(".floatclear","clear:both")
				.css(".panel","padding-left:.5em;padding-right:.5em")
				.css(".stp","background-color:#ee0")
				.css(".brk","background-color:#ee2")
				.css(".nbr","width:3em;border:1px dotted;text-align:right")
				.css(".laycent","display:table;margin-left:auto;margin-right:auto")
				.css("textarea","line-height:1.4em")
				.css(di,"text-align:right;width:12em;margin-right:1em;overflow:scroll")
				.css(ec.src,"min-width:16em;min-height:140em;line-height:1.4em")
				.css(ajaxsts,"position:fixed;bottom:0;right:0")
				;
			switch(th.toint()){
			case 0:;break;
			case 1:x
				.css("html","background:#111;color:#060")
				.css("a","color:#007")
				.css(".stp","background-color:#020")
				.css(".brk","background-color:#021")
				;
				break;
			case 2:x
				.css("html","background:#421;color:#830")
				.css("a","color:#000")
				.css(".stp","background-color:#a30")
				.css(".brk","background-color:#a30")
				;
				break;
			}
			x.style_().spanh(ajaxsts);
//			try(final jskeys j=new jskeys(x)){
//				j.add("cS","$x('"+id+" s')");//? x.axstr(id,func,param):"$x('..','... ...');
//				j.add("cL","$x('"+id+" l')");
//				j.add("cT","$x('"+id+" n')");
//				j.add("cR","$x('"+id+" r')");
//				j.add("cF","$x('"+id+" i')");
//				j.add("cG","$x('"+id+" g')");
//				j.add("cU","$x('"+id+" u')");
//				j.add("cO","$x('"+id+" c')");
//				j.add("cF","$x('"+id+" f')");
//				j.add("cD","$x('"+ra.id()+" rfh')");
//				j.add("cB","$x('"+id+" b')");
//				j.add("cK","alert('info')");
//			}
		}
		x.divo(this);
		if(hasbit(bit_show_logo)){logo_to(x);copyright_to(x);}
		if(hasbit(bit_show_pramble)){pramble_to(x);x.nl();}
		if(hasbit(bit_show_schematics))schematics_to(x);
		if(hasbit(bit_show_instructions_table))instructions_table_to(x);
//		x.divh(sl,"panel");
		if(hasbit(bit_show_screen))x.r(ra);
//		x.divh(sr,"panel");
		if(hasbit(bit_menu))
			x.divo()
				.ax(this,"r",""," reset","r")
				.ax(this,"f",""," frame","f")
				.ax(this,"n",""," step","s")
				.ax(this,"g",""," go","g")
				.ax(this,"u",""," bench","b")
//				.ax(this,"b"," run-to-break-point")
			.div_();
		x.divo("laycent");
		if(hasbit(bit_show_panel))x.divo("float panel").spanh(st,"fontbold").rdiv(sy).rdiv(re).rdiv(ca).rdiv(lo).div_();
		if(hasbit(bit_show_rom))x.divo(ro,"float panel").r(ro).div_();
		if(hasbit(bit_show_source_editor))x.divh(ec,"float textleft panel").divh(toc,"float textleft panel").divh(di,"float panel","padding-top:1em");
		if(pt()==null){
			x.divo("floatclear").p("theme: ").inptxt(th,this,"t","nbr").p("  display-bits:").inptxt(bi,this,"t","nbr").div_();
		}
		x.div_();
	}
	public boolean hasbit(final int bit){return(bi.toint()&bit)==bit;}
	synchronized public void x_t(xwriter x,String s)throws Throwable{
		pl("x_t");
		x.xuo(this);
		x.xfocus(th);
	}
	/**reset*/public void x_r(xwriter x,String s)throws Throwable{
		pl("x_r");
		going=false;
		cor.reset();
		cor.meter_frames=cor.meter_instructions=0;
		st.set("reseted");
		if(x==null)return;
		xupd_focus(x);
		x.xu(st,sy,re,ca,lo);
		if(hasbit(bit_show_screen))ra.xupd(x);
	}
	/**step*/synchronized public void x_n(final xwriter x,final String s)throws Throwable{
		pl("x_n");
		final boolean refresh_display=cor.loading_register==-1&&(
				(cor.instruction&0x00d8)==0x00d8
				||
				(cor.instruction&stc.op)==stc.op
		);
		st.clr();
		cor.step();
		if(x==null)return;
		if(hasbit(bit_show_screen)&&refresh_display)ra.xupd(x);
		x.xu(sy,re,ca,lo);
		xupd_focus(x);
	}
	private boolean going;
	/**go*/
	synchronized public void x_g(final xwriter x,final String s)throws Throwable{
		pl("x_n");
		int i=0;
		going=true;
		while(going){
			pl("x_g "+i++);
			x_n(x,null);
			x.flush();
			Thread.sleep(500);
		}
	}
	private void xupd_focus(xwriter x){
		if(hasbit(bit_show_rom))ro.xfocus_on_binary_location(x,cor.program_counter);
	}
	private long runms=1000;
	synchronized public void x_u(final xwriter x,final String s)throws Throwable{
		pl("x_u");
		going=false;
		if(x!=null)x.xu(st.set("benching "+runms+" ms")).flush();
		long t0=System.currentTimeMillis();
		cor.meter_instructions=0;
		cor.meter_frames=0;
		long dt=0;
		while(true){
			cor.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(cor.instruction==-1)ev(null,this);//refresh display
			if(dt>runms)break;
		}
		if(dt==0)dt=1;
		final xwriter y=new xwriter();
		y.p_data_size(cor.meter_instructions*1000/dt).spc().p("ips").spc().p_data_size(cor.meter_frames*1000/dt).spc().p("fps");
		st.set(y.toString());
		if(x==null)return;
		x.xu(st,re,ca,lo);
		xupd_focus(x);
		if(hasbit(bit_show_screen))ra.xupd(x);
	}
	/**runtobreakpoint*/
	synchronized public void x_b(xwriter x,String s)throws Throwable{//? doesnotstopafterconnectionclose
		if(x!=null)x.xu(st.set("running to breakpoint")).flush();
//		final long t0=System.currentTimeMillis();
//		final long instr0=me.instr;
		st.clr();
		while(true){
			boolean go=true;
			cor.step();
//			final int srclno=lino.get(pc);
//			if(ec.isonbrkpt(srclno)){
//				st.set("breakpoint @ "+srclno);
//				go=false;
//			}
			if(!go)break;
		}
		if(x==null)return;
		x.xu(st,sy,re,ca,lo);
		xupd_focus(x);
		ra.xupd(x);
	}
	/**stepframe*/
	synchronized public void x_f(final xwriter x,final String s)throws Throwable{
		pl("x_f");
		going=false;
//		if(x!=null)x.xu(st.set("running frame")).flush();
		final long t0=System.nanoTime();
		try{cor.meter_instructions=0;
			boolean loop=true;
			while(true){
				cor.step();
				if(loop==false)break;
				if((cor.instruction&0xffff)==0xffff)loop=false;
			}
			final long dt=(System.nanoTime()-t0)/1000;
			final long dinstr=cor.meter_instructions;
			st.set(new xwriter().p("#").p(cor.meter_frames).spc().p_data_size(dinstr).spc().p(dt).spc().p("us").toString());
		}catch(Throwable t){
			st.set(stacktrace(t));
		}
		if(x==null)return;
		xupd_focus(x);
		x.xu(st,sy,re,ca,lo);
		if(hasbit(bit_show_screen))ra.xupd(x);
	}
	public void logo_to(final xwriter x){
		final int con_wi=64;
		for(int k=0;k<con_wi;k++)x.p(Math.random()>.5?'-':' ');x.nl();
		x.p("clare").spc().p_data_size(cor.ram.length).nl();
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
		x.pl("   \\e e t e\\a m             ");
		x.pl("    \\r g   t l m            ");
		x.pl("     \\o       l             ");
	}
	public void pramble_to(final xwriter x){
		final int bits_per_instruction=16;
		final int bits_per_register=16;
		final int bits_per_pixel=16;
		final int bits_per_pixel_rgb=12;
		final int nsprites=16;
		final int ndacs=8;
		
		x.p(ra.wi).spc().p("x").spc().p(ra.hi).spc().p("pixels display").nl();//\n  12 bit rgb\n  20 bit free");
		x.p(bits_per_pixel_rgb).p("b").spc().p("rgb color in").spc().p(bits_per_pixel).p("b").spc().p("pixel").nl();
		x.p(nsprites).spc().p("sprites onscreen collision detection").nl();
		x.p(ndacs).spc().p("sound tracks").nl();
		x.p_data_size(cor.rom.length).spc().p(bits_per_instruction).p("b").spc().p("rom").nl();
		x.p(cor.registers.length).spc().p(bits_per_register).p("b").spc().p("registers").nl();
		x.p(cor.call_stack.length).spc().p("calls stack").nl();
		x.p(cor.loop_stack_address.length).spc().p("loops stack");
	}
	static public void instructions_table_to(final xwriter x){
		x.pl(":------:------:----------------------:");
		x.pl(":  ifz : ...1 : ifz ...              :");
		x.pl(":  ifn : ...2 : ifn ...              :");
		x.pl(":  ifp : ...3 : ifp ...              :");
		x.pl(":  nxt : ...4 :     ... nxt          :");
		x.pl(":  ret : ...8 :     ... ret          :");
		x.pl(":      : ...d : ifz ... nxt ret      :");
		x.pl(":   li : "+fld("x000",Integer.toHexString(li.op))+" : next instr to reg[x] :");
		x.pl(": call : "+fld("ii00",Integer.toHexString(call.op))+" : imm6                 :");
		x.pl(":  skp : "+fld("ii00",Integer.toHexString(program.opskp))+" : pc+=imm6             :");
		x.pl(":  stc : "+fld("yx00",Integer.toHexString(stc.op))+" : ram[x++]=y           :");
		x.pl(":   st : "+fld("yx00",Integer.toHexString(0x00d8))+" : ram[x]=y             :");
 		x.pl(":   lp : "+fld("x000",Integer.toHexString(lp.op))+" : loop r[x] times      :");
		x.pl(":  ldc : "+fld("yx00",Integer.toHexString(ldc.op))+" : y=ram[x++]           :");
		x.pl(":   ld : "+fld("yx00",Integer.toHexString(ld.op))+" : y=ram[x]             :");
		x.pl(":  shf : "+fld("xi00",Integer.toHexString(shf.op))+" : r[x]>>=i             :");
		x.pl(":  shf : "+fld("xi00",Integer.toHexString(shf.op))+" : r[x]<<=i             :");
		x.pl(":  not : "+fld("x000",Integer.toHexString(shf.op))+" : r[x]=~r[x]           :");
		x.pl(":  inc : "+fld("x000",Integer.toHexString(inc.op))+" : r[x]++               :");
		x.pl(":  neg : "+fld("x000",Integer.toHexString(program.opneg))+" : r[x]=-r[x]           :");
		x.pl(":  add : "+fld("yx00",Integer.toHexString(add.op))+" : r[y]+=r[x]           :");
		x.pl(":   tx : "+fld("yx00",Integer.toHexString(tx.op))+" : r[y]=r[x]            :");
//		x.pl(":  skp : "+fld("im00",Integer.toHexString(opskp))+" : pc+=imm8             :");
		x.pl(":  sub : "+fld("xy00",Integer.toHexString(sub.op))+" : r[y]-=r[x]           :");
		x.pl(":  dac : "+fld("x000",Integer.toHexString(program.opdac))+" : dac=r[x]             :");
		x.pl(":  eof : ffff : end-of-frame         :");
		x.pl(":------:------:----------------------:");
		x.pl(":      : ..18 : cr invalids          :");
		x.pl(": wait : "+fld("x000",Integer.toHexString(program.opwait))+" : wait                 :");
		x.pl(":notify: "+fld("x000",Integer.toHexString(program.opnotify))+" : notify               :");
		x.pl(":------:------:----------------------:");
	}
	
	private static final long serialVersionUID=1;







	final static String fld(final String def,final String s){
		final String s1=s.length()>def.length()?s.substring(s.length()-def.length()):s;
		final int a=def.length()-s1.length();
		if(a<0)return s1;
		final String s2=def.substring(0,a)+s1;
		return s2;
	}
//	private static String strdatasize2(final int i){
//		final StringBuilder sb=new StringBuilder();//! final StringBuilder sb=;
//		int x=i;
//		final int megs=(x>>20);
//		if(megs>0){
//			x-=(megs<<20);
//			sb.append(megs).append("m");			
//		}
//		final int kilos=(x>>10);
//		if(kilos>0){
//			x-=(kilos<<10);
//			sb.append(kilos).append("k");
//		}
//		if(x>0){
//			sb.append(x);
//		}
//		return sb.toString();
//	}
//	public static String strdatasize3(final long i){
//		final StringBuilder sb=new StringBuilder();//! final StringBuilder sb=;
//		long x=i;
//		final long megs=(x>>20);
//		if(megs>0){
//			x-=(megs<<20);
//			sb.append(megs).append("m");
//			return sb.toString();
//		}
//		final long kilos=(x>>10);
//		if(kilos>0){
//			x-=(kilos<<10);
//			sb.append(kilos).append("k");
//			return sb.toString();
//		}
//		if(x>0){
//			sb.append(x);
//		}
//		return sb.toString();
//	}
//	public static String strdatasize(final int i){
//		final StringBuilder sb=new StringBuilder();//! final StringBuilder sb=;
//		int x=i;
//		final int megs=(x>>20);
//		if(megs>0){
//			x-=(megs<<20);
//			sb.append(megs).append("m");			
//		}
//		final int kilos=(x>>10);
//		if(kilos>0){
//			x-=(kilos<<10);
//			sb.append(kilos).append("k");
//		}
//		if(x>0){
//			sb.append(x).append("b");
//		}
//		return sb.toString();
//	}
}
