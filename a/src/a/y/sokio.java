package a.y;
import java.io.*;
import java.nio.*;
import java.util.*;

import b.*;
import static b.b.*;
final public class sokio extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	private String name;
	private final ByteBuffer in=ByteBuffer.allocate(1*K);
	private final ByteBuffer out=ByteBuffer.allocate(4*K);
//	transient private final static List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
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
		public String toString(){return name;}
	}
	public static class place extends any{
		private static final long serialVersionUID=1;
		private List<place>exits;
		private List<thing>things;
		transient protected List<sokio>sokios;
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
			for(final place e:exits){
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
	public static class thing extends place implements Cloneable{
		private static final long serialVersionUID=1;
		protected place place;
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
	private static class locdeps extends place{static final long serialVersionUID=1;{
		name="hallway";
		description="u r in the hallway of departments";
		exits_add(new depguns());
		exits_add(new dephealth());
		exits_add(new deptreasury());
	}}
	private static class depguns extends place{static final long serialVersionUID=1;{
		name="guns";
	}}
	private static class dephealth extends place{static final long serialVersionUID=1;{
		name="health";
	}}
	private static class deptreasury extends place{static final long serialVersionUID=1;{
		name="treasury";
		description="u r in the chamber of echos\n formerly known as treasury";
		things_add(new dust());
		things_add(new shoebox());
	}}
	private static class dust extends thing{static final long serialVersionUID=1;{
		name="dust";
		things_add(new footsteps());
	}}
	private static class footsteps extends thing{static final long serialVersionUID=1;{
		name="foot steps";
	}}
	private static class shoebox extends thing{static final long serialVersionUID=1;{
		aan="a";
		name="shoe box";
		description="u c numerous iou notes";
	}}
	public static place root=new locdeps();
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
	private void location_print(final place e) {
		if(e.description!=null&&e.description.length()>0){
			out.put(tobytes("\n"));
			out.put(tobytes(e.description));
			out.put("\n".getBytes());
		}
		if(!e.exits_isempty()){
			for(final place ee:e.exits){
				out.put("   ".getBytes());
				out.put(tobytes(ee.toString()));
				out.put("\n".getBytes());
			}
		}
		if(!e.things_isempty()){
			out.put(tobytes("\nu c"));
			final int n=e.things.size();
			if(n<5){
				boolean first=true;
				for(final Iterator<thing>it=e.things.iterator();it.hasNext();){
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
				for(final any s:e.things){
					out.put(tobytes("\n  "));
					out.put(tobytes(s.toString()));
				}				
			}
			out.put(tobytes("\n"));
		}
		if(e.sokios!=null&&e.sokios.size()>1){
			out.put(tobytes("\n"));
			for(final sokio s:e.sokios){
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
		for(final thing e:place().things){
			if(e.toString().startsWith(what)){
				inventory.add(e);
				place().things.remove(e);
				e.place=null;
				place().sokios_recv(name+" took the "+e,this);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void copy(){
		final String what=in_toeol();
		for(final thing e:place().things){
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
		final place nl=new place();
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
		root=(place)p.readobj();
		path.clear();
		path.add(root);
		out.put(("loaded "+p.size()+" bytes from "+p).getBytes());
	}

	
	
	
	public place place(){return path.peek();}
	public List<thing>selection(){return selection;}
}
