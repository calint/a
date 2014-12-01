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
import a.pz.bas.constexpr;
import a.pz.bas.def_comment;
import a.pz.bas.def_const;
import a.pz.bas.def_func;
import a.pz.bas.def_label;
import a.pz.bas.def_struct;
import a.pz.bas.def_type;
import a.pz.bas.expr_add;
import a.pz.bas.expr_assign;
import a.pz.bas.expr_func_call;
import a.pz.bas.expr_increment;
import a.pz.bas.expr_store;
import a.pz.bas.expr_var;
import b.xwriter;

public final class program extends stmt implements Serializable{
	final public List<stmt> statements=new ArrayList<>();
	final public Map<String,def_const> defines=new LinkedHashMap<>();
	final public Map<String,def_type> typedefs=new LinkedHashMap<>();
	final public Map<String,def_struct> structs=new LinkedHashMap<>();
	final public Map<String,def_label> labels=new LinkedHashMap<>();
	final public Map<String,def_func> functions=new LinkedHashMap<>();
	final public Map<String,data_int> data=new LinkedHashMap<>();

	public program(final String source){
		this(null,new StringReader(source),false);
	}
	public program(final program context_program,final String source){
		this(context_program,new StringReader(source),false);
	}
	public program(final program context_program){
		this(context_program,(Reader)null,true);
	}
	private boolean is_reading_code_block;
	public program(final program context_program,final Reader source,final boolean is_in_code_block){
		super(null);
		is_reading_code_block=is_in_code_block;
		if(context_program!=null){
			typedefs.putAll(context_program.typedefs);
			labels.putAll(context_program.labels);
			defines.putAll(context_program.defines);
			if(source==null)
				pr=context_program.pr;
			line_number=context_program.line_number;
			character_number_in_line=context_program.character_number_in_line;
		}
		if(source!=null)
			pr=new PushbackReader(source,1);
		while(true){
			final stmt s;
			try{
				if(is_reading_code_block&&is_next_char_mustache_right())
					break;
				s=next_statement(true);
				skip_whitespace();
			}catch(IOException e){
				throw new Error(e);
			}
			if(s==null)
				break;
			pl(s.toString());
			statements.add(s);
		}
		statements.forEach(e->e.validate_references_to_labels(this));
		final xwriter x=new xwriter();
		for(final stmt s:statements)
			x.p(s.toString()).nl();
		txt=x.toString();
	}
	public void build(){
		compile(this);
		link(this);
	}
	@Override public void compile(program p){
		//		statements.forEach(e->e.compile(p));
		int pc=0;
		for(final stmt s:statements){
			//			s.link(p);
			s.location_in_binary=pc;
			s.compile(p);
			if(s.bin!=null)
				pc+=s.bin.length;
		}
		program_length=pc;
		bin=new int[program_length];
		//		int i=0;
		//		for(final stmt s:statements){
		//			if(s.bin==null)
		//				continue;
		//			System.arraycopy(s.bin,0,bin,i,s.bin.length);
		//			i+=s.bin.length;
		//		}
	}
	@Override public void link(program p){
		//		statements.forEach(e->e.link(p));
		//		bin=new int[program_length];
		int i=0;
		for(final stmt s:statements){
			s.link(p);
			if(s.bin==null)
				continue;
			System.arraycopy(s.bin,0,bin,i,s.bin.length);
			i+=s.bin.length;
		}
	}
	public int program_length;
	public stmt next_statement(boolean allow_instr) throws IOException{
		String tk="";
		while(true){
			skip_whitespace();
			pl(" line "+location_in_source());
			//			if(is_reading_code_block&&is_next_char_mustache_right())
			//				return null;
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
				throw new Error();
			if(tk.equals("const")){
				final def_const s=new def_const(this);
				defines.put(s.name(),s);
				return s;
			}
			if(tk.equals("typedef")){
				final def_type s=new def_type(this);
				typedefs.put(s.name(),s);
				return s;
			}
			if(tk.equals("struct")){
				final def_struct s=new def_struct(this);
				structs.put(s.name(),s);
				return s;
			}
			if(tk.equals("var")){
				final expr_var s=new expr_var(this);
				return s;
			}
			if(tk.startsWith(":")){
				final def_label s=new def_label(this,tk.substring(1));
				consume_rest_of_line();//? savecomment
				labels.put(s.name(),s);
				return s;
			}
			final def_type td=typedefs.get(tk);
			if(td!=null){// int a=2   int main(int a)
				final String name=next_token_in_line();
				if(is_next_char_paranthesis_left()){//  int main(int a)
					final def_func df=new def_func(name,td.name(),this);
					functions.put(name,df);
					return df;
				}
				final data_int s=new data_int(name,td.name(),this);
				data.put(s.name,s);
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
			final expr_assign s=new expr_assign(this,tk);
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
			final expr_func_call sc=new expr_func_call(this,tk);
			return sc;
		default:
			unread(nxtch);
		}
		
		if(is_reference_to_register(tk)){
			return new reg_ref(tk,this);
		}
		
		if(!allow_instr){
			final constexpr ce=constexpr.from(this,tk);
			return ce;
		}
		// machine code instruction
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
			s=(instr)Class.forName(getClass().getPackage().getName()+"."+tk).getConstructor(program.class).newInstance(this);
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
		while(true){//nxt ret
			final String t=next_token_in_line();
			if(t==null)
				break;
			if("nxt".equals(t)){
				s.znxr|=4;
				continue;
			}
			if("ret".equals(t)){
				s.znxr|=8;
				continue;
			}
			if(t.startsWith("//")){
				consume_rest_of_line();//? savecomment
				break;
			}
			throw new Error("3 "+t);
		}
		s.mkstr();
		return s;
	}
	public boolean is_register_allocated(String register){
		return allocated_registers.containsKey(register);
	}
	public boolean is_next_char_plus() throws IOException{
		final int ch=read();
		if(ch=='+')
			return true;
		unread(ch);
		return false;
	}
	//	boolean is_next_char_bracket_left() throws IOException{
	//		final int ch=read();
	//		if(ch=='[')
	//			return true;
	//		unread(ch);
	//		return false;
	//	}
	boolean is_next_char_paranthesis_left() throws IOException{
		final int ch=read();
		if(ch=='(')
			return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_paranthesis_right() throws IOException{
		final int ch=read();
		if(ch==')')
			return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_comma() throws IOException{
		final int ch=read();
		if(ch==',')
			return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_star() throws IOException{
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
	public boolean is_next_char_equals() throws IOException{
		final int ch=read();
		if(ch=='=')
			return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_end_of_file() throws IOException{
		final int ch=read();
		if(ch==-1||ch==65536)
			return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_mustache_left() throws IOException{
		final int ch=read();
		if(ch=='{')
			return true;
		unread(ch);
		return false;
	}
	boolean is_next_char_mustache_right() throws IOException{
		final int ch=read();
		if(ch=='}')
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
	public String location_in_source(){
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
	public String next_token_in_line() throws IOException{
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
	public void skip_whitespace_on_same_line() throws IOException{
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
	public int line_number=1;
	public int character_number_in_line;
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
	public String next_identifier() throws IOException{
		final String id=next_token_in_line();
		if(id==null)
			throw new compiler_error(location_in_source(),"expected identifier but got end of line");
		if(id.length()==0)
			throw new compiler_error(location_in_source(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))
			throw new compiler_error(location_in_source(),"identifier '"+id+"' starts with a number");
		return id;
	}
	public String next_type_identifier() throws IOException{
		final String id=next_token_in_line();
		if(id==null)
			throw new compiler_error(location_in_source(),"expected type identifier but got end of line");
		if(id.length()==0)
			throw new compiler_error(location_in_source(),"type identifier is empty");
		//is_valid_type_identifier
		if(Character.isDigit(id.charAt(0)))
			throw new compiler_error(location_in_source(),"type identifier '"+id+"' starts with a number");
		return id;
	}
	public boolean is_next_char_end_of_line() throws IOException{
		final int ch=read();
		if(ch=='\n'||ch==-1)
			return true;
		unread(ch);
		return false;
	}
	//	int next_register_identifier() throws IOException{
	//		final String s=next_token_in_line();
	//		if(s==null)
	//			throw new stmt.compiler_error(location_in_source(),"expected register but found end of line");
	//		if(s.length()!=1)
	//			throw new stmt.compiler_error(location_in_source(),"register name unknown '"+s+"'");
	//		final char first_char=s.charAt(0);
	//		final int reg=first_char-'a';
	//		final int max=(1<<4)-1;//? magicnumber
	//		final int min=0;
	//		if(reg>max||reg<min)
	//			throw new stmt.compiler_error(location_in_source(),"register '"+s+"' out range 'a' through 'p'");
	//		return reg;
	//	}
	//	int next_int(int bit_width) throws IOException{
	//		final String s=next_token_in_line();
	//		if(s==null)
	//			throw new stmt.compiler_error(location_in_source(),"expected number but found end of line");
	//		try{
	//			final int i=Integer.parseInt(s);
	//			final int max=(1<<(bit_width-1))-1;
	//			final int min=-1<<(bit_width-1);
	//			if(i>max)
	//				throw new stmt.compiler_error(location_in_source(),"number '"+s+"' out of "+bit_width+" bits range");
	//			if(i<min)
	//				throw new stmt.compiler_error(location_in_source(),"number '"+s+"' out of "+bit_width+" bits range");
	//			return i;
	//		}catch(NumberFormatException e){
	//			throw new stmt.compiler_error(location_in_source(),"can not translate number '"+s+"'");
	//		}
	//	}
	//	private void assert_and_consume_end_of_line()throws IOException{
	//		final int eos=read();
	//		if(eos!='\n'&&eos!=-1)throw new program.compiler_error(hrs_location(),"expected end of line or end of file");
	//	}
	public String consume_rest_of_line() throws IOException{
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
	//	public String toString(){
	//		final xwriter x=new xwriter();
	//		disassemble_to(x);
	//		return x.toString();
	//	}
	int register_index_from_string(String register){
		if(register.length()!=1)
			throw new compiler_error(location_in_source(),"not a register: "+register);
		final int i=register.charAt(0)-'a';
		final int nregs=16;//? magicnumber
		if(i<0||i>=nregs)
			throw new compiler_error(location_in_source(),"register not found: "+register);
		return i;
	}
	public static boolean is_reference_to_register(String ref){
		if(ref.length()!=1)
			return false;
		final char ch=ref.charAt(0);
		return ch>='a'&&ch<='p';
	}

	final public Map<String,stmt> allocated_registers=new LinkedHashMap<>();
	public void allocate_register(stmt e,String regname){
		final stmt s=allocated_registers.get(regname);
		if(s!=null)
			throw new compiler_error(e,"register '"+regname+"' is already allocated at line "+s.location_in_source);
		allocated_registers.put(regname,e);
		return;
	}
	String type_for_register(stmt s,String name){
		final stmt v=allocated_registers.get(name);
		if(v==null)
			throw new compiler_error(s,"register not found",name);
		return v.type;
	}

	private static final long serialVersionUID=1;

	public boolean is_binary_equal(program p){
		for(int i=0;i<p.bin.length;i++){
			if(bin[i]!=p.bin[i])
				return false;
		}
		return true;
	}
	
	final static class reg_ref extends stmt{
		public reg_ref(String tk,program p){
			super(p,tk);
		}
		private static final long serialVersionUID=1;
	}
}