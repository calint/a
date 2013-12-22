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
		exits_add(new guns());
		exits_add(new health());
		exits_add(new treasury());
		exits_add(new pathplace(b.path()));
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
	public static void scantillnexttoken(final ByteBuffer bb){
		while(true){
			final byte b=bb.get();
			if(b==' ')break;
			if(b=='\n')break;
		}
	}
	protected boolean dodo()throws Throwable{
		final byte cmd=in.get();
		scantillnexttoken(in);
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
		List<place>exits();
		List<thing>things();
		List<sokio>sokios();
		boolean things_isempty();
		thing things_get(final String qry);
		void things_add(final thing o);
		void sokios_add(final sokio s);
		void sokios_remove(final sokio s);
		void sokios_recv(final String msg,final sokio exclude);	
		boolean exits_isempty();
		place exits_get(final String qry);
		void exits_add(final place o);
		place enter(final sokio so,final String qry);
	}
	public static class pathplace implements place{
		static final long serialVersionUID=1;
		final private path p;
		pathplace(final path p){this.p=p;}
		public String toString(){
			return p.name();
		}
		public String description(){return null;}
		public List<place>exits(){
			final List<place>dir=new LinkedList<place>();
			for(final String s:p.list()){
				final path pp=p.get(s);
				dir.add(new pathplace(pp));
			}
			return dir;
		}
		public List<thing>things(){return null;}
		public List<sokio>sokios(){return null;}
		public boolean things_isempty(){return true;}
		public thing things_get(final String qry){return null;}
		public void things_add(final thing o){}
		public void sokios_add(final sokio s){}
		public void sokios_remove(final sokio s){}
		public void sokios_recv(final String msg,final sokio exclude){}
		public boolean exits_isempty(){return p.list().length==0;}
		public place exits_get(final String qry){
			for(final String s:p.list()){
				final path pp=p.get(s);
				if(pp.name().startsWith(qry))return new pathplace(pp);
			}
			return null;
		}
		public void exits_add(final place o){}
		public place enter(final sokio so,final String qry){
			place dest=exits_get(qry);
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
		public List<thing>things(){return things;}
		public List<sokio>sokios(){return sokios;}
		public void things_add(final thing o){
			if(o.place!=null)
				o.place.things.remove(o);
			o.place=this;
			if(things==null)
				things=Collections.synchronizedList(new LinkedList<thing>());
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
				try{e.so.write(ByteBuffer.wrap(tobytes("\n"+msg+"\n")));}catch(final IOException ex){throw new Error(ex);}
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
		public place exits_get(final String qry){
			if(exits==null)return null;
			for(final place e:exits()){
				if(e.toString().startsWith(qry)){
					return e;
				}
			}
			return null;
		}
		public void exits_add(final place o){
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
		public boolean exits_isempty() {
			if(exits==null)return true;
			return exits.isEmpty();
		}
		public place enter(final sokio so,final String qry){
			place dest=exits_get(qry);
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
				return name;
			return aan+" "+name;
		}
	}

	public static anyplace root=new hallway();
	private Stack<place>path=new Stack<place>();{path.push(root);}
	private List<thing>selection=new LinkedList<thing>();
	private List<thing>inventory=new LinkedList<thing>();
	
	public void help(){
		out.put("\n\n\n retro text adventure game sokio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n".getBytes());
	}
	public void look(){
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
			final place loc=place().exits_get(qry);
			if(loc!=null){
				location_print(loc);
				return;				
			}
		}
	}
	private void location_print(final place e){
		final String d=e.description();
		if(d!=null&&d.length()>0){
			out.put(tobytes("\n"));
			out.put(tobytes(d));
			out.put("\n".getBytes());
		}
		if(!e.exits_isempty()){
			for(final place ee:e.exits()){
				out.put("   ".getBytes());
				out.put(tobytes(ee.toString()));
				out.put("\n".getBytes());
			}
		}
		if(!e.things_isempty()){
			out.put(tobytes("\nu c"));
			final int n=e.things().size();
			if(n<5){
				boolean first=true;
				for(final Iterator<thing>it=e.things().iterator();it.hasNext();){
					final thing th=it.next();
					if(first){
						first=false;
						out.put(tobytes(" "));
					}else if(!it.hasNext()){
						out.put(tobytes(" and "));
					}else{
						out.put(tobytes(", "));						
					}
					if(th.aan!=null){
						out.put(tobytes(th.aan));
						out.put(tobytes(" "));
					}
					out.put(tobytes(th.toString()));
				}
			}else{
//				out.put(tobytes(":"));
				for(final any s:e.things()){
					out.put(tobytes("\n  "));
					out.put(tobytes(s.toString()));
				}				
			}
			out.put(tobytes("\n"));
		}
		if(e.sokios()!=null&&e.sokios().size()>1){
			out.put(tobytes("\n"));
			for(final sokio s:e.sokios()){
				if(s==this)continue;
				out.put((byte)' ');
				out.put(tobytes(s.name));
			}
			out.put(tobytes(" is here\n"));
		}
	}
	public void enter(){
		final String where=in_toeol();
		final place dest=place().enter(this,where);
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
		for(final thing e:place().things()){
			if(e.toString().startsWith(what)){
				inventory.add(e);
				place().things().remove(e);
				e.place=null;
				place().sokios_recv(name+" took the "+e,this);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void copy(){
		final String what=in_toeol();
		for(final thing e:place().things()){
			if(e.toString().startsWith(what)){
				final thing copy=(thing)e.clone();
				copy.place=null;
				copy.name="copy of "+copy.name;
				copy.aan="a";
				inventory.add(copy);
				place().sokios_recv(name+" copied the "+e,this);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	thing inventory_get(final String qry){
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
		place().exits_add(nl);
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
