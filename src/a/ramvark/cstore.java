package a.ramvark;
import java.util.Date;

import b.a;
import b.cacheable;
import b.xwriter;
public final class cstore{
	private final static store st=new file_store();
	static public itm create(Class<? extends itm>cls,itm owner)throws Throwable{return st.create(cls,owner);}
	static public void save(itm e)throws Throwable{st.save(e);}
	static public itm load(Class<? extends itm>cls,String did)throws Throwable{return st.load(cls,did);}
	static public void foreach(Class<? extends itm>cls,itm owner,String q,store.visitor v)throws Throwable{st.foreach(cls,owner,q,v);}
	static public void delete(Class<? extends itm>cls,String did)throws Throwable{st.delete(cls,did);}

	public static class meters extends a implements cacheable{
		static final long serialVersionUID=1;
		public static long foreaches,creates,loads,saves,deletes;
		public void to(final xwriter x)throws Throwable{spclst(x);}
		public static void spclst(final xwriter x)throws Throwable{
			x.p(foreaches).spc();
			x.p(creates).spc();
			x.p(loads).spc();
			x.p(saves).spc();
			x.p(deletes);
		}

		// cache
		public String filetype(){return "ls";}
		public String contenttype(){return "plain/text";}
		private long t0;
		private static String t0d;
		private static long cachetimems=1000;
		public String lastmod(){//? called3timesinonereq
			final long t1=System.currentTimeMillis();
			if(t1-t0>cachetimems){
				t0=t1;
				t0d=new Date(t0).toString();
				return t0d;
			}
			return t0d;
		}
		public long lastmodupdms(){return 1000;}
		public boolean cacheforeachuser(){return false;}
	}
}
