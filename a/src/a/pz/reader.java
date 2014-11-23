package a.pz;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import a.pz.program.compiler_error;
import a.pz.program.define_const;
import a.pz.program.define_data;
import a.pz.program.define_data_int;
import a.pz.program.define_label;
import a.pz.program.define_struct;
import a.pz.program.define_typedef;
import a.pz.program.define_var;
import a.pz.program.eof;
import a.pz.program.stmt;
import b.xwriter;

public final class reader extends Reader implements AutoCloseable{
//	public program p;
	public List<stmt>s;
	final public Map<String,define_const>defines=new LinkedHashMap<>();
	final public Map<String,define_typedef>typedefs=new LinkedHashMap<>();
	final public Map<String,define_struct>structs=new LinkedHashMap<>();
	final public Map<String,define_label>labels=new LinkedHashMap<>();

	public reader(final String source)throws IOException{
		this(new StringReader(source));
	}
	public reader(final Reader source)throws IOException{
		this.source=new PushbackReader(source,1);
//		this.p=p;
		s=new ArrayList<>();
		while(true){
			final int ch=read();
			if(ch==-1)break;
			unread(ch);
			final stmt st=read_next_statement_from(this);
			s.add(st);
		}
		s.forEach(e->e.validate_references_to_labels(this));
		s.forEach(e->e.compile(this));
		int pc=0;
		for(final stmt ss:s){
			ss.location_in_binary=pc;
			if(ss.bin!=null)pc+=ss.bin.length;
		}
		s.forEach(e->e.link(this));
	}
	private stmt read_next_statement_from(final reader r)throws IOException,compiler_error{
		String tk="";
		while(true){
			r.skip_whitespace();
			tk=r.next_token_in_line();
			if(tk==null)return new eof(r);
			if(tk.equals("const")){
				final define_const s=new define_const(r);
				defines.put(s.name,s);
				return s;
			}
			if(tk.equals("typedef")){
				final define_typedef s=new define_typedef(r);
				typedefs.put(s.name,s);
				return s;
			}
			if(tk.equals("struct")){
				final define_struct s=new define_struct(r);
				structs.put(s.name,s);
				return s;
			}
			if(tk.equals("var")){
				final define_var s=new define_var(r);
//				structs.put(s.name,s);
				return s;
			}
			if(tk.startsWith(":")){
				final define_label s=new define_label(r,tk.substring(1));
				labels.put(s.name,s);
				r.consume_line();
				return s;
			}
			final define_typedef td=typedefs.get(tk);
			if(td!=null){
				final define_data_int s=new define_data_int(r);
				labels.put(s.name,s);
				return s;
			}
			if(tk.equals(".")){
				final define_data s=new define_data(r);
				r.consume_line();
				return s;
			}
			if(tk.equals("..")){
				final eof s=new eof(r);
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
		final stmt s;
		try{
			s=(stmt)Class.forName(program.class.getName()+"$"+tk).getConstructor(reader.class).newInstance(r);
		}catch(InvocationTargetException t){
			if(t.getCause()instanceof compiler_error)throw(compiler_error)t.getCause();
			throw new compiler_error(r.hrs_location(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new compiler_error(r.hrs_location(),t.toString());
		}catch(ClassNotFoundException t){
			throw new compiler_error(r.hrs_location(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new compiler_error(r.hrs_location(),t.toString());
		}
		if(!(s instanceof define_data)){
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
	public void disassemble_to(xwriter x){
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
		for(final stmt ss:s){
			if(ss.bin==null)continue;
			final int c=ss.bin.length;
			System.arraycopy(ss.bin,0,rom,pc,c);
			pc+=c;
		}
	}	
	public String hrs_location(){
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
	public final String next_token_in_line()throws IOException{
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
	public final void skip_whitespace_on_same_line()throws IOException{
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
	public void skip_whitespace()throws IOException{
		while(true){
			final int ch=read();
			if(Character.isWhitespace(ch))continue;
			if(ch==-1)return;
			unread(ch);
			return;
		}
	}
	public String next_identifier()throws IOException{
		final String id=next_token_in_line();
		if(id==null)throw new compiler_error(hrs_location(),"expected identifier but got end of line");
		if(id.length()==0)throw new compiler_error(hrs_location(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))	throw new compiler_error(hrs_location(),"identifier '"+id+"' starts with a number");
		return id;
	}
	public String next_type_identifier()throws IOException{
		final String id=next_token_in_line();
		if(id==null)throw new compiler_error(hrs_location(),"expected type identifier but got end of line");
		if(id.length()==0)throw new compiler_error(hrs_location(),"type identifier is empty");
		//is_valid_type_identifier
		if(Character.isDigit(id.charAt(0)))	throw new compiler_error(hrs_location(),"type identifier '"+id+"' starts with a number");
		return id;
	}
	public boolean is_at_end_of_line()throws IOException{
		final int ch=read();
		unread(ch);
		if(ch=='\n')return true;
		return false;
	}
	public int next_register_identifier()throws IOException{
		final String s=next_token_in_line();
		if(s==null)throw new compiler_error(hrs_location(),"expected register but found end of line");
		if(s.length()!=1)throw new compiler_error(hrs_location(),"register name unknown '"+s+"'");
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		final int max=(1<<4)-1;//? magicnumber
		final int min=0;
		if(reg>max||reg<min)throw new compiler_error(hrs_location(),"register '"+s+"' out range 'a' through 'p'");
		return reg;
	}
	public int next_int(int bit_width)throws IOException{
		final String s=next_token_in_line();
		if(s==null)throw new compiler_error(hrs_location(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)throw new compiler_error(hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)throw new compiler_error(hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){throw new compiler_error(hrs_location(),"can not translate number '"+s+"'");}
	}
	public void assert_and_consume_end_of_line()throws IOException{
		final int eos=read();
		if(eos!='\n'&&eos!=-1)throw new compiler_error(hrs_location(),"expected end of line or end of file");
	}
	public void consume_line()throws IOException{
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