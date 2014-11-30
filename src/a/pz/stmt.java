package a.pz;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import b.xwriter;

public abstract class stmt implements Serializable{
	final public static class add extends stmt.instr{
		final public static int op=0x00a0;
		public add(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line());
			txt=new xwriter().p("add").spc().p(ra).spc().p(rd).toString();
		}
		private static final long serialVersionUID=1;
	}
	public static class instr extends stmt{
		int znxr;
		int op;
		String ra;
		String rd;
		int rai;
		int rdi;
		//		int imm;
		public instr(program p){
			super(p);
		}
		public instr(final program p,final int znxr,final int op,final String rd){
			this(p,znxr,op,null,rd);
		}
		public instr(final program p,final int znxr,final int op,final String ra,final String rd){
			super(p);
			this.znxr=znxr;
			this.op=op;
			this.ra=ra;
			this.rd=rd;
			rai=ra==null?0:p.register_index_from_string(ra);
			rdi=rd==null?0:p.register_index_from_string(rd);
		}
		public instr(final program p,final int znxr,final int op,final String ra,final String rd,boolean fliprdra){
			this(p,znxr,op,ra,rd);
			if(fliprdra){
				final int i=rai;
				rai=rdi;
				rdi=i;
			}
		}
		void mkstr(){
			final xwriter x=new xwriter();
			if((znxr&3)==3)
				x.p("ifp ");
			else if((znxr&1)==1)
				x.p("ifz ");
			else if((znxr&2)==2)
				x.p("ifn ");
			x.p(txt);
			if((znxr&4)==4)
				x.p(" nxt");
			if((znxr&8)==8)
				x.p(" ret");
			txt=x.toString();
		}
		//		public instr(final program r,final int op,final int ra,final int rd){
		//			super(r,op,ra,rd);
		//		}
		//		public instr(final program r,final int op,final int ra,final int rd,boolean flip_ra_rd){
		//			super(r,op,ra,rd,flip_ra_rd);
		//		}
		protected int znxr_ci__ra__rd__(){
			return znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		}
		protected void compile(program r){
			bin=new int[]{znxr_ci__ra__rd__()};
		}
		private static final long serialVersionUID=1;
	}
	final static public class def_const extends def{
		public String value;
		public def_const(final program r) throws IOException{
			super(r);
			type=r.next_token_in_line();
			final def_type t=r.typedefs.get(type);
			if(t==null)
				throw new compiler_error(this,"type not found",type);
			name=r.next_token_in_line();
			final def_const d=r.defines.get(name);
			if(d!=null)
				throw new compiler_error(this,"define '"+name+"' already declared at "+d.location_in_source);
			if(!r.is_next_char_equals())
				throw new compiler_error(this,"expected format:  const int a=1");
			value=r.consume_rest_of_line();
			txt="const "+type+" "+name+"="+value;
		}
		public int toInt(program p){
			return Integer.parseInt(value,16);
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_var extends expr{
		public String rh;
		public expr_var(final program p) throws IOException{
			super(p,p.next_token_in_line());
			p.allocate_register(this,to_register);
			if(p.is_next_char_equals())
				rh=p.next_token_in_line();
			if(!p.is_next_char_end_of_line())
				throw new compiler_error(this,"expected end of line");
			final xwriter x=new xwriter().p("var").spc().p(to_register);
			if(rh!=null)
				x.p("=").p(rh.toString());
			txt=x.toString();
			if(rh==null)
				return;
			final def_const dc=p.defines.get(rh);
			if(dc!=null){
				type=dc.type;
				return;
			}
		}
		@Override protected void compile(program p){
			if(rh==null)
				return;
			final def_const dc=p.defines.get(rh);
			if(dc!=null){
				final program p2=new program(p,"li "+to_register+" 0");
				p2.build();
				final stmt s=p2.statements.get(0);
				bin=s.bin;
				//type="int&"
				return;
			}
			if(rh.startsWith("&")){// li
				final program p2=new program(p,"li "+to_register+" 0");
				p2.build();
				final stmt s=p2.statements.get(0);
				bin=s.bin;
				//type="int&"
				return;
			}
			if(program.is_reference_to_register(rh)){// tx
				if(!p.is_register_allocated(rh))
					throw new compiler_error(this,"var not declared",rh);
				//? checktypes
				final program p2=new program("tx "+to_register+" "+rh);
				p2.build();
				final stmt s=p2.statements.get(0);
				bin=s.bin;
				//type=p.register(default_value).type
				return;
			}
			// constant
			final li l=new li(p,to_register,constexpr.from(p,rh));
			l.compile(p);
			l.link(p);
			bin=l.bin;
		}
		@Override protected void link(program p){
			if(rh==null)
				return;

			final def_const dc=p.defines.get(rh);
			if(dc!=null){
				final constexpr ce=constexpr.from(p,dc.value);
				final int i=ce.calc(p);
				bin[1]=i;
				return;
			}
			final constexpr ce=constexpr.from(p,rh);
			final int i=ce.calc(p);
			bin[1]=i;
			return;
			
//			if(rh.startsWith("&")){// li
//				final String nm=rh.substring(1);
//				final def_label lb=p.labels.get(nm);
//				if(lb==null)
//					throw new compiler_error(this,"label not found",nm);
//				bin[1]=lb.location_in_binary;
//				return;
//			}
		}
		private static final long serialVersionUID=1;
	}
	abstract public static class expr extends stmt{
		String to_register;
		public expr(program p,String to_register){
			super(p);
			this.to_register=to_register;
		}
		private static final long serialVersionUID=1;
	}
//	public final static class expr_plus extends expr{
//		public expr_plus(program p,String lhs,String to_register){
//			super(p,to_register);
//		}
//		private static final long serialVersionUID=1;
//	}
	abstract public static class constexpr extends stmt{
		static constexpr from(program p,String expr){
			// &dots
			if(expr.startsWith("&")){
				final def_label dl=p.labels.get(expr.substring(1));
				final int i=dl.location_in_binary;
				return new constexpr_int(p,i);
			}
				
			// linewi
			final def_const dc=p.defines.get(expr);
			if(dc!=null)
				return new constexpr_int(p,dc.toInt(p));
			
			//linewi-wi
			final int i1=expr.indexOf('-');
			if(i1!=-1)
				return new constexpr_minus(p,expr.substring(0,i1),expr.substring(i1+1));
				
			//wi+1
			final int i2=expr.indexOf('+');
			if(i2!=-1)
				return new constexpr_plus(p,expr.substring(0,i2),expr.substring(i2+1));
				
			try{return new constexpr_int(p,Integer.parseInt(expr,16));}catch(Throwable t){throw new compiler_error("","not a hex: "+expr);}
		}
		private String expr;
		public constexpr(final program p){super(p);}
		abstract public int calc(final program p);
		public constexpr(program p,String expr){
			super(p);this.expr=expr;
//			if(expr.startsWith("&")){
//			}
//			boolean is_int=true;
//			try{expr_int=Integer.parseInt(expr);}catch(Throwable t){is_int=false;}
//			if(is_int)
//				txt=
		}
		public String toString(){
			return expr;
		}
		private static final long serialVersionUID=1;
	}
	public final static class constexpr_minus extends constexpr{
		private String lhs,rhs;
		public constexpr_minus(program p,String lhs,String rhs){
			super(p);this.lhs=lhs;this.rhs=rhs;
		}
		@Override public int calc(program p){
			final constexpr lh=from(p,lhs);
			final constexpr rh=from(p,rhs);
			final int lhi=lh.calc(p);
			final int rhi=rh.calc(p);
			return lhi-rhi;//? bug const int skp=linewi-wi-1-1
		}
		private static final long serialVersionUID=1;
	}
	public final static class constexpr_plus extends constexpr{
		private String lhs,rhs;
		public constexpr_plus(program p,String lhs,String rhs){
			super(p);this.lhs=lhs;this.rhs=rhs;
		}
		@Override public int calc(program p){
			final constexpr lh=from(p,lhs);
			final constexpr rh=from(p,rhs);
			final int lhi=lh.calc(p);
			final int rhi=rh.calc(p);
			return lhi+rhi;
		}
		private static final long serialVersionUID=1;
	}
	public final static class constexpr_int extends constexpr{
		private int i;
		public constexpr_int(program p,int i){
			super(p);this.i=i;
		}
		@Override public int calc(program p){
			return i;
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_assign extends expr{
//		stmt rhs;
		String rh;
		boolean is_ld;
		boolean is_ldc;
		public expr_assign(final program p,final String register) throws IOException{
			super(p,register);
			if(!p.is_register_allocated(register))
				throw new compiler_error(this,"var '"+register+"' has not been declared");
			if(p.is_next_char_star()){// d=*a
				rh=p.next_token_in_line();
				if(p.is_next_char_plus()){
					if(!p.is_next_char_plus())
						throw new compiler_error(this,"expected format *d=a++");
					is_ldc=true;
				}else
					is_ld=true;
				txt=new xwriter().p(register).p("=*").p(rh).p(is_ldc?"++":"").toString();
				return;
			}
			rh=p.next_token_in_line();
			txt=new xwriter().p(register).p("=").p(rh.toString()).toString();
//			final def_const c=p.defines.get(rh);
//			if(c!=null){//li
//				rhs=c;
//				return;
//			}
		}
		@Override protected void compile(program p){
			if(is_ld){
				final instr s=new instr(p,0,ld.op,rh,to_register);
				s.compile(p);
				bin=s.bin;
				return;
			}
			if(is_ldc){
				final instr s=new instr(p,0,ldc.op,rh,to_register);
				s.compile(p);
				bin=s.bin;
				return;
			}
//			if(rhs instanceof def_const){
//				final instr s=new instr(p,0,li.op,null,to_register);
//				s.compile(p);
//				final int i=new constexpr(p,((def_const)rhs).value).calc(p);
//				bin=new int[]{s.bin[0],i};
//				return;
//			}
			if(program.is_reference_to_register(rh)){
				final instr s=new instr(p,0,tx.op,to_register,rh);
				s.compile(p);
				bin=s.bin;
				return;
			}
			if(rh.startsWith("&")){
				final instr s=new instr(p,0,li.op,null,to_register);
				s.compile(p);
				bin=new int[]{s.bin[0],0};
				return;
			}
			// const
			final li s=new li(p,to_register,constexpr.from(p,rh));
			s.compile(p);
			bin=s.bin;
			return;
		}
		@Override protected void link(program p){
			if(rh.startsWith("&")){
				final String label_name=rh.substring(1);
				final def_label lbl=p.labels.get(label_name);
				if(lbl==null)
					throw new compiler_error(this,"label not found "+lbl);
				bin[1]=lbl.location_in_binary;
				return;
			}
			if(is_ld||is_ldc)
				return;
			final int i=constexpr.from(p,rh).calc(p);
			bin[1]=i;
		}
		private static final long serialVersionUID=1;
	}
	final static public class def_struct extends def{
		public String name;
		private List<def_struct_member> fields;
		public def_struct(final program p) throws IOException{
			super(p);
			name=p.next_identifier();
			fields=new ArrayList<>();
			while(true){
				p.skip_whitespace_on_same_line();
				if(p.is_next_char_end_of_line())
					break;
				final def_struct_member f=new def_struct_member(p);
				fields.add(f);
			}
			final xwriter x=new xwriter();
			x.p("struct").spc().p(name);
			fields.forEach(f->x.spc().p(f.toString()));
			txt=x.toString();
		}
		@Override protected void validate_references_to_labels(program p){
			fields.forEach(e->e.validate_references_to_labels(p));
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_struct_member extends def{
		private String type;
		private String name;
		private String default_value;
		public def_struct_member(program p) throws IOException{
			super(p);
			name=p.next_identifier();
			type=p.next_type_identifier();
			default_value=p.next_token_in_line();
			final xwriter x=new xwriter();
			x.p(name).spc().p(type).spc().p(default_value);
			txt=x.toString();
		}
		@Override protected void validate_references_to_labels(program p) throws compiler_error{
			if(!p.typedefs.containsKey(type))
				throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
		}
		private static final long serialVersionUID=1;
	}
	final static public class def_type extends def{
		//		public String name;
		public def_type(final program r) throws IOException{
			super(r);
			name=r.next_identifier();
			txt=new xwriter().p("typedef").spc().p(name).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public static class li extends instr{
		private String data;
		private int value;
		private constexpr ce;
		final public static int op=0x0000;
		public li(program r) throws IOException{
			super(r,0,li.op,null,r.next_token_in_line());
			data=r.next_token_in_line();
			txt="li "+rd+" "+data;
		}
//		public li(program r,String reg,String data){
//			super(r,0,li.op,null,reg);
//			this.data=data;
//			txt="li "+reg+" "+data;
//		}
		public li(program r,String reg,constexpr ce){
			super(r,0,li.op,null,reg);
			this.ce=ce;
			txt="li "+reg+" "+ce;
		}
		//		private boolean is_integer(){try{Integer.parseInt(data);return true;}catch(Throwable t){return false;}}
		@Override protected void compile(program p){
			bin=new int[]{znxr_ci__ra__rd__(),0};
		}
		@Override protected void link(program p){
			if(ce!=null){
				final int i=ce.calc(p);
				bin[1]=i;
				return;
			}
			final def_const def=p.defines.get(data);
			if(def!=null){
				data=def.value;
			}
			if(data.startsWith("&")){
				final String nm=data.substring(1);
				final def_label l=p.labels.get(nm);
				if(l==null)
					throw new compiler_error(this,"label not found",nm);
				value=l.location_in_binary;
			}else{
				try{
					value=Integer.parseInt(data,16);
				}catch(NumberFormatException e){
					throw new compiler_error(this,"cannot parse number '"+data+"'");
				}
			}
			final int bit_width=16;
			//			final int i=Integer.parseInt(data,16);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(value>max)
				throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			if(value<min)
				throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			bin=new int[]{bin[0],value};
		}
		private static final long serialVersionUID=1;
	}
	final public static class inc extends instr{
		final public static int op=0x0200;
		public inc(program r) throws IOException{
			super(r,0,op,null,r.next_token_in_line());
			txt=new xwriter().p("inc").spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public static class st extends instr{
		final public static int op=0x00d8;
		public st(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line());
			txt=new xwriter().p("st").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public static class eof extends instr{
		public eof(program r) throws IOException{
			super(r);
			txt="..";
		}
		@Override protected void compile(program p){
			bin=new int[]{-1};
		}
		private static final long serialVersionUID=1;
	}
	final public static class nxt extends instr{
		final public static int op=0x0004;
		public nxt(program r) throws IOException{
			super(r,0,nxt.op,null,null);
			txt="nxt";
		}
		private static final long serialVersionUID=1;
	}
	final public static class ret extends instr{
		final public static int op=0x0008;
		public ret(program r) throws IOException{
			super(r,0,ret.op,null,null);
			txt="ret";
		}
		private static final long serialVersionUID=1;
	}
	final public static class lp extends instr{
		final public static int op=0x0100;
		public lp(program r) throws IOException{
			super(r,0,lp.op,null,r.next_token_in_line());
			txt=new xwriter().p("lp").spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public static class stc extends instr{
		final public static int op=0x0040;
		public stc(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line());
			txt=new xwriter().p("stc").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public static class sub extends instr{
		final public static int op=0x0020;
		public sub(program r) throws IOException{
			super(r,0,sub.op,r.next_token_in_line(),r.next_token_in_line());
		}
		private static final long serialVersionUID=1;
	}
	final public static class call extends instr{
		String label;
		final public static int op=0x0010;
		public call(program r) throws IOException{
			super(r,0,op,null,null);
			label=r.next_token_in_line();
			txt="call "+label;
		}
		@Override protected void link(program p){
			def_label l=p.labels.get(label);
			if(l==null)
				throw new compiler_error(this,"label not found",label);
			final int a=l.location_in_binary;
			bin[0]|=(a<<6);
		}
		private static final long serialVersionUID=1;
	}
	final public static class data extends stmt{
		private List<String> data;
		public data(program r) throws IOException{
			super(r);
			data=new ArrayList<>();
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)
					break;
				data.add(t);
			}
			final xwriter x=new xwriter().p(". ");
			data.forEach(e->x.spc().p(e));
			txt=x.toString();
		}
		@Override protected void compile(program p){
			bin=new int[data.size()];
			int i=0;
			for(final String s:data){
				bin[i++]=Integer.parseInt(s,16);
			}
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_label extends def{
		public String name;
		public def_label(program p,String nm){
			super(p);
			this.name=nm;
			final def_label d=p.labels.get(name);
			if(d!=null)
				throw new compiler_error(this,"label '"+name+"' already declared at "+d.location_in_source);
			txt=":"+nm;
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_comment extends def{
		public String line;
		public def_comment(program p) throws IOException{
			super(p);
			line=p.consume_rest_of_line();
			txt="//"+line;
		}
		private static final long serialVersionUID=1;
	}
	final public static class ld extends instr{
		final public static int op=0x00f8;
		public ld(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
		}
		private static final long serialVersionUID=1;
	}
	final public static class ldc extends instr{
		final public static int op=0x00c0;
		public ldc(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
			txt=new xwriter().p("ldc").spc().p((char)(rdi+'a')).spc().p((char)(rai+'a')).toString();
		}
		private static final long serialVersionUID=1;
	}
	final public static class tx extends instr{
		final public static int op=0x00e0;
		public tx(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
		}
		private static final long serialVersionUID=1;
	}
	final public static class shf extends instr{
		final public static int op=0x0060;
		public shf(program r) throws IOException{
			super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
		}
		private static final long serialVersionUID=1;
	}
	final public static class data_int extends stmt{
		public String name,type,default_value;
		public data_int(String name,String type,program p) throws IOException{
			super(p);
			if(p.is_next_char_equals())//default value
				default_value=p.next_token_in_line();
			final xwriter x=new xwriter().p(type).spc().p(name);
			if(default_value!=null)
				x.p("=").p(default_value);
			txt=x.toString();
			//			r.skip_whitespace_on_same_line();
			if(!p.is_next_char_end_of_line())
				throw new compiler_error(this,"expected end of line after: ["+txt+"]");
		}
		@Override protected void compile(program p){
			final int d=Integer.parseInt(default_value==null?"0":default_value,16);
			bin=new int[]{d};
		}
		private static final long serialVersionUID=1;
	}
	final public static class compiler_error extends RuntimeException{
		public String source_location;
		public String message;
		public compiler_error(stmt s,String message){
			this(s.location_in_source,message);
		}
		public compiler_error(stmt s,String msg,String offender){
			this(s.location_in_source,msg+": "+offender);
		}
		public compiler_error(String source_location,String message){
			super(source_location+" "+message);
			this.source_location=source_location;
			this.message=message;
		}
		@Override public String toString(){
			return new xwriter().p("line ").p(source_location).spc().p(message).toString();
		}
		public int source_location_line(){
			return Integer.parseInt(source_location.split(":")[0]);
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_increment extends expr{
		public expr_increment(final program p,final String register) throws IOException{
			super(p,register);
			txt=new xwriter().p(register).p("++").toString();
		}
		@Override protected void compile(program p){
			final instr s=new instr(p,0,inc.op,null,to_register);
			s.compile(p);
			bin=s.bin;
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_add extends expr{
		public String rhs;
		public expr_add(final program p,final String register) throws IOException{
			super(p,register);
			rhs=p.next_token_in_line();
			txt=new xwriter().p(register).p("+=").p(rhs).toString();
		}
		@Override protected void compile(program p){
			final instr s=new instr(p,0,add.op,to_register,rhs);
			s.compile(p);
			bin=s.bin;
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_store extends expr{
		public String rhs;
		public boolean inca;
		public expr_store(final program p) throws IOException{
			super(p,p.next_token_in_line());
			if(p.is_next_char_plus()){
				if(!p.is_next_char_plus())
					throw new compiler_error(this,"expected format *a++=d");
				inca=true;
				p.skip_whitespace_on_same_line();
				if(!p.is_next_char_equals())
					throw new compiler_error(this,"expected format *a++=d");
				rhs=p.next_token_in_line();
				txt=new xwriter().p("*").p(to_register).p("++=").p(rhs).toString();
				return;
			}
			if(!p.is_next_char_equals())
				throw new compiler_error(this,"expected '='");
			rhs=p.next_token_in_line();
			txt=new xwriter().p("*").p(to_register).p("=").p(rhs).toString();
		}
		@Override protected void compile(program p){
			//? ensure lhs,rhs are registers
			//			final expr lhse=expr.make_from_source_text(p,register);
			final instr s=new instr(p,0,inca?stc.op:st.op,to_register,rhs);
			s.compile(p);
			bin=s.bin;
		}
		private static final long serialVersionUID=1;
	}
	abstract public static class def extends stmt{
		String name,type;
		public def(program p){
			super(p);
		}
		public def(program p,String type,String name){
			super(p);
			this.type=type;
			this.name=name;
		}
		@Override protected void compile(program p){}
		@Override protected void link(program p){}
		private static final long serialVersionUID=1;
	}
	final public static class def_func extends def{
		public List<def_func_arg> args=new ArrayList<>();
		public program code_block;
		public def_func(String name,String return_type,program p) throws IOException{
			super(p,return_type,name);
			if(!p.is_next_char_paranthesis_right()){
				while(true){
					final def_func_arg a=new def_func_arg(p);
					args.add(a);
					if(p.is_next_char_paranthesis_right())
						break;
					if(!p.is_next_char_comma())
						throw new compiler_error(this,"expected ',' after function argument definition");
				}
			}
			p.skip_whitespace_on_same_line();
			if(p.is_next_char_mustache_left()){
				code_block=new program(p);
				p.line_number=code_block.line_number;
				p.character_number_in_line=code_block.character_number_in_line;
			}
			final xwriter x=new xwriter().p(return_type).spc().p(name).p("(");
			for(Iterator<def_func_arg> i=args.iterator();i.hasNext();){
				final def_func_arg a=i.next();
				x.p(a.toString());
				if(i.hasNext())
					x.p(",");
			}
			x.p(")");
			if(code_block!=null)
				x.p("{").nl().p(code_block.toString()).p("}");
			txt=x.toString();
		}
		@Override protected void compile(program p){
			if(code_block!=null){
				code_block.compile(p);
				bin=code_block.bin;
			}
		}
		@Override protected void link(program p){
			if(code_block!=null){
				code_block.link(p);
//				bin=code_block.bin;
			}
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_func_arg extends def{
		public String type,name,default_value;
		public boolean is_const;
		public def_func_arg(program p) throws IOException{
			super(p);
			type=p.next_token_in_line();
			if(type.equals("const")){
				is_const=true;
				type=p.next_token_in_line();
			}
			name=p.next_token_in_line();
			if(!p.is_next_char_equals())
				throw new compiler_error(this,"expected function argument format: int a=2,...");
			default_value=p.next_token_in_line();
			final xwriter x=new xwriter();
			if(is_const)
				x.p("const").spc();
			x.p(type).spc().p(name).p("=").p(default_value);
			txt=x.toString();
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_func_call extends expr{
		public String function_name;
		public List<expr_func_call_arg> args=new ArrayList<>();
		public expr_func_call(final program p,final String function) throws IOException{
			super(p,function);
			function_name=function;
			while(true){
				if(p.is_next_char_paranthesis_right())
					break;
				final expr_func_call_arg a=new expr_func_call_arg(p);
				args.add(a);
				if(p.is_next_char_comma())
					continue;
			}
			final xwriter x=new xwriter();
			x.p(function_name).p("(");
			final Iterator<expr_func_call_arg> i=args.iterator();
			while(true){
				if(!i.hasNext())
					break;
				x.p(i.next().toString());
				if(i.hasNext())
					x.p(",");
			}
			x.p(")");
			txt=x.toString();
			final def_func f=p.functions.get(function_name);
			if(f==null)
				throw new compiler_error(this,"function not found",function_name);
			int ii=0;
			for(expr_func_call_arg e:args){
				final def_func_arg fa=f.args.get(ii++);
				if(!e.txt.equals(fa.name))
					throw new compiler_error(this," argument "+ii+"  expected '"+fa.name+"' but got '"+e.txt+"'\n  "+f);
				final String type_in_register=p.type_for_register(this,e.toString());
				if(!fa.type.equals(type_in_register))
					throw new compiler_error(this," argument "+ii+"  expected type '"+fa.type+"' but var '"+fa.name+"' is of type '"+type_in_register+"'\n  "+f);
			}
		}
		@Override protected void compile(program p){
			bin=new int[]{call.op};
		}
		@Override protected void link(program p){
			final def_func f=p.functions.get(function_name);
			if(f==null)
				throw new compiler_error(this,"function not found",function_name);
			final int a=f.location_in_binary;
			bin[0]|=(a<<6);
		}
		private static final long serialVersionUID=1;
	}
	final static public class expr_func_call_arg extends expr{
		public expr_func_call_arg(final program p) throws IOException{
			super(p,null);
			txt=p.next_token_in_line();
		}
		@Override protected void compile(program p){}
		@Override protected void link(program p){}
		private static final long serialVersionUID=1;
	}
	protected String location_in_source;
	protected String txt;
	protected String type;
	protected int[] bin;
	protected int location_in_binary;
	public stmt(final program p){
		if(p!=null)
			location_in_source=p.location_in_source();
	}
	protected void validate_references_to_labels(program r){}
	protected void compile(program r){}
	protected void link(program p){}
	public String toString(){
		return txt;
	}
	//		public void source_to(xwriter x){}
	private static final long serialVersionUID=1;
}