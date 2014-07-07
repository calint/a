package a.ramvark;

import static b.b.K;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import a.ramvark.cstore.meters;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class store_in_ram implements store{
	final private ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>maps=new ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>();
//	public store_in_ram(){}
	@Override public itm create(@NotNull final Class<? extends itm>cls,final itm owner)throws Throwable{
		cstore.meters.creates++;
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		final String docid=cstore.mkdocid();
		e.did.set(docid);
		return e;
	}
	@Override public void save(@NotNull final itm e)throws Throwable{
		final Class<? extends itm>cls=e.getClass();
		ConcurrentHashMap<String,byte[]>map=maps.get(cls);
		if(map==null){
			map=new ConcurrentHashMap<String,byte[]>();
			maps.put(cls,map);
		}
		final ByteOutputStream bos=new ByteOutputStream(K);
		e.save(bos);
		map.put(e.did.toString(),bos.getBytes());
		e.notnew=true;
	}
	@Override public itm load(@NotNull final Class<? extends itm>cls,@NotNull final String did)throws Throwable{
//		final byte[]ba=items.get(cls)?.get(did);
		final Map<String,byte[]>map=maps.get(cls);
		if(map==null)return null;
		final byte[]ba=map.get(did);
		if(ba==null)return null;
		final itm e=cls.newInstance();
		try(final InputStream is=new ByteInputStream(ba,ba.length)){
			e.load(is);
		}
		return e;
		
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		cstore.meters.foreaches++;
		final Map<String,byte[]>map=maps.get(cls);
		if(map==null)return;
		if(q!=null&&!q.isEmpty())
			map.entrySet().stream().forEach(me->{
				if(!new String(me.getValue(),0,q.length()).equals(q))return;//? CharSequence wrap of byte[]
				try{final itm e=cls.newInstance();e.load(new ByteArrayInputStream(me.getValue()));v.visit(e);}catch(Throwable t){throw new Error(t);}
			});
		else
			map.entrySet().stream().forEach(me->{
				try{final itm e=cls.newInstance();e.load(new ByteArrayInputStream(me.getValue()));v.visit(e);}catch(Throwable t){throw new Error(t);}
			});
	}
	@Override public void delete(@NotNull final Class<? extends itm>cls,@NotNull final String did)throws Throwable{
		cstore.meters.deletes++;
		final ConcurrentHashMap<String,byte[]>map=maps.get(cls);
		map.remove(did);//? cascade agr
		if(map.isEmpty())maps.remove(cls);//? y
	}
	public void load_from(final InputStream is)throws Throwable{}
	public void save_to(final OutputStream is)throws Throwable{}
}
