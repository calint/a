package a.cap;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
final class vm{
		static class stmt{
			String code;
			type t;
			type rht;
			stmt(String code){this.code=code;}
			stmt(type t,String code){this.code=code;this.t=t;}
//			stmt(Reader r)throws Throwable{}
			public String toString(){return code;}
			void to(final PrintWriter pw){pw.print(code);}
			String end_delim(){return";";}
			@Override public boolean equals(Object obj){
				if(obj==null)return false;
//				if(!(obj instanceof type))return false;
				return obj.toString().equals(code);
			}
			type type(){return t;}
			type type_at_right_edge(){return rht;}
		};
		static class call extends stmt{
			public call(String funcname,stmt...args){
				super(funcname+"("+args_to_string(args)+")");
			}
			public call(String funcname,stmt o,stmt...args){//fcall
				super(funcname+"("+args_to_string(o,args)+")");
			}
		};
		static String args_to_string(stmt...a){
			if(a.length==0)return"";
			if(a.length==1&&a[0]==null)return"";
			final StringBuilder sb=new StringBuilder();
			for(stmt s:a)sb.append(s.toString()).append(",");
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		private static String args_to_string(stmt o,stmt...a){// call to function with object context
			final StringBuilder sb=new StringBuilder();
			sb.append("&");
//			final boolean isexpr=!(o instanceof var);
//			if(isexpr)sb.append("(");
			sb.append(o.toString());
//			if(isexpr)sb.append(")");
			if(a.length==0)return sb.toString();
			if(a.length==1&&a[0]==null)return sb.toString();
			sb.append(",");
			for(stmt s:a)sb.append(s.toString()).append(",");
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		final static class let extends stmt{
//			public let(type t,var v){super(t+" "+v);}
			public let(type t,varx v,stmt s){
				super(t+" "+v+"="+s);
//				if(!v.t.equals(s.type()))throw new Error(" at yyyy:xx  tried '"+code+"'  but  '"+v+"' is '"+t+"' and '"+s+"' is '"+s.type()+"'     try '"+t+" "+v+"="+t+"("+s+")'");
			}
		};
		static class set extends stmt{
			public set(varx lh,stmt rh){
				super(lh+"="+rh);
//				if(!lh.t.equals(rh.type()))
//					throw new Error("at yyyy:xxx tried '"+code+"'  but  '"+lh.code+"' is '"+lh.t+"'  and  '"+rh.code+"' is '"+rh.type()+"'   try: '"+lh.code+"="+lh.type()+"("+rh.code+")'");
			}
		};
		final static class set_struct_member extends stmt{
			public set_struct_member(varx lh,String struct_member_name,stmt rh,toc tc){
				super(lh+"."+struct_member_name+"="+rh);
//				final type t=tc.find_struct_member_type_or_break(lh.type().toString(),struct_member_name);
//				if(!t.equals(rh.type()))
//					throw new Error("at yyyy:xxx tried '"+code+"'  but  '"+lh+"."+struct_member_name+"' is '"+t+"'  and  '"+rh.code+"' is '"+rh.type()+"'   try: '"+lh.code+"="+t+"("+rh.code+")'");
			}
		};
		static class value extends stmt{public value(String stmt){super(stmt);}};
		final static class struct_member extends value{
			private type t;
			public struct_member(varx v,String struct_member_name,toc tc){
				super(v+"."+struct_member_name);
				t=tc.find_struct_member_type(v.type().name(),struct_member_name,true);
			}
			@Override type type(){return t;}
		};
		static class varx extends stmt{
			private boolean sm;
			public varx(type t,String name){super(name);this.t=rht=t;}
			public varx(boolean struct_member,type slot_type,String slot_name){
				super(slot_name);
				this.t=slot_type;
				this.sm=struct_member;
			}
			final @Override public String toString(){
				return (sm?"o->":"")+code;
			}
//			final @Override type type(){return t;}
		};
//		final static class struct_member extends var{
//			public struct_member(type t,String name){super(t,"o->"+name);}
//		};
		static class op extends stmt{public op(String name,stmt lh,stmt rh){super(lh+name+rh);}};
		final static class add extends op{
			public add(stmt lh,stmt rh){
				super("+",lh,rh);
				if(!lh.type().equals(rh.type())){
					throw new Error("tried "+lh+"+"+rh+"   but   "+lh.code+" is "+lh.type()+"  and  "+rh.code+" is "+rh.type()+"   try '"+lh.code+"="+lh.type()+"("+code+")'");
				}
				t=lh.type();
			}
			@Override type type(){return t;}
			private type t;
		}
		final static class str extends value{
			public str(String v){
				super("\""+v+"\"");
			}
			public str(Reader r)throws Throwable{
				super(read_string(r));
			}
			private static String read_string(Reader r)throws Throwable{
				int ch=0,chp;
				final StringBuilder sb=new StringBuilder(128);
				sb.append("\"");
				while(true){
					chp=ch;
					ch=r.read();
					if(ch==-1)break;
					sb.append((char)ch);
					if(ch=='\"'&&chp!='\\'){
						final String s=sb.toString();
						return s;
					}	
				}
				throw new Error("did not find end of string in '"+sb+"'");
			}

			@Override type type(){return t;}
			final static type t=new type("str");
		};
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
//			if(a.length==1)return statements_to_string(a);
			return"{"+statements_to_string(a)+"}";
		}
		private static String block_to_string(List<stmt>a){
//			if(a.size()==1)return statements_to_string(a);
			return"{"+statements_to_string(a)+"}";
		}
		final static class inti extends value{
			public inti(int i){super(Integer.toString(i));}
			public inti(int i,boolean hex){super("0x"+Integer.toString(i,16));}
			@Override type type(){return t;}
			final static type t=new type("int");
		};
		static class type extends stmt{
			public type(String name){super(name);t=this;}
			final String name(){return code;}
		};
//		final static class integer extends type{public integer(){super("int");}};
//		final static class floating extends type{public floating(){super("float");}};
		final static class printf extends call{public printf(stmt...s){super("printf",s);}};
		final static class iff extends stmt{
			public iff(stmt condition,stmt if_true){super("if("+condition+")"+if_true+if_true.end_delim());}
			@Override String end_delim(){return "";}
		};
		final static class ife extends stmt{
			public ife(stmt condition,stmt if_true,stmt els){super("if("+condition+")"+if_true+if_true.end_delim()+"else "+els+els.end_delim());}
			@Override String end_delim(){return "";}
		};
		final static class ifi extends stmt{
			public ifi(stmt diff,stmt ifn,stmt ifp,stmt ifz){super("/todo");}
			@Override String end_delim(){return "";}
		};
		final static class eq extends op{public eq(stmt lh,stmt rh){super("==",lh,rh);}}
		final static class brk extends stmt{public brk(){super("break");}};
		final static class cont extends stmt{public cont(){super("continue");}};
		final static class block extends stmt{
			block(stmt...ss){super(block_to_string(ss));}
			block(List<stmt>ss){super(block_to_string(ss));}
			block(Reader r){super(parse_to_string(r));}
			private static String parse_to_string(Reader r){
				new Throwable().printStackTrace();
				return null;
			}
			String end_delim(){return"";}
		}
		final static class incn extends op{public incn(varx v,stmt rh){super("+=",v,rh);}}
		final static class decn extends op{public decn(stmt v,stmt rh){super("-=",v,rh);}}
		final static class inc extends stmt{public inc(stmt v){super(v+"++");}}
		final static class incpre extends stmt{public incpre(stmt v){super("++"+v);}}
		final static class dec extends stmt{public dec(stmt v){super(v+"--");}}
		final static class decpre extends stmt{public decpre(stmt v){super("--"+v);}}
		final static class fcall extends call{
			public fcall(varx o,String funcname,stmt...args){super(o.t+"_"+funcname,o,args);}
			public fcall(varx o,struct s,String funcname,stmt...args){super(s.name+"_"+funcname,o,args);}
			public fcall(stmt o,struct s,type return_type,String funcname,stmt...args){
				super(s.name+"_"+funcname,o,args);
				t=rht=return_type;
			}
		}
		final static class floati extends value{
			public floati(float f){super(Float.toString(f)+"f");}
			@Override type type(){return t;}
			final static type t=new type("float");
		};
		final static class ctor extends value{
			public ctor(type t){super(t.code+"_default");this.t=t;}
			@Override type type(){return t;}
			private type t;
		}
		final static class voidi extends value{
			public voidi(){super("");}
			@Override type type(){return t;}
			final static type t=new type("void");
		};

		public static void main(String[] args){
				//final ArrayList<stmt>stms=new ArrayList<>();
				final type integer=new type("int");
		//		final type floating=new type("float");
				final type file=new type("file");
				
				final varx a=new varx(integer,"a");
				final varx b=new varx(integer,"b");//change type for type missmatch error
				final varx f=new varx(file,"f");
				final varx d=new varx(file,"d");
		//		final var e=new var(floating,"e");
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
		//	System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);;

//		final static class t_null extends type{public t_null(){super("null");}};
	}