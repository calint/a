package a.ramvark;
public class store_wrap_cached extends store_wrap{
	private final store cache;
	public store_wrap_cached(final store source,final store cache){super(source);this.cache=cache;}
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{return cstore.mk(cls,owner);}
	@Override public void save(final itm e)throws Throwable{}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final visitor v)throws Throwable{}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{return null;}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{}
//	public void invalidate(final Class<? extends itm>cls,final String did)throws Throwable{}
}
