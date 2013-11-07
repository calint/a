package a;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class bgt extends a{
	static final long serialVersionUID=1l;
	public static String fsroot=bgt.class.getName();
	public static String[]colrs={"green","black","blue","green","black","blue"};
	private Map<path,Boolean>folds=new HashMap<path,Boolean>();
	public void to(final xwriter x) throws Throwable{
		x.style();
		x.css("table.bgt td","border:1px dotted black;padding:3px");
		x.css("table.bgt td.n","text-align:right");
		x.styleEnd();
		x.pre().ax(this,"recalc","::resum").spc().ax(this,"foldall","::fold").nl().nl();
		final path p=req.get().session().path(fsroot);
		x.table("bgt").tr().td();
		x.td("n").p("daily");
		x.td("n").p("weekly");
		x.td("n").p("monthly");
		x.td("n").p("yearly");
		x.td("n").p("install");
		x.td("n").p("uninstall");
		x.nl();
		prtfl(0,x,p);
		x.tableEnd();
		x.nl().p(" source '").p(p.fullpath()).p("'");
	}
	private void prtfl(final int indent,final xwriter x,final path p)throws Throwable{
		x.tr().td();
		for(int n=0;n<indent;n++)
			x.p("&nbsp;").p("&nbsp;").p("&nbsp;");
		final String nm=p.name();
		final String in=nm.substring(nm.indexOf('.')+1);
		if(p.isdir()){
			x.ax(this,"clk "+req.get().session().inpath(p),(folds.get(p)==Boolean.TRUE?"↓ ":"→ ")+in);
		}else{
			x.p(in);
		}
		final BufferedReader r;
		if(p.isfile()){
			r=new BufferedReader(p.reader());
		}else if(p.isdir()){
			final path sumry=p.get("0");
			if(!sumry.exists())
				return;
			r=new BufferedReader(sumry.reader());
		}else
			return;
		for(String line=r.readLine();line!=null;line=r.readLine()){
			x.td("n").tago("font").attr("color",colrs[indent]).tagoe().p(line);
		}
		r.close();
		if(p.isdir()){
			if(folds.get(p)==Boolean.TRUE){
				for(final String fn:p.list()){
					if("0".equals(fn))
						continue;
					final path f=p.get(fn);
					prtfl(indent+1,x,f);
				}
			}
		}
	}
	public final void ax_clk(final xwriter x,final String[]p){
		final path pth=req.get().session().path(p[2]);
		if(folds.get(pth)==Boolean.TRUE)
			folds.remove(pth);
		else
			folds.put(pth,Boolean.TRUE);
		x.xreload();
	}
	private void recalc(final path dir)throws Throwable{
		final int[]sum=new int[6];
		for(final String fn:dir.list()){
			if("0".equals(fn))
				continue;
			final path f=dir.get(fn);
			final BufferedReader r;
			if(f.isfile()){
				r=new BufferedReader(f.reader());
			}else if(f.isdir()){
				recalc(f);
				r=new BufferedReader(f.get("0").reader());
			}else
				throw new Error();
			int si=0;
			try{for(String line=r.readLine();line!=null;line=r.readLine()){
				sum[si++]+=Integer.parseInt(line);
			}}catch(Throwable t){throw new Error(f.fullpath()+":"+b.b.stacktraceline(t));}
			r.close();
		}
		final PrintWriter wr=new PrintWriter(dir.get("0").writer(false));
		for(final int s:sum){
			wr.println(s);
		}
		wr.close();
	}
	public final void ax_recalc(final xwriter x,final String[]a)throws Throwable{
		final path p=req.get().session().path(fsroot);
		recalc(p);
		x.xreload();
	}
	public final void ax_foldall(final xwriter x,final String[]a)throws Throwable{
		folds.clear();
		x.xreload();
	}
}
