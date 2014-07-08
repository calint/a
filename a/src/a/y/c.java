package a.y;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import a.x.osinc;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class c extends a {
	static final long serialVersionUID=1;
	private path pathroot(){return req.get().session().path("");}
	private a[]menu=new a[3];
	{for(int n=0;n<menu.length;n++){
		menu[n]=new a(this,"0"+n);
	}}
	protected a chldq(final String id){
		if(id.charAt(0)=='0')
			return menu[Integer.parseInt(id)];
		return super.chldq(id);
	}
	public void to(final xwriter x)throws Throwable{
		final String qry=req.get().query();
		final String[]qparts=b.isempty(qry)?new String[0]:qry.split("/");
		x.pre();
		x.span(menu[0]).nl();
		x.span(menu[1]).nl();

		path p=pathroot();
		//p.assureacess
		x.script();
		int i=0;
		for(final String s:qparts){
			updlvl(x,i++,p,s);
			p=p.get(s);
			//p.assureacess
		}
		x.script_();
		x.div("editor2");
		if(p.isfile())try{
			final Map<String,Object>ctx=new HashMap<String,Object>();
			ctx.put("blank","blank••");
			p.to(new osinc(x.outputstream(),p.parent(),ctx,this));}catch(Throwable t){
			x.pl(b.stacktraceline(t));
			}
		else{
			x.script();
			updlvl(x,i++,p,"");
			x.script_();
		}
		x.div_();
			
	}
	private void updlvl(final xwriter x,final int lvl,final path pth,final String sel){
		final xwriter y=new xwriter();
		final session ses=req.get().session();
		for(final String s:pth.list()){
			y.p(" • ");
			if(s.equals(sel))
				y.p("[").p(s).p("]");
			else
				y.p("<a href=c?").p(b.urlencode(ses.inpath(pth.get(s)))).p(">").p(s).a_();
		}
		x.xu(menu[lvl],y.toString());
	}
	
	public String name="blankø";
	public String funcu(String s){return "fun¢u"+s;}
	public static String func2(){return "func2ti";}
	public static void funcu3(OutputStream os,String args)throws IOException{
		os.write(b.tobytes("hello world ("+args+")"));
	};
	public void funcu4(OutputStream os,String args)throws IOException{
		os.write(b.tobytes("2hello world ("+args+")"));
	};
}
