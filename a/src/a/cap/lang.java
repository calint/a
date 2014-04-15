package a.cap;
import java.io.PrintWriter;
import java.io.Reader;
final class lang{
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
			lang.type type(){return null;}
			
			static lang.stmt read_code_block(Reader r)throws Throwable{
				// find let/set/loop/call/ret/const
				return null;
			}
		};
		static class call extends lang.stmt{public call(String funcname,lang.stmt...args){super(funcname+"("+args_to_string(args)+")");}};
		private static String args_to_string(lang.stmt...a){
			if(a.length==0)return"";
			final StringBuilder sb=new StringBuilder();
			for(lang.stmt s:a)sb.append(s.toString()).append(",");
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		final static class let extends lang.stmt{
			public let(lang.type t,lang.var v){super(t+" "+v);}
			public let(lang.type t,lang.var v,lang.stmt s){
				super(t+" "+v+"="+s);
//				if(!s.t.equals(v.t))throw new Error();
			}
		};
		final static class set extends lang.stmt{
			public set(lang.var lh,lang.stmt rh){
				super(lh+"="+rh);
				if(!lh.t.equals(rh.type()))throw new Error("at yyyy:xxx tried "+code+"  but  "+lh.code+" is "+lh.t+"  and  "+rh.code+" is "+rh.type()+"   try: "+lh.code+"="+lh.type()+"("+rh.code+")");
			}
		};
		static class value extends lang.stmt{public value(String stmt){super(stmt);}};
		final static class var extends lang.stmt{
			private lang.type t;
			public var(String name){super(name);}
			public var(lang.type t,String name){super(name);this.t=t;}
			@Override lang.type type(){return t;}
		};
		static class op extends lang.stmt{public op(String name,lang.stmt lh,lang.stmt rh){super(lh+name+rh);}};
		final static class add extends lang.op{
			public add(lang.stmt lh,lang.stmt rh){
				super("+",lh,rh);
				if(!lh.type().equals(rh.type())){
					throw new Error("at yyyy:xxx tried "+lh+"+"+rh+"   but   "+lh.code+" is "+lh.type()+"  and  "+rh.code+" is "+rh.type()+"   try: "+lh.code+"="+lh.type()+"("+code+")");
				}
				t=lh.type();
			}
			@Override lang.type type(){return t;}
			private lang.type t;
		}
		final static class str extends lang.value{public str(String v){super("\""+v+"\"");}};
		final static class ret extends lang.stmt{public ret(lang.stmt s){super("return "+s);}};
		final static class loop extends lang.stmt{
			public loop(lang.block b){super("while(true)"+b);}
			@Override String end_delim(){return "";}
		};
		private static String statements_to_string(lang.stmt...a){
			if(a.length==0)return"";
			final StringBuilder sb=new StringBuilder();
			for(lang.stmt s:a){
				sb.append(s.toString());
				sb.append(s.end_delim());
			}
	//		sb.setLength(sb.length()-1);
			return sb.toString();
		}
		private static String block_to_string(lang.stmt...a){
			if(a.length==1)return statements_to_string(a);
			return"{"+statements_to_string(a)+"}";
		}
		final static class inti extends lang.value{
			public inti(int i){
				super(Integer.toString(i));
			}
			@Override lang.type type(){return new type("int");}
		};
		static class type extends lang.stmt{public type(String name){super(name);}};
		final static class integer extends lang.type{public integer(){super("int");}};
		final static class floating extends lang.type{public floating(){super("float");}};
		final static class printf extends lang.call{public printf(lang.stmt...s){super("printf",s);}};
		final static class iff extends lang.stmt{
			public iff(lang.stmt s,lang.block b){
				super("if("+s+")"+b);
			}
			@Override String end_delim(){return "";}
		};
		final static class ife extends lang.stmt{
			public ife(lang.stmt s,lang.block b,lang.stmt els){
				super("if("+s+")"+b+"else "+els);
			}
			@Override String end_delim(){return "";}
		};
		final static class eq extends lang.op{public eq(lang.stmt lh,lang.stmt rh){super("==",lh,rh);}}
		final static class brk extends lang.stmt{public brk(){super("break");}};
		final static class cont extends lang.stmt{public cont(){super("continue");}};
		final static class block extends lang.stmt{block(lang.stmt...ss){super(block_to_string(ss));}}
		final static class incn extends lang.op{
			public incn(lang.var v,lang.stmt rh){super("+=",v,rh);}
		}
		final static class decn extends lang.op{public decn(lang.var v,lang.stmt rh){super("-=",v,rh);}}
		final static class inc extends lang.stmt{public inc(lang.var v){super(v+"++");}}
		final static class incpre extends lang.stmt{public incpre(lang.var v){super("++"+v);}}
		final static class dec extends lang.stmt{public dec(lang.var v){super(v+"--");}}
		final static class decpre extends lang.stmt{public decpre(lang.var v){super("--"+v);}}
		final static class fcall extends lang.call{public fcall(lang.var o,String funcname,lang.stmt...args){super(o+"."+funcname,args);}}
//		final static class t_null extends type{public t_null(){super("null");}};
	}