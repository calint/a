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
		enter(null,place());
		final long n=out.send_start(so);
		if(n==0)return op.write;
		meters_output+=n;
		if(!out.send_isdone())return op.write;
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
				if(c==0)return op.write;
				meters_output+=c;
			}
		return op.read;
	}
	final public op write()throws Throwable{
		final long c=out.send_resume(so);
		if(c==0)return op.write;
		meters_output+=c;
		if(!out.send_isdone())return op.write;
		out.clear();
		return op.read;
	}
	
	private static enum state{cmd,readline,parse};
	private state st=state.cmd;
	
	private StringBuilder in_cmd=new StringBuilder(2);
	private StringBuilder in_line=new StringBuilder(128);
	
	final private boolean c()throws Throwable{
		while(true){
			switch(st){
			case cmd:{
				if(!in.hasRemaining())return false;
				final byte cmd=in.get();
				if(cmd==' '){
					in_line.setLength(0);
					st=state.readline;
					break;
				}
				if(cmd=='\n'){
					in_line.setLength(0);
					st=state.parse;
					break;
				}
				in_cmd.append((char)cmd);
				break;}
			case readline:{
				if(!in.hasRemaining())return false;
				final byte ch=in.get();
				if(ch=='\n'){
					st=state.parse;
					break;
				}
				in_line.append((char)ch);
				break;}
			case parse:
				if(in_cmd.length()==0){
					st=state.cmd;
					break;
				}
				final char ch=in_cmd.charAt(0);
				try{
					final String ln=in_line.toString();
					switch(ch){
					case'l':c_look(ln);break;
					case'g':case'e':c_enter(ln);break;
					case'b':case'x':c_back();break;
					case't':c_take(ln);break;
					case'd':c_drop(ln);break;
					case'c':c_copy(ln);break;
					case's':c_select(ln);break;
					case'i':c_inventory();break;
					case'p':c_newplace(ln);break;
					case'o':c_newthing(ln);break;
					case'w':c_write(ln);break;
					case'z':c_say(ln);break;
					case'0':c_save(ln);break;
					case'9':c_load(ln);break;
					case'3':c_namesok(ln);break;
					case'2':c_stats();break;
					case'n':c_nameplace(ln);break;
					case'1':c_help();break;
					default:
					}
				}catch(final Throwable t){
					out.put(stacktrace(t));
				}
				out_prompt();
				in_cmd.setLength(0);
				st=state.cmd;
				return true;
			}
		}
	}
	final private void c_look(final String qry)throws Throwable{
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
			if(b.b||d!=null)out.put("\n");
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
	final private void c_enter(final String where){
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
		
		moveto(dest);
	}
	void moveto(final place to){
		final place from=place();
		enter(from,to);
		leave(from,to);
		path_push(to);
	}
	final private void enter(final place from,final place to){
		to.sokios_add(this);
		final String msg;
		if(from==null){
			msg=name+" arrived";
		}else{
			msg=name+" arrived from "+from;
		}
		to.sokios_recv(msg,this);//? msgq
	}
	final private void leave(final place from,final place to){
		from.sokios_remove(this);
		from.sokios_recv(name+" departed to "+to,this);
	}
	final private void c_back(){
		if(path.size()==1){
			out.put("cannot\n");
			return;
		}
		final place from=place();
		path.pop();
		final place to=place();
		leave(from,to);
		enter(from,to);
	}
	final private void c_take(final String what){
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
	final private void c_drop(final String what){
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
	final private void c_copy(final String what)throws Throwable{
		final thing o=place().things_get(what);
		if(o==null){
			out.put("not found\n");
			return;
		}
		final thing copy=(thing)o.clone();
		copy.place=null;
		copy.name="copy of "+copy.name;
		copy.aan="a";
		inventory.add(copy);
		place().sokios_recv(name+" copied the "+o,$.this);
	}
	final private void c_select(final String what){
		final thing e=place().things_get(what);
		if(e==null){
			out.put("not found\n");
			return;
		}
		selection().add(e);
	}
	final private void c_inventory(){
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
	final private void c_newplace(final String nm){
		if(nm==null)throw new Error("must name");
		final splace newplace=new splace();
		placeincontext=newplace;
		newplace.name=nm;
		place().places_add(newplace);
		place().sokios_recv(name+" created "+newplace,this);
		if(in_cmd.length()<2)return;
		final char op=in_cmd.charAt(1);
		if(op=='e')moveto(newplace);
	}
	final private void c_newthing(final String nm){
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
		if(in_cmd.length()<2)return;
		final char op=in_cmd.charAt(1);
		if(op=='d'){
			drop(o);
			return;
		}
		if(op=='e'){
			drop(o);
			moveto(o);
		}
	}
	final private void c_write(final String s){
		final String s1=s.replaceAll("\\\\n","\n");
		place().description(s1);
	}
	final private void c_say(final String s){
		place().sokios_recv(name+" says "+s,this);
	}
	final private void c_save(final String nm)throws Throwable{
		final path p=b.path().get("u").get(getClass().getName()).get(nm==null||nm.length()==0?"roome.ser":nm);
		p.writeobj(root);
		out.put("saved "+p.size()+" bytes to "+p);
	}
	final private void c_load(final String nm)throws Throwable{
		final path p=b.path().get("u").get(getClass().getName()).get(nm==null||nm.length()==0?"roome.ser":nm);
		root=(splace)p.readobj();
		path.clear();
		path.add(root);
		out.put("loaded "+p.size()+" bytes from "+p);
	}
	final private void c_namesok(final String nm){name=nm;}
	final private void c_stats(){
		out.put("(input,output)B=("+meters_input+","+meters_output+")\n");
	}
	final private void c_nameplace(final String s){
		final String s1=s.replaceAll("\\\\n","\n");
		place().name(s1);
	}
	final private void c_help(){
		out.put("\nkeywords: look go enter back exit select take drop copy  say goto inventory\n");
	}
	
	final private void out_prompt(){out.put("\n< ");}

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
//	final private String in_toeol(){
//		if(!in.hasRemaining())return null;
//		final StringBuilder sb=new StringBuilder(32);
//		while(true){
//			final byte b=in.get();
//			if(b=='\n')break;
//			sb.append((char)b);
//		}
//		final String nm=sb.toString().trim();
//		return nm;
//	}
	final place place(){return path.peek();}
	final private List<thing>selection(){return selection;}
	final void path_push(final place p){path.push(p);}
	final int so_write(final ByteBuffer bb)throws Throwable{return so.write(bb);}//? msgq
	final void so_close(){so.close();}
	
	private sockio so;
	final private Stack<place>path=new Stack<place>();{path.push(root);}
	final private List<thing>inventory=new LinkedList<thing>();
	final private List<thing>selection=new LinkedList<thing>();

	private place placeincontext;
	private String name;
	private ByteBuffer in;
	private final bufx out=new bufx(256);

	private static String rootcls=roome.class.getName();
	private static splace root;static{try{root=(splace)Class.forName(rootcls).newInstance();}catch(final Throwable t){throw new Error(t);}}	
}
