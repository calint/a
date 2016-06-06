package a.pzm.lang;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import b.a;
import b.xwriter;

public final class reader{
	public reader(Reader r){this.r=new PushbackReader(r,1);}
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
	public boolean is_next_char_expression_open_pushback(){
		final int ch=read();
		unread(ch);
		if(ch=='(')
			return true;
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
	public void set_location_cue(){bmsrcloc.copy_from(srcloc);}
	public String location_cue(){return bmsrcloc.toString();}
	public String location_in_source(){return srcloc.toString();}
//	private final String tokens=" \r\n\t{}()[]=+,";
	public token next_token(){
		final token tk=new token();
		tk.ws_pre=next_empty_space();
		token_start_loc.copy_from(srcloc);
		final xwriter x=new xwriter();
		while(true){
			final int ch=read();
			if(ch==-1||Character.isWhitespace(ch)||
					ch=='{'||ch=='}'||ch=='('||ch==')'||ch=='['||ch==']'||
					ch=='='||ch=='+'||ch==','||ch=='>'||ch=='<'||ch=='!'
			){
				unread(ch);
				break;
			}
			x.p((char)ch);
		}
		tk.name=x.toString();
		token_end_loc.copy_from(srcloc);
		tk.ws_post=next_empty_space();
		return tk;
	}
	private int read(){
		try{
			final int ch=r.read();
			last_read_char=ch;
			srcloc.nchar++;
			if(ch=='\n'){
				srcloc.line++;
				prvsrcloc.col=srcloc.col;
				srcloc.col=1;
				return ch;
			}
			srcloc.col++;
			return ch;
		}catch(IOException e){
			throw new Error(e);
		}
	}
	private void unread(final int ch){
		try{
			srcloc.nchar--;
			if(ch!=-1)r.unread(ch);
			if(ch=='\n'){
				srcloc.line--;
				srcloc.col=prvsrcloc.col;
				return;
			}
			srcloc.col--;
			//				if(col==0)throw new Error();//?
		}catch(IOException e){
			throw new Error(e);
		}
	}
	public void unread_last_char(){
		unread(last_read_char);
	}
	public String toString(){
		return location_in_source();
	}
	public boolean is_next_char_pointer_dereference(){
		final int ch=read();
		if(ch=='*')return true;
		unread(ch);
		return false;
	}
	public boolean is_next_char_plus(){
		final int ch=read();
		if(ch=='+')return true;
		unread(ch);
		return false;
	}
	public List<token>read_annotation(){
		ArrayList<token>annot=null;
		while(true){
			if(!is_next_char_annotation_open())break;
			final token tk=next_token();
			if(annot==null)annot=new ArrayList<>();
			annot.add(tk);
		}
		return annot;
	}
	public boolean is_next_char_comma(){
		final int ch=read();
		if(ch==',')return true;
		unread(ch);
		return false;
	}
	final public LinkedHashMap<String,statement>toc=new LinkedHashMap<>();
	private PushbackReader r;
//	private int line=1,col=1,prevcol=1;

	public static class source_location{
		public int line=1,col=1,nchar;
		public String toString(){return line+":"+col+":"+nchar;}
		public void copy_from(source_location o){line=o.line;col=o.col;nchar=o.nchar;}
	}
	public static final class source_range extends source_location{
		public int ncharto;
		public String toString(){return super.toString()+":"+ncharto;}
	}
	
	private source_location bmsrcloc=new source_location();
	private source_location srcloc=new source_location();
	private source_location prvsrcloc=new source_location();

	private source_location token_start_loc=new source_location();
	source_location token_end_loc=new source_location();

//	private int nchar=0;
//	private int bm_line,bm_col,bm_nchar;
	private int last_read_char;
	
	
	final static public class token extends a{
		public token(){}
		public token(String ws_pre, String name, String ws_after){this.ws_pre=ws_pre;this.name=name;this.ws_post=ws_after;}
		@Override public void to(xwriter x){x.p(ws_pre).p(name).p(ws_post);}
		@Override public String toString(){xwriter x=new xwriter();to(x);return x.toString();}
		public String ws_pre,name,ws_post;
		private static final long serialVersionUID=1;
	}

}