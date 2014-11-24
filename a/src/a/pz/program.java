package a.pz;

import static b.b.pl;
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

public final class program extends stmt implements Serializable{
	final public List<stmt> statements=new ArrayList<>();
	final public Map<String,stmt.def_const> defines=new LinkedHashMap<>();
	final public Map<String,stmt.def_type> typedefs=new LinkedHashMap<>();
	final public Map<String,stmt.def_struct> structs=new LinkedHashMap<>();
	final public Map<String,stmt.def_label> labels=new LinkedHashMap<>();
	final public Map<String,stmt.def_func> functions=new LinkedHashMap<>();
	final public Map<String,stmt.data_int> data=new LinkedHashMap<>();

	public program(final String source){
		this(null,new StringReader(source));
	}
	public program(final program context_program,final String source){
		this(context_program,new StringReader(source));
	}
	public program(final program context_program,final Reader source){
		super(null);
		try{
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
		}catch(IOException e){
			throw new Error(e);
		}
	}
	stmt next_statement() throws IOException{
		String tk="";
		while(true){
			skip_whitespace();
			pl(" line "+location_in_source());
			if(is_next_char_end_of_file())
				return null;
			if(is_next_char_slash())
				if(is_next_char_slash())
					return new stmt.def_comment(this);
				else
					unread('/');
			if(is_next_char_star())//st or stc   *a=d   *a++=d   *(a+++b)=d 
				return new stmt.expr_store(this);
			tk=next_token_in_line();
			if(tk==null)
				return new stmt.eof(this);
			if(tk.equals("const")){
				final stmt.def_const s=new stmt.def_const(this);
				defines.put(s.name,s);
				return s;
			}
			if(tk.equals("typedef")){
				final stmt.def_type s=new stmt.def_type(this);
				typedefs.put(s.name,s);
				return s;
			}
			if(tk.equals("struct")){
				final stmt.def_struct s=new stmt.def_struct(this);
				structs.put(s.name,s);
				return s;
			}
			if(tk.equals("var")){
				final stmt.expr_var s=new stmt.expr_var(this);
				//				structs.put(s.name,s);
				return s;
			}
			if(tk.startsWith(":")){
				final stmt.def_label s=new stmt.def_label(this,tk.substring(1));
				consume_rest_of_line();
				labels.put(s.name,s);
				return s;
			}
			final stmt.def_type td=typedefs.get(tk);
			if(td!=null){// int a=2   int main(int a)
				final String name=next_token_in_line();
				if(is_next_char_paranthesis_left()){//  int main(int a)
					final stmt.def_func df=new stmt.def_func(name,td.name,this);
					functions.put(name,df);
					return df;
				}
				final stmt.data_int s=new stmt.data_int(name,td.name,this);
				data.put(s.name,s);
				return s;
			}
			if(tk.equals(".")){
				final stmt.data s=new stmt.data(this);
				consume_rest_of_line();
				return s;
			}
			if(tk.equals("..")){
				final stmt.eof s=new stmt.eof(this);
				consume_rest_of_line();
				return s;
			}
			break;
		}
		final int nxtch=read();
		switch(nxtch){
		case '='://assignment
			final stmt.expr_assign s=new stmt.expr_assign(this,tk);
			return s;
		case '+'://expression or addstore or inc
			if(is_next_char_plus()){
				final stmt.expr_increment st=new stmt.expr_increment(this,tk);
				return st;
			}else if(is_next_char_equals()){
				final stmt.expr_add st=new stmt.expr_add(this,tk);
				return st;
			}
			throw new Error("expressions not supported yet");
		case '('://function call
			final stmt.expr_func_call sc=new stmt.expr_func_call(this,tk);
			return sc;
		default:
			unread(nxtch);
		}
		int znxr=0;
		switch(tk){
		case "ifz":
			znxr=1;
			tk=next_token_in_line();
			break;
		case "ifn":
			znxr=2;
			tk=next_token_in_line();
			break;
		case "ifp":
			znxr=3;
			tk=next_token_in_line();
			break;
		}
		final instr s;
//		switch(tk){
//		}
		try{
			s=(instr)Class.forName(stmt.class.getName()+"$"+tk).getConstructor(program.class).newInstance(this);
			s.znxr=znxr;
		}catch(InvocationTargetException t){
			if(t.getCause() instanceof stmt.compiler_error)
				throw (stmt.compiler_error)t.getCause();
			throw new stmt.compiler_error(location_in_source(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new stmt.compiler_error(location_in_source(),t.toString());
		}catch(ClassNotFoundException t){
			throw new stmt.compiler_error(location_in_source(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new stmt.compiler_error(location_in_source(),t.toString());
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
	boolean is_register_allocated(String register){
		return allocated_registers.containsKey(register);
	}
	boolean is_next_char_plus() throws IOException{
		final int ch=read();
		if(ch=='+')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_bracket_left() throws IOException{
		final int ch=read();
		if(ch=='[')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_paranthesis_left() throws IOException{
		final int ch=read();
		if(ch=='(')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_paranthesis_right() throws IOException{
		final int ch=read();
		if(ch==')')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_comma() throws IOException{
		final int ch=read();
		if(ch==',')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_star() throws IOException{
		final int ch=read();
		if(ch=='*')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_slash() throws IOException{
		final int ch=read();
		if(ch=='/')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_equals() throws IOException{
		final int ch=read();
		if(ch=='=')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_end_of_file() throws IOException{
		final int ch=read();
		if(ch==-1)
			return true;
		unread(ch);
		return false;
	}
	void disassemble_to(xwriter x){
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
	String location_in_source(){
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
	String next_token_in_line() throws IOException{
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
	void skip_whitespace_on_same_line() throws IOException{
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
	void skip_whitespace() throws IOException{
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
	String next_identifier() throws IOException{
		final String id=next_token_in_line();
		if(id==null)
			throw new stmt.compiler_error(location_in_source(),"expected identifier but got end of line");
		if(id.length()==0)
			throw new stmt.compiler_error(location_in_source(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))
			throw new stmt.compiler_error(location_in_source(),"identifier '"+id+"' starts with a number");
		return id;
	}
	String next_type_identifier() throws IOException{
		final String id=next_token_in_line();
		if(id==null)
			throw new stmt.compiler_error(location_in_source(),"expected type identifier but got end of line");
		if(id.length()==0)
			throw new stmt.compiler_error(location_in_source(),"type identifier is empty");
		//is_valid_type_identifier
		if(Character.isDigit(id.charAt(0)))
			throw new stmt.compiler_error(location_in_source(),"type identifier '"+id+"' starts with a number");
		return id;
	}
	boolean is_next_char_end_of_line() throws IOException{
		final int ch=read();
		if(ch=='\n')
			return true;
		unread(ch);
		return false;
	}
	int next_register_identifier() throws IOException{
		final String s=next_token_in_line();
		if(s==null)
			throw new stmt.compiler_error(location_in_source(),"expected register but found end of line");
		if(s.length()!=1)
			throw new stmt.compiler_error(location_in_source(),"register name unknown '"+s+"'");
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		final int max=(1<<4)-1;//? magicnumber
		final int min=0;
		if(reg>max||reg<min)
			throw new stmt.compiler_error(location_in_source(),"register '"+s+"' out range 'a' through 'p'");
		return reg;
	}
	int next_int(int bit_width) throws IOException{
		final String s=next_token_in_line();
		if(s==null)
			throw new stmt.compiler_error(location_in_source(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)
				throw new stmt.compiler_error(location_in_source(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)
				throw new stmt.compiler_error(location_in_source(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){
			throw new stmt.compiler_error(location_in_source(),"can not translate number '"+s+"'");
		}
	}
	//	private void assert_and_consume_end_of_line()throws IOException{
	//		final int eos=read();
	//		if(eos!='\n'&&eos!=-1)throw new program.compiler_error(hrs_location(),"expected end of line or end of file");
	//	}
	String consume_rest_of_line() throws IOException{
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
			throw new stmt.compiler_error(p.location_in_source(),"not a register: "+register);
		final int i=register.charAt(0)-'a';
		final int nregs=16;//? magicnumber
		if(i<0||i>=nregs)
			throw new stmt.compiler_error(p.location_in_source(),"register not found: "+register);
		return i;
	}
	static boolean is_reference_to_register(String ref){
		if(ref.length()!=1)
			return false;
		final char ch=ref.charAt(0);
		return ch>='a'&&ch<='p';
	}

	final public Map<String,stmt> allocated_registers=new LinkedHashMap<>();
	void allocate_register(stmt e,String regname){
		final stmt s=allocated_registers.get(regname);
		if(s!=null)
			throw new stmt.compiler_error(e,"register '"+regname+"' is already allocated at line "+s.location_in_source);
		allocated_registers.put(regname,e);
		return;
	}
	String type_for_register(stmt s,String name){
		final stmt v=allocated_registers.get(name);
		if(v==null)
			throw new stmt.compiler_error(s,"register not found",name);
		return v.type;
	}

	private static final long serialVersionUID=1;
}