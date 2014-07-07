package a.ramvark;
public class store_wrap_backed implements store{
	private final store source;
	private final store save_also_to;
	public store_wrap_backed(final store source,final store save_also_to){this.source=source;this.save_also_to=save_also_to;}
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{return cstore.mk(cls,owner);}
	@Override public void save(final itm e)throws Throwable{
		source.save(e);
		save_also_to.save(e);
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		source.foreach(cls,owner,q,v);
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		return source.load(cls,did);
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		source.delete(cls,did);
		save_also_to.delete(cls,did);
	}
}
