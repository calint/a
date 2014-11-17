package a.rch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import b.a;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class index extends a{
	private static final long serialVersionUID=1L;
	public a sts;
	public void to(xwriter x) throws Throwable{
		session ses=req.get().session();
		String basehref=ses.href();
		path p=ses.path("rch/store.ix");
		if(!p.exists()){
			return;
		}
		InputStream is=p.fileinputstream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf8"));
		x.pre().nl();
		x.r(sts);
		int lineno=0;
		Set<String> set=new HashSet<String>();
		for(String line=br.readLine();line!=null;line=br.readLine()){
			String hash=line.substring(0,32);
			String type=line.substring(109);
			String size=line.substring(36,49);
			String datestr=line.substring(53,73);
			String href=basehref+$.store_dir+$.filename_for_hash(hash)+"."+type;
			set.add(type);
			x.a(href).p(type).a_();
			x.p("    ").p(size);
			x.p("    ").p(datestr);
			x.p("    ").p(++lineno);
			x.nl();
		}
		x.pre_();
		x.script();
		sts.set(set.toString());
		x.script_();
		is.close();
	}
}
