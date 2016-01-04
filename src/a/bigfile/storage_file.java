package a.bigfile;

import static b.b.tobytes;

import java.io.OutputStream;

import b.a;
import b.osnl;
import b.path;
import b.req;
import b.xwriter;

public class storage_file extends storage{
	public storage_file(){}
	public storage_file(a p,String id){super(p,id);}
	@Override synchronized public long used_space_in_bytes(){return req.get().session().path("storage-file-"+nm()+".txt").size();}
	@Override synchronized public void x_add(xwriter x,String s)throws Throwable{
		final path p=req.get().session().path(filename());
		final String size_in_bytes=""+(tobytes(s).length+1);// 1 for the newline at the end
		try(OutputStream os=p.outputstream(true)){
			new xwriter(os).spc().p(size_in_bytes).spc().p(s).nl();
		}
		ev(x,this,"storage.add");
	}
	@Override public void query(final req r,final String q,final reply c)throws Throwable{
		final path p=r.session().path(filename());
		if(p.exists())p.to(new osnl(){@Override public void onnewline(final String s)throws Throwable{
			if(s.startsWith(q,s.indexOf(' ',1)+1)){
				c.p(s);
			}
		}});
	}
	@Override public void to(xwriter x)throws Throwable{
		x.p(":  ").pl(filename());
		super.to(x);
	}
	public String filename(){
		return "storage-file-"+nm()+".txt";
	}
	private static final long serialVersionUID=1;
}
