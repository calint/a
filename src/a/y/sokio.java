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
		out.put(" retro text adventure game sokio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n\n< ".getBytes());
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
	protected boolean dodo()throws Throwable{
		out.put("\n".getBytes());
		root.lookable(this);
//		out.put("> u typed ".getBytes());
//		out.put(in.array(),in.position(),in.remaining());
		out.put("\n< ".getBytes());
		return true;
	}
	
	
	public static interface lookable{void lookable(final sokio so);} 
	public static interface enterable{} 
	public static interface selectable{}
	public static interface takeable{}
	
	public static class any{
		protected String name;
		protected String description;		
	}
	public static class location extends any implements lookable,enterable{
		public void lookable(final sokio so){
			so.out.put(tobytes(description));
			so.out.put("\n".getBytes());
			for(final enterable e:exits){
				so.out.put("   ".getBytes());
				so.out.put(tobytes(e.toString()));
				so.out.put("\n".getBytes());
			}
		}
		protected List<location>exits=new LinkedList<location>();
		protected List<any>things=new LinkedList<any>();
		public String toString(){return name;}
	}
	private static class locdeps extends location{locdeps(){
		name="hallway";
		description="u r in the hallway of departments";
		exits.add(new depguns());
		exits.add(new dephealth());
		exits.add(new deptreasury());
	}}
	private static class depguns extends location{depguns(){
		name="guns";
	}}
	private static class dephealth extends location{dephealth(){
		name="health";
	}}
	private static class deptreasury extends location{deptreasury(){
		name="treasury";		
	}}
	public static location root=new locdeps();
	private Stack<enterable>path=new Stack<enterable>();{path.push(root);}
	private List<List<selectable>>selectlists=new LinkedList<List<selectable>>();{selectlists.add(new LinkedList<selectable>());}
	private List<takeable>inventory=new LinkedList<takeable>();
	
	public void look(){
		final enterable e=loc();
		if(e instanceof lookable){
			((lookable)e).lookable(this);
			return;
		}
		out.put(tobytes("not lookable"));
	}
	public void enter(final enterable o){
		path.push(o);
	}
	public void select(final selectable o){
		selection().add(o);
	}
	public void inventory(){
		for(final takeable t:inventory)
			out.put(tobytes(t.toString())).put("\n".getBytes());
	}
	public void xit(){path.pop();}
	public enterable loc(){return path.peek();}
	public List<selectable>selection(){return selectlists.get(selectlists.size()-1);}
}
