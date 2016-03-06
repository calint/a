package a.ramvark;
abstract public class store_wrap implements store{
	private final store wrap;
	public store_wrap(final store wrap){this.wrap=wrap;}
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{return wrap.create(cls,owner);}
	@Override public void save(final itm e)throws Throwable{wrap.save(e);}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final visitor v)throws Throwable{wrap.foreach(cls,owner,q,v);}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{return wrap.load(cls,did);}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{wrap.delete(cls,did);}
}
