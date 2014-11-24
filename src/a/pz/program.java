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

public final class program implements Serializable{
public static class add extends program.stmt{
		final public static int    op=0x00a0;
		public add(program r)throws IOException{
			super(r,op,r.next_register_identifier(),r.next_register_identifier());
			txt=new xwriter().p("add").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final static public class define_const extends stmt{
	public String name,type,value;
	public define_const(final program r)throws IOException{
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
	@Override protected void compile(program p){}
	private static final long serialVersionUID=1;
}
final static public class define_var extends stmt{
	private List<String>vars;
	public define_var(final program r)throws IOException{
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
	@Override protected void compile(program p){}
	private static final long serialVersionUID=1;
}
final static public class define_assignment extends stmt{
	public String lhs;
	public stmt rhs;
	public String rh;
	private boolean is_reference_to_register(String ref){
		if(ref.length()!=1)return false;
		final char ch=ref.charAt(0);
		return ch>='a'&&ch<='p';
	}
	public define_assignment(final program p,final String lhs)throws IOException{
		super(p);
		this.lhs=lhs;
		rh=p.next_token_in_line();
		txt=new xwriter().p(lhs).p("=").p(rh.toString()).toString();
		final define_const c=p.defines.get(rh);
		if(c!=null){//li
			rhs=c;
			return;
		}
	}
	@Override protected void compile(program p){
		if(rhs instanceof define_const){
			final stmt s=new stmt(p,li.op,0,lhs.charAt(0)-'a');
			s.compile(p);
			bin=new int[]{s.bin[0],Integer.parseInt(((define_const)rhs).value,16)};
			return;
		}
		if(is_reference_to_register(rh)){
			final stmt s=new stmt(p,tx.op,lhs.charAt(0)-'a',rh.charAt(0)-'a');
			s.compile(p);
			bin=s.bin;
			return;
		}
		final stmt s=new stmt(p,li.op,0,lhs.charAt(0)-'a');
		s.compile(p);
		bin=new int[]{s.bin[0],Integer.parseInt(rh,16)};
		return;
	}
	private static final long serialVersionUID=1;
}
final static public class define_struct extends stmt{
	public String name;
	private List<member>fields;
	public define_struct(final program p)throws IOException{
		super(p);
		name=p.next_identifier();
		fields=new ArrayList<>();
		while(true){
			p.skip_whitespace_on_same_line();
			if(p.is_at_end_of_line())
				break;
			final member f=new member(p);
			fields.add(f);
		}
		final xwriter x=new xwriter();
		x.p("struct").spc().p(name);
		fields.forEach(f->x.spc().p(f.toString()));
		txt=x.toString();
	}
	@Override protected void validate_references_to_labels(program p){fields.forEach(e->e.validate_references_to_labels(p));}
	@Override protected void compile(program p){}
	private static final long serialVersionUID=1;
public static class member extends stmt{
	private String type;
	private String name;
	private String default_value;
	public member(program p)throws IOException{
		super(p);
		name=p.next_identifier();
		type=p.next_type_identifier();
		default_value=p.next_token_in_line();
		final xwriter x=new xwriter();
		x.p(name).spc().p(type).spc().p(default_value);
		txt=x.toString();
	}
	@Override protected void validate_references_to_labels(program p)throws compiler_error{
		if(!p.typedefs.containsKey(type))throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
	}
	@Override protected void compile(program p){}
	private static final long serialVersionUID=1;
}

}
final static public class define_typedef extends stmt{
	public String name;
	public define_typedef(final program r)throws IOException{
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
	protected int[]bin;
	protected int location_in_binary;
	protected int znxr;
	protected int op;
	protected int rai;
	protected int rdi;
	public stmt(final program r){location_in_source=r.hrs_location();}
	protected stmt(final program r,final int op,final int ra,final int rd){
		this(r);
		this.op=op;
		this.rai=ra;
		this.rdi=rd;
	}
	protected stmt(final program r,final int op,final int ra,final int rd,final boolean flip_ra_rd){this(r,op,rd,ra);}
	protected void validate_references_to_labels(program r){}
	protected void compile(program r){bin=new int[]{znxr_ci__ra__rd__()};}
	protected void link(program p){}
	protected int znxr_ci__ra__rd__(){return znxr|op|((rai&15)<<8)|((rdi&15)<<12);}
	final@Override public String toString(){
		if(txt!=null)return txt;
		final xwriter x=new xwriter();
		if((znxr&3)==3)x.p("ifp ");
		else if((znxr&1)==1)x.p("ifz ");
		else if((znxr&2)==2)x.p("ifn ");
		x.p(txt);
		x.spc();
		if((znxr&4)==4)x.p(" nxt");
		if((znxr&8)==8)x.p(" ret");
		return x.toString();
	}
	public void source_to(xwriter x){}
	private static final long serialVersionUID=1;
}
public static class li extends stmt{
		private String data;
		private int value;
		final public static int     op=0x0000;
		public li(program r)throws IOException{
			super(r,li.op,0,r.next_register_identifier());
			data=r.next_token_in_line();
			txt="li "+(char)(rdi+'a')+" "+data;
		}
//		private boolean is_integer(){try{Integer.parseInt(data);return true;}catch(Throwable t){return false;}}
		@Override protected void compile(program p){
			bin=new int[]{znxr_ci__ra__rd__(),0};
		}
		@Override protected void link(program p){
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
	final public static int    op=0x0200;
	public inc(program r)throws IOException{
		super(r,op,0,r.next_register_identifier());
		txt=new xwriter().p("inc").spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}
public static class st extends stmt{
	final public static int     op=0x00d8;
	public st(program r)throws IOException{
		super(r,op,r.next_register_identifier(),r.next_register_identifier());
		txt=new xwriter().p("st").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}
public static class eof extends stmt{
	public eof(program r)throws IOException{
		super(r);
		txt="..";
	}
	@Override protected void compile(program p){bin=new int[]{-1};}
	private static final long serialVersionUID=1;
}
public static class nxt extends stmt{
	final public static int    op=0x0004;
	public nxt(program r)throws IOException{
		super(r,nxt.op,0,0);
	}
	private static final long serialVersionUID=1;
}
public static class ret extends stmt{
	final public static int    op=0x0008;
	public ret(program r)throws IOException{
		super(r,ret.op,0,0);
		txt="ret";
	}
	private static final long serialVersionUID=1;
}
public static class lp extends stmt{
	final public static int     op=0x0100;
	public lp(program r)throws IOException{
		super(r,lp.op,0,r.next_register_identifier());
		txt=new xwriter().p("lp").spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}
public static class stc extends stmt{
	final public static int    op=0x0040;
	public stc(program r)throws IOException{
		super(r,op,r.next_register_identifier(),r.next_register_identifier());
		txt=new xwriter().p("stc").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}
public static class sub extends stmt{
	final public static int    op=0x0020;
	public sub(program r)throws IOException{
		super(r,sub.op,r.next_register_identifier(),r.next_register_identifier());
	}
	private static final long serialVersionUID=1;
}
public static class call extends stmt{
	String label;
	final public static int   op=0x0010;
	public call(program r)throws IOException{
		super(r,op,0,0);
		label=r.next_token_in_line();
		txt="call "+label;
	}
	@Override protected void link(program p){
		define_label l=p.labels.get(label);
		if(l==null)throw new compiler_error(this,"label not found",label);
		final int a=l.location_in_binary;
		bin[0]|=(a<<6);
	}
	private static final long serialVersionUID=1;
}
public static class define_data extends stmt{
	private List<String>data;
	public define_data(program r)throws IOException{
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
	@Override protected void compile(program p){
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
	public define_label(program p,String nm){
		super(p);
		name=nm;
		final define_label d=p.labels.get(name);
		if(d!=null)throw new compiler_error(this,"label '"+name+"' already declared at "+d.location_in_source);
		txt=":"+nm;
	}
	@Override protected void compile(program p){}
	private static final long serialVersionUID=1;
}
public static class ld extends stmt{
	final public static int     op=0x00f8;
	public ld(program r)throws IOException{
		super(r,op,r.next_register_identifier(),r.next_register_identifier(),true);
	}
	private static final long serialVersionUID=1;
}
public static class ldc extends stmt{
	final public static int    op=0x00c0;
	public ldc(program r)throws IOException{
		super(r,op,r.next_register_identifier(),r.next_register_identifier(),true);
		txt=new xwriter().p("ldc").spc().p((char)(rdi+'a')).spc().p((char)(rai+'a')).toString();
	}
	private static final long serialVersionUID=1;
}
public static class tx extends stmt{
	final public static int     op=0x00e0;
	public tx(program r)throws IOException{
		super(r,op,r.next_register_identifier(),r.next_register_identifier(),true);
	}
	private static final long serialVersionUID=1;
}
public static class shf extends stmt{
	final public static int    op=0x0060;
	public shf(program r)throws IOException{
		super(r,op,r.next_register_identifier(),r.next_int(4),true);
	}
	private static final long serialVersionUID=1;
}
public static class define_data_int extends define_label{
	private String default_value;
	public define_data_int(program r)throws IOException{
		super(r,r.next_identifier());
		default_value=r.next_token_in_line();
		if(default_value==null)default_value="0";
		txt="int "+name+" "+default_value;
	}
	@Override protected void compile(program p){
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
	final public List<stmt>statements=new ArrayList<>();
	final public Map<String,define_const>defines=new LinkedHashMap<>();
	final public Map<String,define_typedef>typedefs=new LinkedHashMap<>();
	final public Map<String,define_struct>structs=new LinkedHashMap<>();
	final public Map<String,define_label>labels=new LinkedHashMap<>();

	public program(final String source)throws IOException{
		this(new StringReader(source));
	}
	public stmt next_expression(){
		// TODO Auto-generated method stub
		return null;
	}
	public program(final Reader source)throws IOException{
		this.pr=new PushbackReader(source,1);
		while(true){
			final int ch=read();
			if(ch==-1)break;
			unread(ch);
			final stmt st=next_statement();
			statements.add(st);
		}
		statements.forEach(e->e.validate_references_to_labels(this));
		statements.forEach(e->e.compile(this));
		int pc=0;
		for(final stmt ss:statements){
			ss.location_in_binary=pc;
			if(ss.bin!=null)pc+=ss.bin.length;
		}
		statements.forEach(e->e.link(this));
	}
	private stmt next_statement()throws IOException{
		String tk="";
		while(true){
			skip_whitespace();
			tk=next_token_in_line();
			if(tk==null)return new eof(this);
			if(tk.equals("const")){
				final define_const s=new define_const(this);
				defines.put(s.name,s);
				return s;
			}
			if(tk.equals("typedef")){
				final define_typedef s=new define_typedef(this);
				typedefs.put(s.name,s);
				return s;
			}
			if(tk.equals("struct")){
				final define_struct s=new define_struct(this);
				structs.put(s.name,s);
				return s;
			}
			if(tk.equals("var")){
				final define_var s=new define_var(this);
//				structs.put(s.name,s);
				return s;
			}
			if(tk.startsWith(":")){
				final define_label s=new define_label(this,tk.substring(1));
				labels.put(s.name,s);
				consume_rest_of_line();
				return s;
			}
			final define_typedef td=typedefs.get(tk);
			if(td!=null){
				final define_data_int s=new define_data_int(this);
				labels.put(s.name,s);
				return s;
			}
			if(tk.equals(".")){
				final define_data s=new define_data(this);
				consume_rest_of_line();
				return s;
			}
			if(tk.equals("..")){
				final eof s=new eof(this);
				consume_rest_of_line();
				return s;
			}
			if(tk.startsWith("//")){
				consume_rest_of_line();
				continue;
			}
			break;
		}
		final int nxtch=read();
		switch(nxtch){
		case'='://assignment
			final define_assignment s=new define_assignment(this,tk);
			return s;
		case'+'://expression or addstore or inc
			if(next_char_is_plus()){
				final define_increment st=new define_increment(this,tk);
				return st;
			}
			throw new Error("expressions not supported yet");
		default:
			unread(nxtch);
		}
		int znxr=0;
		switch(tk){
		case"ifz":{znxr=1;tk=next_token_in_line();break;}
		case"ifn":{znxr=2;tk=next_token_in_line();break;}
		case"ifp":{znxr=3;tk=next_token_in_line();break;}
		}
		final program.stmt s;
		try{
			s=(program.stmt)Class.forName(getClass().getName()+"$"+tk).getConstructor(program.class).newInstance(this);
		}catch(InvocationTargetException t){
			if(t.getCause()instanceof program.compiler_error)throw(program.compiler_error)t.getCause();
			throw new program.compiler_error(hrs_location(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new program.compiler_error(hrs_location(),t.toString());
		}catch(ClassNotFoundException t){
			throw new program.compiler_error(hrs_location(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new program.compiler_error(hrs_location(),t.toString());
		}
		if(!(s instanceof define_data)){
			while(true){
				final String t=next_token_in_line();
				if(t==null)break;
				if("nxt".equalsIgnoreCase(t)){znxr|=4;continue;}
				if("ret".equalsIgnoreCase(t)){znxr|=8;continue;}
				if(t.startsWith("//")){consume_rest_of_line();break;}
				throw new Error("3 "+t);
			}
			s.znxr=znxr;
		}
		consume_rest_of_line();
		return s;
	}
	private boolean next_char_is_plus()throws IOException{
		final int ch=read();
		if(ch=='+')return true;
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
	/**writes binary*/
	final public void zap(int[]rom){//? arraycopybinary
		for(int i=0;i<rom.length;i++)rom[i]=-1;
		int pc=0;
		for(final program.stmt ss:statements){
			if(ss.bin==null)continue;
			final int c=ss.bin.length;
			System.arraycopy(ss.bin,0,rom,pc,c);
			pc+=c;
		}
	}	
	private String hrs_location(){
		return hr_location_string_from_line_and_col(line_number,character_number_in_line);
	}
	private static String hr_location_string_from_line_and_col(final int ln,final int col) {
		return ln+":"+col;
	}
	private int read()throws IOException{
		final int ch=pr.read();
		character_number_in_line++;
		if(ch==newline){line_number++;character_number_in_line=0;}
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
	private void unread(int c)throws IOException{
		pr.unread(c);
		character_number_in_line--;
		if(character_number_in_line<0){
			line_number--;
			character_number_in_line=0;
			if(line_number==0)throw new Error();
		}
	}
	private final String next_token_in_line()throws IOException{
		skip_whitespace_on_same_line();
		final StringBuilder sb=new StringBuilder();
		while(true){
			final int ch=read();
			if(ch==-1)break;
			if(ch=='\n'){unread(ch);break;}
			if(Character.isWhitespace(ch))break;
			if(ch=='='){unread(ch);break;}
			if(ch=='+'){unread(ch);break;}
			sb.append((char)ch);
		}
		skip_whitespace_on_same_line();
		if(sb.length()==0)return null;
		return sb.toString();
	}
	private final void skip_whitespace_on_same_line()throws IOException{
		while(true){
			final int ch=read();
			if(ch==-1)return;
			if(ch=='\n'){unread(ch);return;}
			if(Character.isWhitespace(ch))continue;
			unread(ch);
			return;
		}
	}
	
	
	private PushbackReader pr;
	private final static int newline='\n';
	private int line_number=1,character_number_in_line;
	final public static int    opneg=0x0300;
	final public static int    opskp=0x0080;
	final public static int    opdac=0x0400;
	final public static int   opwait=0x0058;
	final public static int opnotify=0x0078;
	private void skip_whitespace()throws IOException{
		while(true){
			final int ch=read();
			if(Character.isWhitespace(ch))continue;
			if(ch==-1)return;
			unread(ch);
			return;
		}
	}
	private String next_identifier()throws IOException{
		final String id=next_token_in_line();
		if(id==null)throw new program.compiler_error(hrs_location(),"expected identifier but got end of line");
		if(id.length()==0)throw new program.compiler_error(hrs_location(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))	throw new program.compiler_error(hrs_location(),"identifier '"+id+"' starts with a number");
		return id;
	}
	private String next_type_identifier()throws IOException{
		final String id=next_token_in_line();
		if(id==null)throw new program.compiler_error(hrs_location(),"expected type identifier but got end of line");
		if(id.length()==0)throw new program.compiler_error(hrs_location(),"type identifier is empty");
		//is_valid_type_identifier
		if(Character.isDigit(id.charAt(0)))	throw new program.compiler_error(hrs_location(),"type identifier '"+id+"' starts with a number");
		return id;
	}
	private boolean is_at_end_of_line()throws IOException{
		final int ch=read();
		unread(ch);
		if(ch=='\n')return true;
		return false;
	}
	private int next_register_identifier()throws IOException{
		final String s=next_token_in_line();
		if(s==null)throw new program.compiler_error(hrs_location(),"expected register but found end of line");
		if(s.length()!=1)throw new program.compiler_error(hrs_location(),"register name unknown '"+s+"'");
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		final int max=(1<<4)-1;//? magicnumber
		final int min=0;
		if(reg>max||reg<min)throw new program.compiler_error(hrs_location(),"register '"+s+"' out range 'a' through 'p'");
		return reg;
	}
	private int next_int(int bit_width)throws IOException{
		final String s=next_token_in_line();
		if(s==null)throw new program.compiler_error(hrs_location(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)throw new program.compiler_error(hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)throw new program.compiler_error(hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){throw new program.compiler_error(hrs_location(),"can not translate number '"+s+"'");}
	}
//	private void assert_and_consume_end_of_line()throws IOException{
//		final int eos=read();
//		if(eos!='\n'&&eos!=-1)throw new program.compiler_error(hrs_location(),"expected end of line or end of file");
//	}
	private void consume_rest_of_line()throws IOException{
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

final static public class define_increment extends stmt{
	public String lhs;
	public define_increment(final program p,final String lhs)throws IOException{
		super(p);
		this.lhs=lhs;
		txt=new xwriter().p(lhs).p("++").toString();
	}
	@Override protected void compile(program p){
		final stmt s=new stmt(p,inc.op,0,lhs.charAt(0)-'a');
		s.compile(p);
		bin=s.bin;
	}
	private static final long serialVersionUID=1;
}
	
	private static final long serialVersionUID=1;
}