package a.pz;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import b.xwriter;

public final class prog extends Reader implements AutoCloseable{
public static class add extends prog.stmt{
		public add(prog r)throws IOException{
			super(r,prog.opadd,r.next_register_identifier(),r.next_register_identifier());
		}
		@Override public void source_to(xwriter x){
			x.p("add").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a'));
		}
		private static final long serialVersionUID=1;
	}
	final static public class define_const extends stmt{
	public String name,type,value;
	public define_const(final prog r)throws IOException{
		super(r);
		name=r.next_token_in_line();
		final define_const d=r.defines.get(name);
		if(d!=null)throw new compiler_error(this,"define '"+name+"' already declared at "+d.location_in_source);
		type=r.next_token_in_line();
		final define_typedef t=r.typedefs.get(type);
		if(t==null)throw new compiler_error(this,"type not found",type);
		value=r.next_token_in_line();
		txt="const "+name+" "+type+" "+value;
	}
	@Override protected void compile(prog p){}
	private static final long serialVersionUID=1;
}
final static public class define_var extends stmt{
	public List<String>vars;
	public define_var(final prog r)throws IOException{
		super(r);
		vars=new ArrayList<>();
		while(true){
			final String t=r.next_token_in_line();
			if(t==null)break;
			vars.add(t);
		}
		if(vars.isEmpty())throw new compiler_error(this,"expected list of registers after 'var' statement");
		final xwriter x=new xwriter().p("var").spc();
		vars.forEach(e->x.spc().p(e));
		txt=x.toString();
	}
	@Override protected void compile(prog p){}
	private static final long serialVersionUID=1;
}
final static public class define_struct extends stmt{
	public String name;
	public List<define_struct_member>fields;
	public define_struct(final prog r)throws IOException{
		super(r);
		name=r.next_identifier();
		fields=new ArrayList<>();
		while(true){
			r.skip_whitespace_on_same_line();
			if(r.is_at_end_of_line())
				break;
			final define_struct_member f=new define_struct_member(r);
			fields.add(f);
		}
		final xwriter x=new xwriter();
		x.p("struct").spc().p(name);
		fields.forEach(f->x.spc().p(f.toString()));
		txt=x.toString();
	}
	@Override public void validate_references_to_labels(prog p){fields.forEach(e->e.validate_references_to_labels(p));}
	@Override protected void compile(prog p){}
	private static final long serialVersionUID=1;
}
public static class define_struct_member extends stmt{
	public String type;
	public String name;
	public String default_value;
	public define_struct_member(prog r)throws IOException{
		super(r);
		name=r.next_identifier();
		type=r.next_type_identifier();
		default_value=r.next_token_in_line();
		final xwriter x=new xwriter();
		x.p(name).spc().p(type).spc().p(default_value);
		txt=x.toString();
	}
	@Override public void validate_references_to_labels(prog p)throws compiler_error{
		if(!p.typedefs.containsKey(type))throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
	}
	@Override protected void compile(prog p){}
	private static final long serialVersionUID=1;
}
final static public class define_typedef extends stmt{
	public String name;
	public define_typedef(final prog r)throws IOException{
		super(r);
		name=r.next_identifier();
		txt=new xwriter().p("typedef").spc().p(name).toString();
	}
	@Override protected void compile(prog p){}
	private static final long serialVersionUID=1;
}
public static class stmt implements Serializable{
	public String location_in_source;
	public String txt;
	public int[]bin;
	public int location_in_binary;
	public int znxr;
	/**opcode*/public int opcode;
	public int rai;
	public int rdi;
	public stmt(final prog r){location_in_source=r.hrs_location();}
	protected stmt(final prog r,final int op,final int ra,final int rd){
		this(r);
		this.opcode=op;
		this.rai=ra;
		this.rdi=rd;
	}
	protected stmt(final prog r,final int op,final int ra,final int rd,final boolean flip_ra_rd){
		this(r,op,rd,ra);
	}
	protected void validate_references_to_labels(prog r){}
	protected void compile(prog r){bin=new int[]{znxr_ci__ra__rd__()};}
	protected void link(prog p){}
	protected int znxr_ci__ra__rd__(){return znxr|opcode|((rai&15)<<8)|((rdi&15)<<12);}
	final@Override public String toString(){
		if(txt!=null)return txt;
		final xwriter x=new xwriter();
		if((znxr&3)==3)x.p("ifp ");
		else if((znxr&1)==1)x.p("ifz ");
		else if((znxr&2)==2)x.p("ifn ");
		source_to(x);
		x.spc();
		if((znxr&4)==4)x.p(" nxt");
		if((znxr&8)==8)x.p(" ret");
		return x.toString();
	}
	public void source_to(xwriter x){}
	private static final long serialVersionUID=1;
}
public static class li extends stmt{
		public String data;
		public int value;
		public li(prog r)throws IOException{
			super(r,prog.opli,0,r.next_register_identifier());
			data=r.next_token_in_line();
			txt="li "+(char)(rdi+'a')+" "+data;
		}
		public boolean is_integer(){try{Integer.parseInt(data);return true;}catch(Throwable t){return false;}}
		@Override protected void compile(prog p){
			bin=new int[]{znxr_ci__ra__rd__(),0};
		}
		@Override protected void link(prog p){
			final define_const def=p.defines.get(data);
			if(def!=null){
				data=def.value;
			}
			if(data.startsWith("&")){
				final String nm=data.substring(1);
				final define_label l=p.labels.get(nm);
				if(l==null)throw new compiler_error(this,"label not found",nm);
				value=l.location_in_binary;
			}else{
				try{value=Integer.parseInt(data,16);}catch(NumberFormatException e){
					throw new compiler_error(this,"cannot parse number '"+data+"'");
				}
			}
			final int bit_width=16;
//			final int i=Integer.parseInt(data,16);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(value>max)throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			if(value<min)throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			bin=new int[]{bin[0],value};
		}
		private static final long serialVersionUID=1;
	}
public static class inc extends stmt{
	public inc(prog r)throws IOException{
		super(r,prog.opinc,0,r.next_register_identifier());
	}
	@Override public void source_to(xwriter x){
		x.p("inc").spc().p((char)(rdi+'a'));
	}
	private static final long serialVersionUID=1;
}
public static class st extends stmt{
	public st(prog r)throws IOException{
		super(r,prog.opst,r.next_register_identifier(),r.next_register_identifier());
	}
	@Override public void source_to(xwriter x){
		x.p("st").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a'));
	}
	private static final long serialVersionUID=1;
}
public static class eof extends stmt{
	public eof(prog r)throws IOException{
		super(r);
		txt="..";
	}
	@Override protected void compile(prog p){bin=new int[]{-1};}
	private static final long serialVersionUID=1;
}
public static class nxt extends stmt{
	public nxt(prog r)throws IOException{
		super(r,prog.opnxt,0,0);
	}
	private static final long serialVersionUID=1;
}
public static class ret extends stmt{
	public ret(prog r)throws IOException{
		super(r,prog.opret,0,0);
		txt="ret";
	}
	private static final long serialVersionUID=1;
}
public static class lp extends stmt{
	public lp(prog r)throws IOException{
		super(r,prog.oplp,0,r.next_register_identifier());
	}
	@Override public void source_to(xwriter x){
		x.p("lp").spc().p((char)(rdi+'a'));
	}
	private static final long serialVersionUID=1;
}
public static class stc extends stmt{
	public stc(prog r)throws IOException{
		super(r,prog.opstc,r.next_register_identifier(),r.next_register_identifier());
	}
	@Override public void source_to(xwriter x){
		x.p("stc").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a'));
	}
	private static final long serialVersionUID=1;
}
public static class sub extends stmt{
	public sub(prog r)throws IOException{
		super(r,prog.opsub,r.next_register_identifier(),r.next_register_identifier());
	}
	private static final long serialVersionUID=1;
}
public static class call extends stmt{
	String label;
	public call(prog r)throws IOException{
		super(r,prog.opcall,0,0);
		label=r.next_token_in_line();
		txt="call "+label;
	}
	@Override protected void link(prog p){
		define_label l=p.labels.get(label);
		if(l==null)throw new compiler_error(this,"label not found",label);
		final int a=l.location_in_binary;
		bin[0]|=(a<<6);
	}
	private static final long serialVersionUID=1;
}
public static class define_data extends stmt{
		public List<String>data;
		public define_data(prog r)throws IOException{
			super(r);
			data=new ArrayList<>();
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)break;
				data.add(t);
			}
			final xwriter x=new xwriter().p(". ");
			data.forEach(e->x.spc().p(e));
			txt=x.toString();
		}
		@Override protected void compile(prog p){
//			super.generate_code_pass_1(p);
			bin=new int[data.size()];
			int i=0;
			for(final String s:data){
				bin[i++]=Integer.parseInt(s,16);
			}
		}
		private static final long serialVersionUID=1;
	}
public static class define_label extends stmt{
	public String name;
	public define_label(prog r,String nm){
		super(r);
		name=nm;
		final define_label d=r.labels.get(name);
		if(d!=null)throw new compiler_error(this,"label '"+name+"' already declared at "+d.location_in_source);
		txt=":"+nm;
	}
	@Override protected void compile(prog p){}
	private static final long serialVersionUID=1;
}
public static class ld extends stmt{
	public ld(prog r)throws IOException{
		super(r,prog.opld,r.next_register_identifier(),r.next_register_identifier(),true);
	}
	private static final long serialVersionUID=1;
}
public static class ldc extends stmt{
	public ldc(prog r)throws IOException{
		super(r,prog.opldc,r.next_register_identifier(),r.next_register_identifier(),true);
	}
	@Override public void source_to(xwriter x){
		x.p("ldc").spc().p((char)(rdi+'a')).spc().p((char)(rai+'a'));
	}
	private static final long serialVersionUID=1;
}
public static class tx extends stmt{
	public tx(prog r)throws IOException{
		super(r,prog.optx,r.next_register_identifier(),r.next_register_identifier(),true);
	}
	private static final long serialVersionUID=1;
}
public static class shf extends stmt{
	public shf(prog r)throws IOException{
		super(r,prog.opshf,r.next_register_identifier(),r.next_int(4),true);
	}
	private static final long serialVersionUID=1;
}
public static class define_data_int extends define_label{
	public String default_value;
	public define_data_int(prog r)throws IOException{
		super(r,r.next_identifier());
		default_value=r.next_token_in_line();
		if(default_value==null)default_value="0";
		txt="int "+name+" "+default_value;
	}
	@Override protected void compile(prog p){
		final int d=Integer.parseInt(default_value,16);
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
	@Override public String toString(){return new xwriter().p("line ").p(source_location).spc().p(message).toString();}
	private static final long serialVersionUID=1;
}
	//	public program p;
	public List<prog.stmt>s;
	final public Map<String,prog.define_const>defines=new LinkedHashMap<>();
	final public Map<String,prog.define_typedef>typedefs=new LinkedHashMap<>();
	final public Map<String,prog.define_struct>structs=new LinkedHashMap<>();
	final public Map<String,prog.define_label>labels=new LinkedHashMap<>();

	public prog(final String source)throws IOException{
		this(new StringReader(source));
	}
	public prog(final Reader source)throws IOException{
		this.source=new PushbackReader(source,1);
//		this.p=p;
		s=new ArrayList<>();
		while(true){
			final int ch=read();
			if(ch==-1)break;
			unread(ch);
			final prog.stmt st=read_next_statement_from(this);
			s.add(st);
		}
		s.forEach(e->e.validate_references_to_labels(this));
		s.forEach(e->e.compile(this));
		int pc=0;
		for(final prog.stmt ss:s){
			ss.location_in_binary=pc;
			if(ss.bin!=null)pc+=ss.bin.length;
		}
		s.forEach(e->e.link(this));
	}
	private prog.stmt read_next_statement_from(final prog r)throws IOException,prog.compiler_error{
		String tk="";
		while(true){
			r.skip_whitespace();
			tk=r.next_token_in_line();
			if(tk==null)return new prog.eof(r);
			if(tk.equals("const")){
				final prog.define_const s=new prog.define_const(r);
				defines.put(s.name,s);
				return s;
			}
			if(tk.equals("typedef")){
				final prog.define_typedef s=new prog.define_typedef(r);
				typedefs.put(s.name,s);
				return s;
			}
			if(tk.equals("struct")){
				final prog.define_struct s=new prog.define_struct(r);
				structs.put(s.name,s);
				return s;
			}
			if(tk.equals("var")){
				final prog.define_var s=new prog.define_var(r);
//				structs.put(s.name,s);
				return s;
			}
			if(tk.startsWith(":")){
				final prog.define_label s=new prog.define_label(r,tk.substring(1));
				labels.put(s.name,s);
				r.consume_line();
				return s;
			}
			final prog.define_typedef td=typedefs.get(tk);
			if(td!=null){
				final prog.define_data_int s=new prog.define_data_int(r);
				labels.put(s.name,s);
				return s;
			}
			if(tk.equals(".")){
				final prog.define_data s=new prog.define_data(r);
				r.consume_line();
				return s;
			}
			if(tk.equals("..")){
				final prog.eof s=new prog.eof(r);
				r.consume_line();
				return s;
			}
			if(tk.startsWith("//")){
				r.consume_line();
				continue;
			}
			break;
		}
		int znxr=0;
		switch(tk){
		case"ifz":{znxr=1;tk=r.next_token_in_line();break;}
		case"ifn":{znxr=2;tk=r.next_token_in_line();break;}
		case"ifp":{znxr=3;tk=r.next_token_in_line();break;}
		}
		final prog.stmt s;
		try{
			s=(prog.stmt)Class.forName(getClass().getName()+"$"+tk).getConstructor(prog.class).newInstance(r);
		}catch(InvocationTargetException t){
			if(t.getCause()instanceof prog.compiler_error)throw(prog.compiler_error)t.getCause();
			throw new prog.compiler_error(r.hrs_location(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new prog.compiler_error(r.hrs_location(),t.toString());
		}catch(ClassNotFoundException t){
			throw new prog.compiler_error(r.hrs_location(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new prog.compiler_error(r.hrs_location(),t.toString());
		}
		if(!(s instanceof prog.define_data)){
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)break;
				if("nxt".equalsIgnoreCase(t)){znxr|=4;continue;}
				if("ret".equalsIgnoreCase(t)){znxr|=8;continue;}
				if(t.startsWith("//")){r.consume_line();break;}
				throw new Error("3 "+t);
			}
			s.znxr=znxr;
		}
		r.consume_line();
		return s;
	}
	protected void disassemble_to(xwriter x){
		s.forEach(e->x.pl(e.toString()));
	}
//	public source_reader(final Reader source,final int lineno,final int charno){
//		this.source=new PushbackReader(source,1);
//		this.line_number=lineno;
//		this.character_number_in_line=charno;
//	}
//	@Override public String toString(){
//		return hr_location_string_from_line_and_col(line_number,character_number_in_line);
//	}
	/**writes binary*/
	final public void zap(int[]rom){//? arraycopybinary
		for(int i=0;i<rom.length;i++)rom[i]=-1;
		int pc=0;
		for(final prog.stmt ss:s){
			if(ss.bin==null)continue;
			final int c=ss.bin.length;
			System.arraycopy(ss.bin,0,rom,pc,c);
			pc+=c;
		}
	}	
	protected String hrs_location(){
		return hr_location_string_from_line_and_col(line_number,character_number_in_line);
	}
	static String hr_location_string_from_line_and_col(final int ln,final int col) {
		return ln+":"+col;
	}
	@Override public int read()throws IOException{
		final int ch=source.read();
		character_number_in_line++;
		if(ch==newline){line_number++;character_number_in_line=0;}
		return ch;
	}
	@Override public int read(final char[]cbuf,int off,int len)throws IOException{
		final int i=source.read(cbuf,off,len);
		while(len-->0){
			final int ch=cbuf[off++];
			character_number_in_line++;
			if(ch==newline){line_number++;character_number_in_line=0;}
		}
		return i;
	}
	@Override public void close()throws IOException{}
	final public void unread(int c)throws IOException{
		source.unread(c);
		character_number_in_line--;
		if(character_number_in_line<0){
			line_number--;
			character_number_in_line=0;
			if(line_number==0)throw new Error();
		}
	}
	protected final String next_token_in_line()throws IOException{
		skip_whitespace_on_same_line();
		final StringBuilder sb=new StringBuilder();
		while(true){
			final int ch=read();
			if(ch==-1)break;
			if(ch=='\n'){unread(ch);break;}
			if(Character.isWhitespace(ch))break;
			sb.append((char)ch);
		}
		skip_whitespace_on_same_line();
		if(sb.length()==0)return null;
		return sb.toString();
	}
	protected final void skip_whitespace_on_same_line()throws IOException{
		while(true){
			final int ch=read();
			if(ch==-1)return;
			if(ch=='\n'){unread(ch);return;}
			if(Character.isWhitespace(ch))continue;
			unread(ch);
			return;
		}
	}
	
	
	private PushbackReader source;
	private final static int newline='\n';
	public int line_number=1,character_number_in_line;
	final public static int     opli=0x0000;
	final public static int     oplp=0x0100;
	final public static int    opinc=0x0200;
	final public static int    opneg=0x0300;
	final public static int     optx=0x00e0;
	final public static int    opldc=0x00c0;
	final public static int    opadd=0x00a0;
	final public static int    opskp=0x0080;
	final public static int    opshf=0x0060;
	final public static int    opstc=0x0040;
	final public static int    opsub=0x0020;
	final public static int   opcall=0x0010;
	final public static int     opst=0x00d8;
	final public static int     opld=0x00f8;
	final public static int    opnxt=0x0004;
	final public static int    opret=0x0008;
	final public static int    opdac=0x0400;
	final public static int   opwait=0x0058;
	final public static int opnotify=0x0078;
	protected void skip_whitespace()throws IOException{
		while(true){
			final int ch=read();
			if(Character.isWhitespace(ch))continue;
			if(ch==-1)return;
			unread(ch);
			return;
		}
	}
	protected String next_identifier()throws IOException{
		final String id=next_token_in_line();
		if(id==null)throw new prog.compiler_error(hrs_location(),"expected identifier but got end of line");
		if(id.length()==0)throw new prog.compiler_error(hrs_location(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))	throw new prog.compiler_error(hrs_location(),"identifier '"+id+"' starts with a number");
		return id;
	}
	protected String next_type_identifier()throws IOException{
		final String id=next_token_in_line();
		if(id==null)throw new prog.compiler_error(hrs_location(),"expected type identifier but got end of line");
		if(id.length()==0)throw new prog.compiler_error(hrs_location(),"type identifier is empty");
		//is_valid_type_identifier
		if(Character.isDigit(id.charAt(0)))	throw new prog.compiler_error(hrs_location(),"type identifier '"+id+"' starts with a number");
		return id;
	}
	protected boolean is_at_end_of_line()throws IOException{
		final int ch=read();
		unread(ch);
		if(ch=='\n')return true;
		return false;
	}
	protected int next_register_identifier()throws IOException{
		final String s=next_token_in_line();
		if(s==null)throw new prog.compiler_error(hrs_location(),"expected register but found end of line");
		if(s.length()!=1)throw new prog.compiler_error(hrs_location(),"register name unknown '"+s+"'");
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		final int max=(1<<4)-1;//? magicnumber
		final int min=0;
		if(reg>max||reg<min)throw new prog.compiler_error(hrs_location(),"register '"+s+"' out range 'a' through 'p'");
		return reg;
	}
	protected int next_int(int bit_width)throws IOException{
		final String s=next_token_in_line();
		if(s==null)throw new prog.compiler_error(hrs_location(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)throw new prog.compiler_error(hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)throw new prog.compiler_error(hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){throw new prog.compiler_error(hrs_location(),"can not translate number '"+s+"'");}
	}
	protected void assert_and_consume_end_of_line()throws IOException{
		final int eos=read();
		if(eos!='\n'&&eos!=-1)throw new prog.compiler_error(hrs_location(),"expected end of line or end of file");
	}
	protected void consume_line()throws IOException{
		while(true){
			final int ch=read();
			if(ch==-1)break;
			if(ch=='\n')break;
		}
	}
	public String toString(){
		final xwriter x=new xwriter();
		disassemble_to(x);
		return x.toString();
	}

}