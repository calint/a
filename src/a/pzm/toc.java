package a.pzm;
import static b.b.isempty;

import java.util.LinkedList;
import java.util.List;

import a.pzm.lang.block;
import a.pzm.lang.statement;
import b.a;
import b.xwriter;
public class toc extends a{
	private statement stmt;public void set_statement(statement s){this.stmt=s;}
	private String textareaid;public void set_textareaid(String s){this.textareaid=s;}
	public a filter;{filter.set(".");}
	public void to(xwriter x) throws Throwable{
		x.el(this);
		if(stmt==null){
			x.pl(" no statement specified");
			x.el_();
			return;
		}
		x.style(this,"display:inline-block;background:#eee;box-shadow:0 0 3px rgba(0,0,0,.5);padding-left:1em;padding-right:1em");
		final LinkedList<String>indents=new LinkedList<>();
		x.inpax(filter,null,this,"",null).nl();
		x.style(filter,"background:#ff0;width:4em");
		x.focus(filter);
		rec(x,indents,stmt);
		x.el_();
	}
	private void rec(xwriter x,LinkedList<String>indents,statement e){
		final String fltr=filter.str();
		if(fltr.indexOf('.')!=-1)if(!e.has_annotations())
			return;
		String q=fltr;
		while(q.startsWith(".")){
			q=q.substring(1);
		}
		final String qry=q;
		if(qry.length()>0){
			for(String s:e.annotations(true).keySet()){
				if(!s.startsWith(q))
					return;
			}
		}
		
		indents.forEach(s->x.p(s));
		e.annotations(true).keySet().forEach(s->{
			x.p(s).spc();
		});
		final String[]ix=e.location_in_source().split(":");
		final String[]ixe=e.location_in_source_end().split(":");
		x.p("<a onmouseenter=\"");
		x.p("{var e=$('"+textareaid+"');e.selectionStart="+Integer.parseInt(ix[2])+";e.selectionEnd="+Integer.parseInt(ixe[2])+";e.focus();}");
		x.p("\">");
//		final String clsnm=e.getClass().getName();
		final String nm=e.nm();
		if(b.b.isempty(nm)){
//			e.source_to(x);
			x.p(e.token());
//			x.p(e.toString());
		}else{
			x.p(nm);
		}
//		x.p(!b.b.isempty(nm)?nm:clsnm.subSequence(clsnm.lastIndexOf('.')+1,clsnm.length()));
		x.a_();
//		x.spc().p('[').p(e.location_in_source()).p(',').p(e.location_in_source_end()).p(']');
//		if(e instanceof def){
//			final def d=(def)e;
//		}
//		e.source_to(x);
//		x.pl(e.toString());
		x.nl();
		
//		final String clsnm=e.getClass().getName();
//		x.pl(clsnm.substring(clsnm.lastIndexOf('.')+1));
		if(e instanceof block){
			indents.add("  ");
			final List<statement>ls=((block)e).statements();//? e instanceof block
			if(ls!=null)ls.forEach(
					s->rec(x,indents,s));
			indents.removeLast();
			return;
		}
//		x.pl(e.getClass().getName());			
	}
	public void x_(xwriter x,String p)throws Throwable{
		x.xuo(this);
		x.xfocus(filter);
	}
	private static final long serialVersionUID=1;
}
