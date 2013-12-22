package a.y;
import java.io.*;
import java.nio.*;
import java.util.*;

import b.*;
import static b.b.*;
final public class sokio extends a implements sock{
	static final long serialVersionUID=1;
/////////////////////////////////////////////////////////////////////////////////////	
	private static class hallway extends anyplace{static final long serialVersionUID=1;{
		name="hallway";
		description="u r in the hallway of departments";
		places_add(new guns());
		places_add(new health());
		places_add(new treasury());
		places_add(new pathplace(b.path()));
	}}
	private static class guns extends anyplace{static final long serialVersionUID=1;{}}
	private static class health extends anyplace{static final long serialVersionUID=1;{}}
	private static class treasury extends anyplace{static final long serialVersionUID=1;{
		description="u r in the chamber of echos\n formerly known as treasury";
		things_add(new dust());
		things_add(new shoebox());
	}}
	private static class dust extends thing{static final long serialVersionUID=1;{
		things_add(new foot_steps());
	}}
	private static class foot_steps extends thing{static final long serialVersionUID=1;{}}
	private static class shoebox extends thing{static final long serialVersionUID=1;{
		aan="a";
		name="shoe box";
		description="u c numerous iou notes";
	}}
/////////////////////////////////////////////////////////////////////////////////////	
	final public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
		name=req.get().session().id();
//		sokios.add(this);
//		out.clear();
		help();
		out_prompt();
		out.flip();
		place().sokios_add(this);
		place().sokios_recv(name+" arrived",this);
		return write();
	}
	final public op read()throws Throwable{
		in.clear();
		final int c=so.read(in);
		if(c==-1)return op.close;
		if(c==0)return op.read;
		in.flip();
		if(dodo()){
			out.flip();
			return write();
		}else return op.read;
	}
	final public op write()throws Throwable{
		final int cc=so.write(out);if(cc==0);
		if(out.hasRemaining())
			return op.write;
		out.clear();
		return op.read;
	}
	private void in_tillnexttoken(){
		while(true){
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n')break;
		}
	}
	protected boolean dodo()throws Throwable{
		final byte cmd=in.get();
		in_tillnexttoken();
		try{
			switch(cmd){
			case'l':look();break;
			case'g':case'e':enter();break;
			case's':select();break;
			case'x':case'b':back();break;
			case'i':inventory();break;
			case't':take();break;
			case'd':drop();break;
			case'c':copy();break;
			case'z':say();break;
			case'h':help();break;
			case'n':name();break;
			case'k':newplace();break;
			case'.':save();break;
			case',':load();break;
			case'o':newthing();break;
			default:
			}
		}catch(final Throwable t){
			out.put(tobytes(stacktrace(t)));
		}
		out_prompt();
		return true;
	}
	private void out_prompt(){out.put("\n< ".getBytes());}
	
	
	
	
	public static class any implements Serializable{
		private static final long serialVersionUID=1;
		protected String name;
		protected String description;
		public String name(){return name;}
		public String description(){return description;}
		public String toString(){
			if(name!=null)return name;
			final String s=getClass().getName().replace('_',' ');
			final int i=s.lastIndexOf('$');
			if(i==-1)return s;
			return s.substring(i+1);
		}
	}
	
	static public interface place{
		String description();
		
		place places_get(final String qry);
		void places_add(final place o);
		void places_foreach(final visitor v)throws Throwable;
		place places_enter(final sokio so,final String qry);
		
		boolean things_isempty();
		int things_size();
		thing things_get(final String qry);
		void things_add(final thing o);
		void things_remove(final thing o);
		void things_foreach(final visitor v)throws Throwable;
		
		int sokios_size();
		void sokios_add(final sokio s);
		void sokios_remove(final sokio s);
		void sokios_recv(final String msg,final sokio exclude);	
		void sokios_foreach(final sokiovisitor v)throws Throwable;

		static public interface visitor{boolean visit(final place p)throws Throwable;}
		static public interface sokiovisitor{boolean visit(final sokio o)throws Throwable;}
	}
	public static class pathplace implements place{
		static final long serialVersionUID=1;
		final private path p;
		pathplace(final path p){this.p=p;}
		public String toString(){return p.name();}
		public String description(){try{return p.readstr();}catch(final Throwable t){throw new Error(t);}}

		private List<place>places(){
			final List<place>dir=new LinkedList<place>();
			for(final String s:p.list()){
				final path pp=p.get(s);
				dir.add(new pathplace(pp));
			}
			return dir;
		}
		public void places_foreach(final visitor v)throws Throwable{
			for(final place p:places())
				if(!v.visit(p))break;
		}
		public boolean things_isempty(){return true;}
		public int things_size(){return 0;}
		public void things_foreach(final visitor v)throws Throwable{}
		public thing things_get(final String qry){return null;}
		public void things_add(final thing o){}
		public void things_remove(final thing o){}

		public int sokios_size(){return 0;}
		public void sokios_foreach(sokiovisitor v)throws Throwable{}
		public void sokios_add(final sokio s){}
		public void sokios_remove(final sokio s){}
		public void sokios_recv(final String msg,final sokio exclude){}

		public boolean places_isempty(){return p.list().length==0;}
		public place places_get(final String qry){
			for(final String s:p.list()){
				final path pp=p.get(s);
				if(pp.name().startsWith(qry))return new pathplace(pp);
			}
			return null;
		}
		public void places_add(final place o){
			final path f=p.get(o.toString());
			if(f.exists())return;
			final String txt=o.description();
			if(txt==null||txt.length()==0){
				try{f.mkfile();}catch(Throwable t){throw new Error(t);}
				return;
			}
			try{f.append(txt);}catch(Throwable t){throw new Error(t);}
		}
		public place places_enter(final sokio so,final String qry){
			place dest=places_get(qry);
			if(dest==null)dest=things_get(qry);
			if(dest==null)return null;
			so.place().sokios_remove(so);
			so.place().sokios_recv(this+" departed to "+dest,so);
			dest.sokios_recv(this+" arrived from "+so.place(),so);
			dest.sokios_add(so);
			so.path.push(dest);
			return dest;
		}
	}
	public static class anyplace extends any implements place{
		static final long serialVersionUID=1;
		private List<place>exits;
		private List<thing>things;
		transient protected List<sokio>sokios;
		public List<place>exits(){return exits;}
		public void places_foreach(final visitor v)throws Throwable{
			if(exits==null)return;
			for(final place p:exits)
				if(!v.visit(p))break;
		}
		public void things_foreach(final visitor v)throws Throwable{
			if(things==null)return;
			for(final thing p:things)
				if(!v.visit(p))break;
		}
		public void sokios_foreach(final sokiovisitor v)throws Throwable{
			if(sokios==null)return;
			for(final sokio s:sokios)
				if(!v.visit(s))break;
		}
		public int things_size(){if(things==null)return 0;return things.size();}
		public void things_remove(final thing o){if(things==null)return;things.remove(o);}
		public int sokios_size(){return sokios==null?0:sokios.size();}
		public List<sokio>sokios(){return sokios;}
		public void things_add(final thing o){
			if(o.place!=null)
				o.place.things.remove(o);
			o.place=this;
			if(things==null)things=Collections.synchronizedList(new LinkedList<thing>());
			things.add(o);
		}
		public void sokios_add(final sokio s){
			if(sokios==null)sokios=Collections.synchronizedList(new LinkedList<sokio>());
			sokios.add(s);
		}
		public void sokios_recv(final String msg,final sokio exclude){
			if(sokios==null)return;
			for(final sokio e:sokios){
				if(e==exclude)continue;
				try{e.so.write(ByteBuffer.wrap(tobytes("\n"+msg+"\n")));}
				catch(final IOException ex){throw new Error(ex);}
			}		
		}
		public thing things_get(final String qry){
			if(things==null)return null;
			for(final thing e:things){
				if(e.toString().startsWith(qry)){
					return e;
				}
			}
			return null;
		}
		public place places_get(final String qry){
			if(exits==null)return null;
			for(final place e:exits){
				if(e.toString().startsWith(qry)){
					return e;
				}
			}
			return null;
		}
		public void places_add(final place o){
			if(exits==null)
				exits=Collections.synchronizedList(new LinkedList<place>());
			exits.add(o);
		}
		public void sokios_remove(final sokio s){
			if(sokios==null)return;
			sokios.remove(s);
		}
		public boolean things_isempty(){
			if(things==null)return true;
			return things.isEmpty();
		}
//		public boolean exits_isempty() {
//			if(exits==null)return true;
//			return exits.isEmpty();
//		}
		public place places_enter(final sokio so,final String qry){
			place dest=places_get(qry);
			if(dest==null)dest=things_get(qry);
			if(dest==null)return null;
			so.place().sokios_remove(so);
			so.place().sokios_recv(name+" departed to "+dest,so);
			dest.sokios_recv(name+" arrived from "+so.place(),so);
			dest.sokios_add(so);
			so.path.push(dest);
			return dest;
		}
	}
	public static class thing extends anyplace implements Cloneable{
		private static final long serialVersionUID=1;
		protected anyplace place;
		protected String aan;
		public Object clone(){
			//? deepcopy
			try{return super.clone();}catch(CloneNotSupportedException e){throw new Error(e);}
		}
		public String aanname(){
			if(aan==null||aan.length()==0)
				return toString();
			return aan+" "+toString();
		}
	}

	private static anyplace root=new hallway();
	private Stack<place>path=new Stack<place>();{path.push(root);}
	private List<thing>selection=new LinkedList<thing>();
	private List<thing>inventory=new LinkedList<thing>();
	
	public void help(){
		out.put("\n\n\n retro text adventure game sokio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n".getBytes());
	}
	public void look()throws Throwable{
		final String qry=in_toeol();
		if(qry==null||qry.length()==0){
			location_print(place());
		}else{
			final thing th=inventory_get(qry);
			if(th!=null){
				location_print(th);
				return;
			}
			final thing thl=place().things_get(qry);
			if(thl!=null){
				location_print(thl);
				return;
			}
			final place loc=place().places_get(qry);
			if(loc!=null){
				location_print(loc);
				return;				
			}
		}
	}
	private void location_print(final place e)throws Throwable{
		final String d=e.description();
		if(d!=null&&d.length()>0){
			out.put(tobytes("\n"));
			out.put(tobytes(d));
			out.put("\n".getBytes());
		}
		e.places_foreach(new place.visitor(){public boolean visit(final place p)throws Throwable{
			out.put("   ".getBytes());
			out.put(tobytes(p.toString()));
			out.put("\n".getBytes());
			return true;
		}});
		if(!e.things_isempty()){
			out.put(tobytes("\nu c"));
			final int n=e.things_size();
			if(n<5){
				final class counter{int c;}final counter c=new counter();
				e.things_foreach(new place.visitor(){public boolean visit(final place p)throws Throwable{
					if(c.c==0){
						out.put(tobytes(" "));						
					}else if(c.c==n-1){
						out.put(tobytes(" and "));						
					}else{
						out.put(tobytes(", "));						
					}
					out.put(tobytes(((thing)p).aanname()));
					c.c++;
					return true;
				}});
			}else{
				out.put(tobytes(":"));
				e.things_foreach(new place.visitor(){public boolean visit(final place p)throws Throwable{
					out.put(tobytes("\n  "));
					final String tag=((thing)p).aanname();
					out.put(tobytes(tag));
					return true;
				}});
			}
			out.put(tobytes("\n"));
		}
		final int n=e.sokios_size();
		if(n>1){
			out.put(tobytes("\n"));
			e.sokios_foreach(new place.sokiovisitor(){public boolean visit(final sokio o)throws Throwable{
				if(o==sokio.this)return true;
				out.put((byte)' ');
				out.put(tobytes(o.name));
				return true;
			}});
			out.put(tobytes(" is here\n"));
		}
	}
	public void enter(){
		final String where=in_toeol();
		final place dest=place().places_enter(this,where);
		if(dest==null){
			out.put(tobytes("not found"));
			return;
		}
	}
	public void name(){
		name=in_toeol();
	}
	public void select(){
		final String what=in_toeol();
		final thing e=place().things_get(what);
		if(e==null){
			out.put(tobytes("not found"));
			return;
		}
		selection().add(e);
	}
	public void take(){
		final String what=in_toeol();
		final thing e=place().things_get(what);
		if(e==null){
			out.put(tobytes("not found"));
			return;
		}
		inventory.add(e);
		place().things_remove(e);
		e.place=null;
		place().sokios_recv(name+" took the "+e,this);
	}
	public void copy()throws Throwable{
		final String what=in_toeol();
		place().things_foreach(new place.visitor(){public boolean visit(final place p)throws Throwable{
			if(!p.toString().startsWith(what))return true;
			final thing copy=(thing)((thing)p).clone();
			copy.place=null;
			copy.name="copy of "+copy.name;
			copy.aan="a";
			inventory.add(copy);
			place().sokios_recv(name+" copied the "+p,sokio.this);
			return false;
		}});
		out.put(tobytes("not found"));
	}
	private thing inventory_get(final String qry){
		for(final thing e:inventory){
			if(e.toString().startsWith(qry)){
				return e;
			}
		}
		return null;
	}
	public void drop(){
		final String what=in_toeol();
		final thing e=inventory_get(what);
		if(e==null){
			out.put(tobytes("not have"));
			return;
		}
		inventory.remove(e);
		place().things_add(e);
		e.place.sokios_recv(name+" dropped "+e.aanname(),this);
	}
	public void say(){
		final String say=in_toeol();
		place().sokios_recv(name+" says "+say,this);
	}
	public void inventory(){
		out.put(tobytes("\nu hav"));
		for(final thing t:inventory){
			out.put("\n  ".getBytes());
			if(t.aan!=null){
				out.put(tobytes(t.aan)).put((byte)' ');
			}
			out.put(tobytes(t.toString()));
		}
		if(inventory.isEmpty())
			out.put(tobytes(" nothing"));
		out.put(tobytes("\n\nu hav selected"));
		for(final thing s:selection())
			out.put("\n  ".getBytes()).put(tobytes(s.toString())).put(tobytes(" from ")).put(tobytes(s.place.toString()));
		if(selection().isEmpty())
			out.put(tobytes(" nothing"));
		out.put("\n".getBytes());
	}
	public void back(){
		if(path.size()==1){
			out.put(tobytes("cannot"));
			return;
		}
		final place loc=place();
		loc.sokios_remove(this);
		path.pop();
		loc.sokios_recv(name+" departed to "+place(),this);
		place().sokios_add(this);
		place().sokios_recv(name+" arrived from "+loc,this);
	}
	public void newplace(){
		final String nm=in_toeol();
		final anyplace nl=new anyplace();
		nl.name=nm;
		place().places_add(nl);
		place().sokios_recv(name+" created "+nl,this);
	}
	private String in_toeol(){
		if(!in.hasRemaining())return null;
		final StringBuilder sb=new StringBuilder(32);
		while(true){
			final byte b=in.get();
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String nm=sb.toString().trim();
		return nm;
	}
	public void newthing(){
		final String nm=in_toeol();
		final thing o=new thing();
		if(nm.startsWith("a ")){
			o.aan="a";		
			o.name=nm.substring("a ".length());
		}else if(nm.startsWith("an ")){
			o.aan="an";
			o.name=nm.substring("an ".length());
		}else{
			o.name=nm;
		}
		inventory.add(o);
//		location().things_add(o);
//		location().sokiomes(name+" created "+nm,this);
	}
	public void save()throws Throwable{
//		final String where=in_toeol();
		final path p=b.path().get("u").get(getClass().getName()).get("root");
		p.writeobj(root);
		out.put(("saved "+p.size()+" bytes to "+p).getBytes());
	}
	public void load()throws Throwable{
//		final String where=in_toeol();
		final path p=b.path().get("u").get(getClass().getName()).get("root");
		root=(anyplace)p.readobj();
		path.clear();
		path.add(root);
		out.put(("loaded "+p.size()+" bytes from "+p).getBytes());
	}

	
	
	
	public place place(){return path.peek();}
	public List<thing>selection(){return selection;}

	private sockio so;
	private String name;
	private final ByteBuffer in=ByteBuffer.allocate(1*K);
	private final ByteBuffer out=ByteBuffer.allocate(4*K);
//	transient private final static List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
}
