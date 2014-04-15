package a.cap;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
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
		while(true){
			if(l==0)break;
			final char ch=cbuf[o];o++;l--;
			switch(state){
			case state_class_ident:{
				if(token.length()==0&&is_white_space(ch))break;// trims leading white space
				if(ch!='{'){token.append(ch);break;}// look for opening class block
				final String clsident=clean_whitespaces(token.toString());					
				token.setLength(0);
				if(!is_valid_class_identifier(clsident))throw new Error("line "+lineno+": "+charno+": invalid class name '"+clsident+"'");
				classes.push(new struct(clsident));
				namespace_enter(clsident);
				state_push(state_class_block);
				break;
			}
			case state_class_block:{// find type
				if(token.length()==0&&is_white_space(ch))break;// trim leading white space
				if(is_char_arguments_open(ch)){// found type+name function i.e. const int foo•(
					final String ident=clean_whitespaces(token.toString());
					token.setLength(0);// ignore class block content
					namespace_enter(ident);
					classes.peek().slots.push(new struct.slot(ident,true));
//					// if constructor format but not a constructor
//					if(ident.indexOf('*')==-1&&ident.indexOf(' ')==-1&&!ident.equals(classes.peek().name))throw new Error("line "+lineno+":"+charno+": in class '"+classes.peek().name+"' item '"+ident+"' is declared like a constructor but does not have the class name.");
					// assume it returns void or itself for chained calls					
					state_push(state_function_arguments);
					break;
				}
				if(is_char_statement_close(ch)){// found type+name field i.e. int a•;
					final String ident=clean_whitespaces(token.toString());
					token.setLength(0);// ignore class block content
					classes.peek().slots.push(new struct.slot(ident,false));
//					state_back_to(state_class_block);
					break;
				}
				if(is_char_statement_assigment(ch)){// found type+name field i.e. int a•=0;
					final String ident=clean_whitespaces(token.toString());
					token.setLength(0);// ignore class block content
					classes.peek().slots.push(new struct.slot(ident,false));
					state_push(state_struct_member_default_value);
					break;
				}
				if(is_char_block_close(ch)){// close class block
					token.setLength(0);// ignore class block content
					state_back_to(state_class_ident);
					namespace_pop();
					break;
				}
				token.append(ch);
				break;
			}
			case state_struct_member_default_value:{// int a=•0•;
				if(is_white_space(ch)&&token.length()==0)continue;//trim lead space
				if(is_char_statement_close(ch)){// int a=0•;
					final String def=token.toString().trim();
					token.setLength(0);
					classes.peek().slots.peek().args=def;			
					state_back_to(state_class_block);
					break;
				}
				token.append(ch);
				break;
			}
			case state_function_arguments:{// class file{func(•size s,int i•){}}
				if(is_char_arguments_close(ch)){
					state_push(state_find_function_block);
					final String funcargs=clean_whitespaces(token.toString());
					classes.peek().slots.peek().args=funcargs;
					token.setLength(0);
					break;
				}
				token.append(ch);
				break;
			}
			case state_find_function_block:{// class file{func(size s) •{}
				if(is_white_space(ch)&&token.length()==0)continue;//trim lead space
				if(is_char_block_open(ch)){state_push(state_function_block);break;}//found
				token.append(ch);
				break;
			}
			case state_function_block:{// class file{func(size s){•int a=2;a+=2;•}
				token.append(ch);
				if(is_white_space(ch)&&token.length()==0)continue;
				if(is_char_block_open(ch)){
					state_push(state_in_code_block);
					break;
				}
				if(is_char_block_close(ch)){
					token.setLength(token.length()-1);
					classes.peek().slots.peek().body=token.toString();
					token.setLength(0);
					state_back_to(state_class_block);
					namespace_pop();
					break;
				}
				if(is_char_string_open(ch)){
					state_push(state_in_string);
				}
				break;
			}
			case state_in_string:{// string a="•hello world•";
				token.append(ch);
				if(is_char_string_close(ch)){
					state_pop();
				}
				break;
			}
			case state_in_code_block:{// while(true){•pl("hello world")};
				token.append(ch);
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
			default:throw new Error("unknown state "+state);
			}
			charno++;if(ch=='\n'){lineno++;charno=1;}
		}
		Collections.reverse(classes);
	}
	private boolean is_char_string_close(char ch) {return ch=='\"';}
	private boolean is_char_string_open(char ch){return ch=='\"';}
	private boolean is_char_statement_assigment(char ch){return ch=='=';}
//	@Override public void flush()throws IOException{out.flush();}
//	@Override public void close()throws IOException{out.close();}
	final @Override public void flush()throws IOException{}
	final @Override public void close()throws IOException{}
	final void namespace_pop(){namespace_stack.pop();}
	final void namespace_enter(String name){namespace_stack.push(new namespace(name));}

	private static String clean_whitespaces(String s){return s.trim().replaceAll("\\s+"," ").replaceAll(" \\*","\\*").replaceAll("\\* ","\\*");}
	private void state_push(int newstate){state_stack.push(state);state=newstate;}
	private void state_back_to(final int s){while(s!=(state=state_stack.pop()));}
	private void state_pop(){state=state_stack.pop();}
	
	
//	PrintWriter out;
	private int lineno=1,charno=1;
	private StringBuilder token=new StringBuilder(32);

	final private LinkedList<namespace>namespace_stack=new LinkedList<>();
	final private LinkedList<Integer>state_stack=new LinkedList<>();
	final private LinkedList<struct>classes=new LinkedList<>();
	final public List<struct>classes(){return classes;}

	private int state;
	private static final int state_class_ident=0;
	private static final int state_class_block=1;
	private static final int state_function_arguments=2;
	private static final int state_find_function_block=3;
	private static final int state_function_block=4;
	private static final int state_struct_member_default_value=5;
	private static final int state_in_string=6;
	private static final int state_in_code_block=7;
	
	private static boolean is_char_block_open(char ch){return ch=='{';}
	private static boolean is_char_arguments_open(char ch){return ch=='(';}
	private static boolean is_char_arguments_close(char ch){return ch==')';}
	private static boolean is_char_block_close(char ch){return ch=='}';}
	private static boolean is_char_statement_close(char ch){return ch==';';}
	private static boolean is_white_space(char ch){return Character.isWhitespace(ch);}
	private static boolean is_valid_class_identifier(String nm){return true;}
	
	
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
			String tn;
			String name;
			String type;
			String args="";//when field this is default value
			String body="";
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
	final static ArrayList<stmt>stms=new ArrayList<>();
	public static void main(String[] args){
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
		
		stms.add(new let(integer,a,i3));
		stms.add(new set(a,i4));
		stms.add(new set(b,a));
//		stms.add(new let(file,f));// error uninitialized
//		stms.add(new set(a,e));// error
		stms.add(new loop(new block(
					new incn(a,i3),
					new decn(a,i1),
					new inc(a),
					new incpre(a),
					new dec(a),
					new decpre(a),
					new printf(s1,a),
					new ife(new eq(a,i8),new block(brk),new block(cont)),
					new iff(new eq(a,i8),new block(brk))
		)));
		stms.add(new set(a,new add(a,i5)));
//		stms.add(new set(a,new add(a,e)));//error
		stms.add(new printf(s1,a));
		stms.add(new fcall(f,"to",d));
		stms.add(
				new ife(new eq(a,i8),new block(new decpre(a)),
				new ife(new eq(a,i8),new block(brk),
				new block(cont)
				)));
		stms.add(new ret(a));
		stms.add(new loop(new block(
				new set(a,new add(a,i1))
		)));
		
		final PrintWriter pw=new PrintWriter(new OutputStreamWriter(System.out));
		for(stmt s:stms){
			s.to(pw);
			pw.println(s.end_delim());
		}
		pw.close();
//		System.out.println(statements);
	}
	// notes
//	System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);

}