package a.sokio;
import java.nio.*;
import java.util.*;

import b.*;
import static b.b.*;
final public class $ extends a implements sock,threadedsock{
	private static final long serialVersionUID=1;
	public long meters_input;
	public long meters_output;
	final public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
		in=so.inbuf();
		name=req.get().session().id();
		c_help();
		out_prompt();
		place().sokios_recv(name+" arrived",this);
		place().sokios_add(this);
		final long n=out.send_start(so);
		if(n==0)
			return op.write;
		meters_output+=n;
		if(!out.send_isdone())
			return op.write;
		return op.read;
	}
	final public op read()throws Throwable{
		if(!in.hasRemaining()){
			in.clear();
			final int c=so.read(in);
			if(c==-1)return op.close;
			if(c==0)return op.read;
			meters_input+=c;
			in.flip();
		}
		while(in.hasRemaining())
			if(c()){
				final long c=out.send_start(so);
				if(c==0)
					return op.write;
				meters_output+=c;
			}
		return op.read;
	}
	final public op write()throws Throwable{
		final long c=out.send_resume(so);
		if(c==0)
			return op.write;
		meters_output+=c;
		if(!out.send_isdone())
			return op.write;
		out.clear();
		return op.read;
	}
	final private boolean c()throws Throwable{
		final byte cmd=in.get();
		try{
			switch(cmd){
			case'l':c_look();break;
			case'g':case'e':c_enter();break;
			case'b':case'x':c_back();break;
			case't':c_take();break;
			case'd':c_drop();break;
			case'c':c_copy();break;
			case's':c_select();break;
			case'i':c_inventory();break;
			case'p':c_newplace();break;
			case'o':c_newthing();break;
			case'w':c_write();break;
			case'z':c_say();break;
			case',':c_load();break;
			case'.':c_save();break;
			case'h':c_help();break;
			case'n':c_name();break;
			case'!':c_stats();break;
			default:
			}
		}catch(final Throwable t){
			out.put(stacktrace(t));
		}
		out_prompt();
		return true;
	}
	final private void out_prompt(){out.put("\n< ");}

	private Stack<place>path=new Stack<place>();{path.push(root);}
	private List<thing>selection=new LinkedList<thing>();
	private List<thing>inventory=new LinkedList<thing>();
	
	final private void c_help(){
		out.put("\nkeywords: look go enter back exit select take drop copy  say goto inventory\n");
	}
	final private void c_look()throws Throwable{
		final String qry=in_toeol();
		if(qry==null||qry.length()==0){
			print(place());
		}else{
			final thing th=inventory_get(qry);
			if(th!=null){
				print(th);
				return;
			}
			final thing thl=place().things_get(qry);
			if(thl!=null){
				print(thl);
				return;
			}
			final place loc=place().places_get(qry);
			if(loc!=null){
				print(loc);
				return;				
			}
			out.put("not found\n");
		}
	}
	final private void print(final place e)throws Throwable{
		final String d=e.description();
		out.put("\n");
		if(d!=null&&d.length()>0){
			out.put("\n").put(d).put("\n");
		}
		final class b{boolean b;}
		final b b=new b();
		e.places_foreach(new place.placevisitor(){public boolean visit(final place p)throws Throwable{
			b.b=true;
			out.put("   ").put(p.toString()).put("\n");
			return true;
		}});
		if(!e.things_isempty()){
			if(b.b||d!=null)
				out.put("\n");
			out.put("u c");
			final int n=e.things_size();
			if(n<5){
				final class counter{int c;}final counter c=new counter();
				e.things_foreach(new place.thingvisitor(){public boolean visit(final thing o)throws Throwable{
					if(c.c==0){
						out.put(" ");						
					}else if(c.c==n-1){
						out.put(" and ");						
					}else{
						out.put(", ");						
					}
					out.put(o.aanname());
					c.c++;
					return true;
				}});
			}else{
				out.put(":");
				e.things_foreach(new place.thingvisitor(){public boolean visit(final thing o)throws Throwable{
					out.put("\n  ");
					final String tag=o.aanname();
					out.put(tag);
					return true;
				}});
			}
			out.put("\n");
		}
		final int n=e.sokios_size();
		if(n>1){
			out.put("\n");
			e.sokios_foreach(new place.sokiovisitor(){public boolean visit(final $ o)throws Throwable{
				if(o==$.this)return true;
				out.put(" ").put(o.name);
				return true;
			}});
			out.put(" is here\n");
		}
	}
	final private void c_enter(){
		final String where=in_toeol();
		place dest;
		if(where==null||where.length()==0){
			dest=placeincontext;
		}else{
			dest=place().places_get(where);
			if(dest==null)dest=place().things_get(where);
		}
		if(dest==null){
			out.put("not found\n");
			return;
		}
		
		enter(dest);
	}
	void enter(final place dest){
		dest.sokios_add(this);
		dest.sokios_recv(name+" arrived from "+place(),this);
		place().sokios_remove(this);
		place().sokios_recv(name+" departed to "+dest,this);
		path_push(dest);
	}
	final private void c_name(){
		name=in_toeol();
	}
	final private void c_select(){
		final String what=in_toeol();
		final thing e=place().things_get(what);
		if(e==null){
			out.put("not found");
			return;
		}
		selection().add(e);
	}
	final private void c_take(){
		final String what=in_toeol();
		final thing e=place().things_get(what);
		if(e==null){
			out.put("not found\n");
			return;
		}
		inventory.add(e);
		place().things_remove(e);
		e.place=null;
		place().sokios_recv(name+" took the "+e,this);
	}
	final private void c_copy()throws Throwable{
		final String what=in_toeol();
		place().things_foreach(new place.thingvisitor(){public boolean visit(final thing o)throws Throwable{
			if(!o.toString().startsWith(what))return true;
			final thing copy=(thing)o.clone();
			copy.place=null;
			copy.name="copy of "+copy.name;
			copy.aan="a";
			inventory.add(copy);
			place().sokios_recv(name+" copied the "+o,$.this);
			return false;
		}});
		out.put("not found\n");
	}
	final private thing inventory_get(final String qry){
		for(final thing e:inventory){
			if(e.toString().startsWith(qry)){
				return e;
			}
		}
		return null;
	}
//	private anything inventory_get_first(){
//		if(inventory.isEmpty())return null;
//		return inventory.get(0);
//	}
	final private void c_drop(){
		final String what=in_toeol();
		final thing e=(thing)(what!=null?inventory_get(what):placeincontext);
		if(e==null){
			out.put("not have\n");
			return;
		}
		drop(e);
	}
	private void drop(final thing e) {
		inventory.remove(e);
		place().things_add(e);
		if(e.place!=null)e.place.sokios_recv(name+" dropped "+e.aanname(),this);
	}
	final private void c_say(){
		final String say=in_toeol();
		place().sokios_recv(name+" says "+say,this);
	}
	final private void c_inventory(){
		in.get();//\n
		out.put("\nu hav");
		for(final thing t:inventory){
			out.put("\n  ");
			if(t.aan!=null){
				out.put(t.aan).put(" ");
			}
			out.put(t.toString());
		}
		if(inventory.isEmpty())
			out.put(" nothing");
		out.put("\n\nu hav selected");
		for(final thing s:selection())
			out.put("\n  ").put(s.toString()).put(" from ").put(s.place.toString());
		if(selection().isEmpty())
			out.put(" nothing");
		out.put("\n");
	}
	final private void c_back(){
		in.get();//consume the \n
		if(path.size()==1){
			out.put("cannot\n");
			return;
		}
		final place loc=place();
		loc.sokios_remove(this);
		path.pop();
		loc.sokios_recv(name+" departed to "+place(),this);
		place().sokios_add(this);
		place().sokios_recv(name+" arrived from "+loc,this);
	}
	private place placeincontext;
	final private void c_newplace(){
		final byte op=in.get();
		final String nm=in_toeol();
		if(nm==null)throw new Error("must name");
		final splace newplace=new splace();
		placeincontext=newplace;
		newplace.name=nm;
		place().places_add(newplace);
		place().sokios_recv(name+" created "+newplace,this);	
		if(op=='e'){
			enter(newplace);
		}
	}
	final private void c_stats(){
		in.get();//\n
//		for(int n=0;n<1024*1024;n++){
//			out.put("ashfaosijf pweo posj dfoijsdfposdofioksdnflksncdlkncds");
//		}
		out.put("(input,output)B=("+meters_input+","+meters_output+")\n");
	}

	final private String in_toeol(){
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
	final private void c_newthing(){
		final byte op=in.get();
		final String nm=in_toeol();
		if(nm==null)throw new Error("must name");
		final thing o=new thing();
		placeincontext=o;
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
		if(op=='d'){
			drop(o);
			return;
		}
		if(op=='e'){
			drop(o);
			enter(o);
			return;
		}
	}
	final private void c_save()throws Throwable{
		in_toeol();
		final path p=b.path().get("u").get(getClass().getName()).get("root");
		p.writeobj(root);
		out.put("saved "+p.size()+" bytes to "+p);
	}
	final private void c_load()throws Throwable{
		in_toeol();
		final path p=b.path().get("u").get(getClass().getName()).get("root");
		root=(splace)p.readobj();
		path.clear();
		path.add(root);
		out.put("loaded "+p.size()+" bytes from "+p);
	}
	final private void c_write(){
		final String s=in_toeol();
		final String s1=s.replaceAll("\\\\n","\n");
		place().description(s1);
	}
	
	final place place(){return path.peek();}
	final private List<thing>selection(){return selection;}

	private sockio so;
	private String name;
	private ByteBuffer in;
	private final bufx out=new bufx(256);
	//	transient private final static List<sokio>sokios=Collections.synchronizedList(new LinkedList<sokio>());
	final void path_push(final place p){path.push(p);}
	final int so_write(final ByteBuffer bb)throws Throwable{return so.write(bb);}//? msgq
	final void so_close(){so.close();}


	private static String rootcls=roome.class.getName();
	private static splace root;static{try{root=(splace)Class.forName(rootcls).newInstance();}catch(final Throwable t){throw new Error(t);}}	
}
