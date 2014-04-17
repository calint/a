package a.cap;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import a.cap.vm.add;
import a.cap.vm.block;
import a.cap.vm.brk;
import a.cap.vm.call;
import a.cap.vm.cont;
import a.cap.vm.ctor;
import a.cap.vm.dec;
import a.cap.vm.decn;
import a.cap.vm.decpre;
import a.cap.vm.eq;
import a.cap.vm.fcall;
import a.cap.vm.floati;
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
import a.cap.vm.set_struct_member;
import a.cap.vm.stmt;
import a.cap.vm.str;
import a.cap.vm.struct_member;
import a.cap.vm.type;
import a.cap.vm.value;
import a.cap.vm.var;

final class toc extends Writer{
	final public String state_to_string(){return state_stack.toString()+" "+namespace_stack.toString();}
//	final private LinkedList<type>types=new LinkedList<>();
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
			case state_in_class_name:{
				if(is_token_empty()&&is_white_space(ch))break;// trims leading white space
				if(!is_char_block_open(ch)){token_add(ch);break;}// look for opening class block
				final String clsident=token_take_clean();
				if(!is_valid_class_identifier(clsident))throw new Error("line "+lineno+": "+charno+": invalid class name '"+clsident+"'\n  in: "+namespace_stack);
				structs.push(new struct(clsident));
				namespace_push(clsident);
				if(types_contains(clsident))throw new Error(clsident+" already declared at line xxx");
				final type t=new type(clsident);
				types_add(t);
//				types.add(t);
				namespace_add_var(new var(t,"o"));
				state_push(state_in_class_block);
				break;
			}
			case state_in_class_block:{// find type
				if(is_token_empty()&&is_white_space(ch))break;// trim leading white space
				if(is_char_arguments_open(ch)){// found type+name function i.e. const int foo•(
					final String ident=token_take_clean();
					final struct.slot slt=new struct.slot(ident,true);
//					final type t=new type(slt.type);//? lookup in declared types
//					namespace_add_var(new var(t,slt.name));
					structs.peek().slots.push(slt);
					// assume it returns void or self for chained calls					
					namespace_push(ident);
					state_push(state_in_function_arg);
					break;
				}
				if(is_char_statement_close(ch)){// found type+name field i.e. int a•;
					final String ident=token_take_clean();
					final struct.slot slt=new struct.slot(ident,false);
//					final type t=new type(slt.type);//? lookup in declared types
					final type t=find_type_by_name_or_break(slt.type);
					namespace_add_var(new var(true,t,slt.name));// add variable refering to struct member					
					structs.peek().slots.push(slt);
					break;
				}
				if(is_char_statement_assigment(ch)){// found type+name field=... i.e. int a•=0;
					final String ident=token_take_clean();
					final struct.slot slt=new struct.slot(ident,false);
//					final type t=new type(slt.type);//? lookup in declared types
					final type t=find_type_by_name_or_break(slt.type);
					namespace_add_var(new var(true,t,slt.name));// add variable refering to struct member										
					structs.peek().slots.push(slt);
					state_push(state_in_struct_member_default_value);
					break;
				}
				if(is_char_block_close(ch)){// close class block
					token_clear();// ignore class block content
					state_back_to(state_in_class_name);
					namespace_pop();
					break;
				}
				token_add(ch);
				break;
			}
			case state_in_function_arg:{// class file{func(•size•,•int i•){}}
				if(is_char_arguments_separator(ch)||is_char_arguments_close(ch)){
					final String s=token_take_clean();
					if(s.length()>0){
						final int i=s.indexOf(' ');
						if(i==-1){//  i.e.  file{to(stream){}}
							final String snm=suggest_argument_name(s,namespace_stack);
//							final type t=new type(s);//? lookup in reflection
							final type t=find_type_by_name_or_break(s);
							final var v=new var(t,snm);
							structs.peek().slots.peek().argsvar.add(v);
							namespace_add_var(v);
						}else{//  i.e.  file{to(stream st){}}
							final String typenm=s.substring(0,i);
							final String name=s.substring(i+1);
//							final type t=new type(typenm);//? lookup in reflection
							final type t=find_type_by_name_or_break(typenm);
							final var v=new var(t,name);
							structs.peek().slots.peek().argsvar.add(v);
							namespace_add_var(v);
						}
					}
					if(is_char_arguments_close(ch)){
						state_push(state_find_function_block);
						final String funcargs=token_take_clean();
//						classes.peek().slots.peek().args=funcargs;
						break;
					}
				}
				token_add(ch);
				break;
			}
			case state_in_struct_member_default_value:{// int a=•0•;
				if(is_white_space(ch)&&is_token_empty())continue;//trim lead space
				if(is_char_statement_close(ch)){// int a=0•;
					final String def=token_take_trimmed();
					structs.peek().slots.peek().struct_member_default_value=def;			
					state_back_to(state_in_class_block);
					break;
				}
				token_add(ch);
				break;
			}
//			case state_in_function_arguments:{// class file{func(•size s,int i•){}}
//				if(is_char_arguments_close(ch)){
//					state_push(state_find_function_block);
//					final String funcargs=token_take_clean();
//					classes.peek().slots.peek().args=funcargs;
//					break;
//				}
//				token_add(ch);
//				break;
//			}
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
					final struct.slot sl=structs.peek().slots.peek();
					token_dec_len_by_1();//? remove the }
					sl.func_source=token_take();
					try{
						final Reader r=new StringReader(sl.func_source);
						sl.stm=parse_function_source(r,namespace_stack);
//						System.err.println(sl.stm);
					}catch(Throwable t){
//						t.printStackTrace();
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
			case state_in_code_block:{// while(true){•pl("hello world");•}
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
		Collections.reverse(structs);
	}
	
	private boolean types_contains(String name){
		for(type t:types){
			if(t.name().equals(name))
				return true;
		}
		return false;
	}
	//	type find_declared_type_by_name(String name){
//		for(struct s:structs){
//			if(name.equals(s.name))
//				return new type(s.name);//? use singleton
//		}
//		return null;
//	}
//	private static boolean is_char_blank(char ch){return Character.isWhitespace(ch);}
	private static String suggest_argument_name(String s,LinkedList<namespace>nms){
		return s.charAt(0)+"";
	}
	private static boolean is_char_arguments_separator(char ch){return ch==',';}
	void namespace_add_var(var v){namespace_stack.peek().vars.put(v.code,v);}
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
	final void namespace_push(String name){namespace_stack.push(new namespace(name));}

	private static String clean_whitespaces(String s){return s.trim().replaceAll("\\s+"," ").replaceAll(" \\*","\\*").replaceAll("\\* ","\\*");}
	private void state_push(int newstate){state_stack.push(state);state=newstate;}
	private void state_back_to(final int s){while(s!=(state=state_stack.pop()));}
	private void state_pop(){state=state_stack.pop();}
	
	
	private int lineno=1,charno=0;
	private StringBuilder token=new StringBuilder(32);

	final private LinkedList<namespace>namespace_stack=new LinkedList<>();
	final private LinkedList<Integer>state_stack=new LinkedList<>();
	final private LinkedList<struct>structs=new LinkedList<>();
	final public List<struct>classes(){return structs;}
	type find_struct_member_type_or_break(String struct_name,String member_name){
		for(struct s:structs){
			if(s.name.equals(struct_name)){
				for(struct.slot sl:s.slots){
					if(sl.name.equals(member_name))
						return find_type_by_name_or_break(sl.type);
				}
				throw new Error("struct member '"+member_name+"' in '"+struct_name+"' not found in "+s.slots);
			}
		}
		throw new Error("struct '"+struct_name+"' not found in declared structs: "+structs);
	}

	private LinkedList<type>types=new LinkedList<>();
	void types_add(type t){types.add(t);}
	type find_type_by_name_or_break(String name){
		for(type t:types){
			if(name.equals(t.code))
				return t;
		}
		throw new Error("type '"+name+"' not found in declared types "+types);
	}
	type find_type_by_name_or_make_new(String name){
		for(type t:types){
			if(name.equals(t.code))
				return t;
		}
		final type t=new type(name);
		types.add(t);
		return t;
	}

	
	// fcall  i.e.   foo f={1,2};f.to(out);    
	// alt syntaxes:
	//  foo f={1,2}.to(out);
	//  foo{1,2}.to(out);

	stmt parse_function_source(Reader r,LinkedList<namespace>nms)throws Throwable{
		LinkedList<stmt>stms=new LinkedList<>();
		while(true){
			final stmt s=parse_statement(r,nms);
			if(s==null)break;//eos
			stms.add(s);
//					final int i=r.read();
//					if(i!=';')throw new Error("@ yyyy:xxx  expected end of statement ';' but got '"+(char)i+"' (0x"+Integer.toHexString(i)+")");
		}
		return new block(stms);
	}
	stmt[]parse_function_arguments(Reader r,LinkedList<namespace>nms)throws Throwable{
		ArrayList<stmt>args=new ArrayList<>();
		int ch=0,prvch=0;
		final StringBuilder sb=new StringBuilder(128);
		boolean in_string=false;
		while(true){
			prvch=ch;
			ch=r.read();
			if(ch==-1)break;
			if(ch=='"'){
				if(in_string){
					if(prvch=='\\'){// escaped  i.e.   "%s  \•" quote \"   "
						
					}else{
						in_string=false;
						sb.append((char)ch);
						continue;
					}
				}else{
					in_string=true;
				}
			}
			if(in_string){
				sb.append((char)ch);
				continue;
			}
			if(ch==','){
				// found next argument
				final String code=sb.toString();
				final Reader rc=new StringReader(code);
				final stmt arg=parse_statement(rc,nms);
				args.add(arg);
//						System.out.println(arg);
				sb.setLength(0);
				continue;
			}
			if(ch==')'){
				// found end of arguments
//						System.out.println(sb);
				final String code=sb.toString();
				sb.setLength(0);
				final Reader rc=new StringReader(code);
				final stmt arg=parse_statement(rc,nms);
				args.add(arg);
				break;
			}
			sb.append((char)ch);
//					if(sb.length()==0&&Character.isWhitespace(ch))continue;
		}
		final stmt[]aargs=new stmt[args.size()];
		int i=0;
		for(stmt s:args)
			aargs[i++]=s;
		return aargs;
	}
	stmt parse_statement(Reader r,LinkedList<namespace>nms)throws Throwable{
		// expect call/let/set/const/fcall   loop/ret
		int ch=0;
		final StringBuilder sb=new StringBuilder(128);
		while(true){
			ch=r.read();
			if(ch==')')continue;//? buggy
			if(ch==-1)break;
			if(sb.length()==0&&Character.isWhitespace(ch))continue;
			if(ch=='('){// call
				final String funcname=sb.toString();
				sb.setLength(0);
				final int i=funcname.indexOf('.');
				if(i!=-1){// i.e.   f.to(out);
					final String var=funcname.substring(0,i);
					final String func=funcname.substring(i+1);
					final namespace ns=nms.peek();
					final var v_ns=ns.vars.get(var);
					if(v_ns==null)throw new Error("'"+var+"' not  in "+namespaces_and_declared_types_to_string(nms));
					final stmt s=parse_statement(r,nms);
					final stmt ret;
					//? check args and declaration
					if(s!=null)
						ret=new fcall(v_ns,func,s);
					else
						ret=new fcall(v_ns,func);
					return ret;
				}
				final stmt[]args=parse_function_arguments(r,nms);
				//? check args and declaration
				return new call(funcname,args);
			}
			if(ch==')') // when parsing statements in function arguments
				break;
			if(ch=='='){// let or set
				final String s=sb.toString();
				int i=s.lastIndexOf(' '); // int a=•1;
				if(i==-1)i=s.lastIndexOf('*');// char*c=•'c';
				if(i==-1){// set
					final int i1=s.lastIndexOf('.');
					if(i1==-1){// a=2;
						final namespace ns=nms.peek();
						final var v_ns=ns.vars.get(s);
						if(v_ns==null)throw new Error(" at yyyy:xx  '"+s+"' not in "+namespaces_and_declared_types_to_string(nms));
						return new set(v_ns,parse_statement(r,nms));
					}else{// f.a=2;
						final String varnm=s.substring(0,i1);
						final String struct_member_name=s.substring(i1+1);
						final var v=find_var_in_namespace_stack(varnm,nms);
						if(v==null)throw new Error(" at yyyy:xx  '"+s+"' not in "+nms);
						final stmt st=parse_statement(r,nms);
						return new set_struct_member(v,struct_member_name,st,this);
//						return new stmt(v.code+"."+struct_member_name+"="+st);
					}
				}else{// let    int i=2;
					final String type=s.substring(0,i).trim();
					final type t=find_type_by_name_or_make_new(type);
					final String name=s.substring(i+1);
					final namespace ns=nms.peek();
					final var v_ns=ns.vars.get(name);//? look in namespaces stack
					if(v_ns!=null)throw new Error(" at yyyy:xx  '"+s+"' already in "+namespaces_and_declared_types_to_string(nms));
					final var v=new var(t,name);//? add source position for easier error message
					ns.vars.put(name,v);
					return new let(t,v,parse_statement(r,nms));
				}
			}
			if(ch==';'){
				if(sb.length()==0){//  stray ';'   i.e   pl("hello");;
					continue;
				}
				break;
			}
			sb.append((char)ch);
		}
		if(sb.length()==0){
			if(ch==-1)
				return null;
			throw new Error();
		}
		final String s=sb.toString().trim();
		if(s.startsWith("\"")&&s.endsWith("\"")){// string
			final String t=s.substring(1,s.length()-1);
			return new str(t);
		}
		//
		final int ixspc=s.lastIndexOf(' ');
		if(ixspc!=-1){//   i.e. file f;
			final String name=s.substring(ixspc+1).trim();
			final String type=s.substring(0,ixspc).trim();
			final type t=find_type_by_name_or_break(type);
//			final type t=new type(type);//? lookup in namespace
//			final namespace ns=nms.peek();
			final var v=find_var_in_namespace_stack(name, nms);
			if(v!=null)throw new Error("@yyyy:xxx  '"+name+"' already '"+v.type()+"' in '"+namespaces_and_declared_types_to_string(nms));
			final var nv=new var(t,name);
			namespace_add_var(nv);
			return new let(t,nv,new ctor(t));
		}
		// const number or variable
		final boolean first_char_is_digit=Character.isDigit(s.charAt(0));
		if(first_char_is_digit){
			if(s.endsWith("f")){//? shorthand for float 1f .3f 1.f 0
				final float f=Float.parseFloat(s);
				return new floati(f);
			}
			return new inti(Integer.parseInt(s));
		}
		final int i=s.lastIndexOf('.');
		if(i==-1){// does not refer to struct member
			final var v=find_var_in_namespace_stack(s,nms);
			if(v==null)throw new Error(" at yyyy:xxx   '"+s+"' not in  "+namespaces_and_declared_types_to_string(nms));
			return v;
		}
		final String varnm=s.substring(0,i);
		final var v=find_var_in_namespace_stack(varnm,nms);
		if(v==null)throw new Error();
		final String struc_member=s.substring(i+1);
		final type t=find_struct_member_type_or_break(v.type().name(),struc_member);
		
		return new struct_member(v,struc_member,this);
	}
	private static String namespaces_and_declared_types_to_string(LinkedList<namespace>nms){
		final StringBuilder sb=new StringBuilder(256);
		for(namespace ns:nms){
			sb.append(ns.name).append("{");
			if(!ns.vars.isEmpty()){
				for(var v:ns.vars.values()){
					sb.append(v.type()).append(" ").append(v.code).append(',');
				}
				sb.setLength(sb.length()-1);
			}
			sb.append("} ");
		}
		return sb.toString();
	}
	private static var find_var_in_namespace_stack(final String name,final LinkedList<namespace>ls){
		final Iterator<namespace>i=ls.iterator();
		while(i.hasNext()){
			final namespace ns=i.next();
			final var v=ns.vars.get(name);
			if(v!=null)return v;
		}
		return null;
	}


	private int state;
	private static final int state_in_class_name=0;
	private static final int state_in_class_block=1;
	private static final int state_in_function_arg=2;
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
		HashMap<String,var>vars=new HashMap<>();
		public namespace(String nm){name=nm;}
		@Override public String toString(){return name;}
	}
	final static class struct{
		String name;
		LinkedList<slot>slots=new LinkedList<>();
		public struct(String name){this.name=name;}//autoset
		@Override public String toString(){return name+"{"+slots+"}";}
		final static class slot{
			String tn="";// string from source  i.e. 'int i' 'string s'
			String name="";// decoded from tn  i.e.  'i'     's'
			String type="";//                        'int'   'string'
//			String args="";//when field this is default value
			String struct_member_default_value="";
			String func_source="";//function source
			stmt stm;
			boolean isfunc;
			boolean isctor;
			boolean ispointer;
			final LinkedList<var>argsvar=new LinkedList<>();
			public slot(String type_and_name,boolean func){tn=type_and_name;isfunc=func;decode_tn();}
			@Override public String toString(){
				final StringBuilder sb=new StringBuilder();
				sb.append(type).append(" ").append(name);
				if(isfunc){
					sb.append("(");
					sb.append(args_to_string());
					sb.append(")");
				}
				return sb.toString();
			}
			String args_to_string(){
				final StringBuilder sb=new StringBuilder();
				for(var v:argsvar){
					sb.append(v.type()).append(" ").append(v.code).append(",");
				}
				final int len=sb.length();
				if(len>0)sb.setLength(len-1);
				return sb.toString();
			}
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
				if(!isfunc&&struct_member_default_value.length()==0){// set default value
					struct_member_default_value="0";
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