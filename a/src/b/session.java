package b;
import java.io.*;
import java.util.*;
import java.util.Map.*;
public final class session implements Serializable{
	static final long serialVersionUID=1;
	private static final Map<String,session>all=Collections.synchronizedMap(new HashMap<String,session>(b.hash_size_sessions_store));
	static Map<String,session>all(){return all;}
	static void all_save()throws Throwable{synchronized(all){
		final long t0=System.currentTimeMillis();
		final int s=all.size();
		b.out.print("\n");
		int k=1;
		for(final Iterator<Entry<String,session>>i=all.entrySet().iterator();i.hasNext();k++){
			final Entry<String,session>e=i.next();
			final String id=e.getKey();
			final path p=b.path(b.sessions_dir).get(id).get(b.sessionfile);
			b.out.print("\r");
			b.out.print(k);
			b.out.print("/");
			b.out.print(s);
			b.out.print(" ");
			b.out.print(e.getKey());
			final session se=e.getValue();
			synchronized(se){p.writeobj(se);}
		}
		final long dt=System.currentTimeMillis()-t0;
		b.out.print("\n done in ");
		b.out.print(dt);
		b.out.print(" ms\n  wrote ");
		long sps=s*1000;
		if(dt!=0)
			sps/=dt;
		if(sps>s)
			sps=s;
		b.out.println(sps+" session"+(sps>1?"s":"")+"/second");
	}}
	private final String id;
	private final Map<String,Serializable>kvp;
	int nreq;
	private long bits=1;
	session(final String id){
		this.id=id;
		kvp=Collections.synchronizedMap(new HashMap<String,Serializable>(b.hash_size_session_values));
	}
	public Serializable get(final String key){return kvp.get(key);}
	public String href(){return "/"+b.sessionhref(id);}
	public String id(){return id;}
	public Set<String>keyset(){return Collections.unmodifiableSet(kvp.keySet());}
	public path path(final String path){return b.path(href()+path);}
	public path path(){return b.path(href());}
	public path path(final Class<?>cls){return b.path(href()+cls.getName().replace('.','/'));}
	public void put(final String key,final Serializable value){kvp.put(key,value);}
	public void save()throws IOException{path(b.sessionfile).writeobj(this);}
	public String inpath(final path p)throws Error{
		final String fn=p.toString();
		final String href=b.path(href()).toString();
		if(!fn.startsWith(href))
			throw new Error();
		if(fn.length()==href.length())
			return "";
		return fn.substring(href.length()+1);
	}
	public long bits(){return bits;}
	public void bits(final long b){bits=b;}
	public boolean bitshasany(final long b){return (bits|b)!=0;}
	public boolean bitshasall(final long b){return (bits&b)==b;}
	public void remove(final String key){kvp.remove(key);}
}
