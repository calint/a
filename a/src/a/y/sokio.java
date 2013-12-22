package a.y;
import java.io.IOException;
import java.nio.*;
import java.util.*;

import b.*;
import static b.b.*;
public class sokio extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	protected final ByteBuffer in=ByteBuffer.allocate(128);
	protected final ByteBuffer out=ByteBuffer.allocate(1024);
	private static List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
	final public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
		sokios.add(this);
		out.clear();
		out.put("\n\n\n retro text adventure game sokio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n\n< ".getBytes());
		out.flip();
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
		default:
		}
		out.put("\n< ".getBytes());
		return true;
	}
	
	
	
	
	
	
	
	
	
	public static interface lookable{} 
	public static interface enterable{} 
	public static interface selectable{}
	public static interface takeable{}
	public static interface copyable{}
	
	public static class any{
		protected String name="";
		protected String description="";
		public String toString(){return name;}
	}
	public static class location extends any implements lookable{
		protected List<location>exits=new LinkedList<location>();
		protected List<thing>things=new LinkedList<thing>();
//		public List<location>exits(){return exits;}
//		public List<thing>things(){return things;}
		public void things_put(final thing o){
			if(o.location!=null){
				o.location.things.remove(o);
			}
			o.location=this;
			things.add(o);
		}
	}
	public static class thing extends location implements Cloneable{
		protected location location;
		public Object clone(){
			//? deepcopy
			try{return super.clone();}catch(CloneNotSupportedException e){throw new Error(e);}
		}
	}
	private static class locdeps extends location implements enterable{locdeps(){
		name="hallway";
		description="u r in the hallway of departments";
		exits.add(new depguns());
		exits.add(new dephealth());
		exits.add(new deptreasury());
	}}
	private static class depguns extends location implements enterable{depguns(){
		name="guns";
	}}
	private static class dephealth extends location implements enterable{dephealth(){
		name="health";
	}}
	private static class deptreasury extends location implements enterable{deptreasury(){
		name="treasury";
		description="u r in the chamber of echos\n formerly known as treasury";
		things_put(new dust());
		things_put(new shoebox());
	}}
	private static class dust extends thing implements selectable{dust(){
		name="dust";
		things_put(new footsteps());
	}}
	private static class footsteps extends thing implements selectable{footsteps(){
		name="foot steps";
		description="u c foot steps";
	}}
	private static class shoebox extends thing implements selectable{shoebox(){
		name="shoe box";
		description="u c numerous iou notes";
	}}
	public static location root=new locdeps();
	private Stack<location>path=new Stack<location>();{path.push(root);}
	private List<thing>selection=new LinkedList<thing>();
	private List<thing>inventory=new LinkedList<thing>();
	
	public void look(){
		final location e=location();
		out.put(tobytes("\n"));
		out.put(tobytes(e.description));
		out.put("\n".getBytes());
		for(final location ee:e.exits){
			out.put("   ".getBytes());
			out.put(tobytes(ee.toString()));
			out.put("\n".getBytes());
		}
		if(e.things.isEmpty())
			return;
		out.put(tobytes("\nu c"));
		for(final any s:e.things){
			out.put((byte)' ');
			out.put(tobytes(s.toString()));
		}
		out.put(tobytes("\n"));
	}
	public void enter(){
		final StringBuilder sb=new StringBuilder(32);
		while(true){
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String where=sb.toString().trim();
		for(final location l:location().exits){
			if(l.toString().startsWith(where)){
				path.push(l);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void select(){
		final StringBuilder sb=new StringBuilder(32);
		while(true){
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String what=sb.toString().trim();
		for(final thing e:location().things){
			if(e.toString().startsWith(what)){
				selection().add(e);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void take(){
		final StringBuilder sb=new StringBuilder(32);
		while(true){
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String what=sb.toString().trim();
		for(final thing e:location().things){
			if(e.toString().startsWith(what)){
				inventory.add(e);
				location().things.remove(e);
				e.location=null;
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void copy(){
		final StringBuilder sb=new StringBuilder(32);
		while(true){
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String what=sb.toString().trim();
		for(final thing e:location().things){
			if(e.toString().startsWith(what)){
				final thing copy=(thing)e.clone();
				copy.location=null;
				copy.name="copy of "+copy.name;
				inventory.add(copy);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void drop(){
		final StringBuilder sb=new StringBuilder(32);
		while(true){
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String what=sb.toString().trim();
		for(final thing e:inventory){
			if(e.toString().startsWith(what)){
				inventory.remove(e);
				e.location=location();
				e.location.things.add(e);
				return;
			}
		}
		out.put(tobytes("not have"));
	}
	public void say(){
		final StringBuilder sb=new StringBuilder(64);
		while(true){
			final byte b=in.get();
//			if(b==' ')break;
			if(b=='\n')break;
			sb.append((char)b);
		}
		final String what=sb.toString().trim();
		for(final sokio e:sokios){
			try{e.so.write(ByteBuffer.wrap(("\n"+Integer.toHexString(e.hashCode())+" says "+what+"\n").getBytes()));}catch(final IOException ex){throw new Error(ex);}
		}
	}

	public void inventory(){
		out.put(tobytes("\nu hav"));
		for(final thing t:inventory)
			out.put("\n  ".getBytes()).put(tobytes(t.toString())).put("\n".getBytes());
		if(inventory.isEmpty())
			out.put(tobytes(" nothing\n"));
		out.put(tobytes("\nu hav selected"));
		for(final thing s:selection())
			out.put("\n  ".getBytes()).put(tobytes(s.toString())).put(tobytes(" from ")).put(tobytes(s.location.toString()));
		if(selection().isEmpty())
			out.put(tobytes(" nothing"));
		out.put("\n".getBytes());
	}
	public void back(){path.pop();}
	public location location(){return path.peek();}
	public List<thing>selection(){return selection;}
}
