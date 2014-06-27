package a.any;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import b.a;
import b.b;
import b.xwriter;
public class list extends a{
	private static final long serialVersionUID=1;
	public final static int BIT_ALLOW_QUERY=1;
	public final static int BIT_ALLOW_FILE_LINK=2;
	public final static int BIT_ALLOW_DIR_ENTER=4;
	public final static int BIT_ALLOW_DIR_UP=8;
	public final static int BIT_ALLOW_FILE_OPEN=16;
	public final static int BIT_ALLOW_FILE_EDIT=32;
	public final static int BIT_ALLOW_FILE_DELETE=64;
	public final static int BIT_ALLOW_FILE_CREATE=128;
	public final static int BIT_ALLOW_DIR_CREATE=256;
	public final static int BIT_ALLOW_DIR_DELETE=512;
	public final static int BIT_ALLOW_FILE_MODIFY=1024;
	public final static int BIT_ALLOW_SELECT=2048;
	public final static int BIT_ALLOW_MOVE=4096;
	public final static int BIT_ALLOW_RENAME=8192;
	public final static int BIT_ALLOW_COPY=16384;
	public final static int BIT_DISP_PATH=32768;
	public final static int BIT_ALL=-1;
	public a q;//query field
	public status_line sts;{sts.set("public domain server #1 -- ok");}//application status
	protected final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
	protected final NumberFormat nf=new DecimalFormat("###,###,###,###");
	protected int bits=BIT_DISP_PATH|BIT_ALLOW_QUERY|BIT_ALLOW_FILE_LINK|BIT_ALLOW_DIR_ENTER|BIT_ALLOW_DIR_UP;
//	protected int bits=BIT_ALL;
	public @Retention(RetentionPolicy.RUNTIME)@interface ui_action{}
//	public @interface ui_action{}
	public @interface read{}
	public static interface el extends Serializable{
		el get(final String name);
		String name();
		boolean isdir();
		boolean isfile();
		List<String>list();//? enumerator or foreach
		List<String>list(final String query);
		long size();
		long lastmod();
		String uri();
		boolean exists();
		void append(final String cs);
		String fullpath();
		boolean rm();
		el parent();
//		public void to(final xwriter x);
		OutputStream outputstream();//? set(byte[]), read(input)
		InputStream inputstream();//? void stream_to(output)
		
		boolean ommit_column_edit();
		boolean ommit_column_lastmod();
		boolean ommit_column_size();
		boolean ommit_column_icon();

		
		public static interface el_column_value{public void column_value(final xwriter x)throws Throwable;}
		public static interface el_column_value_editor{public a column_value_editor()throws Throwable;}
		public static interface el_actions{public List<a>actions();}
	}
	//	protected el root=new elpath(null,b.path());
	protected el root;
//	{
//		final elroot l=new elroot(null,"any");
//		root=l;
//		l.add(new elclass(root,list.class,"config any"));
//		l.add(new elclass(root,b.class,"config web server"));
//		l.add(new elclass(root,req.class,"config web server requests"));
//		l.add(new elpath(root,b.path(),"browse server files"));
//	}
	protected el path;
	private ArrayList<a>element_editors=new ArrayList<a>();
	protected boolean sort=true;
	protected boolean sort_dirsfirst=true;
	public a bd;//file content
	public final void root(final el e){this.root=e;}
	public final void root_and_path(final el root,final el path){this.root=root;this.path=path;}
	public final void bits(final int bits){this.bits=bits;}
	public final boolean hasbit(final int i){return (bits&i)!=0;}
	
	synchronized final public void to(final xwriter x) throws Throwable{
		x.spano(this);
//		tago("span").attr("id",e.id()).tagoe();
//		x.tago("span").attr("id",id()).tagoe();
		List<String>files;
		final boolean isfile=path.isfile();
		final String query=q.toString();
		element_editors.clear();
		Throwable exception=null;
		try{
			if(b.isempty(query))files=path.list();
			else files=path.list(query);
		}catch(Throwable t){
//			x.pl(b.stacktrace(t));//? allow ..
			exception=t;
			files=null;
//			return;
		}
		if(files!=null){
			if(sort)sort(files);
			if(sort_dirsfirst)sort_dirsfirst(files);
		}
		x.style();
		x.css("table.f","margin-left:auto;margin-right:auto");
		x.css("table.f tr:first-child","border:0;border-bottom:1px solid green;border-top:1px solid #070");
		x.css("table.f tr:last-child td","border:0;border-top:1px solid #040");
		x.css("table.f th:first-child","border-right:1px dotted #ccc");
		x.css("table.f td","padding:.5em;vertical-align:middle;border-left:1px dotted #ccc;border-bottom:1px dotted #ccc");
		x.css("table.f td:first-child","border-left:0");
		x.css("table.f td.icns","text-align:center");
		x.css("table.f td.size","text-align:right");
		x.css("table.f td.total","font-weight:bold");
		x.css("table.f td.name","min-width:100px");
		x.css("table.f th","padding:.5em;text-align:left;background:#f0f0f0;color:black");
		if(hasbit(BIT_ALLOW_QUERY))x.css(q,"float:right;background:yellow;border:1px dotted #555;text-align:right;width:10em;margin-left:1em");
//		x.css("table.f td.value","padding:0");
		x.css("table.f td.value input","width:18em;background:#eee;border:1px dotted #abc;padding:.5em");
		x.styleEnd();

		if(path instanceof el.el_actions){
			actions=((el.el_actions)path).actions();
			x.p("<div style=\"text-align:center\">");
			int i=0;
			for(final a e:actions){
				e.nm(Integer.toString(i));
				e.pt(this);
				i++;
				e.to(x);
			}
			x.divEnd();
		}
		x.table("f").nl();
		x.tr().th();
		if(hasbit(BIT_ALLOW_DIR_UP))if(!path.equals(root))x.ax(this,"up","••");
		final boolean acttd=hasbit(BIT_ALLOW_FILE_CREATE)||hasbit(BIT_ALLOW_DIR_CREATE);
		int cols=acttd?6:5;
		final boolean ommit_col_edit=path.ommit_column_edit();if(ommit_col_edit)cols--;
		final boolean ommit_col_lastmod=path.ommit_column_lastmod();if(ommit_col_lastmod)cols--;
		final boolean ommit_col_size=path.ommit_column_size();if(ommit_col_size)cols--;
		final boolean ommit_col_icon=path.ommit_column_icon();if(ommit_col_icon)cols--;
		x.th(cols);
		if(hasbit(BIT_DISP_PATH)){
//			if(path.isin(root)){
//			String pp=path.fullpath().substring(root.fullpath().length());
			String pp=path.name();
			x.span("float:left");
			x.p(pp);
			x.spanEnd();
//			}
		}
		final String icnfile="◻";
		final String icndir="⧉";
		final String icndel="x";
		final String icnsel="s";
		final String icnren="r";
		x.span("margin-left:22px;float:right");
		if(isfile){
			x.ax(this,"s",icnfile);
			x.ax(this,"sx","▣");
			x.spanEnd();
			x.nl();
		}else{
			if(hasbit(BIT_ALLOW_QUERY))x.inputax(q,null,this,null).focus(q);
			if(hasbit(BIT_ALLOW_FILE_CREATE))x.ax(this,"c",icnfile);
			if(hasbit(BIT_ALLOW_DIR_CREATE))x.ax(this,"d",icndir);
			x.spanEnd();
			x.nl();
			long total_bytes=0;
			firstinlist=null;
			if(files!=null)for(final String filenm:files){
				final el p=path.get(filenm);
				if(firstinlist==null)firstinlist=p;
				final String fnm=p.name();
//				final String nameenc=b.urlencode(name);
				final boolean isdir=p.isdir();
				x.tr();
				if(!ommit_col_icon){
					x.td("icns");
					if(isdir)
						if((bits&BIT_ALLOW_DIR_ENTER)!=0)x.ax(this,"e "+fnm,icndir);
						else x.p(icndir);
					else
						if((bits&BIT_ALLOW_FILE_OPEN)!=0&&p.isfile())x.ax(this,"e "+fnm,icnfile);
						else x.p(icnfile);
				}
				x.td("name");
				final String uri=p.uri();
				if((bits&BIT_ALLOW_FILE_LINK)!=0&&p.isfile()&&uri!=null)
					x.a(uri,fnm);
				else
					x.p(fnm);
				if(acttd){
					x.td("del");
					if(p.isfile()&&hasbit(BIT_ALLOW_FILE_DELETE))x.ax(this,"r "+fnm,icndel);				
					if(p.isdir()&&hasbit(BIT_ALLOW_DIR_DELETE))x.ax(this,"r "+fnm,icndel);
					if(hasbit(BIT_ALLOW_SELECT))x.ax(this,"se "+fnm,icnsel);
					if(hasbit(BIT_ALLOW_RENAME))x.ax(this,"ren "+fnm,icnren);
				}
				if(!ommit_col_edit){
					x.td("value");
					if(p instanceof el.el_column_value_editor){
						final a e=((el.el_column_value_editor)p).column_value_editor();
						e.nm(p.name().replace('_','X'));
						e.pt(this);
						element_editors.add(e);
						e.to(x);
					}else if(p instanceof el.el_column_value){
						((el.el_column_value)p).column_value(x);
					}
				}
				if(!ommit_col_lastmod){
					x.td("date");
					final long lm=p.lastmod();
					if(lm!=0)x.p(ttoa(lm));
				}
				if(!ommit_col_size){
					final long size=p.size();
					if(p.isfile())total_bytes+=size;
					x.td("size").p(isdir?"--":btoa(size));
				}
				x.nl();
			}
			x.tr();
			if(!ommit_col_icon)x.td();//icon
			x.td();//name
			if(!ommit_col_edit)x.td();//value
			if(acttd)x.td();//
			if(!ommit_col_lastmod)x.td();//lastmod
			if(!ommit_col_size)x.td("total size last").p(nf.format(total_bytes));//size
			x.nl();
		}
		x.tableEnd();
		if(isfile){
//			x.pre().nl().flush();
//			path.to(new osltgt(x.outputstream()));
			x.style().css(bd,"width:100%;height:100%;border:1px dotted green").styleEnd();
			x.inputTextArea(bd,"ed");
			x.focus(bd);
		}else{
			x.focus(q);
		}
		x.nl().nl();
		x.style(sts,"position:absolute;left:0;top:0;float:right");
		sts.to(x);
		if(exception!=null)
			x.nl().pl(b.stacktrace(exception));//? allow ..
		x.spanEnd();
		
		
	}
	private el firstinlist;
	private List<a>actions;
	@Override protected a chldq(String id){
		for(final a e:element_editors)
			if(e.nm().equals(id))return e;
		try{return actions.get(Integer.parseInt(id));}catch(Throwable ignored){}
		return super.chldq(id);
	}
	
	
	synchronized public void x_sel(final xwriter x,final String s)throws Throwable{
		if(q.str().equals("..")){
			x_up(x,s);
			return;
		}
		//? iffileandallowenter
		if(firstinlist!=null)x_e(x,firstinlist.name());
	}
	synchronized public final void x_e(final xwriter x,final String p)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_ENTER))throw new Error("notallowed");
		path=path.get(p);
		if(path.isfile())bd.from(path.inputstream());
		q.clr();
		x.xu(this);
 		x.xfocus(path.isfile()?bd:q);
	}

	private void sort_dirsfirst(final List<String>files){
		Collections.sort(files,new Comparator<String>(){public int compare(final String a,final String b){try{
			final boolean da=path.get(a).isdir();
			final boolean db=path.get(b).isdir();
			if(da&&db)return 0;
			if(!da&&!db)return 0;
			if(da&&!db)return -1;
			if(!da&&db)return 1;
			return 0;
		}catch(final Throwable t){throw new Error(t);}}});
	}
	private void sort(final List<String>files){
		Collections.sort(files,new Comparator<String>(){public int compare(final String a,final String b){
			return a.toString().toLowerCase(Locale.US).compareTo(b.toString().toLowerCase());
		}});
	}
	final protected String ttoa(final long ms){return df.format(ms);}
	final protected String btoa(final long n){return nf.format(n);}
//	private String query;
	synchronized public final void x_(final xwriter x,final String p)throws Throwable{
		if(!hasbit(BIT_ALLOW_QUERY))throw new Error("notallowed");
//		query=q.toString();
		x.xuo(this);
//		to(x.xub(this));x.xube();
//		x.p("var e=$('").p(q.id()).p("');e.setSelectionRange(e.value.length,e.value.length)").nl();
		x.xfocus(q);
	}
	synchronized public final void x_up(final xwriter x,final String y)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_UP))throw new Error("notallowed");
		final el p=path.parent();
		if(p==null)return;
		x.xu(sts.set("up "+p.name()));
//		path=p.isin(root)?p:root;
		path=p;
		x.xu(this);
		x.xfocus(q);
	}
	synchronized public final void x_c(final xwriter x,final String s)throws Throwable{
		if(!hasbit(BIT_ALLOW_FILE_CREATE))throw new Error("notallowed");
		if(q.toString().length()==0){x.xalert("enter name");x.xfocus(q);return;}
		path=path.get(q.toString());
		if(!path.exists())path.append("");
		bd.from(path.inputstream());
		x.xu(this);
		x.xfocus(bd);
	}
	synchronized public final void x_d(final xwriter x,final String s)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_CREATE))throw new Error("notallowed");
		if(q.toString().length()==0){x.xalert("enter name");x.xfocus(q);return;}
//		path.get(q.toString()).mkdirs();
		x.xu(this);
	}
	synchronized public final void x_r(final xwriter x,final String s)throws Throwable{
		final el p=path.get(s);
		if(path.isfile()&&!hasbit(BIT_ALLOW_FILE_DELETE))throw new Error("notallowed");
		if(path.isdir()&&!hasbit(BIT_ALLOW_DIR_DELETE))throw new Error("notallowed");//? onlydir
		p.rm();
		x.xu(this);
		x.xfocus(q);
	}
	synchronized public void x_s(final xwriter x,final String s)throws Throwable{
		if(!hasbit(BIT_ALLOW_FILE_MODIFY))throw new Error("notallowed");
		bd.to(path.outputstream());
	}
	synchronized public void x_sx(final xwriter x,final String s)throws Throwable{x_s(x,s);x_up(x,"");}
	synchronized public void x_ren(final xwriter x,final String s)throws Throwable{
		if(!hasbit(BIT_ALLOW_RENAME))throw new Error("notallowed");
//		if(!path.get(s).rename(path.get(selection.rnm.toString()))){
//			x.xalert("could not rename '"+s+"' to '"+selection.rnm+"'");
//		}
		x.xuo(this);
	}

	
	/// testing
	public static String string_prop;
	public static int int_prop;
	public static long long_prop;
	public static float float_prop;
	public static double double_prop;
	public static boolean bool_prop;
	public static @ui_action void elui_test(final xwriter x,final String q)throws Throwable{
		double_prop=Math.sin(int_prop*Math.PI/180);
	}
}
