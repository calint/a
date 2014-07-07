package a.ramvark;
public class store_wrap_backed extends store_wrap{
	private final store save_also_to;
	public store_wrap_backed(final store source,final store save_also_to){super(source);this.save_also_to=save_also_to;}
	@Override public void save(final itm e)throws Throwable{
		super.save(e);
		save_also_to.save(e);
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		super.delete(cls,did);
		save_also_to.delete(cls,did);
	}
}
