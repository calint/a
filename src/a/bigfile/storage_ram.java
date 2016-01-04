package a.bigfile;

import java.util.LinkedList;

import b.a;
import b.req;
import b.xwriter;

public class storage_ram extends storage{
	private LinkedList<blob>blobs=new LinkedList<>();
	private long size_in_bytes;
	public storage_ram(){}
	public storage_ram(a p,String id){super(p,id);}
	@Override synchronized public void x_add(xwriter x,String s)throws Throwable{
		final int n=s.getBytes().length;
		final blob b=new blob(this,Integer.toString(blobs.size()),s);
		blobs.add(b);
		size_in_bytes+=n;
	}
	@Override synchronized public long used_space_in_bytes(){return size_in_bytes;}
	@Override public void query(final req r,final String q,final reply c)throws Throwable{
		for(final blob b:blobs){
			final String s=b.str();
			if(s.startsWith(q)){
				c.p(s);
			}
		}
	}
	private static final long serialVersionUID=1;
}
