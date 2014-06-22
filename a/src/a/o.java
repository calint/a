package a;
import java.io.*;
import java.util.*;
import a.x.*;
import b.*;
public class o extends a{
	static final long serialVersionUID=1;
	public a opts;
	public a f1;
	public a fc;
	public a ed;
	public a st;
	private path dir=req.get().session().path("");
	public void to(final xwriter x)throws Throwable{
		final String id=id();
		final jskeys js=new jskeys(x);
//		js.open();
		js.add("cW","$x('"+id+" up')");
		js.add("cS","$x('"+id+" u')");
		js.add("cE","$x('"+id+" e')");
		js.close();
		x.nl();
		final String val=toString();
		x.pre().code();
		x.spanh(opts).nl();
		x.p("   “");
		x.tago("input").attr("value",val).attr("title","label").attrdef(this).attr("type","text").attr("class","keywords");
		x.attr("onkeyup","if(!event)event=window.event;if(event.keyCode==13)return $r(event,this,'"+id+" s');$b(this);$d(event.keyCode);if(event.keyCode==40){$d('scroll down');}else if(event.keyCode==38)$d('scroll up');$x('"+id+" a');return false;");
		x.tagoe().p("” ·");
//		x.ax(this,"v","ᓂ·");
		x.ax(this,"e","ᓃ·");
		x.ax(this,"u","◍·");
		x.ax(this,"c","◧·");
		x.spanh(f1);
		x.focus(this);
		x.nl().spanh(st);
		if(b.isempty(val)&&dispallopts())
			refreshopts();

		x.style().css(ed,"height:100%;width:100%").styleEnd();
		x.p("<div style='height:320px;width:100%;border:1px dotted blue'>");
		x.span(fc).inputTextArea(ed,null);
		x.divEnd();
		x.script().xhide(fc).xhide(ed).scriptEnd();
	}
	private int refreshopts(){
		final String[]opls=options();
		final ByteArrayOutputStream baos=new ByteArrayOutputStream();
		final xwriter y=new xwriter(baos);
		for(final String f:opls)
			y.tago("span").attr("style","font-size:"+(9+b.rndint(0,8))+"px"+(dir.get(f).isdir()?";color:brown":"")).tagoe().p(f.toString()).spanEnd();
		y.flush();
		opts.set(y.toString());
		return opls.length;
	}
	public void x_a(final xwriter x,final String s)throws Throwable{
		final String val=toString();
		if(!b.isempty(val)||dispallopts()){
			refreshopts();
			x.xu(f1.clr());
//			x.xhide(st);
//			x.xhide(fc);
//			x.xhide(ed);
		}else
			opts.clr();
		x.xu(opts);
	}
	public void x_c(final xwriter x,final String s)throws Throwable{
		final String val=toString();
		final path newdir=dir.get(val);
		dir.mkdirs();
		dir=newdir;
		updinfo(x,dir);
//		x.xshow(st);
		//x.xalert("created dir "+dir);	
	}
	private String strax(final String axpbfn,final String arehhtml){
		final StringBuilder sb=new StringBuilder(64);
		sb.append("<a href=\"javascript:$x('").append(id()).append(" ").append(axpbfn).append("')\">").append(arehhtml).append("</a>");
		return sb.toString();
	}
	public void x_s(final xwriter x,final String s)throws Throwable{
		String nm=toString();
		if("..".equals(nm)){
			if(dir.equals(req.get().session().path())){
				x.xu(clr());
				refreshopts();
				x.xu(opts);
				return;
			}
			dir=dir.parent();
			nm="";
			x.xu(this,set(nm).toString());
			updinfo(x,dir);
//			x.xshow(st);
			refreshopts();
			x.xu(opts);				
			x.xu(f1.set(strax("d","⌫")));
			return;
		}
		final String[]opls=options();
		final boolean create=opls.length==0;
		final String name=create?nm:opls[0];
		final path pth=dir.get(name);
		if(create){
			pth.append("new file","\n\n");
			x_e(x,"");
//			fc.read(pth);
//			x.xupd(fc);
//			x.xshow(fc);
		}else{
			x.xu(this,set(name).toString());
			x.xfocus(this);
		}
//		x.xshow(st);
		updinfo(x,pth);
		if(pth.isdir()){
			dir=pth;
			x.xu(this,set("").toString());
			refreshopts();
			x.xu(opts);				
		}else{
			x.xu(opts.clr());
			if(!create)
				x_v(x,"");
		}
		x.xu(f1.set(strax("d","⌫")));
	}
	private void updinfo(final xwriter x,final path pth)throws Throwable{
		x.xu(st.set("   href: "+pth.uri()+"\n   size: "+pth.size()+" B"+"\nlastmod: "+new Date(pth.lastmod()).toString()));
	}
	public void x_d(final xwriter x,final String s)throws Throwable{
		final String pth=toString();
		final path ptho=b.isempty(pth)?dir:dir.get(pth);
		if(ptho.equals(req.get().session().path(""))){
			return;
		}
		if(ptho.rm()){
			x.xu(f1.clr());
			dir=ptho.parent();
		}else
			x.xalert("could not delete:\n"+pth);
		x.xu(fc.clr());
		x.xu(st.clr());
//		x.xhide(ed);
//		x.xhide(fc);
		x.xu(this,set("").toString());
		refreshopts();
		x.xu(opts);
		x.xfocus(this);
	}
	public void x_e(final xwriter x,final String s)throws Throwable{
		x.xhide(fc);
		x.xshow(ed);
		final path pth=dir.get(toString());
		ed.from(pth);
		fc.clr();
		x.xu(fc).xu(ed).xfocus(ed);		
	}
	public void x_v(final xwriter x,final String s)throws Throwable{
		x.xshow(fc);
		x.xhide(ed);
		final path pth=dir.get(toString());
		if(pth.isdir()){
			updinfo(x,pth);
			x.xshow(st);
			return;
		}
		fc.from(pth);
		x.xu(fc);		
	}
	public void x_u(final xwriter x,final String s)throws Throwable{
		final path pth=dir.get(toString());
		ed.to(pth);
		updinfo(x,pth);
//		refreshopts();
//		x.xu(opts);
	}
	public void x_up(final xwriter x,final String s)throws Throwable{
		if(dir.equals(req.get().session().path())){
			x.xu(clr());
			refreshopts();
			x.xu(opts);
			updinfo(x,dir);
//			x.xshow(st);
			x.xfocus(this);
			return;
		}
		dir=dir.parent();
		x.xu(clr());
		updinfo(x,dir);
//		x.xshow(st);
		refreshopts();
		x.xu(opts);				
		x.xu(f1.set(strax("d","⌫")));
		x.xfocus(this);
		return;
	}

	protected boolean dispallopts(){return true;}
	protected String[]options(){
		final String qry=toString();
		final String[]filenames=dir.list(new FilenameFilter(){public boolean accept(final File dir,final String name){
				return name.startsWith(qry);
		}});
		return filenames;
	}
}
