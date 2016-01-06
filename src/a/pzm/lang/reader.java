package a.pzm.lang;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import b.xwriter;

public final class reader{
	final public LinkedHashMap<String,statement> toc=new LinkedHashMap<>();
	private PushbackReader r;
	public int line=1,col=1,prevcol=1,nchar=0;
	public int bm_line,bm_col,bm_nchar;
	public int last_read_char;
	public final class range{
		final public int from_index,to_index,from_line,to_line,from_line_char,to_line_char;
		public range(int from_index,int to_index,int from_line,int to_line,int from_line_char,int to_line_char){
			super();
			this.from_index=from_index;
			this.to_index=to_index;
			this.from_line=from_line;
			this.to_line=to_line;
			this.from_line_char=from_line_char;
			this.to_line_char=to_line_char;
		}
		public range(reader r){
			from_index=r.bm_nchar;
			to_index=r.nchar;
			from_line=r.bm_line;
			from_line_char=r.bm_col;
			to_line=r.line;
			to_line_char=r.col;
		}
	}
	public reader(Reader r){
		this.r=new PushbackReader(r,1);
	}
	public void bm(){
		bm_line=line;
		bm_col=col;
		bm_nchar=nchar;
	}
	public String bm_str(){
		return bm_line+":"+bm_col;
	}
	public boolean is_next_char_block_close(){
		final int ch=read();
		if(ch=='}') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_block_open(){
		final int ch=read();
		if(ch=='{') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_annotation_open(){
		final int ch=read();
		if(ch=='@') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_expression_close(){
		final int ch=read();
		if(ch==')') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_expression_open(){
		final int ch=read();
		if(ch=='(') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_struct_close(){
		final int ch=read();
		if(ch==']') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_struct_open(){
		final int ch=read();
		if(ch=='[') return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_assign(){
		final int ch=read();
		if(ch=='=')return true;
		unread(ch);
		return false;
	}
	public String next_empty_space(){
		final xwriter x=new xwriter();
		while(true){
			final int ch=read();
			if(Character.isWhitespace(ch)){
				x.p((char)ch);
				continue;
			}
			unread(ch);
			break;
		}
		return x.toString();
	}
	public String next_token(){
		final xwriter x=new xwriter();
		while(true){
			final int ch=read();
			if(ch==-1||Character.isWhitespace(ch)||ch=='{'||ch=='}'||ch=='('||ch==')'||ch=='['||ch==']'||ch=='='){
				unread(ch);
				break;
			}
			x.p((char)ch);
		}
		return x.toString();
	}
	private int read(){
		try{
			final int ch=r.read();
			last_read_char=ch;
			nchar++;
			if(ch=='\n'){
				line++;
				prevcol=col;
				col=1;
				return ch;
			}
			col++;
			return ch;
		}catch(IOException e){
			throw new Error(e);
		}
	}
	private void unread(final int ch){
		try{
			nchar--;
			if(ch!=-1) r.unread(ch);
			if(ch=='\n'){
				line--;
				col=prevcol;
				return;
			}
			col--;
			//				if(col==0)throw new Error();//?
		}catch(IOException e){
			throw new Error(e);
		}
	}
	public void unread_last_char(){
		unread(last_read_char);
	}
}