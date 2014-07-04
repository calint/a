package a.ramvark;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class store_in_ram implements store{
	final ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>items=new ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>();
	public store_in_ram(){}
	@Override public itm create(final Class<? extends itm> cls,final itm owner)throws Throwable{
		return null;
	}
	@Override public void save(final itm e)throws Throwable{
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
//		final byte[]ba=items.get(cls)?.get(did);
		final Map<String,byte[]>clsmap=items.get(cls);
		if(clsmap==null)return null;
		final byte[]ba=clsmap.get(did);
		if(ba==null)return null;
		final itm e=cls.newInstance();
		try(final InputStream is=new ByteInputStream(ba,ba.length)){
			e.load(is);
		}
		return e;
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
	}
	public void read_from(final InputStream is)throws Throwable{}
	public void write_to(final OutputStream is)throws Throwable{}
}
