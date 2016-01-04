package a.bigfile;

import b.a;
import b.req;
import b.xwriter;

abstract public class storage extends a{
	public storage(){}
	public storage(a p,String id){super(p,id);}
	@Override public void to(xwriter x)throws Throwable{
		x.pl(".- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		x.p(":  used ").p_data_size(used_space_in_bytes()).pl(" bytes");
		x.pl(".- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		query(req.get(),"",s->x.p(". ").pl(s));
		x.pl(".- - - - - - - end of list - - - - - - - - - - - - - - -");
	}
	abstract public void x_add(xwriter x,String s)throws Throwable;
	abstract public long used_space_in_bytes()throws Throwable;
	abstract public void query(final req r,final String q,final reply c)throws Throwable;
	public interface reply{void p(String s)throws Throwable;}
	public static String key_from_blob_string(String s){
		final int i=s.indexOf(' ');
		return i==-1?s:s.substring(0,i);
	}
}
