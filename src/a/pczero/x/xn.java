package a.pczero.x;
import a.pczero.ram;
import a.pczero.vintage;
import a.x.jskeys;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class xn extends a{
	static final long serialVersionUID=1;
	public boolean disppramble;
	@SuppressWarnings("static-access")
	void pramble(final xwriter x){
		x.pl("zn rix vintage multi core computing - notinca 16b ");
		x.pl(" "+vintage.strdatasize(ram.size)+" x 16b data");
		x.pl(" "+size+" threads x");
		x.pl("   "+core[0].regs.size+" registers");
		x.pl("   "+vintage.strdatasize3(core[0].rom.size)+"instructions");
		x.pl(" pre hlt version");
	}
	public ram ram;
//	public ram ram2;
	public a sts;
	final static int size=16;
	private vintage[]core=new vintage[size];
	{for(int i=0;i<core.length;i++){
		final vintage c=new vintage();
		c.mode=1;
		c.ram=ram;
		c.coreid.set(""+(char)('a'+i));
		c.dispbits=8;
		c.pt(this).nm("core"+i);
		core[i]=c;
	}}
//	private srcviwr[]srcs=new srcviwr[size];
//	{for(int i=0;i<srcs.length;i++)srcs[i]=(srcviwr)ax1[i].src.pt(this).nm("src"+i);}
	protected a chldq(final String id){
//		if(id.startsWith("src")){
//			return srcs[Integer.parseInt(id.substring("src".length()))];
//		}else 
		if(id.startsWith("core")){
			return core[Integer.parseInt(id.substring("core".length()))];
		}else
			return super.chldq(id);
	}
	public void to(final xwriter x)throws Throwable{
		x.el(this);
		if(pt()==null){
//			x.nl().style();
//			x.nl().cssfont("tini","/ttf/tini.ttf");
//			x.nl().css("body","background:#222;font-size:4px;font-family:tini;margin:1em 0 0 1em");
//			x.nl().cssfont("tini","/ttf/tini.ttf");
//			x.nl().styleEnd();
			x.nl().style("body","font-size:4px;font-family:tini;margin:1em 0 0 1em");
//			x.nl().style("body","color:#343;background:#222;");
			final String id=id();
			final jskeys jskeys=new jskeys(x);
			x.script();
			jskeys.add("cS","$x('"+id+" s')");
			jskeys.add("cL","$x('"+id+" l')");
			jskeys.add("cT","$x('"+id+" t')");
			jskeys.add("cR","$x('"+id+" r')");
			jskeys.add("cF","$x('"+id+" f')");
			jskeys.add("cG","$x('"+id+" g')");
			jskeys.add("cU","$x('"+id+" u')");
			jskeys.add("cO","$x('"+id+" c')");
			jskeys.add("cD","$x('"+ram.id()+" rfh')");
			x.scriptEnd();
		}
		
		if(disppramble){
			x.pre();
			pramble(x);
		}
		x.el("display:inline-block;border:1px solid #080");
		ram.to(x);
		x.elend();
		x.nl();
		x.el("display:inline-block;border:0px solid #f00");
		x.ax(this,"l"," load");
		x.ax(this,"c"," compile");
		x.ax(this,"r"," reset");
		x.ax(this,"t"," step");
		x.ax(this,"g"," go");
		x.ax(this,"f"," frame");
		x.ax(this,"u"," run");
		x.ax(this,"s"," save");
		x.spc().span(sts);
		x.elend();
		x.nl();
		for(final vintage c:core){
			x.el("display:inline-block;vertical-align:top;border:0px solid red");
			c.to(x);
			x.elend();
		}
		x.elend();
	}
	synchronized public void ax_l(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final vintage c:core){
			final path path=req.get().session().path(getClass().getName());
			final path p=path.get("c"+i+".src");
			if(p.exists())
				c.src.txt.from(p);
			else
				c.src.txt.set("..");
			if(x!=null)
				x.xuo(c);
			i++;
		}
		sts.set("loaded");
		if(x==null)return;
		x.xu(sts);
	}
	synchronized public void x_c(xwriter x,String s)throws Throwable{
		for(final vintage e:core){
			e.x_c(null,null);
		}
		sts.set("compiled");
		if(x==null)return;
		x.xu(sts);
	}
	public void x_t(xwriter x,String s)throws Throwable{
		if(running){
			running=false;
			return;
		}
		for(final vintage c:core)
			c.x_n(x,null);
		sts.set("stepped");
	}
	synchronized public void x_g(xwriter x,String s)throws Throwable{
		if(running)return;running=true;
		x.xu(sts.set("going"));
		while(running){
			x_t(x,s);
			x.flush();
			Thread.sleep(500);
		}
		running=false;
	}
	/**run*/
	private boolean running;
	synchronized public void x_u(xwriter x,String s)throws Throwable{
		running=true;
		long t0=System.currentTimeMillis();
		long minstr=0;for(final vintage c:core){minstr+=c.mtrinstr;c.sts.clr();}
		final long mframes=core[0].mtrframes;
		long dt=0;
		final long runms=1000;
		while(running){
			for(final vintage c:core)c.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(dt>runms)
				break;
		}
		long minstr1=0;for(final vintage c:core)minstr1+=c.mtrinstr;
		final long dminstr=minstr1-minstr;
		final long dmframes=core[0].mtrframes-mframes;
		running=false;
		if(x==null)return;
		if(dt==0)dt=1;
		sts.set(vintage.strdatasize3((long)dminstr*1000/dt)+"ips\n"+vintage.strdatasize3((long)dmframes*1000/dt)+"fps");
		x.xu(sts);
		for(final vintage c:core){
			x.xu(c);
			c.xfocusline(x);
		}
		snapshot();
		ram.x_rfh(x,s);
	}

	public void x_r(xwriter x,String s)throws Throwable{
		for(final vintage e:core)
			e.x_r(null,null);
		if(x==null)return;
		x.xuo(this);
		sts.set("reseting");
		x.flush();
		ram.x_rfh(x,null);
		sts.set("reseted");
	}
	synchronized public void x_s(xwriter x,String s)throws Throwable{
		int i=0;
		for(final vintage c:core){
			final path p=req.get().session().path(getClass().getName()).get("c"+(i++)+".src");
			c.src.txt.to(p);
		}
		sts.set("saved in xn");
		if(x==null)return;
		x.xu(sts);
	}
	private void snapshot()throws Throwable{
//	    final BufferedImage bi=new BufferedImage(ram.width,ram.height,BufferedImage.TYPE_USHORT_GRAY);
//	    final DataBuffer db=bi.getData().getDataBuffer();
//	    for(int i=0;i<ram.size;i++){
//	    	db.setElem(i,0xffffffff);
//	    }
//	    final path p=req.get().session().path("xn/snp/img.png");
//	    ImageIO.write(bi,"png",p.outputstream(false));
	}
}
