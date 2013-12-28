package a.budget;
import java.util.*;

import b.*;
public class $ extends a{
	static final long serialVersionUID=1l;
	public static String fsroot=$.class.getPackage().getName();
	public static String[]colrs={"black","green","green","green","green","green","","","",""};
	private Map<path,Boolean>folds=new HashMap<path,Boolean>();
	public void to(final xwriter x) throws Throwable{
		x.style();
		x.css("html","font-size:2em;padding-left:4em");
		x.css("table.bgt td","border:1px dotted black;padding:.5em");
		x.css("table.bgt td.n","text-align:right");
		x.styleEnd();
		x.pre().ax(this,"recalc","::resum").spc().ax(this,"foldall","::fold").nl().nl();
//		final path p=req.get().session().path(fsroot);
		final path p=b.path(fsroot);
		x.table("bgt").tr().td().td();
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
		final String uri=p.uri();
		final String itmname=nm.substring(nm.indexOf('.')+1);
		if(p.isdir()||!p.exists()){
//			x.ax(this,"clk "+req.get().session().inpath(p),(folds.get(p)==Boolean.TRUE?"↓ ":"→ ")+in);
			final String[]files=p.list();
			final boolean isleaf=files.length==0||(files.length==1&&"0".equals(files[0]));
			if(isleaf){
				x.ax(this,"ed "+uri,itmname);
			}else{
				x.ax(this,"clk "+uri,(folds.get(p)==Boolean.TRUE?"↓ ":"→ ")+itmname);
			}
			x.td();
			x.ax(this,"ed "+uri," e");
			x.ax(this,"add "+uri," +");
		}else{
			x.ax(this,"ed "+uri,itmname);
//			x.p(in);
		}
		x.ax(this,"rem "+uri," -");
		final String txt;
		if(p.isfile()){
			txt=p.readstr();
		}else if(p.isdir()){
			final path sumry=p.get("0");
			if(sumry.exists())txt=sumry.readstr();
			else txt="";
		}else txt="";
		
		final String[]a=txt.split(" ");
		for(final String s:a){
			if(s.length()==0)continue;
			x.td("n").tago("span").attr("style","color:"+colrs[indent]).tagoe().p(s).spanEnd();				
		}
		x.nl();
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
//		final path pth=req.get().session().path(p[2]);
		final path pth=b.path(p[2].replace('+',' '));//? accessarbitraryfile
		if(folds.get(pth)==Boolean.TRUE)
			folds.remove(pth);
		else
			folds.put(pth,Boolean.TRUE);
		x.xreload();
	}
	private void recalc(final path dir)throws Throwable{
		final String[]fs=dir.list();
		if(fs.length==0||(fs.length==1&&"0".equals(fs[0])))return;
		final int[]sum=new int[6];
		for(final String fn:fs){
			if("0".equals(fn))
				continue;
			final path f=dir.get(fn);
			final String txt;
			if(f.isfile()){
				txt=f.readstr();
			}else if(f.isdir()){
				recalc(f);
				final path sumry=f.get("0");
				if(sumry.exists())txt=sumry.readstr();
				else txt="";
			}else throw new Error();
			final String[]a=txt.split(" ");
			int i=0;
			for(final String s:a){
				if(s.length()==0)continue;
				sum[i++]+=Integer.parseInt(s);
			}
		}
		final StringBuilder sb=new StringBuilder();
		for(final int i:sum)
			sb.append(Integer.toString(i)).append(" ");
		if(sb.length()>0)sb.setLength(sb.length()-1);
		dir.get("0").writestr(sb.toString());
	}
	public final void ax_recalc(final xwriter x,final String[]a)throws Throwable{
//		final path p=req.get().session().path(fsroot);
		final path p=b.path(fsroot);
		recalc(p);
		x.xreload();
	}
	public final void ax_foldall(final xwriter x,final String[]a)throws Throwable{
		folds.clear();
		x.xreload();
	}
	public final void ax_ed(final xwriter x,final String[]a)throws Throwable{
		final String pathname=b.urldecode(a[2]);
		x.xlocation("budget.ed?"+b.urlencode(pathname));
	}
	public final void ax_add(final xwriter x,final String[]a)throws Throwable{
		final String pathname=b.urldecode(a[2]);
		x.xlocation("budget.ed?"+b.urlencode(pathname+"/new"));
	}
	public final void ax_rem(final xwriter x,final String[]a)throws Throwable{
		final String pathname=b.urldecode(a[2]);
		final path p=b.path(pathname);
		if(!p.isin(b.path($.fsroot)))
			throw new Error("not in path: "+p);
		p.rm();
		x.xreload();
	}
}
