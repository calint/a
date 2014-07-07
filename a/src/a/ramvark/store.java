package a.ramvark;
public interface store {
	itm create(final Class<? extends itm>cls,final itm owner)throws Throwable;
	void save(final itm e)throws Throwable;
	itm load(final Class<? extends itm>cls,final String did)throws Throwable;
	void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable;
	void delete(final Class<? extends itm>cls,final String did)throws Throwable;

	public interface visitor{void visit(final itm e)throws Throwable;}
}
