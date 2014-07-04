package a.ramvark;

import java.io.InputStream;
import java.io.OutputStream;
import a.ramvark.cstore.meters;
import b.path;

abstract public class store_in_files implements store{
	abstract protected path root(final Class<? extends itm>cls);
	public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		meters.deletes++;
		//final itm e=load(cls,did);
	//	e.onpredelete();
		root(cls).get(did).rm();
	}
	public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		final path p=root(cls).get(did);
		if(!p.exists())
			return null;
		final itm e=cls.newInstance();
		load(e,p);
		return e;
	}
	public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{
		meters.creates++;
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		final String docid=cstore.mkdocid();
		e.did.set(docid);
		//e.onnew();
		return e;
	}
	
	final public void save(final itm e)throws Throwable{
		final String fn=e.did.toString();
		final Class<? extends itm>cls=e.getClass();
		final path file=root(cls).get(fn);
		try(final OutputStream os=file.outputstream(false)){e.save(os);}
	}
	final private static void load(final itm e,final path p)throws Throwable{
		try(final InputStream is=p.inputstream()){e.load(is);}
	}
	public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		meters.foreaches++;
		final String[]fns=root(cls).list();//?. stream
		for(final String fn:fns){
			final path p=root(cls).get(fn);
			final itm e=cls.newInstance();
			load(e,p);
			if(owner!=null){//?. implpartialload
				final String pid=e.pid.toString();
				final String did=owner.did.toString();
				if(!pid.equals(did))
					continue;
			}
			final String nm=e.toString();//?. queryfilename
			if(q!=null&&q.length()>0&&!nm.startsWith(q))
				continue;
			v.visit(e);
		}
	}
}
