package a.ramvark;

import static b.b.K;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class store_in_ram implements store{
	final private ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>clsmaps=new ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>();
//	public store_in_ram(){}
	@Override public itm create(@NotNull final Class<? extends itm>cls,final itm owner)throws Throwable{
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		return e;
	}
	@Override public void save(@NotNull final itm e)throws Throwable{
		final Class<? extends itm>cls=e.getClass();
		ConcurrentHashMap<String,byte[]>clsmap=clsmaps.get(cls);
		if(clsmap==null){
			clsmap=new ConcurrentHashMap<String,byte[]>();
			clsmaps.put(cls,clsmap);
		}
		final ByteOutputStream bos=new ByteOutputStream(K);
		e.save(bos);
		clsmap.put(e.did.toString(),bos.getBytes());
	}
	@Override public itm load(@NotNull final Class<? extends itm>cls,@NotNull final String did)throws Throwable{
//		final byte[]ba=items.get(cls)?.get(did);
		final Map<String,byte[]>clsmap=clsmaps.get(cls);
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
	public void load_from(final InputStream is)throws Throwable{}
	public void save_to(final OutputStream is)throws Throwable{}
}
