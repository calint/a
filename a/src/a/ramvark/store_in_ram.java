package a.ramvark;

import static b.b.K;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class store_in_ram implements store,Serializable{
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{
		cstore.meters.creates++;
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		final String docid=cstore.mkdocid();
		e.did.set(docid);
		return e;
	}
	@Override public void save(final itm e)throws Throwable{
		final Class<? extends itm>cls=e.getClass();
		ConcurrentHashMap<String,byte[]>map=maps.get(cls);
		if(map==null){
			map=new ConcurrentHashMap<>();
			maps.put(cls,map);
		}
		final ByteArrayOutputStream bos=new ByteArrayOutputStream(K);
		e.save(bos);
		map.put(e.did.toString(),bos.toByteArray());
		e.notnew=true;
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
//?j		final byte[]ba=items.get(cls)?.get(did);
		final ConcurrentHashMap<String,byte[]>map=maps.get(cls);
		if(map==null)return null;
		final byte[]ba=map.get(did);
		if(ba==null)return null;
		final itm e=cls.newInstance();
		final InputStream is=new ByteArrayInputStream(ba);
		e.load(is);
		return e;
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		cstore.meters.foreaches++;
		final ConcurrentHashMap<String,byte[]>map=maps.get(cls);
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
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		cstore.meters.deletes++;
		final ConcurrentHashMap<String,byte[]>map=maps.get(cls);
		if(map==null)return;//? silently ignores ...
		map.remove(did);//? cascade agr
		if(map.isEmpty())maps.remove(cls);//? y
	}
	
	public void load_from(final InputStream is)throws Throwable{}
	public void save_to(final OutputStream is)throws Throwable{}
	
	final private ConcurrentHashMap<Class<? extends itm>,ConcurrentHashMap<String,byte[]>>maps=new ConcurrentHashMap<>();
	
	private static final long serialVersionUID=1L;
}
