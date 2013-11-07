package a.ix;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import a.diro;
import a.x.xsts;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
import b.sts;
import b.xwriter;
final public class $ extends a{
	static final long serialVersionUID=1;
	public static String pathix="ix";
	public static boolean partialwordindex=false;
	public a st;
	public a qf;
	public xdir xd;
	public a ds;	
	private boolean viewix;
	private ixwriter ix;
	public $() throws IOException{
		xd.root(req.get().session().path(pathix));
		final session ses=req.get().session();
		final path homepth=ses.path("");
		final path ixpth=ses.path(pathix);
		ix=new ixwriter(homepth,ixpth);
	}
	public void to(final xwriter x)throws Throwable{
		if(viewix)
			to_ix(x);
		else
			to_srch(x);
	}
	void to_ix(final xwriter x)throws Throwable{
		x.style().css("body","text-align:center").css(st,"color:grey").styleEnd();
		x.ax(this,"viewix","  search").ax(this,"reindex"," reindex").ax(this,"cancelix"," cancel").ax(this,"ixdel"," delete").ax(this,"size"," size");
		x.nl().span(st).spc().ax(this,"stsx","[·]").nl();
		x.p(xd);
	}
	void to_srch(final xwriter y)throws IOException{
		y.p("“");
		y.style();
		y.css("body","text-align:center");
		y.css(qf,"color:brown;background:yellow;border:0;border-bottom: 1px dotted grey;");
		y.styleEnd();
		y.inputax(qf);
		y.p("”").br();
//		y.style().css(ds,"border:1px solid red").styleEnd();
		y.spanh(ds);
		y.script();
		y.xfocus(qf);
		ax_(y,null);
		y.scriptEnd();
	}
	public void ax_(final xwriter x,final String[]p)throws IOException{
		final xwriter y=x.xub(ds,true,false);
		final String qs=qf.toString();
		final long t0=System.currentTimeMillis();
		final List<Long>ls=query(qs);
		final long t1=System.currentTimeMillis();
		final long dt=t1-t0;
		y.ax(this,"viewix","index").p(" found ").p(ls.size()).p(" in ").p(dt).p(" ms  ");
		y.nl();
		x.style();
		x.css("table.f","margin-left:auto;margin-right:auto");
		x.css("table.f tr:first-child","border:0;border-bottom:1px solid green;border-top:1px solid green");
		x.css("table.f tr:last-child td","border:0;border-top:1px solid green");
		x.css("table.f th:first-child","border-right:1px dotted green");
		x.css("table.f td","padding:5px;vertical-align:middle;border-left:1px dotted grey;border-bottom:1px dotted grey");
		x.css("table.f td:first-child","border-left:0");
		x.css("table.f td.icns","text-align:center");
		x.css("table.f td.size","text-align:right");
		x.css("table.f td.total","font-weight:bold");
		x.css("table.f td.name","min-width:100px");
		x.css("table.f th","padding:3px;text-align:left;background:#f0f0f0;color:black");
		x.styleEnd();
		y.table("f").nl().tr().nl();
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final NumberFormat lnf=new DecimalFormat("#,###,###,###,###");
		final session ses=req.get().session();
		for(final long s:ls){
			final String pth=ix.get(s);
			final path p1=ses.path(pth);
			final boolean isDir=p1.isdir();
			final String iconName;
			if(isDir)
				iconName="⧉";
			else
				iconName="◻";
			y.tr().td("icns first").p(iconName).td("name");
			if(p1.isfile())
				y.a(p1.uri()).p(p1.name()).aEnd();
			else
				y.p(p1.toString());
			y.tdEnd();
			y.td("date").p(sdf.format(new Date(p1.lastmod()))).tdEnd();
			y.td("size last").p(isDir?"----":lnf.format(p1.size())).p(" B").tdEnd();
			y.trEnd().nl();
		}
		y.tr().nl().tableEnd();
		x.xube();
	}
	public void ax_stsx(final xwriter x,final String[]a)throws Throwable{x.xu(st.set(""));}
	private long sizerec(final path p,final xsts sts,long size){
		for(final String f:p.list()){
			final path pp=p.get(f);
			if(pp.isdir()){
				size=sizerec(pp,sts,size);
				continue;
			}
			size+=pp.size()+pp.name().length()+1+8;
		}
		return size;
	}
	public void ax_size(final xwriter x,final String[]a)throws Throwable{
		xsts s=new xsts(x,st,500);
		final path p=req.get().session().path(pathix);
		final long sz=sizerec(p,s,0);
		s.flush();
		x.xu(st.set("index contains "+Long.toString(sz>>10)+" KB data"));
	}
	public void ax_viewix(final xwriter x,final String[]p){viewix=!viewix;x.xreload();}
//	public void ax_ltr(final xwriter x,final String[]args)throws IOException{
//		qs=args[2].substring(0,1);
//		xd.ltr(qs);
//		x.xreload();
//	}
	public void ax_cancelix(final xwriter x,final String[]a)throws Throwable{
		if(ix==null){
			x.xu(st.set("indexer not loaded"));
			return;
		}
		if(!ix.running){
			x.xu(st.set("indexer not running"));
			return;
		}
		if(ix.cancelled){
			x.xu(st.set("indexer already cancelled"));
			return;
		}
		ix.cancel();
		x.xu(st.set("cancelled"));
		x.xu(xd);
	}
	public void ax_reindex(final xwriter x,final String[]args)throws Throwable{
		final session ses=req.get().session();
		final path homepth=ses.path("");
		final path ixpth=ses.path(pathix);
		final xsts stsb=new xsts(x,st,500);
		if(ix!=null)
			ix.cancel();
		ix=new ixwriter(homepth,ixpth);
		ix.reindex(stsb);
		x.xu(xd);
//		x.xreload();
	}
	public void ax_ixdel(final xwriter x,final String[]args)throws Throwable{
		final session ses=req.get().session();
		final path ixpth=ses.path(pathix);
		final xsts s=new xsts(x,st,500);
		ixpth.rm(s);
		s.setsts("index deleted");
		s.flush();
		x.xu(xd);
	}
	public final static class ixwriter extends Writer implements Serializable{
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
		private boolean cancelled;
		private boolean running;
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
			final path p=req.get().session().path(pathix).get("paths.txt");
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
			final path p=s.path(pathix).get("paths.txt");
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
	public static List<Long>query(final String qs)throws IOException{
		String[]q=qs.split(" ");
		if(q.length==1&&q[0].length()==0)
			q=new String[0];
		final String[]qsw=q;
		final BufferedReader keyword_ix_file_readers[]=new BufferedReader[qsw.length];
		//			final long keyword_ix_file_sizes[]=new long[qsw.length];
//		long acc=0;
		boolean allhit=true;
		session ses=req.get().session();
		for(int i=0;i<qsw.length;i++){
			final String dir=qsw[i].substring(0,1);
			final path path=ses.path(pathix).get(dir).get(qsw[i]);
			final long filelen=path.size();
//			acc+=filelen;
			//				keyword_ix_file_sizes[i]=filelen;
			if(filelen>0)
				keyword_ix_file_readers[i]=new BufferedReader(new InputStreamReader(path.inputstream()));
			else
				allhit=false;
		}
		if(qsw.length==0)
			allhit=false;
		final List<Long>found=new ArrayList<Long>(b.K);
		long hit=0;
		int hitcount=0;
		int readerix=0;
		if(allhit){
			if(q.length==1){
				for(String line=keyword_ix_file_readers[readerix].readLine();line!=null;line=keyword_ix_file_readers[readerix].readLine())
					found.add(Long.parseLong(line));
			}else{
				while(true){
					final String line=keyword_ix_file_readers[readerix].readLine();
					if(line==null)
						break;
					final long l=Long.parseLong(line);
					if(l<hit)
						continue;
					if(l>hit){
						hit=Long.parseLong(line);
						readerix=(readerix+1)%keyword_ix_file_readers.length;
						hitcount=1;
						continue;
					}
					hitcount++;
					if(hitcount==keyword_ix_file_readers.length)
						found.add(l);
					readerix=(readerix+1)%keyword_ix_file_readers.length;
				}
			}
		}
		for(final Reader r:keyword_ix_file_readers)
			if(r!=null)
				r.close();
		return found;
	}
	public static class xdir extends diro{
		static final long serialVersionUID=1;
		public xdir() throws Throwable{}
	}
}
