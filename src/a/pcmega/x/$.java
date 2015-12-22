package a.pcmega.x;
import java.util.ArrayList;
import java.util.List;
import a.pczero.vintage;
import b.a;
import b.path;
import b.req;
import b.xwriter;
final public class $ extends a{
	static final long serialVersionUID=1;
	private int ix;
	private void tabadd(final a e){
		lse.add(e.pt(this).nm(Integer.toString(lse.size())));
	}
	private final static display display=new display(null);
	private vintage vt=new vintage();
	private bitoid bt=new bitoid();
	private List<a>lse=new ArrayList<a>(4);
	{
		tabadd(new tinitus());
		final path p=req.get().session().path(vintage.class.getName()+"/a.src");
		vt.setpth(p);
		vt.sts.set(p.parent().name());
		tabadd(vt);
		tabadd(bt);
		bt.dispbits&=0xfffffffe;
		bt.styleolli="background-color:#fff;min-width:11em;text-align:left";
		bt.styletextarea="background-color:#eee;width:11em;text-align:left;box-shadow:0 0 1px #000;";
		ix=2;
		display.connect(bt.vram);
		display.refresh();
	}
	protected a chldq(final String id){return lse.get(Integer.parseInt(id));}
	public void x_(final xwriter x,final String s)throws Throwable{
		ix=Integer.parseInt(s);
		x.xuo(this);
	}
	protected void ev(final xwriter x,final a from,final Object o)throws Throwable{
		if(x==null&&from==vt&&o==null){//frame
			display.refresh();
		}else if(x==null&&from==bt&&o==null){
			display.refresh();
		}else super.ev(x,from,o);
	}
	public void to(final xwriter x)throws Throwable{
		x.el(this,"display:block;background-color:#eef;border:0px solid red;width:54em;padding-left:1em;margin-left:auto;margin-right:auto;box-shadow:0 0 .5em rgba(0,0,0,1);border-radius:0px");
		final String id=id();
		int i=0;
		for(final a e:lse){
			x.el("background-color:#"+(i==ix?"fff":"ddd")+";float:right;text-shadow:0 0 5px #000;padding:.25em;display:inline-table;box-shadow:0 0 .5em rgba(0,0,0,1);border-radius:0px");
			x.p("  ");
			x.tago("a").attr("href","javascript:$x('"+id+"  "+i+++"')").tagoe();
			x.p("   ").p(e.getClass().getName().substring(e.getClass().getPackage().getName().length()+1));
			x.tage("a");
			x.p("  ");
			x.el_();
		}
		x.nl();
		x.nl();
		x.el(id()+"$");
		lse.get(ix).to(x);
		x.el_();
		x.el_();
	}
}
