package a.pz;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import a.pz.program.compiler_error;

final class source_reader extends Reader{
	public source_reader(final Reader source){
		this.source=new PushbackReader(source,1);
	}
	public source_reader(final Reader source,final int lineno,final int charno){
		this.source=new PushbackReader(source,1);
		this.line_number=lineno;
		this.character_number_in_line=charno;
	}
	@Override public String toString(){
		return hr_location_string_from_line_and_col(line_number,character_number_in_line);
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
		if(id==null)throw new program.compiler_error(hrs_location(),"expected identifier but got end of line");
		if(id.length()==0)throw new program.compiler_error(hrs_location(),"identifier is empty");
		if(Character.isDigit(id.charAt(0)))	throw new program.compiler_error(hrs_location(),"identifier '"+id+"' starts with a number");
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
	public int reg()throws IOException{
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
}