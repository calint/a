package a.ix;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import a.diro;
import a.x.xsts;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
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
		x.ax(this,"viewix","  search").ax(this,"cfg","  config").ax(this,"reindex"," reindex").ax(this,"cancelix"," cancel").ax(this,"ixdel"," delete").ax(this,"size"," size").ax(this,"rfsh"," refresh");
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
		x_(y,null);
		y.scriptEnd();
	}
	public void x_(final xwriter x,final String st)throws IOException{
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
	public void x_stsx(final xwriter x,final String s)throws Throwable{x.xu(st.set(""));}
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
	synchronized public void x_size(final xwriter x,final String str)throws Throwable{
		final xsts s=new xsts(x,st,500);
		final path p=req.get().session().path(pathix);
		final long sz=sizerec(p,s,0);
		s.flush();
		x.xu(st.set("index contains "+Long.toString(sz>>10)+" KB data"));
	}
	synchronized public void x_viewix(final xwriter x,final String s){viewix=!viewix;x.xreload();}
//	public void ax_ltr(final xwriter x,final String[]args)throws IOException{
//		qs=args[2].substring(0,1);
//		xd.ltr(qs);
//		x.xreload();
//	}
	public void x_cancelix(final xwriter x,final String s)throws Throwable{
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
	synchronized public void x_reindex(final xwriter x,final String s)throws Throwable{
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
	synchronized public void x_ixdel(final xwriter x,final String str)throws Throwable{
		final session ses=req.get().session();
		final path ixpth=ses.path(pathix);
		final xsts s=new xsts(x,st,500);
		ixpth.rm(s);
		s.setsts("index deleted");
		s.flush();
		x.xu(xd);
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
		final session ses=req.get().session();
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
	public static class xdir extends diro{static final long serialVersionUID=1;}
}
