package a.pz;

import static b.b.pl;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import b.xwriter;

public final class program implements Serializable{
	public static class instr extends stmt{
		public instr(program p){
			super(p);
		}
		public instr(final program r,final int op,final int ra,final int rd){
			super(r,op,ra,rd);
		}
		public instr(final program r,final int op,final int ra,final int rd,boolean flip_ra_rd){
			super(r,op,ra,rd,flip_ra_rd);
		}
		private static final long serialVersionUID=1;
	}
	public static class add extends instr{
		final public static int op=0x00a0;
		public add(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier());
			txt=new xwriter().p("add").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final static public class def_const extends def{
		public String name,value;
		public def_const(final program r) throws IOException{
			super(r);
			type=r.next_token_in_line();
			final def_type t=r.typedefs.get(type);
			if(t==null)
				throw new compiler_error(this,"type not found",type);
			name=r.next_token_in_line();
			final def_const d=r.defines.get(name);
			if(d!=null)
				throw new compiler_error(this,"define '"+name+"' already declared at "+d.location_in_source);
			if(!r.is_next_char_equals())
				throw new compiler_error(this,"expected format:  const int a=1");
			value=r.next_token_in_line();
			txt="const "+type+" "+name+"="+value;
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	final static public class expr_var extends expr{
		public String default_value;
		public expr_var(final program r) throws IOException{
			super(r,r.next_token_in_line());
			r.allocate_register(this,register);
			if(r.is_next_char_equals())
				default_value=r.next_token_in_line();
			if(!r.is_next_char_end_of_line())
				throw new compiler_error(this,"expected end of line");
			final xwriter x=new xwriter().p("var").spc().p(register);
			if(default_value!=null)
				x.p("=").p(default_value);
			txt=x.toString();
			if(default_value==null)
				return;
			final def_const dc=r.defines.get(default_value);
			if(dc!=null){
				type=dc.type;
				return;
			}
		}
		@Override protected void compile(program p){
			if(default_value==null)
				return;
			final def_const dc=p.defines.get(default_value);
			if(dc!=null){
				final program p2=new program("li "+register+" 0",p);
				final stmt s=p2.statements.get(0);
				bin=s.bin;
				//type="int&"
				return;
			}
			if(default_value.startsWith("&")){// li
				final program p2=new program("li "+register+" 0",p);
				final stmt s=p2.statements.get(0);
				bin=s.bin;
				//type="int&"
				return;
			}
			if(is_reference_to_register(default_value)){// tx
				if(!p.is_register_allocated(default_value))
					throw new compiler_error(this,"var not declared",default_value);
				final program p2=new program("tx "+register+" "+default_value);
				final stmt s=p2.statements.get(0);
				bin=s.bin;
				//type=p.register(default_value).type
				return;
			}
		}
		@Override protected void link(program p){
			if(default_value==null)
				return;
			final def_const dc=p.defines.get(default_value);
			if(dc!=null){
				bin[1]=Integer.parseInt(dc.value,16);
				return;
			}
			if(default_value.startsWith("&")){// li
				final String nm=default_value.substring(1);
				final def_label lb=p.labels.get(nm);
				if(lb==null)
					throw new compiler_error(this,"label not found",nm);
				bin[1]=lb.location_in_binary;
				return;
			}
		}
		private static final long serialVersionUID=1;
	}
	public static class expr extends stmt{
		//		public String lhs;
		public String register;
		public expr(program p,String register){
			super(p);
			//			this.lhs=lhs;
			this.register=register;
		}
		public static expr make_from_source_text(program p,String src){

			return null;
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_let extends expr{
		//		public String lhs;
		public stmt rhs;
		public String rh;
		public boolean is_ld;
		public boolean is_ldc;
		public expr_let(final program p,final String register) throws IOException{
			super(p,register);
			if(!p.is_register_allocated(register))
				throw new compiler_error(this,"var '"+register+"' has not been declared");
			if(p.is_next_char_star()){// d=*a
				rh=p.next_token_in_line();
				if(p.is_next_char_plus()){
					if(!p.is_next_char_plus())
						throw new compiler_error(this,"expected format *d=a++");
					is_ldc=true;
				}else
					is_ld=true;
				txt=new xwriter().p(register).p("=*").p(rh).p(is_ldc?"++":"").toString();
				return;
			}
			rh=p.next_token_in_line();
			txt=new xwriter().p(register).p("=").p(rh.toString()).toString();
			final def_const c=p.defines.get(rh);
			if(c!=null){//li
				rhs=c;
				return;
			}
		}
		@Override protected void compile(program p){
			if(is_ld){
				final stmt s=new stmt(p,ld.op,register_index_from_string(p,rh),register_index_from_string(p,register));
				s.compile(p);
				bin=s.bin;
				return;
			}
			if(is_ldc){
				final stmt s=new stmt(p,ldc.op,register_index_from_string(p,rh),register_index_from_string(p,register));
				s.compile(p);
				bin=s.bin;
				return;
			}
			if(rhs instanceof def_const){
				final stmt s=new stmt(p,li.op,0,register_index_from_string(p,register));
				s.compile(p);
				bin=new int[]{s.bin[0],Integer.parseInt(((def_const)rhs).value,16)};
				return;
			}
			if(is_reference_to_register(rh)){
				final stmt s=new stmt(p,tx.op,register_index_from_string(p,register),register_index_from_string(p,rh));
				s.compile(p);
				bin=s.bin;
				return;
			}
			if(rh.startsWith("&")){
				final stmt s=new stmt(p,li.op,0,register_index_from_string(p,register));
				s.compile(p);
				bin=new int[]{s.bin[0],0};
				return;
			}
			final stmt s=new stmt(p,li.op,0,register_index_from_string(p,register));
			s.compile(p);
			bin=new int[]{s.bin[0],Integer.parseInt(rh,16)};
			return;
		}
		@Override protected void link(program p){
			if(rh.startsWith("&")){
				final String label_name=rh.substring(1);
				final def_label lbl=p.labels.get(label_name);
				if(lbl==null)
					throw new compiler_error(this,"label not found "+lbl);
				bin[1]=lbl.location_in_binary;
				return;
			}
		}
		private static final long serialVersionUID=1;
	}
	final static public class def_struct extends def{
		public String name;
		private List<def_struct_member> fields;
		public def_struct(final program p) throws IOException{
			super(p);
			name=p.next_identifier();
			fields=new ArrayList<>();
			while(true){
				p.skip_whitespace_on_same_line();
				if(p.is_next_char_end_of_line())
					break;
				final def_struct_member f=new def_struct_member(p);
				fields.add(f);
			}
			final xwriter x=new xwriter();
			x.p("struct").spc().p(name);
			fields.forEach(f->x.spc().p(f.toString()));
			txt=x.toString();
		}
		@Override protected void validate_references_to_labels(program p){
			fields.forEach(e->e.validate_references_to_labels(p));
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class def_struct_member extends def{
		private String type;
		private String name;
		private String default_value;
		public def_struct_member(program p) throws IOException{
			super(p);
			name=p.next_identifier();
			type=p.next_type_identifier();
			default_value=p.next_token_in_line();
			final xwriter x=new xwriter();
			x.p(name).spc().p(type).spc().p(default_value);
			txt=x.toString();
		}
		@Override protected void validate_references_to_labels(program p) throws compiler_error{
			if(!p.typedefs.containsKey(type))
				throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	final static public class def_type extends def{
		public String name;
		public def_type(final program r) throws IOException{
			super(r);
			name=r.next_identifier();
			txt=new xwriter().p("typedef").spc().p(name).toString();
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class stmt implements Serializable{
		public String location_in_source;
		protected String txt;
		protected int[] bin;
		protected int location_in_binary;
		protected int znxr;
		protected int op;
		protected int rai;
		protected int rdi;
		protected String type;
		public stmt(final program r){
			location_in_source=r.location_in_source();
		}
		protected stmt(final program r,final int op,final int ra,final int rd){
			this(r);
			this.op=op;
			this.rai=ra;
			this.rdi=rd;
		}
		protected stmt(final program r,final int op,final int ra,final int rd,final boolean flip_ra_rd){
			this(r,op,rd,ra);
		}
		protected void validate_references_to_labels(program r){}
		protected void compile(program r){
			bin=new int[]{znxr_ci__ra__rd__()};
		}
		protected void link(program p){}
		protected int znxr_ci__ra__rd__(){
			return znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		}
		final @Override public String toString(){
			if(txt!=null)
				return txt;
			final xwriter x=new xwriter();
			if((znxr&3)==3)
				x.p("ifp ");
			else if((znxr&1)==1)
				x.p("ifz ");
			else if((znxr&2)==2)
				x.p("ifn ");
			x.p(txt);
			x.spc();
			if((znxr&4)==4)
				x.p(" nxt");
			if((znxr&8)==8)
				x.p(" ret");
			return x.toString();
		}
		public void source_to(xwriter x){}
		private static final long serialVersionUID=1;
	}
	public static class li extends instr{
		private String data;
		private int value;
		final public static int op=0x0000;
		public li(program r) throws IOException{
			super(r,li.op,0,r.next_register_identifier());
			data=r.next_token_in_line();
			txt="li "+(char)(rdi+'a')+" "+data;
		}
		//		private boolean is_integer(){try{Integer.parseInt(data);return true;}catch(Throwable t){return false;}}
		@Override protected void compile(program p){
			bin=new int[]{znxr_ci__ra__rd__(),0};
		}
		@Override protected void link(program p){
			final def_const def=p.defines.get(data);
			if(def!=null){
				data=def.value;
			}
			if(data.startsWith("&")){
				final String nm=data.substring(1);
				final def_label l=p.labels.get(nm);
				if(l==null)
					throw new compiler_error(this,"label not found",nm);
				value=l.location_in_binary;
			}else{
				try{
					value=Integer.parseInt(data,16);
				}catch(NumberFormatException e){
					throw new compiler_error(this,"cannot parse number '"+data+"'");
				}
			}
			final int bit_width=16;
			//			final int i=Integer.parseInt(data,16);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(value>max)
				throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			if(value<min)
				throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			bin=new int[]{bin[0],value};
		}
		private static final long serialVersionUID=1;
	}
	public static class inc extends instr{
		final public static int op=0x0200;
		public inc(program r) throws IOException{
			super(r,op,0,r.next_register_identifier());
			txt=new xwriter().p("inc").spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	public static class st extends instr{
		final public static int op=0x00d8;
		public st(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier());
			txt=new xwriter().p("st").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	public static class eof extends instr{
		public eof(program r) throws IOException{
			super(r);
			txt="..";
		}
		@Override protected void compile(program p){
			bin=new int[]{-1};
		}
		private static final long serialVersionUID=1;
	}
	public static class nxt extends instr{
		final public static int op=0x0004;
		public nxt(program r) throws IOException{
			super(r,nxt.op,0,0);
			txt="nxt";
		}
		private static final long serialVersionUID=1;
	}
	public static class ret extends instr{
		final public static int op=0x0008;
		public ret(program r) throws IOException{
			super(r,ret.op,0,0);
			txt="ret";
		}
		private static final long serialVersionUID=1;
	}
	public static class lp extends instr{
		final public static int op=0x0100;
		public lp(program r) throws IOException{
			super(r,lp.op,0,r.next_register_identifier());
			txt=new xwriter().p("lp").spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	public static class stc extends instr{
		final public static int op=0x0040;
		public stc(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier());
			txt=new xwriter().p("stc").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	public static class sub extends instr{
		final public static int op=0x0020;
		public sub(program r) throws IOException{
			super(r,sub.op,r.next_register_identifier(),r.next_register_identifier());
		}
		private static final long serialVersionUID=1;
	}
	public static class call extends instr{
		String label;
		final public static int op=0x0010;
		public call(program r) throws IOException{
			super(r,op,0,0);
			label=r.next_token_in_line();
			txt="call "+label;
		}
		@Override protected void link(program p){
			def_label l=p.labels.get(label);
			if(l==null)
				throw new compiler_error(this,"label not found",label);
			final int a=l.location_in_binary;
			bin[0]|=(a<<6);
		}
		private static final long serialVersionUID=1;
	}
	public static class data extends stmt{
		private List<String> data;
		public data(program r) throws IOException{
			super(r);
			data=new ArrayList<>();
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)
					break;
				data.add(t);
			}
			final xwriter x=new xwriter().p(". ");
			data.forEach(e->x.spc().p(e));
			txt=x.toString();
		}
		@Override protected void compile(program p){
			bin=new int[data.size()];
			int i=0;
			for(final String s:data){
				bin[i++]=Integer.parseInt(s,16);
			}
		}
		private static final long serialVersionUID=1;
	}
	public static class def_label extends def{
		public String name;
		public def_label(program p,String nm){
			super(p);
			this.name=nm;
			final def_label d=p.labels.get(name);
			if(d!=null)
				throw new compiler_error(this,"label '"+name+"' already declared at "+d.location_in_source);
			txt=":"+nm;
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class def_comment extends def{
		public String line;
		public def_comment(program p) throws IOException{
			super(p);
			line=p.consume_rest_of_line();
			txt="//"+line;
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class ld extends instr{
		final public static int op=0x00f8;
		public ld(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class ldc extends instr{
		final public static int op=0x00c0;
		public ldc(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier(),true);
			txt=new xwriter().p("ldc").spc().p((char)(rdi+'a')).spc().p((char)(rai+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	public static class tx extends instr{
		final public static int op=0x00e0;
		public tx(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class shf extends instr{
		final public static int op=0x0060;
		public shf(program r) throws IOException{
			super(r,op,r.next_register_identifier(),r.next_int(4),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class decl_data_int extends def_label{
		public String name,type,default_value;
		public decl_data_int(String name,String type,program p) throws IOException{
			super(p,name);
			if(p.is_next_char_equals()){//default value
				default_value=p.next_token_in_line();
			}
			final xwriter x=new xwriter().p(type).spc().p(name);
			if(default_value!=null)
				x.p("=").p(default_value);
			txt=x.toString();
			//			r.skip_whitespace_on_same_line();
			if(!p.is_next_char_end_of_line())
				throw new compiler_error(this,"expected end of line after: ["+txt+"]");
		}
		@Override protected void compile(program p){
			final int d=Integer.parseInt(default_value==null?"0":default_value,16);
			bin=new int[]{d};
		}
		private static final long serialVersionUID=1;
	}
	public static class compiler_error extends RuntimeException{
		public String source_location;
		public String message;
		public compiler_error(stmt s,String message){
			this(s.location_in_source,message);
		}
		public compiler_error(stmt s,String msg,String offender){
			this(s.location_in_source,msg+": "+offender);
		}
		public compiler_error(String source_location,String message){
			super(source_location+" "+message);
			this.source_location=source_location;
			this.message=message;
		}
		@Override public String toString(){
			return new xwriter().p("line ").p(source_location).spc().p(message).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public List<stmt> statements=new ArrayList<>();
	final public Map<String,def_const> defines=new LinkedHashMap<>();
	final public Map<String,def_type> typedefs=new LinkedHashMap<>();
	final public Map<String,def_struct> structs=new LinkedHashMap<>();
	final public Map<String,def_label> labels=new LinkedHashMap<>();
	final public Map<String,def_func> functions=new LinkedHashMap<>();

	public program(final String source){
		this(null,new StringReader(source));
	}
	public program(final String source,final program context_program){
		this(context_program,new StringReader(source));
	}
	public boolean is_register_allocated(String register){
		return allocated_registers.containsKey(register);
	}
	public program(final program context_program,final Reader source){try{
		if(context_program!=null){
			typedefs.putAll(context_program.typedefs);
			labels.putAll(context_program.labels);
			defines.putAll(context_program.defines);
		}
		this.pr=new PushbackReader(source,1);
		while(true){
			final stmt st=next_statement();
			if(st==null)
				break;
			pl(st.toString());
			statements.add(st);
		}
		statements.forEach(e->e.validate_references_to_labels(this));
		statements.forEach(e->e.compile(this));
		int pc=0;
		for(final stmt ss:statements){
			ss.location_in_binary=pc;
			if(ss.bin!=null)
				pc+=ss.bin.length;
		}
		statements.forEach(e->e.link(this));
	}catch(IOException e){throw new Error(e);}}
	private stmt next_statement() throws IOException{
		String tk="";
		while(true){
			skip_whitespace();
			pl(" line "+location_in_source());
			if(is_next_char_end_of_file())
				return null;
			if(is_next_char_slash())
				if(is_next_char_slash())
					return new def_comment(this);
				else
					unread('/');
			if(is_next_char_star())//st or stc   *a=d   *a++=d   *(a+++b)=d 
				return new expr_store(this);
			tk=next_token_in_line();
			if(tk==null)
				return new eof(this);
			if(tk.equals("const")){
				final def_const s=new def_const(this);
				defines.put(s.name,s);
				return s;
			}
			if(tk.equals("typedef")){
				final def_type s=new def_type(this);
				typedefs.put(s.name,s);
				return s;
			}
			if(tk.equals("struct")){
				final def_struct s=new def_struct(this);
				structs.put(s.name,s);
				return s;
			}
			if(tk.equals("var")){
				final expr_var s=new expr_var(this);
				//				structs.put(s.name,s);
				return s;
			}
			if(tk.startsWith(":")){
				final def_label s=new def_label(this,tk.substring(1));
				consume_rest_of_line();
				labels.put(s.name,s);
				return s;
			}
			final def_type td=typedefs.get(tk);
			if(td!=null){// int a=2   int main(int a)
				final String name=next_token_in_line();
				if(is_next_char_paranthesis_left()){//  int main(int a)
					final def_func df=new def_func(name,td.name,this);
					functions.put(name,df);
					return df;
				}
				final decl_data_int s=new decl_data_int(name,td.name,this);
				labels.put(s.name,s);
				return s;
			}
			if(tk.equals(".")){
				final data s=new data(this);
				consume_rest_of_line();
				return s;
			}
			if(tk.equals("..")){
				final eof s=new eof(this);
				consume_rest_of_line();
				return s;
			}
			break;
		}
		final int nxtch=read();
		switch(nxtch){
		case '='://assignment
			final expr_let s=new expr_let(this,tk);
			return s;
		case '+'://expression or addstore or inc
			if(is_next_char_plus()){
				final expr_increment st=new expr_increment(this,tk);
				return st;
			}else if(is_next_char_equals()){
				final expr_add st=new expr_add(this,tk);
				return st;
			}
			throw new Error("expressions not supported yet");
		case '('://function call
			final expr_function_call sc=new expr_function_call(this,tk);
			return sc;
		default:
			unread(nxtch);
		}
		int znxr=0;
		switch(tk){
		case "ifz":{
			znxr=1;
			tk=next_token_in_line();
			break;
		}
		case "ifn":{
			znxr=2;
			tk=next_token_in_line();
			break;
		}
		case "ifp":{
			znxr=3;
			tk=next_token_in_line();
			break;
		}
		}
		final stmt s;
		try{
			s=(program.stmt)Class.forName(getClass().getName()+"$"+tk).getConstructor(program.class).newInstance(this);
			s.znxr=znxr;
		}catch(InvocationTargetException t){
			if(t.getCause() instanceof compiler_error)
				throw (compiler_error)t.getCause();
			throw new compiler_error(location_in_source(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new compiler_error(location_in_source(),t.toString());
		}catch(ClassNotFoundException t){
			throw new compiler_error(location_in_source(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new compiler_error(location_in_source(),t.toString());
		}
		while(true){
			final String t=next_token_in_line();
			if(t==null)
				break;
			if("nxt".equalsIgnoreCase(t)){
				s.znxr|=4;
				continue;
			}
			if("ret".equalsIgnoreCase(t)){
				s.znxr|=8;
				continue;
			}
			if(t.startsWith("//")){
				consume_rest_of_line();
				break;
			}
			throw new Error("3 "+t);
		}
		return s;
	}
	private boolean is_next_char_plus() throws IOException{
		final int ch=read();
		if(ch=='+')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_bracket_left() throws IOException{
		final int ch=read();
		if(ch=='[')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_paranthesis_left() throws IOException{
		final int ch=read();
		if(ch=='(')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_paranthesis_right() throws IOException{
		final int ch=read();
		if(ch==')')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_comma() throws IOException{
		final int ch=read();
		if(ch==',')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_star() throws IOException{
		final int ch=read();
		if(ch=='*')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_slash() throws IOException{
		final int ch=read();
		if(ch=='/')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_equals() throws IOException{
		final int ch=read();
		if(ch=='=')
			return true;
		unread(ch);
		return false;
	}
	private boolean is_next_char_end_of_file() throws IOException{
		final int ch=read();
		if(ch==-1)
			return true;
		unread(ch);
		return false;
	}
	private void disassemble_to(xwriter x){
		statements.forEach(e->x.pl(e.toString()));
	}
	//	public source_reader(final Reader source,final int lineno,final int charno){
	//		this.source=new PushbackReader(source,1);
	//		this.line_number=lineno;
	//		this.character_number_in_line=charno;
	//	}
	//	@Override public String toString(){
	//		return hr_location_string_from_line_and_col(line_number,character_number_in_line);
	//	}
	/** writes binary */
	final public void zap(int[] rom){//? arraycopybinary
		for(int i=0;i<rom.length;i++)
			rom[i]=-1;
		int pc=0;
		for(final stmt ss:statements){
			if(ss.bin==null)
				continue;
			final int c=ss.bin.length;
			System.arraycopy(ss.bin,0,rom,pc,c);
			pc+=c;
		}
	}
	private String location_in_source(){
		return hr_location_string_from_line_and_col(line_number,character_number_in_line);
	}
	private static String hr_location_string_from_line_and_col(final int ln,final int col){
		return ln+":"+(col+1);
	}
	private int read() throws IOException{
		final int ch=pr.read();
		character_number_in_line++;
		if(ch==newline){
			line_number++;
			character_number_in_line=0;
		}
		return ch;
	}
	//	private int read(final char[]cbuf,int off,int len)throws IOException{
	//		final int i=source.read(cbuf,off,len);
	//		while(len-->0){
	//			final int ch=cbuf[off++];
	//			character_number_in_line++;
	//			if(ch==newline){line_number++;character_number_in_line=0;}
	//		}
	//		return i;
	//	}
	//	protected void close()throws IOException{}
	private void unread(int c) throws IOException{
		pr.unread(c);
		character_number_in_line--;
		if(character_number_in_line<0){
			line_number--;
			character_number_in_line=0;
			if(line_number==0)
				throw new Error();
		}
	}
	private final String next_token_in_line() throws IOException{
		skip_whitespace_on_same_line();
		final StringBuilder sb=new StringBuilder();
		while(true){
			final int ch=read();
			if(ch==-1)
				break;
			if(ch=='\n'){
				unread(ch);
				break;
			}
			if(Character.isWhitespace(ch))
				break;
			if(ch=='='){
				unread(ch);
				break;
			}
			if(ch=='+'){
				unread(ch);
				break;
			}
			if(ch=='('){
				unread(ch);
				break;
			}
			if(ch==','){
				unread(ch);
				break;
			}
			if(ch==')'){
				unread(ch);
				break;
			}
			sb.append((char)ch);
		}
		skip_whitespace_on_same_line();
		if(sb.length()==0)
			return null;
		return sb.toString();
	}
	private final void skip_whitespace_on_same_line() throws IOException{
		while(true){
			final int ch=read();
			if(ch=='\n'){
				unread(ch);
				return;
			}
			if(Character.isWhitespace(ch))
				continue;
			if(ch==-1)
				return;
			unread(ch);
			return;
		}
	}

	private PushbackReader pr;
	private final static int newline='\n';
	private int line_number=1,character_number_in_line;
	final public static int opneg=0x0300;
	final public static int opskp=0x0080;
	final public static int opdac=0x0400;
	final public static int opwait=0x0058;
	final public static int opnotify=0x0078;
	private void skip_whitespace() throws IOException{
		while(true){
			final int ch=read();
			if(Character.isWhitespace(ch))
				continue;
			if(ch==-1)
				return;
			unread(ch);
			return;
		}
	}
	private String next_identifier() throws IOException{
		final String id=next_token_in_line();
		if(id==null)
			throw new compiler_error(location_in_source(),"expected identifier but got end of line");
		if(id.length()==0)
			throw new compiler_error(location_in_source(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))
			throw new compiler_error(location_in_source(),"identifier '"+id+"' starts with a number");
		return id;
	}
	private String next_type_identifier() throws IOException{
		final String id=next_token_in_line();
		if(id==null)
			throw new program.compiler_error(location_in_source(),"expected type identifier but got end of line");
		if(id.length()==0)
			throw new program.compiler_error(location_in_source(),"type identifier is empty");
		//is_valid_type_identifier
		if(Character.isDigit(id.charAt(0)))
			throw new program.compiler_error(location_in_source(),"type identifier '"+id+"' starts with a number");
		return id;
	}
	private boolean is_next_char_end_of_line() throws IOException{
		final int ch=read();
		if(ch=='\n')
			return true;
		unread(ch);
		return false;
	}
	private int next_register_identifier() throws IOException{
		final String s=next_token_in_line();
		if(s==null)
			throw new program.compiler_error(location_in_source(),"expected register but found end of line");
		if(s.length()!=1)
			throw new program.compiler_error(location_in_source(),"register name unknown '"+s+"'");
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		final int max=(1<<4)-1;//? magicnumber
		final int min=0;
		if(reg>max||reg<min)
			throw new program.compiler_error(location_in_source(),"register '"+s+"' out range 'a' through 'p'");
		return reg;
	}
	private int next_int(int bit_width) throws IOException{
		final String s=next_token_in_line();
		if(s==null)
			throw new program.compiler_error(location_in_source(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)
				throw new program.compiler_error(location_in_source(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)
				throw new program.compiler_error(location_in_source(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){
			throw new program.compiler_error(location_in_source(),"can not translate number '"+s+"'");
		}
	}
	//	private void assert_and_consume_end_of_line()throws IOException{
	//		final int eos=read();
	//		if(eos!='\n'&&eos!=-1)throw new program.compiler_error(hrs_location(),"expected end of line or end of file");
	//	}
	private String consume_rest_of_line() throws IOException{
		final StringBuilder sb=new StringBuilder();
		while(true){
			final int ch=read();
			if(ch==-1)
				break;
			if(ch=='\n')
				break;
			sb.append((char)ch);
		}
		return sb.toString();
	}
	public String toString(){
		final xwriter x=new xwriter();
		disassemble_to(x);
		return x.toString();
	}

	static int register_index_from_string(program p,String register){
		if(register.length()!=1)
			throw new compiler_error(p.location_in_source(),"not a register: "+register);
		final int i=register.charAt(0)-'a';
		final int nregs=16;//? magicnumber
		if(i<0||i>=nregs)
			throw new compiler_error(p.location_in_source(),"register not found: "+register);
		return i;
	}
	static boolean is_reference_to_register(String ref){
		if(ref.length()!=1)
			return false;
		final char ch=ref.charAt(0);
		return ch>='a'&&ch<='p';
	}

	final static public class expr_increment extends expr{
		public expr_increment(final program p,final String register) throws IOException{
			super(p,register);
			txt=new xwriter().p(register).p("++").toString();
		}
		@Override protected void compile(program p){
			final stmt s=new stmt(p,inc.op,0,program.register_index_from_string(p,register));
			s.compile(p);
			bin=s.bin;
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_add extends expr{
		public String rhs;
		public expr_add(final program p,final String register) throws IOException{
			super(p,register);
			rhs=p.next_token_in_line();
			txt=new xwriter().p(register).p("+=").p(rhs).toString();
		}
		@Override protected void compile(program p){
			final stmt s=new stmt(p,add.op,program.register_index_from_string(p,register),rhs.charAt(0)-'a');
			s.compile(p);
			bin=s.bin;
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_store extends expr{
		public String rhs;
		public boolean inca;
		public expr_store(final program p) throws IOException{
			super(p,p.next_token_in_line());
			if(p.is_next_char_plus()){
				if(!p.is_next_char_plus())
					throw new compiler_error(this,"expected format *a++=d");
				inca=true;
				p.skip_whitespace_on_same_line();
				if(!p.is_next_char_equals())
					throw new compiler_error(this,"expected format *a++=d");
				rhs=p.next_token_in_line();
				txt=new xwriter().p("*").p(register).p("++=").p(rhs).toString();
				return;
			}
			if(!p.is_next_char_equals())
				throw new compiler_error(this,"expected '=' but found '"+(char)p.read()+"'");
			rhs=p.next_token_in_line();
			txt=new xwriter().p("*").p(register).p("=").p(rhs).toString();
		}
		@Override protected void compile(program p){
			//? ensure lhs,rhs are registers
			//			final expr lhse=expr.make_from_source_text(p,register);
			final stmt s=new stmt(p,inca?stc.op:st.op,register_index_from_string(p,register),register_index_from_string(p,rhs));
			s.compile(p);
			bin=s.bin;
		}
		private static final long serialVersionUID=1;
	}
	public static class def extends stmt{
		public def(program p){
			super(p);
		}
		private static final long serialVersionUID=1;
	}
	public static class def_func extends def{
		public String return_type,name;
		public List<def_func_arg> args=new ArrayList<>();
		public def_func(String name,String return_type,program p) throws IOException{
			super(p);
			this.name=name;
			this.return_type=return_type;
			if(!p.is_next_char_paranthesis_right()){
				while(true){
					final def_func_arg a=new def_func_arg(p);
					args.add(a);
					if(p.is_next_char_paranthesis_right())
						break;
					if(!p.is_next_char_comma())
						throw new compiler_error(this,"expected ',' after function argument definition");
				}
			}
			final xwriter x=new xwriter().p(return_type).spc().p(name).p("(");
			for(Iterator<def_func_arg> i=args.iterator();i.hasNext();){
				final def_func_arg a=i.next();
				x.p(a.toString());
				if(i.hasNext())
					x.p(",");
			}
			x.p(")");
			txt=x.toString();
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class def_func_arg extends def{
		public String type,name,default_value;
		public boolean is_const;
		public def_func_arg(program p) throws IOException{
			super(p);
			type=p.next_token_in_line();
			if(type.equals("const")){
				is_const=true;
				type=p.next_token_in_line();
			}
			name=p.next_token_in_line();
			if(!p.is_next_char_equals())
				throw new compiler_error(this,"expected function argument format: int a=2,...");
			default_value=p.next_token_in_line();
			final xwriter x=new xwriter();
			if(is_const)
				x.p("const").spc();
			x.p(type).spc().p(name).p("=").p(default_value);
			txt=x.toString();
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	final static public class expr_function_call extends expr{
		public String function_name;
		public List<expr_function_call_arg> args=new ArrayList<>();
		public expr_function_call(final program p,final String function) throws IOException{
			super(p,function);
			function_name=function;
			while(true){
				if(p.is_next_char_paranthesis_right())
					break;
				final expr_function_call_arg a=new expr_function_call_arg(p);
				args.add(a);
				if(p.is_next_char_comma())
					continue;
			}
			final xwriter x=new xwriter();
			x.p(function_name).p("(");
			final Iterator<expr_function_call_arg> i=args.iterator();
			while(true){
				if(!i.hasNext())
					break;
				x.p(i.next().toString());
				if(i.hasNext())
					x.p(",");
			}
			x.p(")");
			txt=x.toString();
			final def_func f=p.functions.get(function_name);
			if(f==null)
				throw new compiler_error(this,"function not found",function_name);
			int ii=0;
			for(expr_function_call_arg e:args){
				final def_func_arg fa=f.args.get(ii++);
				if(!e.arg.equals(fa.name))
					throw new compiler_error(this," argument "+ii+"  expected '"+fa.name+"' but got '"+e.arg+"'\n  "+f);
				final String type_in_register=p.type_for_register(this,e.toString());
				if(!fa.type.equals(type_in_register))
					throw new compiler_error(this," argument "+ii+"  expected type '"+fa.type+"' but var '"+fa.name+"' is of type '"+type_in_register+"'\n  "+f);
			}
		}
		@Override protected void compile(program p){
			bin=new int[]{call.op};
		}
		@Override protected void link(program p){
			final def_func f=p.functions.get(function_name);
			if(f==null)
				throw new compiler_error(this,"function not found",function_name);
			final int a=f.location_in_binary;
			bin[0]|=(a<<6);
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_function_call_arg extends expr{
		public String arg;
		public expr_function_call_arg(final program p) throws IOException{
			super(p,null);
			arg=p.next_token_in_line();
			txt=new xwriter().p(arg).toString();
		}
		@Override protected void compile(program p){}
		@Override protected void link(program p){}
		private static final long serialVersionUID=1;
	}
	final public Map<String,stmt> allocated_registers=new LinkedHashMap<>();
	void allocate_register(stmt e,String regname){
		final stmt s=allocated_registers.get(regname);
		if(s!=null)
			throw new compiler_error(e,"register '"+regname+"' is already allocated at line "+s.location_in_source);
		allocated_registers.put(regname,e);
		return;
	}
	public String type_for_register(stmt s,String name){
		final stmt v=allocated_registers.get(name);
		if(v==null)throw new compiler_error(s,"register not found",name);
		return v.type;
	}

	private static final long serialVersionUID=1;
}