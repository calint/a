package a.pzm;
import static b.b.log;
import static b.b.pl;
import static b.b.stacktrace;

import b.a;
import b.xwriter;
final public class ide extends a{
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

	public core cor=new core(64,16,16,256*1024,64*1024);
	public rom ro;
	public display ra;
	public core_status sy;
	public registers re;
	public call_stack ca;
	public loop_stack lo;
	/**statusline*/public a st;
	/**coreid*/public a co;
	public source_editor ec;
	/**theme*/public a th;
	/**display bits*/public a bi;
	/**builtinajaxstatus*/public a ajaxsts;
	//	public metrics me;
	public ide() throws Throwable{
		ec.src.from(getClass().getResourceAsStream("rom"));
		ajaxsts.set("idle");
		bi.set(0b1111110000);
		ro.ints=cor.rom;
		ra.ints=cor.ram;
		re.ints=cor.registers;
		ca.stk=cor.call_stack;
		lo.core=cor;
		th.set(1);
	}
	@Override public void ev(xwriter x,a from,Object o) throws Throwable{
		pl("ev");
		if(o instanceof int[]){
			try{
				pl("*** compiler");
				for(int i=0;i<cor.rom.length;i++)
					cor.rom[i]=0;
				
				final int[]rm=(int[])o;
				if(rm.length>cor.rom.length)
					throw new Error();
//				if(rm.length==0)
//					throw new Error();
				System.arraycopy(rm,0,cor.rom,0,rm.length);
				ec.sts.set(rm.length);
				st.set(rm.length);
				cor.reset();
//				if(bit_show_rom)
				x_f(x,"");
			}catch(Throwable t){
				log(t);
				ec.sts.set(t.toString());
				st.set("");
			}
			if(x==null)return;
			x.xu(ro,st,ec.sts);
			xj_update_focus_on_rom(x);
		}
		else super.ev(x,from,o);
	}
	public void to(final xwriter x) throws Throwable{
		//		final String id=id();
		if(pt()==null){
			x.title("clare 20 bit")
					.style()
//					.css("html","")
//					.css("a","color:#0ff")
					.css("body","padding:1em 0 0 11em;text-align:center;line-height:1.4em;margin-left:auto;margin-right:auto")
					.css(".border","border:1px dotted red").css(".float","display:inline-block").css(".textleft","text-align:left").css(".fontbold","font-weight:bold")
					.css(".floatclear","clear:both").css(".panel","padding-left:.5em;padding-right:.5em").css(".stp","background-color:#ee0")
					.css(".brk","background-color:#ee2").css(".nbr","width:3em;border:1px dotted;text-align:right")
					.css(".laycent","white-space:no-wrap;margin-left:auto;margin-right:auto").css("textarea","line-height:1.4em")
					.css(ec.src,"min-width:40em;min-height:200em;line-height:1.4em")
//					.css(ec.ln,"box-shadow:0 0 .5em rgba(0,0,0,.5);display:inline-block")
					.css(ec.ln,"display:inline-block")
					.css(ec.ln,"li","padding:0 .5em 0 .5em;background:#111;border-left:1px dashed green;border-right:1px dashed green")
					.css(ec.ln,"li.a","background:#fcc")
					.css(ajaxsts,"box-shadow:0 0 .5em rgba(0,0,0,.5);position:fixed;bottom:0;right:0;padding-left:1em;padding-right:1em;padding-top:.5em;padding-bottom:.5em;border:1px dashed green")
					.css(this,"box-shadow:0 0 17px rgba(0,0,0,.5)");
			switch(th.toint()){
			case 0:
				x.css("html","background:#fff;color:#000").css("a","color:#00f").css(".stp","background-color:#ee0").css(".brk","background-color:#0ee");
				x.css(ec.ln,"li","padding:0 .5em 0 .5em;background:#fff;border-left:1px dashed #ddd;border-right:1px dashed #ddd");
				break;
			case 1:
				x.css("html","background:#111;color:#060").css("a","color:#007").css(".stp","background-color:#020").css(".brk","background-color:#021");
				x.css(ec.ln,"li.a","background:#242");
				break;
			case 2:
				x.css("html","background:#421;color:#830").css("a","color:#a32").css(".stp","background-color:#a30").css(".brk","background-color:#a30");
				x.css(ec.ln,"li","background:#421;color:#830;border:1px dashed #720;border-top:0;border-bottom:0");
				x.css(ec.ln,"li.a","background:#830;color:#430");
				x.css(ajaxsts,"background:#421;border:1px dashed #830");
				break;
			}
			x.style_().spanh(ajaxsts);
		}
		x.divo(this);
		if(hasbit(bit_show_logo)){
			logo_to(x);
			copyright_to(x);
		}
		if(hasbit(bit_show_pramble)){
			pramble_to(x);
			x.nl();
		}
		if(hasbit(bit_show_schematics))schematics_to(x);
		if(hasbit(bit_show_instructions_table))instructions_table_to(x);
		if(hasbit(bit_show_screen))x.r(ra);
		if(hasbit(bit_menu))
			x.divo().ax(this,"r",""," reset","r").ax(this,"f",""," frame","f").ax(this,"s",""," step","s").ax(this,"g",""," go","g")
					.ax(this,"b",""," bench","b").ax(this,"e",""," exec","e")
					.div_();
		x.divo("laycent");
		x.table().tr();
		if(hasbit(bit_show_panel))x.td().divo("float panel").spanh(st,"fontbold").rdiv(sy).rdiv(re).rdiv(ca).rdiv(lo).div_();
		if(hasbit(bit_show_rom))x.td().divo(ro,"float panel").r(ro).div_();
		if(hasbit(bit_show_source_editor))x.td().divh(ec,"float textleft panel");
		x.table_();
		x.div_();
		if(pt()==null){
			x.divo("floatclear");
			x.p("clare 20 bit  ").p(cor.registers.length).p(" registers  ").p(ro.ints.length>>10).p(" kb rom  ").p(ra.ints.length>>10).p(" kb ram");
			x.nl();
			x.p("theme: ").inptxt(th,this,"m","nbr").p("  display-bits:").inptxt(bi,this,"m","nbr").div_();
		}
	}
	public boolean hasbit(final int bit){
		return (bi.toint()&bit)==bit;
	}
	/**change theme/reload*/
	synchronized public void x_m(xwriter x,String s) throws Throwable{
		pl("x_t");
		x.xuo(this);
		x.xfocus(th);
	}
	/** reset */
	public void x_r(xwriter x,String s) throws Throwable{
		pl("x_r");
		going=false;
		cor.reset();
		cor.meter_frames=cor.meter_instructions=0;
		st.set("reseted");
		if(x==null) return;
		xj_update_focus_on_rom(x);
		x.xu(st,sy,re,ca,lo);
		if(hasbit(bit_show_screen)) ra.xupd(x);
	}
	/** step */
	synchronized public void x_s(final xwriter x,final String s) throws Throwable{
//		pl("x_n");
		final boolean refresh_display=cor.loading_register==-1&&((cor.instruction&0x00d8)==0x00d8||(cor.instruction&core.op_stc)==core.op_stc);
		st.clr();
		cor.step();
		if(x==null)
			return;
		x.xu(st.set(cor.meter_instructions));
		if(hasbit(bit_show_screen)&&refresh_display)
			ra.xupd(x);
		x.xu(sy,re,ca,lo);
		if(hasbit(bit_show_rom))xj_update_focus_on_rom(x);
	}
	private boolean going;
	/** go */
	synchronized public void x_g(final xwriter x,final String s) throws Throwable{
		final long sleep_ms=50;
		pl("x_n");
		int i=0;
		going=true;
		while(going){
			pl("x_g "+i++);
			x_s(x,null);
			x.flush();
			if(sleep_ms>0)
				Thread.sleep(sleep_ms);
		}
	}
	/** execute, update screen */
	synchronized public void x_e(final xwriter x,final String s)throws Throwable{
		final long sleep_ms=100;
		pl("x_e");
		going=true;
		while(going){
			step_frame();
			if(x==null)
				continue;
			if(hasbit(bit_show_screen))
				ra.xupd(x);
			x.flush();
			if(sleep_ms>0)
				Thread.sleep(sleep_ms);
		}
	}
	void xj_update_focus_on_rom(xwriter x){
		if(hasbit(bit_show_rom))ro.xfocus_on_binary_location(x,cor.program_counter);
	}
	private long runms=1000;
	synchronized public void x_b(final xwriter x,final String s) throws Throwable{
		pl("x_u");
		going=false;
		if(x!=null) x.xu(st.set("benching "+runms+" ms")).flush();
		long t0=System.currentTimeMillis();
		cor.meter_instructions=0;
		cor.meter_frames=0;
		long dt=0;
		while(true){
			cor.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(cor.is_instruction_eof()) ev(null,this);//refresh display
			if(dt>runms) break;
		}
		if(dt==0) dt=1;
		final xwriter y=new xwriter();
		y.p_data_size(cor.meter_instructions*1000/dt).spc().p("ips").spc().p_data_size(cor.meter_frames*1000/dt).spc().p("fps");
		st.set(y.toString());
		if(x==null) return;
		x.xu(st,re,ca,lo);
		xj_update_focus_on_rom(x);
		if(hasbit(bit_show_screen)) ra.xupd(x);
	}
	/** runtobreakpoint */
	synchronized public void x_l(xwriter x,String s) throws Throwable{//? doesnotstopafterconnectionclose
		if(x!=null) x.xu(st.set("running to breakpoint")).flush();
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
			if(!go) break;
		}
		if(x==null) return;
		x.xu(st,sy,re,ca,lo);
		xj_update_focus_on_rom(x);
		ra.xupd(x);
	}
	/** stepframe */
	synchronized public void x_f(final xwriter x,final String s) throws Throwable{
		pl("x_f");
		step_frame();
		if(x==null) return;
		xj_update_focus_on_rom(x);
		x.xu(st,sy,re,ca,lo);
		if(hasbit(bit_show_screen))ra.xupd(x);
	}
	private void step_frame() {
		final long t0=System.nanoTime();
		try{
			cor.meter_instructions=0;
			boolean loop=true;
			while(true){
				cor.step();
				if(loop==false)break;
				if(cor.meter_instructions>1024*1024)loop=false;
				if(cor.is_instruction_eof())loop=false;
			}
			final long dt=(System.nanoTime()-t0)/1000;
			final long dinstr=cor.meter_instructions;
			st.set(new xwriter().p("#").p(cor.meter_frames).spc().p_data_size(dinstr).spc().p(dt).spc().p("us").toString());
		}catch(Throwable t){
			st.set(stacktrace(t));
		}
	}
	public void logo_to(final xwriter x){
		final int con_wi=64;
		for(int k=0;k<con_wi;k++)
			x.p(Math.random()>.5?'-':' ');
		x.nl();
		x.p("clare").spc().p_data_size(cor.ram.length).nl();
		for(int k=0;k<con_wi;k++)
			x.p(Math.random()>.5?'-':' ');
		x.nl();
	}
	static public void copyright_to(final xwriter x){
		//		x.pl("(c) 1984 some rights reserved ltd");
	}
	public void schematics_to(final xwriter x){
		x.pl("|_______|______|______|______|   ");
		x.pl("|z n x r|c i ..|   imm12     |   ");
		x.pl("|_______|______|______|______|   ");
		x.pl("\\1 2 4 8\\. . ..\\......\\......   ");
		x.pl(" \\    n  \\      \\......\\......  ");
		x.pl("  \\z n x r\\c i ..\\aaaaaa\\dddddd");
		x.pl("   \\e e t e\\a m                ");
		x.pl("    \\r g   t l  m              ");
		x.pl("     \\o       l                ");
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
		x.pl(":   li : "+fld("x000",Integer.toHexString(0x00))+" : next instr to reg[x] :");
		x.pl(": call : "+fld("ii00",Integer.toHexString(0x10))+" : imm12                :");
		x.pl(":   lp : "+fld("d000",Integer.toHexString(0x100))+" : loop r[d] times      :");
		x.pl(":  skp : "+fld("ii00",Integer.toHexString(0x80))+" : pc+=imm12            :");
		x.pl(":   st : "+fld("xy00",Integer.toHexString(0xd8))+" : ram[y]=x             :");
		x.pl(":  stc : "+fld("da00",Integer.toHexString(0x40))+" : ram[a++]=d           :");
		x.pl(":   ld : "+fld("da00",Integer.toHexString(0xf8))+" : d=ram[a]             :");
		x.pl(":  ldc : "+fld("da00",Integer.toHexString(0xc0))+" : d=ram[a++]           :");
		x.pl(":  ldd : "+fld("da00",Integer.toHexString(0xb8))+" : d=ram[--a]           :");
		x.pl(":  shf : "+fld("di00",Integer.toHexString(0x60))+" : r[d]>>=i             :");
		x.pl(":  shf : "+fld("di00",Integer.toHexString(0x60))+" : r[d]<<=i             :");
		x.pl(":  not : "+fld("d000",Integer.toHexString(0x60))+" : r[d]=~r[d]           :");
		x.pl(":  inc : "+fld("d000",Integer.toHexString(0x200))+" : r[d]++               :");
//		x.pl(":  dec : "+fld("x000",Integer.toHexString(0xa8))+" : --r[x]               :");
		x.pl(":  neg : "+fld("d000",Integer.toHexString(0x300))+" : r[d]=-r[d]           :");
		x.pl(":  add : "+fld("da00",Integer.toHexString(0xa0))+" : r[a]+=r[d]           :");
		x.pl(": addi : "+fld("ia00",Integer.toHexString(0x18))+" : r[a]+=i              :");
		x.pl(":   tx : "+fld("da00",Integer.toHexString(0xe0))+" : r[a]=r[d]            :");
		//		x.pl(":  skp : "+fld("im00",Integer.toHexString(opskp))+" : pc+=imm8             :");
		x.pl(":  sub : "+fld("da00",Integer.toHexString(0x20))+" : r[a]-=r[d]           :");
		x.pl(":  dac : "+fld("d000",Integer.toHexString(0x400))+" : dac=r[d]             :");
		x.pl(":  eof : ffff : end-of-frame         :");
		x.pl(":------:------:----------------------:");
//		x.pl(":      : ..18 : cr invalids          :");
//		x.pl(": wait : "+fld("x000",Integer.toHexString(0x58))+" : wait                 :");
//		x.pl(":notify: "+fld("x000",Integer.toHexString(0x78))+" : notify               :");
//		x.pl(":------:------:----------------------:");
	}

	private static final long serialVersionUID=1;

	final static String fld(final String def,final String s){
		final String s1=s.length()>def.length()?s.substring(s.length()-def.length()):s;
		final int a=def.length()-s1.length();
		if(a<0) return s1;
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
