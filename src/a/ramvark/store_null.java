package a.ramvark;
public class store_null implements store{
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{return null;}
	@Override public void save(final itm e)throws Throwable{}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final visitor v)throws Throwable{}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{return null;}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{}
}
