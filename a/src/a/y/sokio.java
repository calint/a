package a.y;
import java.io.*;
import java.nio.*;
import java.util.*;
import b.*;
import static b.b.*;
public class sokio extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	protected String name;
	protected final ByteBuffer in=ByteBuffer.allocate(128);
	protected final ByteBuffer out=ByteBuffer.allocate(1024);
	private static List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
	final public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
		name=req.get().session().id();
		sokios.add(this);
//		out.clear();
		help();
		out_prompt();
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
		case'h':help();break;
		default:
		}
		out_prompt();
		return true;
	}
	private void out_prompt(){out.put("\n< ".getBytes());}
	
	
	
	
	
	
	
	
	
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
		protected List<location>exits=Collections.synchronizedList(new LinkedList<location>());
		protected List<thing>things=Collections.synchronizedList(new LinkedList<thing>());
		protected List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
		public void things_put(final thing o){
			if(o.location!=null){
				o.location.things.remove(o);
			}
			o.location=this;
			things.add(o);
		}
		public void sokiomes(final String msg,final sokio exclude){
			for(final sokio e:sokios){
				if(e==exclude)continue;
				try{e.so.write(ByteBuffer.wrap(tobytes("\n"+msg+"\n")));}catch(final IOException ex){throw new Error(ex);}
			}		
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
	
	public void help(){
		out.put("\n\n\n retro text adventure game sokio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n".getBytes());
	}
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
		if(!e.things.isEmpty()){
			out.put(tobytes("\nu c"));
			for(final any s:e.things){
				out.put((byte)' ');
				out.put(tobytes(s.toString()));
			}
			out.put(tobytes("\n"));
		}
		if(e.sokios.size()>1){
			out.put(tobytes("\n"));
			for(final sokio s:e.sokios){
				if(s==this)continue;
				out.put((byte)' ');
				out.put(tobytes(name));
			}
			out.put(tobytes(" is here\n"));
		}
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
				location().sokios.remove(this);
				location().sokiomes(name+" departed to "+l,this);
				l.sokiomes(name+" arrived from "+location(),this);
				l.sokios.add(this);
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
				location().sokiomes(name+" took "+e,this);
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
				location().sokiomes(name+" copied "+e,this);
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
				location().things_put(e);
				e.location.sokiomes(name+" dropped "+e,this);
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
		location().sokiomes(name+" says "+what,this);
	}
	public void inventory(){
		out.put(tobytes("\nu hav"));
		for(final thing t:inventory)
			out.put("\n  ".getBytes()).put(tobytes(t.toString()));
		if(inventory.isEmpty())
			out.put(tobytes(" nothing"));
		out.put(tobytes("\n\nu hav selected"));
		for(final thing s:selection())
			out.put("\n  ".getBytes()).put(tobytes(s.toString())).put(tobytes(" from ")).put(tobytes(s.location.toString()));
		if(selection().isEmpty())
			out.put(tobytes(" nothing"));
		out.put("\n".getBytes());
	}
	public void back(){
		final location loc=location();
		loc.sokios.remove(this);
		path.pop();
		loc.sokiomes(name+" departed to "+location(),this);
		location().sokios.add(this);
		location().sokiomes(name+" arrived from "+loc,this);
	}
	public location location(){return path.peek();}
	public List<thing>selection(){return selection;}
}
