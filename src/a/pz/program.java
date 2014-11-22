package a.pz;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import b.xwriter;

final public class program implements Serializable{
	final public static int     opli=0x0000;
	final public static int     oplp=0x0100;
	final public static int    opinc=0x0200;
	final public static int    opneg=0x0300;
	final public static int     optx=0x00e0;
	final public static int    opldc=0x00c0;
	final public static int    opadd=0x00a0;
	final public static int    opskp=0x0080;
	final public static int    opshf=0x0060;
	final public static int    opstc=0x0040;
	final public static int    opsub=0x0020;
	final public static int   opcall=0x0010;
	final public static int     opst=0x00d8;
	final public static int     opld=0x00f8;
	final public static int    opnxt=0x0004;
	final public static int    opret=0x0008;
	final public static int    opdac=0x0400;
	final public static int   opwait=0x0058;
	final public static int opnotify=0x0078;

	public program(final String cs)throws IOException,compiler_error{this(new StringReader(cs));}
	public program(final Reader rr)throws IOException,compiler_error{
		final reader r=new reader(rr,this);
		s=new ArrayList<>();
		while(true){
			final int ch=r.read();
			if(ch==-1)break;
			r.unread(ch);
			final stmt st=read_next_statement_from(r);
			s.add(st);
		}
		s.forEach(e->e.validate_references_to_labels(this));
		s.forEach(e->e.compile(this));
		int pc=0;
		for(final stmt ss:s){
			ss.location_in_binary=pc;
			if(ss.bin!=null)pc+=ss.bin.length;
		}
		s.forEach(e->e.link(this));
	}
	public void disassemble_to(xwriter x){
		s.forEach(e->x.pl(e.toString()));
	}
	public String toString(){
		final xwriter x=new xwriter();
		disassemble_to(x);
		return x.toString();
	}

	final static public class define_const extends stmt{
		public String name,type,value;
		public define_const(final reader r)throws IOException{
			super(r);
			name=r.next_token_in_line();
			final define_const d=r.p.defines.get(name);
			if(d!=null)throw new compiler_error(this,"define '"+name+"' already declared at "+d.location_in_source);
			type=r.next_token_in_line();
			final define_typedef t=r.p.typedefs.get(type);
			if(t==null)throw new compiler_error(this,"type not found",type);
			value=r.next_token_in_line();
			txt="const "+name+" "+type+" "+value;
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	final static public class define_var extends stmt{
		public List<String>vars;
		public define_var(final reader r)throws IOException{
			super(r);
			vars=new ArrayList<>();
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)break;
				vars.add(t);
			}
			if(vars.isEmpty())throw new compiler_error(this,"expected list of registers after 'var' statement");
			final xwriter x=new xwriter().p("var").spc();
			vars.forEach(e->x.spc().p(e));
			txt=x.toString();
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	final static public class define_struct extends stmt{
		public String name;
		public List<define_struct_member>fields;
		public define_struct(final reader r)throws IOException{
			super(r);
			name=r.next_identifier();
			fields=new ArrayList<>();
			while(true){
				r.skip_whitespace_on_same_line();
				if(r.is_at_end_of_line())
					break;
				final define_struct_member f=new define_struct_member(r);
				fields.add(f);
			}
			final xwriter x=new xwriter();
			x.p("struct").spc().p(name);
			fields.forEach(f->x.spc().p(f.toString()));
			txt=x.toString();
		}
		@Override public void validate_references_to_labels(program p){fields.forEach(e->e.validate_references_to_labels(p));}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class define_struct_member extends stmt{
		public String type;
		public String name;
		public String default_value;
		public define_struct_member(reader r)throws IOException{
			super(r);
			name=r.next_identifier();
			type=r.next_type_identifier();
			default_value=r.next_token_in_line();
			final xwriter x=new xwriter();
			x.p(name).spc().p(type).spc().p(default_value);
			txt=x.toString();
		}
		@Override public void validate_references_to_labels(program p)throws compiler_error{
			if(!p.typedefs.containsKey(type))throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	final static public class define_typedef extends stmt{
		public String name;
		public define_typedef(final reader r)throws IOException{
			super(r);
			name=r.next_identifier();
			txt=new xwriter().p("typedef").spc().p(name).toString();
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	
	
	final public Map<String,define_const>defines=new LinkedHashMap<>();
	final public Map<String,define_typedef>typedefs=new LinkedHashMap<>();
	final public Map<String,define_struct>structs=new LinkedHashMap<>();
	final public Map<String,define_label>labels=new LinkedHashMap<>();
	private stmt read_next_statement_from(final reader r)throws IOException,compiler_error{
		String tk="";
		while(true){
			r.skip_whitespace();
			tk=r.next_token_in_line();
			if(tk==null)return new eof(r);
			if(tk.equals("const")){
				final define_const s=new define_const(r);
				defines.put(s.name,s);
				return s;
			}
			if(tk.equals("typedef")){
				final define_typedef s=new define_typedef(r);
				typedefs.put(s.name,s);
				return s;
			}
			if(tk.equals("struct")){
				final define_struct s=new define_struct(r);
				structs.put(s.name,s);
				return s;
			}
			if(tk.equals("var")){
				final define_var s=new define_var(r);
//				structs.put(s.name,s);
				return s;
			}
			if(tk.startsWith(":")){
				final define_label s=new define_label(r,tk.substring(1));
				labels.put(s.name,s);
				r.consume_line();
				return s;
			}
			final define_typedef td=typedefs.get(tk);
			if(td!=null){
				final define_data_int s=new define_data_int(r);
				labels.put(s.name,s);
				return s;
			}
			if(tk.equals(".")){
				final define_data s=new define_data(r);
				r.consume_line();
				return s;
			}
			if(tk.equals("..")){
				final eof s=new eof(r);
				r.consume_line();
				return s;
			}
			if(tk.startsWith("//")){
				r.consume_line();
				continue;
			}
			break;
		}
		int znxr=0;
		switch(tk){
		case"ifz":{znxr=1;tk=r.next_token_in_line();break;}
		case"ifn":{znxr=2;tk=r.next_token_in_line();break;}
		case"ifp":{znxr=3;tk=r.next_token_in_line();break;}
		}
		final stmt s;
		try{
			s=(stmt)Class.forName(program.class.getName()+"$"+tk).getConstructor(reader.class).newInstance(r);
		}catch(InvocationTargetException t){
			if(t.getCause()instanceof compiler_error)throw(compiler_error)t.getCause();
			throw new compiler_error(r.hrs_location(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new compiler_error(r.hrs_location(),t.toString());
		}catch(ClassNotFoundException t){
			throw new compiler_error(r.hrs_location(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new compiler_error(r.hrs_location(),t.toString());
		}
		if(!(s instanceof define_data)){
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)break;
				if("nxt".equalsIgnoreCase(t)){znxr|=4;continue;}
				if("ret".equalsIgnoreCase(t)){znxr|=8;continue;}
				if(t.startsWith("//")){r.consume_line();break;}
				throw new Error("3 "+t);
			}
			s.znxr=znxr;
		}
		r.consume_line();
		return s;
	}
	/**writes binary*/
	final public void zap(int[]rom){//? arraycopybinary
		for(int i=0;i<rom.length;i++)rom[i]=-1;
		int pc=0;
		for(final stmt ss:s){
			if(ss.bin==null)continue;
			final int c=ss.bin.length;
			System.arraycopy(ss.bin,0,rom,pc,c);
			pc+=c;
		}

//		final linker c=new linker(this,rom);
//		s.forEach(e->{
//			try{
//				e.write_to(c);
//			}catch(compiler_error ee){
//				throw ee;
////			}catch(InvocationTargetException ite){
////				throw new compiler_error(e.source_location,ite.getCause().getMessage());
//			}catch(NumberFormatException t){
//				throw new compiler_error(e.source_location,t.getMessage());
//			}catch(Throwable t){
//				throw new compiler_error(e.source_location,t.getMessage());}
//			});
//		c.finish();
	}
	public List<stmt>s;

	public static class stmt implements Serializable{
		public String location_in_source;
		public String txt;
		public int[]bin;
		public int location_in_binary;
		public int znxr;
		/**opcode*/public int opcode;
		public int rai;
		public int rdi;
		public stmt(final reader r){location_in_source=r.hrs_location();}
		protected stmt(final reader r,final int op,final int ra,final int rd){
			this(r);
			this.opcode=op;
			this.rai=ra;
			this.rdi=rd;
		}
		protected stmt(final reader r,final int op,final int ra,final int rd,final boolean flip_ra_rd){
			this(r,op,rd,ra);
		}
		protected void validate_references_to_labels(program p){}
		protected void compile(program p){bin=new int[]{znxr_ci__ra__rd__()};}
		protected void link(program p){}
		protected int znxr_ci__ra__rd__(){return znxr|opcode|((rai&15)<<8)|((rdi&15)<<12);}
		final@Override public String toString(){
			if(txt!=null)return txt;
			final xwriter x=new xwriter();
			if((znxr&3)==3)x.p("ifp ");
			else if((znxr&1)==1)x.p("ifz ");
			else if((znxr&2)==2)x.p("ifn ");
			source_to(x);
			x.spc();
			if((znxr&4)==4)x.p(" nxt");
			if((znxr&8)==8)x.p(" ret");
			return x.toString();
		}
		public void source_to(xwriter x){}
		private static final long serialVersionUID=1;
	}
	public static class li extends stmt{
		public String data;
		public int value;
		public li(reader r)throws IOException{
			super(r,opli,0,r.next_register_identifier());
			data=r.next_token_in_line();
			txt="li "+(char)(rdi+'a')+" "+data;
		}
		public boolean is_integer(){try{Integer.parseInt(data);return true;}catch(Throwable t){return false;}}
		@Override protected void compile(program p){
			bin=new int[]{znxr_ci__ra__rd__(),0};
		}
		@Override protected void link(program p){
			final define_const def=p.defines.get(data);
			if(def!=null){
				data=def.value;
			}
			if(data.startsWith("&")){
				final String nm=data.substring(1);
				final define_label l=p.labels.get(nm);
				if(l==null)throw new compiler_error(this,"label not found",nm);
				value=l.location_in_binary;
			}else{
				try{value=Integer.parseInt(data,16);}catch(NumberFormatException e){
					throw new compiler_error(this,"cannot parse number '"+data+"'");
				}
			}
			final int bit_width=16;
//			final int i=Integer.parseInt(data,16);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(value>max)throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			if(value<min)throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
			bin=new int[]{bin[0],value};
		}
		private static final long serialVersionUID=1;
	}
	public static class inc extends stmt{
		public inc(reader r)throws IOException{
			super(r,opinc,0,r.next_register_identifier());
		}
		@Override public void source_to(xwriter x){
			x.p("inc").spc().p((char)(rdi+'a'));
		}
		private static final long serialVersionUID=1;
	}
	public static class st extends stmt{
		public st(reader r)throws IOException{
			super(r,opst,r.next_register_identifier(),r.next_register_identifier());
		}
		@Override public void source_to(xwriter x){
			x.p("st").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a'));
		}
		private static final long serialVersionUID=1;
	}
	public static class eof extends stmt{
		public eof(reader r)throws IOException{
			super(r);
			txt="..";
		}
		@Override protected void compile(program p){bin=new int[]{-1};}
		private static final long serialVersionUID=1;
	}
	public static class nxt extends stmt{
		public nxt(reader r)throws IOException{
			super(r,opnxt,0,0);
		}
		private static final long serialVersionUID=1;
	}
	public static class ret extends stmt{
		public ret(reader r)throws IOException{
			super(r,opret,0,0);
			txt="ret";
		}
		private static final long serialVersionUID=1;
	}
	public static class lp extends stmt{
		public lp(reader r)throws IOException{
			super(r,oplp,0,r.next_register_identifier());
		}
		@Override public void source_to(xwriter x){
			x.p("lp").spc().p((char)(rdi+'a'));
		}
		private static final long serialVersionUID=1;
	}
	public static class stc extends stmt{
		public stc(reader r)throws IOException{
			super(r,opstc,r.next_register_identifier(),r.next_register_identifier());
		}
		@Override public void source_to(xwriter x){
			x.p("stc").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a'));
		}
		private static final long serialVersionUID=1;
	}
	public static class add extends stmt{
		public add(reader r)throws IOException{
			super(r,opadd,r.next_register_identifier(),r.next_register_identifier());
		}
		@Override public void source_to(xwriter x){
			x.p("add").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a'));
		}
		private static final long serialVersionUID=1;
	}
	public static class sub extends stmt{
		public sub(reader r)throws IOException{
			super(r,opsub,r.next_register_identifier(),r.next_register_identifier());
		}
		private static final long serialVersionUID=1;
	}
	public static class call extends stmt{
		String label;
		public call(reader r)throws IOException{
			super(r,opcall,0,0);
			label=r.next_token_in_line();
			txt="call "+label;
		}
		@Override protected void link(program p){
			define_label l=p.labels.get(label);
			if(l==null)throw new compiler_error(this,"label not found",label);
			final int a=l.location_in_binary;
			bin[0]|=(a<<6);
		}
		private static final long serialVersionUID=1;
	}
	public static class define_data extends stmt{
		public List<String>data;
		public define_data(reader r)throws IOException{
			super(r);
			data=new ArrayList<>();
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)break;
				data.add(t);
			}
			final xwriter x=new xwriter().p(". ");
			data.forEach(e->x.spc().p(e));
			txt=x.toString();
		}
		@Override protected void compile(program p){
//			super.generate_code_pass_1(p);
			bin=new int[data.size()];
			int i=0;
			for(final String s:data){
				bin[i++]=Integer.parseInt(s,16);
			}
		}
		private static final long serialVersionUID=1;
	}
	public static class define_label extends stmt{
		public String name;
		public define_label(reader r,String nm){
			super(r);
			name=nm;
			final define_label d=r.p.labels.get(name);
			if(d!=null)throw new compiler_error(this,"label '"+name+"' already declared at "+d.location_in_source);
			txt=":"+nm;
		}
		@Override protected void compile(program p){}
		private static final long serialVersionUID=1;
	}
	public static class ld extends stmt{
		public ld(reader r)throws IOException{
			super(r,opld,r.next_register_identifier(),r.next_register_identifier(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class ldc extends stmt{
		public ldc(reader r)throws IOException{
			super(r,opldc,r.next_register_identifier(),r.next_register_identifier(),true);
		}
		@Override public void source_to(xwriter x){
			x.p("ldc").spc().p((char)(rdi+'a')).spc().p((char)(rai+'a'));
		}
		private static final long serialVersionUID=1;
	}
	public static class tx extends stmt{
		public tx(reader r)throws IOException{
			super(r,optx,r.next_register_identifier(),r.next_register_identifier(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class shf extends stmt{
		public shf(reader r)throws IOException{
			super(r,opshf,r.next_register_identifier(),r.next_int(4),true);
		}
		private static final long serialVersionUID=1;
	}
	
	
	public static class define_data_int extends define_label{
		public String default_value;
		public define_data_int(reader r)throws IOException{
			super(r,r.next_identifier());
			default_value=r.next_token_in_line();
			if(default_value==null)default_value="0";
			txt="int "+name+" "+default_value;
		}
		@Override protected void compile(program p){
			final int d=Integer.parseInt(default_value,16);
			bin=new int[]{d};
		}
		private static final long serialVersionUID=1;
	}

	
//	private static void skip_whitespace(source_reader r)throws IOException{
//		while(true){
//			final int ch=r.read();
//			if(Character.isWhitespace(ch))continue;
//			if(ch==-1)return;
//			r.unread(ch);
//			return;
//		}
//	}
//	private static void skip_whitespace_on_same_line(source_reader r)throws IOException{
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)return;
//			if(ch=='\n'){r.unread(ch);return;}
//			if(Character.isWhitespace(ch))continue;
//			r.unread(ch);
//			return;
//		}
//	}
//	private static String next_token_in_line(source_reader r)throws IOException{
//		skip_whitespace_on_same_line(r);
//		final StringBuilder sb=new StringBuilder();
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)break;
//			if(ch=='\n'){r.unread(ch);break;}
//			if(Character.isWhitespace(ch))break;
//			sb.append((char)ch);
//		}
//		skip_whitespace_on_same_line(r);
//		if(sb.length()==0)return null;
//		return sb.toString();
//	}
//	private static String consume_rest_of_line(source_reader r)throws IOException{
//		final StringBuilder sb=new StringBuilder();
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)break;
//			if(ch=='\n'){r.unread(ch);break;}
//			sb.append((char)ch);
//		}
//		return sb.toString();
//	}
	
	public static class compiler_error extends RuntimeException{
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
		@Override public String toString(){return new xwriter().p("line ").p(source_location).spc().p(message).toString();}
		private static final long serialVersionUID=1;
	}

	
	private static final long serialVersionUID=1;
}