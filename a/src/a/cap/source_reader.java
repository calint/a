package a.cap;

import java.io.IOException;
import java.io.Reader;

final public class source_reader extends Reader{
	public source_reader(final Reader source){
		this.source=source;
	}
	public source_reader(final Reader source,final int lineno,final int charno){
		this.source=source;
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
		return "@("+ln+":"+col+")";
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
	@Override public void close()throws IOException{throw new Error("not supported");}
	private Reader source;
	private final static int newline='\n';
	public int line_number=1,character_number_in_line;
}