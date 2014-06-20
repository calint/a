package a.ramvark;

import a.ramvark.cstore.visitor;

public interface store {
	itm create(final Class<? extends itm>cls,final itm owner)throws Throwable;
	void save(final itm e)throws Throwable;
	itm load(final Class<? extends itm>cls,final String did)throws Throwable;
	void foreach(final Class<? extends itm>cls,final itm owner,final String q,final visitor v)throws Throwable;
	void delete(final Class<? extends itm>cls,final String did)throws Throwable;
}
