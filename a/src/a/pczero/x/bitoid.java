package a.pczero.x;
import a.pczero.ram;
import a.pczero.vintage;
import a.x.jskeys;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class bitoid extends a{
	static final long serialVersionUID=1;
	public String styleolli="background-color:#222;min-width:12em;text-align:left";
	public String styletextarea="background-color:#000;width:11em;text-align:left";

	boolean disppramble;
	@SuppressWarnings("static-access")
	void pramble(final xwriter x){
		x.pl("vintage 16 bit computing - bitoid quad core ibobs");
		x.pl("  16b instruction znxr ci.. aaaa dddd ");
		x.pl(" "+vintage.strdatasize(ram.size)+" x 16b video ram "+ram.width+"x"+ram.height);
		x.pl(" "+size+" threads x");
		x.pl("   "+core[0].regs.size+" registers");
		x.pl("   "+vintage.strdatasize3(core[0].rom.size)+"x 16b code cache");
		x.pl("   16 sprites");
//		x.pl(" and right display functioning");
		x.nl();
		vintage.prambleops(x);
	}
	public ram vram;
	public a sts;
	boolean running;
	final static int size=4;
	private vintage[]core=new vintage[size];
	public int dispbits=1;
	{for(int i=0;i<core.length;i++){
		final vintage c=new vintage();
		c.mode=1;
		c.coreid.set(""+(char)('a'+i));
		c.ram=vram;
		c.dispbits=16;
//		c.src.libgbrkpt="#030";
//		c.src.libgstep="#600";
//		c.src.libghvr="#040";
		core[i]=c;
		c.pt(this).nm("co"+i);
	}}
//	private srcviwr[]srcs=new srcviwr[size];
//	{for(int i=0;i<srcs.length;i++)srcs[i]=(srcviwr)ax1[i].src.pt(this).nm("src"+i);}
	protected a chldq(final String id){
		if(id.startsWith("co")){
			return core[Integer.parseInt(id.substring("co".length()))];
		}else
			return super.chldq(id);
	}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null){
			x.el(this,"display:inline-block;border:0px solid red;box-shadow:0 0 1em rgba(0,0,0,1)");
			x.style("body","font-size:11px;margin:0 0 0 1em;color:#444;background:#111;text-align:center");
			final String id=id();
			final jskeys jskeys=new jskeys(x);
			x.script();
			jskeys.add("cS","$x('"+id+" s')");//save
			jskeys.add("cL","$x('"+id+" l')");//load
			jskeys.add("cT","$x('"+id+" t')");//step
			jskeys.add("cR","$x('"+id+" r')");//reset
			jskeys.add("cF","$x('"+id+" f')");//frame
			jskeys.add("cG","$x('"+id+" g')");//go
			jskeys.add("cU","$x('"+id+" u')");//run
			jskeys.add("cO","$x('"+id+" c')");//compile
			jskeys.add("cD","$x('"+vram.id()+" rfh')");
			jskeys.add("cB","$x('"+id+" b')");//run to break point
			x.script_();
		}else
			x.el(this);

		if(disppramble){
			x.pre();
			pramble(x);
		}
		if((dispbits&1)!=0){
			x.nl();
			x.style(vram,"border:1px solid #030;display:inline-block");
			vram.to(x);
			x.nl();
		}
		x.ax(this,"l"," load");
		x.ax(this,"c"," compile");
		x.ax(this,"r"," reset");
		x.ax(this,"f"," frame");
		x.ax(this,"s"," save");
		x.ax(this,"u"," run");
		x.ax(this,"t"," step");
		x.ax(this,"g"," go");
		x.ax(this,"b"," run-to-breakpoint");
		if((dispbits&1)!=0)x.ax(this,"d"," refresh-display");
		x.nl();
		x.spc();
		x.span(sts,"font-weight:bold");
		x.nl();
		for(final vintage c:core){
			x.el("padding:.5em;display:inline-block;vertical-align:top;text-align:center;border:0px solid yellow");
			x.style(c,"ol li",styleolli);
			x.style(c,"textarea",styletextarea);
			x.style(c,"ol li:hover","cursor:pointer;background-color:red");
			x.style(c,"ol li.stp","cursor:pointer;background-color:red");

			c.to(x);
//			x.style(c,"display:inline-block;text-align:left;vertical-align:top;border:0px solid red");
//			x.style(c.src.txt,"width:16em");
			x.el_();
//			x.spc();
		}
		x.el_();
	}
	synchronized public void x_l(xwriter x,String s)throws Throwable{
		int i=0;
		for(final vintage c:core){
			final path p=req.get().session().path(getClass().getName()).get("c"+i+".src");
			if(p.exists()){
				c.src.txt.from(p);
				c.sts.set("loaded "+p.name());
			}else{
				c.src.txt.set("..");
				c.sts.set("loaded default");
			}
			if(x!=null){
				x.xuo(c);
			}
			i++;
		}
		sts.set("loaded");
		if(x==null)return;
		x.xu(sts);
//		for(final x1 c:core)x.xu(c);
	}
	synchronized public void x_c(xwriter x,String s)throws Throwable{
		for(final vintage e:core){
			e.x_c(null,null);
		}
		sts.set("compiled");
		if(x==null)return;
		x.xu(sts);
		for(final vintage c:core)x.xu(c);
	}
	public void x_t(xwriter x,String s)throws Throwable{
		for(final vintage c:core){
			c.x_n(x,null);
		}
		if(x==null)return;
		x.xu(sts.set("stepped"));
	}
	synchronized public void x_d(xwriter x,String s)throws Throwable{
		if((dispbits&1)==0)return;
		vram.x_rfh(x,s);
	}
	synchronized public void x_g(xwriter x,String s)throws Throwable{
		x.xu(sts.set("going"));
		while(true){
			x_t(x,s);
			x.flush();
			Thread.sleep(500);
		}
	}
	/**run to break point*/
	synchronized public void x_b(xwriter x,String s)throws Throwable{
		if(running)throw new Error("already running");
		running=true;
		final long t0=System.currentTimeMillis();
		final long instr0=core[0].mtrinstr;
		for(final vintage c:core)c.sts.clr();
		while(running){
			boolean go=true;
			for(final vintage c:core){
				c.step();
				final int srclno=c.lino.get(c.pcr);
				if(c.src.isonbrkpt(srclno)){
					c.sts.set("breakpoint @ "+srclno);
					go=false;
				}
			}
			if(core[0].wasrerun){
				core[0].wasrerun=false;
				ev(null,this);//refresh display
			}
			if(!go)break;
		}
		if(running){
			running=false;
			if(x==null)return;
			final long dt=System.currentTimeMillis()-t0;
			final long dinstr=core[0].mtrinstr-instr0;
			x.xu(sts.set("core0 "+dinstr+" instr, "+dt+" ms"));
			for(final vintage c:core){
				x.xu(c);
				c.xfocusline(x);
			}
			if((dispbits&1)!=0){
				vram.x_rfh(x,s);
			}
		}
	}
	/**run*/
	synchronized public void x_u(xwriter x,String s)throws Throwable{
		final long runms=1000;
		if(x!=null)x.xu(sts.set("running "+runms+" ms")).flush();
		long minstr=0;for(final vintage c:core){minstr+=c.mtrinstr;c.sts.clr();}
		final long mframes=core[0].mtrframes;
		long t0=System.currentTimeMillis();
		long dt;
		while(true){
			for(final vintage c:core)c.step();
			if(core[0].wasrerun){
				core[0].wasrerun=false;
				ev(null,this);//refresh display
			}
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(dt>runms)
				break;
		}
		long minstr1=0;for(final vintage c:core)minstr1+=c.mtrinstr;
		final long dminstr=minstr1-minstr;
		final long dmframes=core[0].mtrframes-mframes;
		if(x==null)return;
		x.xu(sts.set(vintage.strdatasize3((long)dminstr*1000/dt)+"ips, "+vintage.strdatasize3((long)dmframes*1000/dt)+"fps"));
		for(final vintage c:core){
			x.xu(c);
			c.xfocusline(x);
		}
		x.flush();
		if((dispbits&1)!=0){
			vram.x_rfh(x,s);
		}
	}
	/**run core frame*/
	@SuppressWarnings("static-access")
	synchronized public void x_f(xwriter x,String s)throws Throwable{
		if(running)throw new Error("already running");
		running=true;
		long t0=System.currentTimeMillis();
		final int waitforcore=0;
		long minstr=core[waitforcore].mtrinstr;
		long dt;
		final long runms=1000;
		boolean timedout=false;
		while(true){
			for(final vintage c:core)c.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(core[waitforcore].wasrerun){
				core[waitforcore].wasrerun=false;
				break;
			}
			if(dt>runms){
				timedout=true;
				break;
			}
		}
		if(running){
			running=false;
			final long dminstr=core[waitforcore].mtrinstr-minstr;
			if(x==null)return;
			sts.set("core "+waitforcore+": "+(timedout?"timed-out ":"")+dminstr+" instr, "+dt+"ms, "+dminstr/vram.size+" instr/px");
			x.xu(sts);
			for(final vintage c:core){
				x.xu(c);
				c.xfocusline(x);
			}
			ev(null,this);
			if((dispbits&1)!=0){
				x.xu(sts.set("refreshing display")).flush();
				vram.x_rfh(x,s);
			}
		}
	}

	public void x_r(xwriter x,String s)throws Throwable{
		running=false;
		if(x!=null)x.xu(sts.set("reseting")).flush();
		for(final vintage c:core){
			c.x_r(null,null);
			c.src.edit=false;
		}
		core[0].x_r(null,null);//hack copies rom to ram
		ev(null,this);
		if(x==null)return;
		for(final vintage c:core){
			x.xu(c);
			c.xfocusline(x);
		}
		if((dispbits&1)!=0){
			x.xu(sts.set("refreshing display")).flush();
			vram.x_rfh(x,null);
		}
		x.xu(sts.set("reseted"));
	}
	synchronized public void x_s(xwriter x,String s)throws Throwable{
		int i=0;
		for(final vintage c:core){
			final path p=req.get().session().path(getClass().getName()).get("c"+(i++)+".src");
			c.src.txt.to(p);
		}
		sts.set("saved in "+getClass().getName());
		if(x==null)return;
		x.xu(sts);
	}
	protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(x==null&&o instanceof Integer){//notify
			final int i=((Integer)o).intValue();
			core[i].notify=true;
		}else super.ev(x,from,o);
	}
}
