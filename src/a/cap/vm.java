package a.cap;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import a.cap.toc.namespace;
final class vm{
		static class stmt{
			String code;
			stmt(String code){this.code=code;}
			stmt(Reader r)throws Throwable{}
			public String toString(){return code;}
			void to(final PrintWriter pw){pw.print(code);}
			String end_delim(){return";";}
			@Override public boolean equals(Object obj){
				if(obj==null)return false;
				return obj.toString().equals(code);
			}
			type type(){return null;}
			
			static stmt parse_function_source(Reader r,LinkedList<namespace>ns)throws Throwable{
				// expect let/set/loop/call/ret/const until end of stream
				LinkedList<stmt>stms=new LinkedList<>();
//				int ch=0;
//				final StringBuilder sb=new StringBuilder(128);
				while(true){
					final stmt s=parse_statement(r);
					if(s!=null){
						stms.add(s);
						continue;
					}
					break;
//					
//					ch=r.read();
//					if(ch==-1)break;
//					if(sb.length()==0&&Character.isWhitespace(ch))continue;
//					
//					if(ch=='('){// call
//						final String funcname=sb.toString();
//						stms.add(new call(funcname,parse_statement(r)));
//						continue;
//					}
//					if(ch=='='){// let or set
//						continue;
//					}
//					
//					sb.append((char)ch);
//					System.out.print((char)ch);
				}
				System.out.println();
				return new block(stms);
			}
			static stmt parse_statement(Reader r)throws Throwable{
				int ch=0;
				final StringBuilder sb=new StringBuilder(128);
				while(true){
					ch=r.read();
					if(ch==-1)break;
					if(sb.length()==0&&Character.isWhitespace(ch))continue;
					
					if(ch=='('){// call
						final String funcname=sb.toString();
						return new call(funcname,parse_function_arguments(r));
//						continue;
					}
					if(ch=='='){// let or set
						continue;
					}
					
					sb.append((char)ch);
//					System.out.print((char)ch);
				}
				final String s=sb.toString();
				if(s.startsWith("\"")&&s.endsWith("\"")){// string
					final String t=s.substring(1,s.length()-1);
					return new str(t);
				}
				// const number or variable
				try{return new inti(Integer.parseInt(s));}
				catch(Throwable ok){return new var(s);}
			}
			static stmt[]parse_function_arguments(Reader r)throws Throwable{
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
						final stmt arg=parse_statement(rc);
						args.add(arg);
//						System.out.println(arg);
						sb.setLength(0);
						continue;
					}
					if(ch==')'){
						// found end of arguments
//						System.out.println(sb);
						final String code=sb.toString();
						final Reader rc=new StringReader(code);
						final stmt arg=parse_statement(rc);
						args.add(arg);
						sb.setLength(0);
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
		};
		static class call extends stmt{public call(String funcname,stmt...args){super(funcname+"("+args_to_string(args)+")");}};
		private static String args_to_string(stmt...a){
			if(a.length==0)return"";
			final StringBuilder sb=new StringBuilder();
			for(stmt s:a)sb.append(s.toString()).append(",");
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		final static class let extends stmt{
//			public let(type t,var v){super(t+" "+v);}
			public let(type t,var v,stmt s){
				super(t+" "+v+"="+s);
//				if(!s.t.equals(v.t))throw new Error();
			}
		};
		final static class set extends stmt{
			public set(var lh,stmt rh){
				super(lh+"="+rh);
				if(!lh.t.equals(rh.type()))throw new Error("at yyyy:xxx tried "+code+"  but  "+lh.code+" is "+lh.t+"  and  "+rh.code+" is "+rh.type()+"   try: "+lh.code+"="+lh.type()+"("+rh.code+")");
			}
		};
		static class value extends stmt{public value(String stmt){super(stmt);}};
		final static class var extends stmt{
			private type t;
			public var(String name){super(name);}
			public var(type t,String name){super(name);this.t=t;}
			@Override type type(){return t;}
		};
		static class op extends stmt{public op(String name,stmt lh,stmt rh){super(lh+name+rh);}};
		final static class add extends op{
			public add(stmt lh,stmt rh){
				super("+",lh,rh);
				if(!lh.type().equals(rh.type())){
					throw new Error("at yyyy:xxx tried "+lh+"+"+rh+"   but   "+lh.code+" is "+lh.type()+"  and  "+rh.code+" is "+rh.type()+"   try: "+lh.code+"="+lh.type()+"("+code+")");
				}
				t=lh.type();
			}
			@Override type type(){return t;}
			private type t;
		}
		final static class str extends value{public str(String v){super("\""+v+"\"");}};
		final static class ret extends stmt{public ret(stmt s){super("return "+s);}};
		final static class loop extends stmt{
			public loop(stmt b){super("while(true)"+b+b.end_delim());}
			public loop(Reader r){super("while(true)"+new block(r));}
			@Override String end_delim(){return "";}
			
		};
		private static String statements_to_string(stmt...a){
			if(a.length==0)return"";
			final StringBuilder sb=new StringBuilder();
			for(stmt s:a){
				sb.append(s.toString());
				sb.append(s.end_delim());
			}
			return sb.toString();
		}
		private static String statements_to_string(List<stmt>a){
			if(a.size()==0)return"";
			final StringBuilder sb=new StringBuilder();
			for(stmt s:a){
				sb.append(s.toString());
				sb.append(s.end_delim());
			}
			return sb.toString();
		}
		private static String block_to_string(stmt...a){
			if(a.length==1)return statements_to_string(a);
			return"{"+statements_to_string(a)+"}";
		}
		private static String block_to_string(List<stmt>a){
			if(a.size()==1)return statements_to_string(a);
			return"{"+statements_to_string(a)+"}";
		}
		final static class inti extends value{
			public inti(int i){
				super(Integer.toString(i));
			}
			@Override type type(){return new type("int");}
		};
		static class type extends stmt{public type(String name){super(name);}};
		final static class integer extends type{public integer(){super("int");}};
		final static class floating extends type{public floating(){super("float");}};
		final static class printf extends call{public printf(stmt...s){super("printf",s);}};
		final static class iff extends stmt{
			public iff(stmt condition,stmt if_true){
				super("if("+condition+")"+if_true+if_true.end_delim());
			}
			@Override String end_delim(){return "";}
		};
		final static class ife extends stmt{
			public ife(stmt condition,stmt if_true,stmt els){
				super("if("+condition+")"+if_true+if_true.end_delim()+"else "+els+els.end_delim());
			}
			@Override String end_delim(){return "";}
		};
		final static class ifi extends stmt{
			public ifi(stmt diff,stmt ifn,stmt ifp,stmt ifz){
				super("/todo");
			}
			@Override String end_delim(){return "";}
		};
		final static class eq extends op{public eq(stmt lh,stmt rh){super("==",lh,rh);}}
		final static class brk extends stmt{public brk(){super("break");}};
		final static class cont extends stmt{public cont(){super("continue");}};
		final static class block extends stmt{
			block(stmt...ss){
				super(block_to_string(ss));
			}
			block(List<stmt>ss){
				super(block_to_string(ss));
			}
			block(Reader r){
				super(parse_to_string(r));
			}
			private static String parse_to_string(Reader r){
				new Throwable().printStackTrace();
				return null;
			}
			String end_delim(){return"";}
		}
		final static class incn extends op{
			public incn(var v,stmt rh){super("+=",v,rh);}
		}
		final static class decn extends op{public decn(var v,stmt rh){super("-=",v,rh);}}
		final static class inc extends stmt{public inc(var v){super(v+"++");}}
		final static class incpre extends stmt{public incpre(var v){super("++"+v);}}
		final static class dec extends stmt{public dec(var v){super(v+"--");}}
		final static class decpre extends stmt{public decpre(var v){super("--"+v);}}
		final static class fcall extends call{public fcall(var o,String funcname,stmt...args){super(o+"."+funcname,args);}}
//		final static class t_null extends type{public t_null(){super("null");}};
	}