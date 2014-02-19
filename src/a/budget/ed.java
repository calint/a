package a.budget;
import b.*;
public class ed extends a{
	static final long serialVersionUID=1;
	public a fnm;
	public a s;
	private String fnm_old;
	public void to(xwriter x)throws Throwable{
		final path p=b.path(b.urldecode(req.get().query()));
		if(!p.isin(b.path($.fsroot)))
			throw new Error("not in path: "+p);
		x.style();
		x.css("html","font-size:2em;padding-left:4em;line-height:1.5em");
		x.css(s,"border:1px dotted green;padding:.5em");
		x.styleEnd();
		fnm_old=p.name();
		fnm.set(fnm_old);
		if(!p.exists()||p.isdir())
			x.pl(p.uri());
		else
			x.p(p.parent().uri().substring($.fsroot.length())).p("/");
		final String txt;
		if(!p.exists()){
			txt="";
		}else{
			if(p.isdir())
				txt=p.get("0").readstr();
			else
				txt=p.readstr();
		}
		s.set(txt);
		x.inputText(fnm,null,this,"a").nl();
		x.nl();
		x.p("      : daily : weekly : mothly : yearly : install : uninstall :").nl();
		x.p("price : ").inputText(s,null,this,"a").nl();
		x.nl().p("    ").ax(this,"a","save");
		if(p.exists())
			x.focus(s);
		else{
			x.script().xfocus(fnm).pl("$('"+fnm.id()+"').select();");
		}
	}
	public void x_a(final xwriter x,final String s) throws Throwable{
		final path p=b.path(b.urldecode(req.get().query()));
		if(!p.isin(b.path($.fsroot)))
			throw new Error("not in path: "+p);
		if(p.isfile()){
			p.writestr(s.toString());
		}else if(p.isdir()||!p.exists()){
			p.get("0").writestr(s.toString());
		}
		if(!fnm.toString().equals(fnm_old)){
			p.rename(fnm.toString());
		}
		x.xlocation("/budget");
	}
}
