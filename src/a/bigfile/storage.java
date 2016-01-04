package a.bigfile;

import java.util.LinkedList;

import b.a;
import b.xwriter;

public class storage extends a{
	private LinkedList<blob>blobs=new LinkedList<>();
	private long size_in_bytes;
	public storage(){}
	public storage(a p,String id){super(p,id);}
	@Override public void to(xwriter x)throws Throwable{
		x.pl(".- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		x.p(": b l o b s       ").p(size_in_bytes).pl(" bytes");
		x.pl(".- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		for(final blob b:blobs){
			x.p(". ").p_data_size(b.size_in_bytes()).spc(2);
			x.p(b.str()).nl();
		}
		x.pl(".- - - - - - - end of list - - - - - - - - - - - - - - -");
	}
	synchronized public void x_add(xwriter x,String s)throws Throwable{
		final int n=s.getBytes().length;
		final blob b=new blob(this,Integer.toString(blobs.size()),s);
		blobs.add(b);
		size_in_bytes+=n;
	}
	public void x_q(xwriter x,String s)throws Throwable{}

	synchronized public long size_in_bytes(){return size_in_bytes;}

	interface console{void p(String s);}
	public void query(final String q,final console c){
		for(final blob b:blobs){
			final String s=b.str();
			if(s.startsWith(q)){
				c.p(s);
			}
		}
	}
}
