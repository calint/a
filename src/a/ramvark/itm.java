package a.ramvark;
import static b.b.strenc;
import static b.b.tobytes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import a.ramvark.cstore.meters;
import a.x.jskeys;
import b.a;
import b.b;
import b.xwriter;
public abstract class itm extends an implements $.labeled{
	public a pid,did,colr;

	boolean notnew;
	a elem_in_focus;
	a after_save_write_did_to_elem;
	a after_close_focus_elem;
	lst after_save_add_to_list;
	private String label;
	
	protected itm(){try{
		for(final Field f:getClass().getDeclaredFields()){
			in annot=f.getAnnotation(in.class);
			if(annot==null)continue;
			if(f.getType()==agr.class){
				final agr rf=(agr)f.get(this);
				rf.cls=annot.itm();
				rf.owner=this;
				continue;
			}
			if(f.getType()==ref.class){
				final ref rf=(ref)f.get(this);
				rf.cls=annot.lst().getAnnotation(ls.class).cls();
				continue;
			}
			if(annot.type()==3){
				final lst ls=(lst)f.get(this);
				ls.owner=this;
				continue;
			}
		}
	}catch(Throwable t){throw new Error(t);}}
	protected void onprerender()throws Throwable{}
	final public void to(final xwriter x)throws Throwable{
		onprerender();
		final String id=id();
		x.el(this);
//		x.nl();
		if(notnew)
			x.ax(this,"rn","⌾");
		x.p(" ").ax(this,"cl","••");
//		x.spc().ax(this,"sc","▣");
//		x.spc().ax(this,"sv","▢");
		try(final jskeys jk=new jskeys(x)){
			jk.add("cS","$x('"+id+" sv');ui.alert('saved')");
			jk.add("cW","$x('"+id+" cl')");
			jk.add("cX","$x('"+id+" sc')");
		}
		elem_in_focus=null;
		final LinkedList<Field>flds=new LinkedList<Field>();
		for(final Field f:getClass().getFields())//? fieldorderissue
			flds.addFirst(f);
		x.style();
		x.css("table.fm","width:40em;background:#fff;margin:.5em 0 1em .5em;box-shadow:0 0 .5em rgba(0,0,0,1);border-radius:0px");
//		x.css("table.fm tr","border-top:1px dotted #dea");
		x.css("table.fm tr td","vertical-align:middle;padding:.25em 2em .25em 1em");
		x.css("table.fm tr td.lbl","vertical-align:baseline;text-align:right;padding:1em 0 1em 5em;font-weight:bold");
		x.css("table.fm tr.lst td","background:"+colr+";padding-bottom:1em");
//		x.nl().css("table.fm tr:last-child","border-top:1px dotted green");
		x.css("input,select,textarea","width:20em;padding:.5em;background:#fff;box-shadow:0 0 .5em rgba(0,0,0,.5)");
//		x.css("input.ln","width:100%");
//		x.css("input.nm","width:100%");
		x.css("textarea.ls","width:100%;height:100px");
		x.css("textarea.ed","width:64em;height:400px");
		x.css("input.nbr","text-align:right;width:5em");
		x.style_();
		x.table("fm");
		for(final Field f:flds){
			final in annot=f.getAnnotation(in.class);
			if(annot==null)continue;
			x.nl().tr();
			final int t=annot.type();

			final an e=(an)f.get(this);
			if(e.has_bit(0))
				continue;
			if(elem_in_focus==null)
				elem_in_focus=e;

			if(t==4)
				x.td(2);
			else
				x.td("lbl").tago("label").attr("for",e.id()).tagoe().p(f.getName()).tage("label").td();
			
			try{getClass().getMethod("itm_"+f.getName()+"_in",xwriter.class).invoke(this,x);
				continue;
			}catch(final NoSuchMethodException ok){
			}catch(final InvocationTargetException e1){
				x.p(b.stacktraceline(e1.getTargetException()));
				continue;
			}
			if(e.has_bit(0)){
				x.r(e);
			}else{
				if(f.getType()==ref.class){
					inputref(x,e);
					x.spc();
					final itm m=((ref)e).get();
					if(m==null)
						continue;
					x.p(m.toString()).spc().ax(this,"refclr "+f.getName(),"x");
					continue;
				}
				final Class<? extends lst>lscls=annot.lst();
				if(lscls!=lst.class){
					inputref(x,e);
					x.spc();
					if(!e.isempty()){
						final Class<? extends itm>ocls=lscls.getAnnotation(ls.class).cls();
						final itm m=cstore.load(ocls,e.toString());
						x.p(m.toString());
						x.spc().ax(this,"agrclr "+f.getName(),"x");
					}
					continue;
				}
				if(annot.itm()!=itm.class){
					inputagr(x,e);
					continue;
				}
				if(t==0){
					x.inptxt(e,this,"sc","ln");
					continue;
				}
				if(t==1){
					x.inptxtarea(e,"ls");
					continue;
				}
				if(t==3){//aggr many
	//				((lst)e).owner=this;
					x.r(e);
					continue;
				}
				if(t==4){
					x.inptxtarea(e,"ed");
					continue;
				}
				if(t==5){
					x.inptxt(e,this,"sc","nbr");
					continue;
				}
			}
		}
		
		x.nl().tr("lst").td(2);
		x.style();
		x.nl().css("ul","padding:0");
//		x.nl().css("ul.ac","margin-left:21px");
		x.nl().css("ul li","display:inline;margin-right:1em");
		x.nl().style_();
		x.ul("ac");
		x.li().ax(this,"sc","▣");
		x.li().ax(this,"sv","▢");
		x.ul_();
		x.style(colr,"width:7em;margin-top:.2em");
		x.inptxt(colr,this,"sc");
		x.table_();

		if(elem_in_focus!=null)
			x.focus(elem_in_focus);
		
		x.el_();
	}
	//input aggr11
	final protected void inputagr(final xwriter x,final a e) throws Throwable{
//		final boolean isitm=e instanceof itm;
//		if(isitm&&!((itm)e).colr.isempty())x.p("<span style=\"border-bottom:3px dotted "+((itm)e).colr+"\">");
		x.p("<a href=\"javascript:$x('").p(id()).p(" agr ").p(e.nm()).p("')\" id=").p(e.id()).p(">⌾</a>");
//		if(isitm&&!((itm)e).colr.isempty())x.spanEnd();
	}
	final public synchronized void x_agr(final xwriter x,final String s)throws Throwable{
		final Field f=getClass().getField(s);
		final agr ra=(agr)f.get(this);
		final itm m=ra.get();
		m.label=f.getName();
		m.after_close_focus_elem=ra;
		ev(x,this,m);
	}
	final public synchronized void x_agrclr(final xwriter x,final String s)throws Throwable{
		final Field f=getClass().getField(s);
		final agr ra=(agr)f.get(this);
		ra.rm();
		x.xuo(this);
		x.xfocus(ra);
	}
	//input ref
	final protected void inputref(final xwriter x,final a e) throws Throwable{
		x.p("<a href=\"javascript:$x('").p(id()).p(" ref ").p(e.nm()).p("')\" id=").p(e.id()).p(">⌾</a>");
	}
	//reference select
	final public synchronized void x_ref(final xwriter x,final String s)throws Throwable{
		final Field f=getClass().getField(s);
		final Class<? extends lst>clsls=f.getAnnotation(in.class).lst();
		final lst ls=clsls.newInstance();
		final ref rf=(ref)f.get(this);
		ls.elem_selecting=rf;
		final String q;
		final itm o=rf.get();
		if(o!=null){
			q=o.toString();
		}else
			q="";
		ls.qry.set(q);
		ls.label="select "+f.getName();
		ev(x,this,ls);
	}
	final public synchronized void x_refclr(final xwriter x,final String s)throws Throwable{
		final Field f=getClass().getField(s);
		final ref rf=(ref)f.get(this);
		rf.rm();
		x.xuo(this);
		x.xfocus(rf);
	}

	protected boolean validate(final xwriter x)throws Throwable{
		final LinkedList<Field>flds=new LinkedList<Field>();
		for(final Field f:getClass().getFields())
			flds.addFirst(f);
		for(final Field f:flds){
			final in annot=f.getAnnotation(in.class);
			if(annot==null)continue;
			if(annot.must()){
				final a e=(a)f.get(this);
				if(e.toString().trim().length()==0){
					x.xalert("enter "+f.getName());
					x.xfocus(e);
					return false;
				}
			}
		}
		return true;
	}
	
	//save
	final public synchronized void x_sv(final xwriter x,final String s)throws Throwable{
		if(!validate(x))return;
		onpresave(x);
		cstore.save(this);
		onaftersave(x);
		if(after_save_write_did_to_elem!=null)after_save_write_did_to_elem.set(did.toString());
		if(after_save_add_to_list!=null)after_save_add_to_list.set(after_save_add_to_list.toString()+","+did);
	}
	//close
	final public synchronized void x_cl(final xwriter x,final String s)throws Throwable{
		ev(x,this,"cl");
	}
	//savevandvclose
	final public synchronized void x_sc(final xwriter x,final String s)throws Throwable{
		if(!validate(x))
			return;
		x_sv(x,null);
		if(after_save_write_did_to_elem!=null){
			ev(x,this,"cl2");
			x.xfocus(after_save_write_did_to_elem);
			return;
		}
		x_cl(x,null);
	}
	//rename
	public synchronized void x_rn(final xwriter x,final String s)throws Throwable{
		x.xalert("rename");
	}
	public String label(){return label!=null?label:toString();}
	
	//callbacks
	//protected void onnew()throws Throwable{}
	//protected void onafterload()throws Throwable{}
	protected void onpresave(final xwriter x)throws Throwable{}
	protected void onaftersave(final xwriter x)throws Throwable{}
	//protected void onpredelete()throws Throwable{}
	
////	public static interface ref{
////		// get may return null
////		public itm get()throws Throwable;
////		public boolean isnull();
////		public void ondelete();
////	}
////	public static interface agg extends ref{
////		// getcreate
////		public itm getc()throws Throwable;
////	}
//	public static interface aggm extends agg{
//		// visitor pattern (1:enter,2:up,0:default) 
//		public int apply(final cstore.visit cv);
//		// creates new
//		public itm mk();
//		// deletes did
//		public void rm(final String did);
//	}
//	public static interface refs extends agg{
//		// creates new item and adds it to list
//		public itm mk();
//		// adds did to list
//		public void add(final String did);
//		// removes did from list
//		public void rm(final String did);
//	}
	
	public static class ref extends an{
		protected Class<? extends itm>cls;
		public itm get()throws Throwable{
			if(isempty())return null;
			return cstore.load(cls,toString());
		}
		final public boolean isnull(){return isempty();}
		public void rm(){clr();}
		static final long serialVersionUID=1;
	}
	final public static class agr extends ref{
		protected itm owner;
		public itm get()throws Throwable{
			final itm m=super.get();
			if(m!=null)return m;
			final itm mm=cstore.create(cls,owner);
			set(mm.did.toString());
			return mm;
		}
		public void rm(){
			try{cstore.delete(cls,toString());}catch(final Throwable t){throw new Error(t);}
			super.clr();
		}
		//? ondelete
//		public void ondelete()throws Throwable{rm();}
		static final long serialVersionUID=1;
	}

	
	

	final private static byte[]bafieldsep=tobytes(":");
	final private static byte[]balinesep=tobytes("\n");		
	final public void load(final InputStream is)throws Throwable{
		meters.loads++;
		final Class<? extends itm>cls=getClass();
		final Reader re=new InputStreamReader(is,strenc);
		try{final StringBuilder sbname=new StringBuilder(256);
			final StringBuilder sbvalue=new StringBuilder(256);
			Field fld=null;
			int s=3;
			while(true){
				final int ch=re.read();
				if(ch==-1)
					break;
				switch(s){
				case 3://readfirstline
					if(ch==balinesep[0]){
						set(sbname.toString().trim().replace('\07','\n'));
						sbname.setLength(0);
						s=0;
					}else
						sbname.append((char)ch);
					break;
				case 0:
					if(ch==bafieldsep[0]){
						fld=cls.getField(sbname.toString().trim());
						sbvalue.setLength(0);
						s=1;
					}else
						sbname.append((char)ch);
					break;
				case 1:
					if(ch==balinesep[0]){
						final a ee=(a)fld.get(this);
						ee.set(sbvalue.toString().trim().replace('\07','\n'));
						sbname.setLength(0);
						s=0;
					}else
						sbvalue.append((char)ch);
					break;
				default:throw new Error();
				}
			}
		}finally{
			re.close();
		}
		notnew=true;
	}
	final public void save(final OutputStream os)throws Throwable{
		meters.saves++;
		final Class<? extends itm>cls=getClass();
		//head
		//. access list,summary,load,append,edit,save,remove
		//. time created,edited,deleted
		//. did,pid
		//. class
		//. name
		//. index,...
		os.write(tobytes(toString().replace('\n','\07')));//? e.to(os,enc)
		os.write(balinesep);
		//key value pairs
		for(final Field f:cls.getFields()){
			if(!a.class.isAssignableFrom(f.getType()))
				continue;
//			final in anot=f.getAnnotation(in.class);
//			if(anot!=null)
//				if(anot.type()==3)//dontwriteaggmanyfield
//					continue;
			final a e=(a)f.get(this);
			os.write(tobytes(f.getName()));//? f.getName().to(os,enc)
			os.write(bafieldsep);
			if(e!=null)
				os.write(tobytes(e.toString().replace('\n','\07')));//? m.to(os,enc)
			os.write(balinesep);
		}
		os.close();
		notnew=true;
	}
	
	static final long serialVersionUID=1;
}
