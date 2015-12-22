package a.pcmega.x;
import a.pczero.vintage;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class tinitus extends a{
	static final long serialVersionUID=1;
	public vintage co;{
		co.dispbits-=9;
		final path p=req.get().session().path(getClass().getName()+"/a.src");
		co.setpth(p);
		co.sts.set(p.parent().name());
	}
	public audio tts;
	public void to(xwriter x)throws Throwable{
		if(pt()==null){
			x.el(this,"display:block;background-color:#eef;border:0px solid red;width:54em;padding-left:2em;margin-left:auto;margin-right:auto;box-shadow:0 0 .5em rgba(0,0,0,1);border-radius:0px");
//			x.pre();
			x.el("float:right;color:#040;text-shadow:0 0 5px #ff0000;padding:.25em;border:0px solid red;display:inline-table;box-shadow:0 0 .5em rgba(0,0,0,1);border-radius:0px");
			x.p("  tinitus "+(tts.samplerate/1000)+" kHz  ");
			x.el_();
//			x.el("float:right;color:#040;text-shadow:0 0 5px #ff0000;padding:.25em;border:0px solid red;display:inline-table;box-shadow:0 0 .5em rgba(0,0,0,1);border-radius:0px");
//			x.p("  vintage");
//			x.elend().nl();
			//		x.el("border:1px solid red;display:inline-table").p("tinitus "+x1.strdatasize3(tts.samplerate)+"Hz").elend();
//			x.nl();
		}else
			x.el(this);
//		x.pre();
		co.to(x);
		x.style(co.src.txt,"border:0px solid red;box-shadow:0 0 .25em rgba(0,0,0,1);border-radius:0px");
	}
	protected void ev(final xwriter x,final a from,final Object o)throws Throwable{
		if(x==null&&from==co&&o instanceof Short){
			tts.write(((Short)o).shortValue());
		}else super.ev(x,from,o);
	}
}
