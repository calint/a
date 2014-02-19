package a.rch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import b.a;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class browser extends a{
	private static final long serialVersionUID=1L;
	private long itemno=1;
	public void to(xwriter x) throws Throwable{
		session ses=req.get().session();
		String basehref=ses.href();
		path p=ses.path("rch/store.ix");
		if(!p.exists()){
			x.p("no index");
			return;
		}
		InputStream is=p.fileinputstream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf8"));
		int lineno=0;
		for(String line=br.readLine();line!=null;line=br.readLine()){
			lineno++;
			if(itemno!=lineno)
				continue;
			String hash=line.substring(0,32);
			String type=line.substring(109);
			String size=line.substring(36,49);
			String datestr=line.substring(53,73);
			String ref=$.store_dir+$.filename_for_hash(hash)+"."+type;
			String href=basehref+ref;
			x.ax(this,"nxt","::next");
			x.ax(this,"prv","::previous");
			x.ax(this,"rm "+ref,"::delete");
			x.br().p("item #").p(itemno).p(" ").p(size).p(" ").p(datestr);
			x.br();
			x.p("<img src=\"").p(href).p("\">");
			break;
		}
		br.close();
	}
	public void x_prv(xwriter x,String s){
		if(itemno>1)
			itemno--;
		x.xreload();
	}
	public void x_nxt(xwriter x,String s){
		itemno++;
		x.xreload();
	}
	public void x_rm(xwriter x,String s) throws IOException{
		path pth=req.get().session().path(s);
		pth.rm();
		x.xreload();
	}
}
