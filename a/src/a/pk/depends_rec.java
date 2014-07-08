package a.pk;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import a.x.cli;
import b.a;
import b.b;
import b.osnl;
import b.req;
import b.xwriter;
final public class depends_rec extends a{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String qs=URLDecoder.decode(r.query(),"utf8").toLowerCase().replace(';',' ').replace('&',' ').trim();
		list.clear();
		depends(x,r,qs,"");
	}
	private List<String> list=new ArrayList<String>(b.K);
	private void depends(final xwriter x,final req rt,final String qs,final String indent) throws Throwable{
		new cli("sh",new osnl(){
			@Override public void onnewline(String line) throws Throwable{
				String s=line.toLowerCase();
				int i=s.lastIndexOf(":");
				if(i==-1)
					return;
				if(s.indexOf("depends:")==-1)
					return;
				s=s.substring(i+1).trim();
				//System.out.println(s);
				for(int n=0;n<indent.length();n++)
					x.p("·");
				x.a(rt.path()+"?"+s).p(s).a_();
				if(list.contains(s)){
					x.p("*");
					x.br().nl();
					return;
				}else
					list.add(s);
				x.br().nl();
				depends(x,rt,s,indent+"  ");
			};
		}).p("apt-cache depends ").p(qs).exit();
	}
}
