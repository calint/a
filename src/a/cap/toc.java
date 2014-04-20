package a.cap;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import a.cap.vm.block;
import a.cap.vm.call;
import a.cap.vm.ctor;
import a.cap.vm.fcall;
import a.cap.vm.floati;
import a.cap.vm.inti;
import a.cap.vm.let;
import a.cap.vm.set;
import a.cap.vm.set_struct_member;
import a.cap.vm.stmt;
import a.cap.vm.str;
import a.cap.vm.struct_member;
import a.cap.vm.type;
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
			charno++;if(ch=='\n'){lineno++;charno=0;}
			switch(state){
			case state_1_in_class_name:{
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
				state_push(state_2_in_class_block);
				break;
			}
			case state_2_in_class_block:{// find type
				if(is_token_empty()&&is_white_space(ch))break;// trim leading white space
				if(is_char_arguments_open(ch)){// found type+name function i.e. const int foo•(
					final String ident=token_take_clean();
					final struct.slot slt=new struct.slot(ident,true);
//					final type t=new type(slt.type);//? lookup in declared types
//					namespace_add_var(new var(t,slt.name));
					structs.peek().slots.push(slt);
					// assume it returns void or self for chained calls					
					namespace_push(ident);
					state_push(state_3_in_function_arg);
					break;
				}
				if(is_char_statement_close(ch)){// found type+name field i.e. int a•;
					final String ident=token_take_clean();
					final struct.slot slt=new struct.slot(ident,false);
//					final type t=new type(slt.type);//? lookup in declared types
					final type t=find_type_by_name(slt.type,false);
					if(t==null)
						throw new Error("@("+lineno+":"+charno+")  '"+slt.type+"' not defined\n  defined types:\n    "+types);
					namespace_add_var(new var(true,t,slt.name));// add variable refering to struct member					
					structs.peek().slots.push(slt);
					break;
				}
				if(is_char_block_close(ch)){// close class block
					final String ident=token_take_clean();
						if(ident.length()>0){
						final struct.slot slt=new struct.slot(ident,false);
	//					final type t=new type(slt.type);//? lookup in declared types
						final type t=find_type_by_name_or_break(slt.type);
						namespace_add_var(new var(true,t,slt.name));// add variable refering to struct member					
						structs.peek().slots.push(slt);
					}
					token_clear();// ignore class block content
					state_back_to(state_1_in_class_name);
					namespace_pop();
					break;
				}
				if(is_char_statement_assigment(ch)){// found type+name field=... i.e. int a•=0;
					final String ident=token_take_clean();
					final struct.slot slt=new struct.slot(ident,false);
//					final type t=new type(slt.type);//? lookup in declared types
					final type t=find_type_by_name_or_break(slt.type);
					namespace_add_var(new var(true,t,slt.name));// add variable refering to struct member										
					structs.peek().slots.push(slt);
					state_push(state_struct_member_default_value);
					break;
				}
				token_add(ch);
				break;
			}
			case state_3_in_function_arg:{// class file{func(•size•,•int i•){}}
				if(is_char_arguments_separator(ch)||is_char_arguments_close(ch)){
					final String s=token_take_clean();
					if(s.length()>0){
						final int i=s.indexOf(' ');
						if(i==-1){//  i.e.  file{to(stream){}}
							final type t=find_type_by_name(s,false);
							if(t==null)
								throw new Error(source_reader.hr_location_string_from_line_and_col(lineno,charno)+" '"+s+"' not declared\n  declared types "+types);
							final String snm=suggest_argument_name(t,namespace_stack);
							final var v=new var(t,snm);
							structs.peek().slots.peek().argsvar.add(v);
							namespace_add_var(v);
						}else{//  i.e.  file{to(stream st){}}
							final String typenm=s.substring(0,i);
							final String name=s.substring(i+1);
							final type t=find_type_by_name_or_break(typenm);
							final var v=new var(t,name);
							structs.peek().slots.peek().argsvar.add(v);
							namespace_add_var(v);
						}
					}
					if(is_char_arguments_close(ch))state_push(state_4_find_function_block);
					break;
				}
				token_add(ch);
				break;
			}
			case state_4_find_function_block:{// class file{func(size s) •{}
				if(is_white_space(ch)&&is_token_empty())continue;//trim lead space
				if(!is_char_block_open(ch)){token_add(ch);break;}
				// found
				state_push(state_5_in_function_block);
				lineno_func=lineno;charno_func=charno;
				break;
			}
			case state_5_in_function_block:{// class file{func(size s){•int a=2;a+=2;•}
				token_add(ch);
				if(is_white_space(ch)&&is_token_empty())continue;
				if(is_char_block_open(ch)){
					state_push(state_in_code_block);
					break;
				}
				if(is_char_block_close(ch)){// end of function block
					if(state==state_5_in_function_block){
						final struct.slot sl=structs.peek().slots.peek();
						token_dec_len_by_1();//? remove the }
						sl.func_source=token_take();
						try{
							final source_reader r=new source_reader(new StringReader(sl.func_source),lineno_func,charno_func);
//							final int peek=r.read();
//							if(peek==-1)throw new Error(r.hrs_location()+" unexpected en of line.\n  expected '{' or a statment");
//							if(is_char_block_open((char)peek)){
								sl.stm=parse_block(r,namespace_stack,"}");
//							}else{
//								r.unread(peek);
//								sl.stm=parse_statement(r,namespace_stack,";");
//							}
//							System.err.println(sl.stm);
						}catch(Throwable t){
//							t.printStackTrace();
							throw new Error(t);
						}
						state_back_to(state_2_in_class_block);
						namespace_pop();
						break;
					}
					state_pop();
					break;
				}
				if(is_char_string_open(ch))
					state_push(state_in_string);
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
			case state_struct_member_default_value:{// int a=•0•;
				if(is_white_space(ch)&&is_token_empty())continue;//trim lead space
				if(is_char_statement_close(ch)){// int a=0•;
					final String def=token_take_trimmed();
					structs.peek().slots.peek().struct_member_default_value=def;			
					state_back_to(state_2_in_class_block);
					break;
				}
				token_add(ch);
				break;
			}
			case state_in_string:{// string a="•hello world•";
				token_add(ch);
				if(is_char_string_close(ch))
					state_pop();
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
	private static String suggest_argument_name(type t,LinkedList<namespace>nms){
		return t.name().charAt(0)+"";
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
	private int lineno_func,charno_func;// used to mark where a function body starts for error location messaging
	private StringBuilder token=new StringBuilder(32);

	final private LinkedList<namespace>namespace_stack=new LinkedList<>();
	final private LinkedList<Integer>state_stack=new LinkedList<>();
	final private LinkedList<struct>structs=new LinkedList<>();
	final public List<struct>classes(){return structs;}
	type find_struct_member_type(String struct_name,String member_name,boolean throw_excetion_if_not_found){
		for(struct s:structs){
			if(s.name.equals(struct_name)){
				for(struct.slot sl:s.slots){
					if(sl.name.equals(member_name))
						return find_type_by_name_or_break(sl.type);
				}
				if(throw_excetion_if_not_found)throw new Error("struct member '"+member_name+"' in '"+struct_name+"' not found in "+s.slots);
				return null;
			}
		}
		if(throw_excetion_if_not_found)throw new Error("struct '"+struct_name+"' not found in declared structs: "+structs);
		return null;
	}
//	type find_struct_member_type_or_break(String struct_name,String member_name){
//		
//	}
	struct find_struct_or_break(final type t){return find_struct(t,true);}
	struct find_struct(final type t,final boolean break_if_not_found){
		for(struct s:structs)
			if(s.name.equals(t.name()))
				return s;
		if(break_if_not_found)throw new Error("struct '"+t.name()+"' not found in declared structs: "+structs);
		return null;
	}
	private LinkedList<type>types=new LinkedList<>();
	void types_add(type t){types.add(t);}
	type find_type_by_name(String name,boolean break_if_not_found){
		for(type t:types){
			if(name.equals(t.code))
				return t;
		}
		if(break_if_not_found)throw new Error("type '"+name+"' not found in declared types "+types);
		return null;
	}
	type find_type_by_name_or_break(String name){return find_type_by_name(name,true);}
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

	stmt parse_block(final source_reader r,final LinkedList<namespace>nms,final String delims)throws Throwable{
		final LinkedList<stmt>stms=new LinkedList<>();
		while(true){
			final stmt s=parse_statement(r,nms,delims);
			if(s==null)break;//eos
			stms.add(s);
		}
//		if(stms.size()==1)return stms.peek();
		return new block(stms);
	}
	stmt[]parse_function_arguments(final source_reader r,final LinkedList<namespace>nms,final String delims)throws Throwable{
		final ArrayList<stmt>args=new ArrayList<>();
		while(true){
			final stmt st=parse_statement(r,nms,",)");
			if(st==null)break;
			args.add(st);
			final int c=r.read();
			if(c==',')continue;
			if(c==')')break;
			throw new Error(r.hrs_location()+"  expected ',' or ')' but found '"+(char)c+"'");
//			break;
		}
		final stmt[]aargs=new stmt[args.size()];
		int i=0;
		for(stmt s:args)
			aargs[i++]=s;
		return aargs;

//			
//		int ch=0,prvch=0;
//		final StringBuilder sb=new StringBuilder(128);
//		boolean in_string=false;
//		while(true){
//			prvch=ch;
//			ch=r.read();
//			if(ch==-1)break;
//			if(ch=='"'){
//				if(in_string){
//					if(prvch=='\\'){// escaped  i.e.   "%s  \•" quote \"   "
//						
//					}else{
//						in_string=false;
//						sb.append((char)ch);
//						continue;
//					}
//				}else{
//					in_string=true;
//				}
//			}
//			if(in_string){
//				sb.append((char)ch);
//				continue;
//			}
//			if(ch==','){
//				// found next argument
//				final String code=sb.toString();
//				final source_reader rc=new source_reader(new StringReader(code));
//				final stmt arg=parse_statement(rc,nms,delims);
//				args.add(arg);
////						System.out.println(arg);
//				sb.setLength(0);
//				continue;
//			}
//			if(ch==')'){
//				// found end of arguments
////						System.out.println(sb);
//				final String code=sb.toString();
//				sb.setLength(0);
//				final source_reader rc=new source_reader(new StringReader(code));
//				final stmt arg=parse_statement(rc,nms,delims);
//				args.add(arg);
//				break;
//			}
//			sb.append((char)ch);
////					if(sb.length()==0&&Character.isWhitespace(ch))continue;
//		}
//		final stmt[]aargs=new stmt[args.size()];
//		int i=0;
//		for(stmt s:args)
//			aargs[i++]=s;
//		return aargs;
	}
	private final static class accessor{
//		String member_name;
		struct struct;
		stmt accessor_statement;
	}
	stmt parse_statement(source_reader r,LinkedList<namespace>nms,final String delims)throws Throwable{
		return parse_statement(r,nms,delims,false,null);
	}
	stmt parse_statement(source_reader r,LinkedList<namespace>nms,final String delims,final boolean consume_delimiter,final stmt previous_statement_in_accessor)throws Throwable{
		// expect call/let/set/const/fcall/str/   int/float/loop/ret
		int ch=0;
		final StringBuilder sb=new StringBuilder(128);
		while(true){
			ch=r.read();
			if(ch==-1)break;
			if(sb.length()==0&&Character.isWhitespace(ch))continue;
			if(delims.indexOf(ch)!=-1){
				if(!consume_delimiter)r.unread((char)ch);
				if(sb.length()==0)
					return null;
				break;
			}
			if(ch=='\"')return new str(r);
			if(ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='%'||ch=='^'){
				final String lhs=sb.toString();
				sb.setLength(0);
				final stmt lh=parse_statement(new source_reader(new StringReader(lhs),r.line_number,r.character_number_in_line),nms,"");
				return parse_operator((char)ch,lh,r,nms,delims);
			}
			if(Character.isDigit(ch)){
				// return (value v=read_number(r))
			}
			if(ch=='('){// call
				final String funcname=sb.toString();
				sb.setLength(0);
				if(funcname.length()==0){// i.e. ^(int i)(i+3){}
					return parse_statement(r,nms,")");
				}
				final int i=funcname.lastIndexOf('.');
				if(i!=-1){// i.e.   f.to(out)
					final String funcnm=funcname.substring(i+1);
					final String accessor=funcname.substring(0,i);
					final source_reader sr=new source_reader(new StringReader(accessor),r.line_number,r.character_number_in_line);
					final accessor ac=parse_accessor(sr,namespace_stack,null);
					final stmt[]args=parse_function_arguments(r,nms,")");
					validate_function_arguments(r,funcnm,ac.struct,args);
					final stmt ret=new fcall(ac.accessor_statement,ac.struct,funcnm,args);
					return ret;
				}
				final stmt[]args=parse_function_arguments(r,nms,")");
//				validate_function_arguments(r,funcname,ac.struct,args);
				//? check args and declaration
				return new call(funcname,args);
			}
			if(ch=='='){// let or set
				final String s=sb.toString();
				int i=s.lastIndexOf(' '); // int a=•1;
				if(i==-1)i=s.lastIndexOf('*');// char*c=•'c';
				if(i==-1){// set
					final int i1=s.indexOf('.');
					if(i1==-1){// a=2;
						final namespace ns=nms.peek();
						final var lh=ns.vars.get(s);
						if(lh==null)throw new Error(r.hrs_location()+" variable '"+s+"' not found in:\n"+namespaces_and_declared_types_to_string(nms));
						final stmt rh=parse_statement(r,nms,delims);
						if(!lh.type().equals(rh.type()))throw new Error(r.hrs_location()+"  '"+lh.code+"' is '"+lh.type()+"'  and  '"+rh.code+"' is '"+rh.type()+"'   try: '"+lh+"="+lh.type()+"("+rh.code+")'");
						return new set(lh,rh);
					}else{// f.a=2;
						final String varnm=s.substring(0,i1);
						final var v=find_var_in_namespace_stack(varnm,nms);
						if(v==null)throw new Error(r.hrs_location()+" struct member '"+s+"."+varnm+"' not found in:\n"+namespaces_and_declared_types_to_string(nms));
						final String accessor=s.substring(i1+1);
						final source_reader sr=new source_reader(new StringReader(accessor),r.line_number,r.character_number_in_line);
						final accessor ac=parse_accessor(sr,namespace_stack,v);
						final StringBuilder acstr=new StringBuilder(ac.accessor_statement.toString());
						final stmt rh=parse_statement(r,nms,delims);
						if(!rh.type().equals(ac.accessor_statement.type())){
							type t=ac.accessor_statement.type();
							boolean ok=false;
							while(true){
								final struct st=find_struct_or_break(t);
								final struct.slot parent=st.inherits_from();
								if(parent==null)throw new Error();
								t=find_type_by_name_or_break(parent.type);
								acstr.append(".").append(parent.name);
								if(t.equals(rh.type())){ok=true;break;}
							}
							if(!ok)throw new Error(r.hrs_location()+" incompatible types\n  '"+v+"."+ac.accessor_statement+"' is a '"+ac.struct.name+"' and '"+rh+"' is a '"+rh.type()+"'");
						}
						return new set_struct_member(v,acstr.toString(),rh,this);
					}
				}else{// let    int i=2;
					final String type=s.substring(0,i).trim();
					final type t=find_type_by_name_or_make_new(type);
					final String name=s.substring(i+1);
					final namespace ns=nms.peek();
					final var v_ns=ns.vars.get(name);//? look in namespaces stack
					if(v_ns!=null)throw new Error(r.hrs_location()+" variable '"+s+"' already in "+ns);
					final var shadow=find_var_in_namespace_stack(name,nms);
					if(shadow!=null)throw new Error(r.hrs_location()+" variable '"+name+"' shadows '"+shadow.type()+"'");
					final var v=new var(t,name);//? add source position for easier error message
					ns.vars.put(name,v);
					final stmt st=parse_statement(r,nms,delims);
					if(!v.type().equals(st.type()))throw new Error(r.hrs_location()+"    '"+v+"' is '"+t+"' and '"+st+"' is '"+st.type()+"'\n   try '"+t+" "+v+"="+(v.type()==floati.t?(st+"f"):(t+"("+st+")"))+"'");
					return new let(t,v,st);
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
		if(sb.length()==0){// end of stream
			return null;
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
			final type t=find_type_by_name(type,false);
			if(t==null)
				throw new Error(r.hrs_location()+"  type '"+type+"' not declared\n  declared types: "+types);
			final var v=find_var_in_namespace_stack(name, nms);
			if(v!=null)throw new Error(r.hrs_location()+" '"+name+"' already declared '"+v.type()+"' in '"+namespaces_and_declared_types_to_string(nms));
			final var nv=new var(t,name);
			namespace_add_var(nv);
			return new let(t,nv,new ctor(t));
		}
		if(s.startsWith("0x")){
			final String hex=s.substring("0x".length());
			final int hexi=Integer.parseInt(hex,16);
			return new inti(hexi,true);
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
		String vnm=s;
		boolean preinc=false,postinc=false,predec=false,postdec=false;
		if(vnm.startsWith("++")){
			preinc=true;
			vnm=vnm.substring("++".length());
		}
		if(vnm.startsWith("--")){
			predec=true;
			vnm=vnm.substring("--".length());
		}
		if(vnm.endsWith("++")){
			postinc=true;
			vnm=vnm.substring(0,vnm.length()-"++".length());
		}
		if(vnm.endsWith("--")){
			postdec=true;
			vnm=vnm.substring(0,vnm.length()-"--".length());
		}
		check_validity_of_decinc(postinc,postdec,preinc,predec);
		
		final int i=vnm.lastIndexOf('.');
		if(i==-1){// does not refer to struct member
			if(previous_statement_in_accessor==null){//find variable
				final var v=find_var_in_namespace_stack(vnm,nms);
				if(v==null)throw new Error(r.hrs_location()+"   variable '"+vnm+"' not in stack:\n"+namespaces_and_declared_types_to_string(nms));
				return wrap_variable_with_inc_dec(v,preinc,postinc,predec,postdec);
			}else{// coninues accessor chain   i.e.   f.•address.to(out)     f being prv stmt
				final type t=previous_statement_in_accessor.type();
				final struct st=find_struct_or_break(t);
				final type mt=find_struct_member_type(st.name,vnm,false);
				if(mt==null)throw new Error(r.hrs_location()+" member '"+vnm+"' not found in struct '"+st.name+"'");
//				if(!mt.equals(t))throw new Error(r.hrs_location()+" "+mt+"!="+t);
				return wrap_variable_with_inc_dec(new stmt(mt,vnm),preinc,postinc,predec,postdec);
			}
		}
		// refers to struct member
		final String varnm=vnm.substring(0,i);
		final var v=find_var_in_namespace_stack(varnm,nms);
		if(v==null)throw new Error(r.hrs_location()+"  variable '"+varnm+"' not found\n  "+namespaces_and_declared_types_to_string(nms));
		final String struc_member=vnm.substring(i+1);
//		find_struct_member_type_or_break(v.type().name(),struc_member,true);
		return wrap_variable_with_inc_dec(new struct_member(v,struc_member,this),preinc,postinc,predec,postdec);
	}
	private void validate_function_arguments(source_reader r,final String funcnm,final struct s,final stmt[]args)throws Throwable{
		final int nargs=args.length==1&&args[0]==null?0:args.length;
		final struct.slot func=s.find_function(funcnm,false);
		if(func==null)throw new Error(r.hrs_location()+" function '"+funcnm+"' in struct '"+s.name+"' not found\n  struct "+s.name+":"+s.slots);
		final int func_nargs=func.argument_count();
		if(nargs!=func_nargs)throw new Error(r.hrs_location()+" function '"+funcnm+"' in struct '"+s.name+"' requires "+(func_nargs==0?"no":Integer.toString(func_nargs))+" argument"+(func_nargs!=1?"s":"")+"\n  but provided "+nargs+"\n   '"+vm.args_to_string(args)+"'");
		// check arguments with declaration
		int arg_i=0;
		for(var va:func.argsvar){
			if(args[arg_i++].type().equals(va.type()))continue;
			throw new Error(r.hrs_location()+" while calling function '"+funcnm+"' in struct '"+s.name+"'\n  provided argument "+arg_i+" '"+args[arg_i-1]+"' of type '"+args[arg_i-1].type()+"' does not match the required type '"+va.type()+"'");
		}
	}
	private stmt parse_operator(final char op,final stmt lh,source_reader r,LinkedList<namespace>nms,final String delims)throws Throwable{
		if(op=='+'){
			return new vm.add(lh,parse_statement(r,nms,delims));
		}
		throw new Error("unknown operator '"+op+"'");
	}
	private accessor parse_accessor(source_reader r,LinkedList<namespace>nms,stmt stmprv)throws Throwable{
		LinkedList<stmt>ls=new LinkedList<>();
		while(true){
			stmt stm=parse_statement(r,nms,".",true,stmprv);
			if(stm==null)break;
			stmprv=stm;
			ls.add(stm);
		}
		final accessor a=new accessor();
		final StringBuilder sb=new StringBuilder();
		for(stmt s:ls)
			sb.append(s).append('.');
		sb.setLength(sb.length()-1);
		final type t=ls.peekLast().type();
		a.accessor_statement=new stmt(t,sb.toString());
		a.struct=find_struct(stmprv.type(),false);
		return a;
	}
	private static void check_validity_of_decinc(boolean postinc,boolean postdec,boolean preinc, boolean predec) {
		// TODO Auto-generated method stub
	}
	private static stmt wrap_variable_with_inc_dec(final stmt v,final boolean preinc,final boolean postinc,final boolean predec,final boolean postdec){
		stmt s=v;
		if(postinc)s=new vm.inc(s);
		if(preinc)s=new vm.incpre(s);
		if(predec)s=new vm.decpre(s);
		if(postdec)s=new vm.dec(s);
		return s;
	}
	private static String namespaces_and_declared_types_to_string(LinkedList<namespace>nms){
		final StringBuilder sb=new StringBuilder(256);
		StringBuilder indent=new StringBuilder("  ");
		for(namespace ns:nms){
			sb.append(indent).append(ns.name).append("{");
			if(!ns.vars.isEmpty()){
				for(var v:ns.vars.values()){
					sb.append(v.type());
					if(!v.type().name().equals(v.code))
						sb.append(" ").append(v.code);
					sb.append(',');
				}
				sb.setLength(sb.length()-1);
			}
			sb.append("}\n");
			indent.append("  ");
		}
//		sb.append(indent).append(System.getProperty("os.name"));
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
	private static final int state_1_in_class_name=0;
	private static final int state_2_in_class_block=1;
	private static final int state_3_in_function_arg=2;
	private static final int state_4_find_function_block=3;
	private static final int state_5_in_function_block=4;
	private static final int state_in_code_block=5;
	private static final int state_struct_member_default_value=6;
	private static final int state_in_string=7;
	private static final int state_in_line_comment=8;
	
	private static boolean is_char_block_open(char ch){return ch=='{';}
	private static boolean is_char_arguments_open(char ch){return ch=='(';}
	private static boolean is_char_arguments_close(char ch){return ch==')';}
	private static boolean is_char_block_close(char ch){return ch=='}';}
	private static boolean is_char_statement_close(char ch){return ch==';';}
	private static boolean is_white_space(char ch){return Character.isWhitespace(ch);}
	private static boolean is_char_is_newline(char ch){return ch=='\n';}
	private static boolean is_char_string_close(char ch) {return ch=='\"';}
	private static boolean is_char_string_open(char ch){return ch=='\"';}
	private static boolean is_char_statement_assigment(char ch){return ch=='=';}

	static boolean is_valid_class_identifier(String nm){return true;}
}