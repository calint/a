package a.cap;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import a.cap.vm.add;
import a.cap.vm.block;
import a.cap.vm.brk;
import a.cap.vm.cont;
import a.cap.vm.dec;
import a.cap.vm.decn;
import a.cap.vm.decpre;
import a.cap.vm.eq;
import a.cap.vm.fcall;
import a.cap.vm.ife;
import a.cap.vm.iff;
import a.cap.vm.inc;
import a.cap.vm.incn;
import a.cap.vm.incpre;
import a.cap.vm.inti;
import a.cap.vm.let;
import a.cap.vm.loop;
import a.cap.vm.printf;
import a.cap.vm.ret;
import a.cap.vm.set;
import a.cap.vm.stmt;
import a.cap.vm.str;
import a.cap.vm.type;
import a.cap.vm.value;
import a.cap.vm.var;

final class toc extends Writer{
	final public String state_to_string(){return state_stack.toString()+" "+namespace_stack.toString();}
	final @Override public void write(final char[]cbuf,final int off,final int len)throws IOException{
		int o=off,l=len;
		char lastchar=0;
		while(true){
			if(l==0)break;
			final char ch=cbuf[o];o++;l--;
			if(state!=state_in_line_comment){
				if(lastchar=='/'&&ch=='/'){
					state_push(state_in_line_comment);
				}
			}
			lastchar=ch;
			charno++;if(ch=='\n'){lineno++;charno=1;}
			switch(state){
			case state_in_class_ident:{
				if(is_token_empty()&&is_white_space(ch))break;// trims leading white space
				if(!is_char_block_open(ch)){token_add(ch);break;}// look for opening class block
				final String clsident=token_take_clean();
				if(!is_valid_class_identifier(clsident))throw new Error("line "+lineno+": "+charno+": invalid class name '"+clsident+"'");
				classes.push(new struct(clsident));
				namespace_enter(clsident);
				state_push(state_in_class_block);
				break;
			}
			case state_in_class_block:{// find type
				if(is_token_empty()&&is_white_space(ch))break;// trim leading white space
				if(is_char_arguments_open(ch)){// found type+name function i.e. const int foo•(
					final String ident=token_take_clean();
					namespace_enter(ident);
					classes.peek().slots.push(new struct.slot(ident,true));
					// assume it returns void or self for chained calls					
					state_push(state_in_function_arguments);
					break;
				}
				if(is_char_statement_close(ch)){// found type+name field i.e. int a•;
					final String ident=token_take_clean();
					classes.peek().slots.push(new struct.slot(ident,false));
					break;
				}
				if(is_char_statement_assigment(ch)){// found type+name field=... i.e. int a•=0;
					final String ident=token_take_clean();
					classes.peek().slots.push(new struct.slot(ident,false));
					state_push(state_in_struct_member_default_value);
					break;
				}
				if(is_char_block_close(ch)){// close class block
					token_clear();// ignore class block content
					state_back_to(state_in_class_ident);
					namespace_pop();
					break;
				}
				token_add(ch);
				break;
			}
			case state_in_struct_member_default_value:{// int a=•0•;
				if(is_white_space(ch)&&is_token_empty())continue;//trim lead space
				if(is_char_statement_close(ch)){// int a=0•;
					final String def=token_take_trimmed();
					classes.peek().slots.peek().args=def;			
					state_back_to(state_in_class_block);
					break;
				}
				token_add(ch);
				break;
			}
			case state_in_function_arguments:{// class file{func(•size s,int i•){}}
				if(is_char_arguments_close(ch)){
					state_push(state_find_function_block);
					final String funcargs=token_take_clean();
					classes.peek().slots.peek().args=funcargs;
					break;
				}
				token_add(ch);
				break;
			}
			case state_find_function_block:{// class file{func(size s) •{}
				if(is_white_space(ch)&&is_token_empty())continue;//trim lead space
				if(is_char_block_open(ch)){state_push(state_in_function_block);break;}//found
				token_add(ch);
				break;
			}
			case state_in_function_block:{// class file{func(size s){•int a=2;a+=2;•}
				token_add(ch);
				if(is_white_space(ch)&&is_token_empty())continue;
				if(is_char_block_open(ch)){
					state_push(state_in_code_block);
					break;
				}
				if(is_char_block_close(ch)){
					final struct.slot sl=classes.peek().slots.peek();
					token_dec_len_by_1();//? remove the }
					sl.func_source=token_take();
					try{
						final Reader r=new StringReader(sl.func_source);
						sl.stm=block.parse_function_source(r,namespace_stack);
					}catch(Throwable t){
						t.printStackTrace();
						throw new Error(t);
					}
					state_back_to(state_in_class_block);
					namespace_pop();
					break;
				}
				if(is_char_string_open(ch))
					state_push(state_in_string);
				break;
			}
			case state_in_string:{// string a="•hello world•";
				token_add(ch);
				if(is_char_string_close(ch))
					state_pop();
				break;
			}
			case state_in_code_block:{// while(true){•pl("hello world")};
				token_add(ch);
				if(is_char_block_open(ch)){
					state_push(state_in_code_block);
					break;
				}
				if(is_char_block_close(ch)){
					state_pop();
					break;
				}
				break;
			}
			case state_in_line_comment:{//• comment
				token_add(ch);
				if(is_char_is_newline(ch)){
					token_clear();
					state_pop();
					break;
				}
				break;
			}
			default:throw new Error("unknown state "+state);
			}
		}
		Collections.reverse(classes);
	}
	private void token_add(final char ch) {
		token.append(ch);
	}
	private String token_take() {
		final String s=token.toString();
		token_clear();
		return s;
	}
	private void token_dec_len_by_1() {
		token.setLength(token.length()-1);
	}
	private String token_take_trimmed() {
		final String def=token_take().trim();
		token_clear();
		return def;
	}
	private void token_clear() {
		token.setLength(0);
	}
	private String token_take_clean(){
		final String t=clean_whitespaces(token_take());
		token_clear();
		return t;
	}
	private boolean is_token_empty(){return token.length()==0;}
	final @Override public void flush()throws IOException{}
	final @Override public void close()throws IOException{}
	final void namespace_pop(){namespace_stack.pop();}
	final void namespace_enter(String name){namespace_stack.push(new namespace(name));}

	private static String clean_whitespaces(String s){return s.trim().replaceAll("\\s+"," ").replaceAll(" \\*","\\*").replaceAll("\\* ","\\*");}
	private void state_push(int newstate){state_stack.push(state);state=newstate;}
	private void state_back_to(final int s){while(s!=(state=state_stack.pop()));}
	private void state_pop(){state=state_stack.pop();}
	
	
	private int lineno=1,charno=0;
	private StringBuilder token=new StringBuilder(32);

	final private LinkedList<namespace>namespace_stack=new LinkedList<>();
	final private LinkedList<Integer>state_stack=new LinkedList<>();
	final private LinkedList<struct>classes=new LinkedList<>();
	final public List<struct>classes(){return classes;}

	private int state;
	private static final int state_in_class_ident=0;
	private static final int state_in_class_block=1;
	private static final int state_in_function_arguments=2;
	private static final int state_find_function_block=3;
	private static final int state_in_function_block=4;
	private static final int state_in_struct_member_default_value=5;
	private static final int state_in_string=6;
	private static final int state_in_code_block=7;
	private static final int state_in_line_comment=8;
	
	private static boolean is_char_block_open(char ch){return ch=='{';}
	private static boolean is_char_arguments_open(char ch){return ch=='(';}
	private static boolean is_char_arguments_close(char ch){return ch==')';}
	private static boolean is_char_block_close(char ch){return ch=='}';}
	private static boolean is_char_statement_close(char ch){return ch==';';}
	private static boolean is_white_space(char ch){return Character.isWhitespace(ch);}
	private static boolean is_valid_class_identifier(String nm){return true;}
	private static boolean is_char_is_newline(char ch){return ch=='\n';}
	private static boolean is_char_string_close(char ch) {return ch=='\"';}
	private static boolean is_char_string_open(char ch){return ch=='\"';}
	private static boolean is_char_statement_assigment(char ch){return ch=='=';}
	
	final static class namespace{
		private String name;
		public namespace(String nm){name=nm;}
		@Override public String toString(){return name;}
	}
	final static class struct{
		String name;
		LinkedList<slot>slots=new LinkedList<>();
		public struct(String name){this.name=name;}//autoset
		@Override public String toString(){return name+"{"+slots+"}";}
		final static class slot{
			String tn;// string from source  i.e. 'int i' 'string s'
			String name;// decoded from tn  i.e.  'i'     's'
			String type;//                        'int'   'string'
			String args="";//when field this is default value
			String func_source="";//function source
			stmt stm;
			boolean isfunc;
			boolean isctor;
			boolean ispointer;
			public slot(String type_and_name,boolean func){tn=type_and_name;isfunc=func;decode_tn();}
			@Override public String toString(){return tn+(isfunc?("("+args+")"):"");}
			private void decode_tn(){
				//  get func name from i.e. 'const int*func'   'const int func'
				final int i1=tn.lastIndexOf('*');
				final int i2=tn.lastIndexOf(' ');
//				if(i1==-1&&i2==-1){isctor=true;name="";type=tn;return;}
				if(i1==-1&&i2==-1){
					if(isfunc){name=tn;type="void";}
					else{name=tn;type="int";}
				}else if(i1>i2){name=tn.substring(i1+1);type=tn.substring(0,i1+1);}
				else{name=tn.substring(i2+1);type=tn.substring(0,i2);}
				ispointer=type.endsWith("*");
				if(!isfunc&&args.length()==0){// set default value
					args="0";
				}
			}
//			final public boolean is_pointer(){return ispointer;}
		}
	}
	public static void main(String[] args){
		//final ArrayList<stmt>stms=new ArrayList<>();
		final type integer=new type("int");
		final type floating=new type("float");
		final type file=new type("file");
		
		final var a=new var(integer,"a");
		final var b=new var(integer,"b");//change type for type missmatch error
		final var f=new var(file,"f");
		final var d=new var(file,"d");
		final var e=new var(floating,"e");
		final stmt brk=new brk();
		final stmt cont=new cont();
		final value i3=new inti(3);
		final value i4=new inti(4);
		final value i1=new inti(1);
		final value i8=new inti(8);
		final value i5=new inti(5);
		final value s1=new str("a=%d");
		
		final stmt prog=new block(
				new let(integer,a,i3),
				new set(a,i4),
				new set(b,a),
//				new let(file,f),// error uninitialized
//				new set(a,e),// error
				new loop(new block(
					new incn(a,i3),
					new decn(a,i1),
					new inc(a),
					new incpre(a),
					new dec(a),
					new decpre(a),
					new printf(s1,a),
					new ife(new eq(a,i8),brk,cont),
					new iff(new eq(a,i8),new block(new set(a,i5),brk))
				)),
				new set(a,new add(a,i5)),
//				new set(a,new add(a,e)),//error
				new printf(s1,a),
				new fcall(f,"to",d),
				new ife(new eq(a,i8),new decpre(a),
				new ife(new eq(a,i8),brk,
				cont
				)),
				new ret(a),
				new loop(new set(a,new add(a,i1))
		));
		
		final PrintWriter pw=new PrintWriter(new OutputStreamWriter(System.out));
		pw.println(prog);
		pw.close();
	}
	// notes
//	System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);

}