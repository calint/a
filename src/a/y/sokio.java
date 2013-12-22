package a.y;
import java.nio.*;
import java.util.*;
import b.*;
import static b.b.*;
public class sokio extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	protected final ByteBuffer in=ByteBuffer.allocate(128);
	protected final ByteBuffer out=ByteBuffer.allocate(1024);
	final public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
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
		case'e':enter();break;
		case's':select();break;
		case'x':xit();break;
		case'i':inventory();break;
		default:
		}
		out.put("\n< ".getBytes());
		return true;
	}
	
	
	
	
	
	
	
	
	
	public static interface lookable{void lookable(final sokio so);} 
	public static interface enterable{} 
	public static interface selectable{}
	public static interface takeable{}
	
	public static class any{
		protected String name="";
		protected String description="";
		protected location location;
		public String toString(){return name;}
		public any in(final location l){location=l;return this;}
	}
	public static class location extends any implements lookable{
		public void lookable(final sokio so){
			so.out.put(tobytes(description));
			so.out.put("\n".getBytes());
			for(final location e:exits){
				so.out.put("   ".getBytes());
				so.out.put(tobytes(e.toString()));
				so.out.put("\n".getBytes());
			}
		}
		protected List<location>exits=new LinkedList<location>();
		protected List<any>selectables=new LinkedList<any>();
		protected List<any>things=new LinkedList<any>();
		public List<location>exits(){return exits;}
		public List<any>selectables(){return selectables;}
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
		selectables.add(new dust().in(this));
		selectables.add(new shoebox().in(this));
	}}
	private static class dust extends location implements selectable{dust(){
		name="dust";
		selectables.add(new footsteps().in(this));
	}}
	private static class footsteps extends any implements selectable{footsteps(){
		name="foot steps";
	}}
	private static class shoebox extends any implements selectable{shoebox(){
		name="shoe box";
	}}
	public static location root=new locdeps();
	private Stack<location>path=new Stack<location>();{path.push(root);}
	private List<List<any>>selectlists=new LinkedList<List<any>>();{selectlists.add(new LinkedList<any>());}
	private List<takeable>inventory=new LinkedList<takeable>();
	
	public void look(){
		final location e=loc();
		out.put(tobytes("\n"));
		e.lookable(this);
		if(e.selectables.isEmpty())
			return;
		out.put(tobytes("\nu c"));
		for(final any s:e.selectables){
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
		for(final location l:loc().exits()){
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
		for(final any e:loc().selectables()){
			if(e.toString().startsWith(what)){
				selection().add(e);
				return;
			}
		}
		out.put(tobytes("not found"));
	}
	public void inventory(){
		out.put(tobytes("\nu hav"));
		for(final takeable t:inventory)
			out.put("\n  ".getBytes()).put(tobytes(t.toString())).put("\n".getBytes());
		if(inventory.isEmpty())
			out.put(tobytes(" nothing\n"));
		out.put(tobytes("\nu hav selected"));
		for(final any s:selection())
			out.put("\n  ".getBytes()).put(tobytes(s.toString())).put(tobytes(" from ")).put(tobytes(s.location.toString()));
		if(selection().isEmpty())
			out.put(tobytes(" nothing"));
		out.put("\n".getBytes());
	}
	public void xit(){path.pop();}
	public location loc(){return path.peek();}
	public List<any>selection(){return selectlists.get(selectlists.size()-1);}
}
