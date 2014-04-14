package a.cap.c;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import a.cap.c.cap.clazz;
import a.cap.c.cap.namespace;

final class toc extends Writer{
	@Override public void write(char[]cbuf,final int off,final int len)throws IOException{
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
				classes.push(new clazz(clsident));
				namespace_push(clsident);
				state_push(state_class_block);
				break;
			}
			case state_class_block:{// find type
				if(token.length()==0&&is_white_space(ch))break;// trim leading white space
				if(is_char_arguments_open(ch)){// found type+name function i.e. const int foo•(
					final String ident=clean_whitespaces(token.toString());
					token.setLength(0);// ignore class block content
					namespace_push(ident);
					classes.peek().slots.push(new clazz.slot(ident,true));
					// if constructor format but not a constructor
					if(ident.indexOf('*')==-1&&ident.indexOf(' ')==-1&&!ident.equals(classes.peek().name))throw new Error("line "+lineno+":"+charno+": in class '"+classes.peek().name+"' item '"+ident+"' is declared like a constructor but does not have the class name.");
					state_push(state_function_arguments);
					break;
				}
				if(is_char_statement_close(ch)){// found type+name field i.e. int a•;
					final String ident=clean_whitespaces(token.toString());
					token.setLength(0);// ignore class block content
					classes.peek().slots.push(new clazz.slot(ident,false));
					state_push(state_class_block);
					break;
				}
				if(is_char_block_close(ch)){// close class block
					token.setLength(0);// ignore class block content
					state_pop_until_state(state_class_ident);
					namespace_pop();
					break;
				}
				token.append(ch);
				break;
			}
			case state_function_arguments:{// class file{func(•size s,int i•){}}
				if(is_char_arguments_close(ch)){
					state_push(state_find_function_block);
					final String funcargs=clean_whitespaces(token.toString());
//						System.out.println("function args: "+funcargs);
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
				if(is_white_space(ch)&&token.length()==0)continue;
				if(is_char_block_close(ch)){state_pop_until_state(state_class_block);namespace_pop();break;}
//					token.append(ch);
				break;
			}
			default:throw new Error("unknown state "+state);
			}
			charno++;if(ch=='\n'){lineno++;charno=1;}
		}
	}
	private static String clean_whitespaces(String s){return s.trim().replaceAll("\\s+"," ").replaceAll(" \\*","\\*").replaceAll("\\* ","\\*");}
//		System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);
	private void state_push(int newstate){state_stack.push(state);state=newstate;}
	private void state_pop_until_state(final int s){while(s!=(state=state_stack.pop()));}
	private void namespace_push(String name){namespace_stack.push(new namespace(name));}
	void namespace_pop(){namespace_stack.pop();}
	void namespace_enter(String name){namespace_push(name);}
	@Override public void flush()throws IOException{out.flush();}
	@Override public void close()throws IOException{out.close();}
	
	final private LinkedList<Integer>state_stack=new LinkedList<>();
	final private LinkedList<clazz>classes=new LinkedList<>();
	final public List<clazz>classes(){return classes;}
	final private LinkedList<namespace>namespace_stack=new LinkedList<>();
	
	
	PrintWriter out;
	private int lineno=1,charno=1;
	private StringBuilder token=new StringBuilder(32);

	private int state;
	private static final int state_class_ident=0;
	private static final int state_class_block=1;
	private static final int state_function_arguments=2;
	private static final int state_find_function_block=3;
	private static final int state_function_block=4;
	
	private static boolean is_char_block_open(char ch){return ch=='{';}
	private static boolean is_char_arguments_open(char ch){return ch=='(';}
	private static boolean is_char_arguments_close(char ch){return ch==')';}
	private static boolean is_char_block_close(char ch){return ch=='}';}
	private static boolean is_char_statement_close(char ch){return ch==';';}
	private static boolean is_white_space(char ch){return Character.isWhitespace(ch);}
	private static boolean is_valid_class_identifier(String nm){return true;}
}