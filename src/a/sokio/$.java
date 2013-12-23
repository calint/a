package a.sokio;
import java.nio.*;
import java.util.*;

import b.*;
import static b.b.*;
final public class $ extends a implements sock,threadedsock{
	static final long serialVersionUID=1;
//	public static String rootcls="a.sokio.ep2.$$hallway";
	public static String rootcls=entry.class.getName();
	public static class entry extends anyplace{static final long serialVersionUID=1;{
		description="\n\n\n retro text adventure game sokio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look enter go back exit select take drop copy  say goto inventory\n";
		places_add(new fileplace(path()));
	}}
	final public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
		name=req.get().session().id();
		help();
		out_prompt();
		out.flip();
		place().sokios_recv(name+" arrived",this);
		place().sokios_add(this);
		in.put(so.spil());
		in.flip();
		return write();
	}
//	private static enum state{line,sending};
//	private state st=state.line;
	public long meters_input;
	final public op read()throws Throwable{
		if(!in.hasRemaining()){
			in.clear();
			final int c=so.read(in);
			if(c==-1)return op.close;
			if(c==0)return op.read;
			meters_input+=c;
			in.flip();
		}
		while(in.hasRemaining()){
			if(parse()){
				out.flip();
				if(write()==op.write)
					return op.write;
			}
		}
		return op.read;
	}
	public long meters_output;
	final public op write()throws Throwable{
		final int c=so.write(out);
		meters_output+=c;
		if(out.hasRemaining())
			return op.write;
		out.clear();
		return op.read;
	}
	private boolean in_tillnexttoken(){
		wasonewordcmd=false;
		while(true){
			if(!in.hasRemaining())return false;
			final byte b=in.get();
			if(b==' ')break;
			if(b=='\n'){wasonewordcmd=true;break;}
		}
		return true;
	}
	protected boolean parse()throws Throwable{
		final byte cmd=in.get();
		if(cmd=='\n'){out_prompt();return true;}
		if(!in_tillnexttoken())throw new Error();
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
			case'w':describe();break;
			default:
			}
		}catch(final Throwable t){
			out.put(tobytes(stacktrace(t)));
		}
		out_prompt();
		return true;
	}
	private void out_prompt(){out.put("\n< ".getBytes());}
	private static anyplace root;static{try{root=(anyplace)Class.forName(rootcls).newInstance();}catch(final Throwable t){throw new Error(t);}}

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
			out.put("not found".getBytes());
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
			e.sokios_foreach(new place.sokiovisitor(){public boolean visit(final $ o)throws Throwable{
				if(o==$.this)return true;
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
			place().sokios_recv(name+" copied the "+p,$.this);
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
	private boolean wasonewordcmd;
	private String in_toeol(){
		if(wasonewordcmd)return null;
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
	}
	public void save()throws Throwable{
		in_toeol();
		final path p=b.path().get("u").get(getClass().getName()).get("root");
		p.writeobj(root);
		out.put(("saved "+p.size()+" bytes to "+p).getBytes());
	}
	public void load()throws Throwable{
		in_toeol();
		final path p=b.path().get("u").get(getClass().getName()).get("root");
		root=(anyplace)p.readobj();
		path.clear();
		path.add(root);
		out.put(("loaded "+p.size()+" bytes from "+p).getBytes());
	}
	public void describe(){
		final String s=in_toeol();
		final String s1=s.replaceAll("\\\\n","\n");
		place().description(s1);
	}

	
	
	
	public place place(){return path.peek();}
	public List<thing>selection(){return selection;}

	private sockio so;
	private String name;
	private final ByteBuffer in=ByteBuffer.allocate(1*K);
	private final ByteBuffer out=ByteBuffer.allocate(4*K);
//	transient private final static List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
	public void path_push(final place p){path.push(p);}
	public int so_write(final ByteBuffer bb)throws Throwable{return so.write(bb);}
	public void so_close(){so.close();}
}
