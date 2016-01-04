package a.bigfile;

import b.a;
import b.req;
import b.xwriter;

abstract public class storage extends a{
	protected boolean hide_list=true;
	public storage(){}
	public storage(a p,String id){super(p,id);}
	@Override public void to(xwriter x)throws Throwable{
		x.p(":    ").ax(this,"hl","",used_space_in_bytes()+" bytes","l").nl();
		x.pl("............... ............... ............... ............... ");
		if(!hide_list){
			final int cap=16*3+8;
			query(req.get(),"",s->{
				x.p(":");
				x.pl(s.length()<cap?s:s.substring(0,cap));
			});
		}
	}
	abstract public void x_add(xwriter x,String s)throws Throwable;
	/** toggle display file list */
	public void x_hl(xwriter x,String s)throws Throwable{
		hide_list=!hide_list;
		x.xu(this);
	}
	abstract public long used_space_in_bytes()throws Throwable;
	abstract public void query(final req r,final String q,final reply c)throws Throwable;
	public interface reply{void p(String s)throws Throwable;}
	public static String key_from_blob_string(String s){
		final int i=s.indexOf(' ');
		return i==-1?s:s.substring(0,i);
	}
	private static final long serialVersionUID=1;
}
