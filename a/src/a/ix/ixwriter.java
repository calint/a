package a.ix;
import java.io.*;
import java.text.*;
import java.util.*;
import a.x.*;
import b.*;
public final class ixwriter extends Writer implements Serializable{
		static final long serialVersionUID=1;
		public static String[]inctypes={"","txt","text","html","htm","js","css","java","h","c","hpp","cc","cpp","s","asm","sh"};
		private StringBuilder sb;
		private path ixstore;
		private int token_size_max=255;
		private int token_size_min=1;
//		private int count_tokens=0;
		private int count_files=0;
		private int count_tokens_found=0;
		private long count_bytes=0;
		private final NumberFormat nf=new DecimalFormat("#,###,###,###,###");
		transient private String fileid;
		transient private Set<String>tokens;
		transient private Map<Long,String>pathbylng;
		transient private long lineno;
		transient private Writer paths;
		private path docroot;
		boolean cancelled;
		boolean running;
		private int sespthlen;
		public ixwriter(final path docroot,final path ixstore)throws IOException{
			this.docroot=docroot;
			this.ixstore=ixstore;
			sb=new StringBuilder(token_size_max);
			tokens=new HashSet<String>(b.K);
		}
		void ensure_pathbylng(){
			if(pathbylng!=null)
				return;
			pathbylng=new HashMap<Long,String>(b.K);
			try{reloadpaths();}catch(IOException e){throw new Error(e);}
		}
		synchronized void reloadpaths()throws IOException{
			final path p=req.get().session().path($.pathix).get("paths.txt");
			final BufferedReader r=new BufferedReader(p.reader());
			pathbylng.clear();
			long i=0;
			for(String l=r.readLine();l!=null;l=r.readLine())
				pathbylng.put(i++,l);
		}
		synchronized String get(final long i){
			ensure_pathbylng();
			return pathbylng.get(i);
		}
		private long index_t0;
		public synchronized void reindex(final xsts st)throws Throwable{
			cancelled=false;running=true;
			count_bytes=count_files=count_tokens_found=0;
//			count_tokens=0;
			index_t0=System.currentTimeMillis();
			final session s=req.get().session();
			sespthlen=s.path("").toString().length()+1;
			st.setsts("deleting previous index");
			if(cancelled)return;
			ixstore.rm(st);
			final path p=s.path($.pathix).get("paths.txt");
			paths=p.writer(true);
			st.setsts("reindexing "+docroot);
			if(cancelled)return;
			traverse(docroot,st);
			final NumberFormat nf=new DecimalFormat("#,###,###,###,###");
			long dt=System.currentTimeMillis()-index_t0;
			if(dt==0)
				dt++;
			st.setsts("done, "+dt+" ms, "+nf.format(count_files)+" files, "+nf.format(count_bytes)+" B, "+nf.format(count_tokens_found)+" words, "+count_bytes/dt+" kB/s.");
			//? bytes/sec
			paths.close();
			st.flush();
			cancelled=false;running=false;
		}
		private void enter_path(final path p)throws IOException{
			tokens.clear();
			final String subpth=p.toString().substring(sespthlen);
			ensure_pathbylng();			
			//? if p.lastmod()!=paths[subpth][1] refresh else skip 
			paths.append(subpth).append('\n').flush();
			pathbylng.put(lineno,subpth);
			fileid=Long.toString(lineno)+"\n";
			lineno++;
			count_files++;
		}
		public void write(final char[]c,final int off,final int len)throws IOException{
			for(int n=0;n<len;n++){
				char ch=c[off+n];
				ch=Character.toLowerCase(ch);
				if(Character.isLetterOrDigit(ch)){
					sb.append(ch);
					continue;
				}
				if(ch=='+'){
					sb.append(ch);
					continue;
				}
				if(ch=='#'){
					sb.append(ch);
					continue;
				}
				if(sb.length()==0)
					continue;
				onToken(sb.toString());
				sb.setLength(0);
			}
			count_bytes+=len;
		}
		private void onToken(final String token)throws IOException{
//			count_tokens++;
			final int len=token.length();
			if(len<token_size_min)
				return;
			if(len>token_size_max)
				return;
			if(!tokens.add(token))
				return;
			count_tokens_found++;
			final path fileixpath=ixstore.get(token.substring(0,1)).get(token);
			final Writer wr=fileixpath.writer(true);
			wr.write(fileid);
			wr.close();
		}
		private void traverse(final path rootpath,final xsts sts)throws Throwable{
			if(cancelled)
				return;
			sts.setsts("dir "+rootpath);
			final String[]dir=rootpath.list();
			if(dir==null)
				return;
			Arrays.sort(dir,new Comparator<String>(){public int compare(String a,String b){
				return a.toLowerCase().compareTo(b.toLowerCase());
			}});
			for(int i=0;i<dir.length;i++){
				if(cancelled)
					return;
				final path p=rootpath.get(dir[i]);
				long dt=System.currentTimeMillis()-index_t0;
				if(dt==0)
					dt++;
				sts.setsts("indexed "+nf.format(count_bytes)+" B ("+count_bytes/dt+" kB/s): "+p);
				if(p.ishidden())
					continue;
				if(p.equals(ixstore))
					continue;
				if(p.isdir()){
					traverse(p,sts);
					continue;
				}
//				final String type=p.type();
//				if(!inctype(type))
//					continue;
				enter_path(p);
				final char[]fnchs=(p.name()+"\n").toCharArray();
				this.write(fnchs,0,fnchs.length);
				final Reader r=p.reader();
				final String strlen=nf.format(p.size());
				final int sespthlen=req.get().session().path().toString().length()+1;
				b.cp(r,this,new sts(){public void setsts(final String s) throws Throwable{
					sts.setsts("indexing "+p.toString().substring(sespthlen)+": "+nf.format(Long.parseLong(s))+"/"+strlen+" bytes");
				}public void flush() throws Throwable{sts.flush();}});
				r.close();
				if(sb.length()!=0){
					onToken(sb.toString());
					sb.setLength(0);
				}
			}
		}
//		private boolean inctype(final String type){for(final String s:inctypes)if(type.equals(s))return true;return false;}
		public void flush()throws IOException{}
		public void close()throws IOException{}
		public void cancel(){cancelled=true;}
	}