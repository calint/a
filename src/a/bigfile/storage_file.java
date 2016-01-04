package a.bigfile;

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
		final path p=req.get().session().path("storage-file-"+nm()+".txt");
		p.append(" "+b.b.tobytes(s).length+" ");
		p.append(s,"\n");
	}
	@Override public void query(final req r,final String q,final reply c)throws Throwable{
		final path p=r.session().path("storage-file-"+nm()+".txt");
		if(p.exists())
		p.to(new osnl(){@Override public void onnewline(final String s)throws Throwable{
			final String size_str=s.substring(0,s.indexOf(' ',1)+1);
			final String qq=size_str+q;
			if(s.startsWith(qq)){
				c.p(s);
			}
		}});
	}
}
