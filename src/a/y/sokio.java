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
		out.put("> u typed ".getBytes());
		out.put(in.array(),in.position(),in.remaining());
		out.put("\n< ".getBytes());
		return true;
	}
	
	
	public static interface lookable{void lookable(final sokio so);} 
	public static interface enterable{} 
	public static interface selectable{}
	public static interface takeable{}
	
	public static class any{
		protected String description;		
	}
	public static class location extends any implements lookable,enterable{
		public void lookable(final sokio so){
			so.out.put(tobytes(description));
		}
	}
	private static class rootloc extends location{
		rootloc(){description="u r in initial location";}
	}
	public static location root=new rootloc();
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
