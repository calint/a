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
import java.util.Scanner;
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
		final source_reader r=new source_reader(rr);
		s=new ArrayList<>();
		while(true){
			final int ch=r.read();
			if(ch==-1)break;
			r.unread(ch);
			final stmt st=read_next_statement_from(r);
			s.add(st);
		}
		s.forEach(e->e.second_pass(this));
	}
	final static public class define extends stmt{
		public String identifier,val,source_loc;
		public define(final source_reader r)throws IOException{
			super(r);
			identifier=r.next_token_in_line();
			val=r.next_token_in_line();
			txt="#define "+identifier+" "+val;
		}
		@Override public void write_to(final linker c){}
		private static final long serialVersionUID=1;
	}
	final static public class define_struct extends stmt{
		public String name;
		public List<define_struct_member>fields;
		public define_struct(final source_reader r)throws IOException{
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
			x.p("class").spc().p(name);
			fields.forEach(f->x.spc().p(f.toString()));
			txt=x.toString();
		}
		@Override public void write_to(final linker c){}
		@Override public void second_pass(program p){fields.forEach(e->e.second_pass(p));}
		private static final long serialVersionUID=1;
	}
	public static class define_struct_member extends stmt{
		public String type;
		public String name;
		public String default_value;
		public define_struct_member(source_reader r)throws IOException{
			super(r);
			type=r.next_type_identifier();
			name=r.next_identifier();
			default_value=r.next_token_in_line();
			final xwriter x=new xwriter();
			x.p(type).spc().p(name);
			if(default_value!=null)x.spc().p(default_value);
			txt=x.toString();
		}
		@Override public void write_to(linker c){}
		@Override public void second_pass(program p)throws compiler_error{
			if(!p.typedefs.containsKey(type))throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
			super.second_pass(p);
		}
		private static final long serialVersionUID=1;
	}
	final static public class define_typedef extends stmt{
		public String name;
		public define_typedef(final source_reader r)throws IOException{
			super(r);
			name=r.next_identifier();
			txt=new xwriter().p("typedef").spc().p(name).toString();
		}
		@Override public void write_to(final linker c){}
		private static final long serialVersionUID=1;
	}
	
	
	final public Map<String,define>defines=new LinkedHashMap<>();
	final public Map<String,define_typedef>typedefs=new LinkedHashMap<>();
	final public Map<String,define_struct>structs=new LinkedHashMap<>();
	private stmt read_next_statement_from(final source_reader r)throws IOException,compiler_error{
		String tk="";
		while(true){
			r.skip_whitespace();
			tk=r.next_token_in_line();
			if(tk.equals("#define")){
				final define s=new define(r);
				defines.put(s.identifier,s);
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
			if(!tk.startsWith("//"))break;
			consume_rest_of_line(r);
		}
		if(tk.endsWith(":"))return new source_label(r,tk.substring(0,tk.length()-1));
		int znxr=0;
		switch(tk){
		case"ifz":{znxr=1;tk=r.next_token_in_line();break;}
		case"ifn":{znxr=2;tk=r.next_token_in_line();break;}
		case"ifp":{znxr=3;tk=r.next_token_in_line();break;}
		}
		if(tk.equals(".."))tk="eof";
		if(tk.equals("."))tk="data_span";
		if(tk.equals("int"))tk="data_int";
		final stmt s;
		try{
			s=(stmt)Class.forName(program.class.getName()+"$"+tk).getConstructor(source_reader.class).newInstance(r);
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
		if(!(s instanceof data_span)){
			while(true){
				final String t=r.next_token_in_line();
				if(t==null)break;
				if("nxt".equalsIgnoreCase(t)){znxr|=4;continue;}
				if("ret".equalsIgnoreCase(t)){znxr|=8;continue;}
				if(t.startsWith("//")){consume_rest_of_line(r);break;}
				throw new Error("3 "+t);
			}
			s.znxr=znxr;
		}
		final int eos=r.read();
		if(eos!='\n'&&eos!=-1)throw new Error(r+" expected end of line or end of file");
		return s;
	}
	/**writes binary*/
	final public void zap(int[]rom){//? arraycopybinary
		for(int i=0;i<rom.length;i++)rom[i]=-1;
		final linker c=new linker(this,rom);
		s.forEach(e->{
			try{
				e.write_to(c);
			}catch(compiler_error ee){
				throw ee;
//			}catch(InvocationTargetException ite){
//				throw new compiler_error(e.source_location,ite.getCause().getMessage());
			}catch(NumberFormatException t){
				throw new compiler_error(e.source_location,t.getMessage());
			}catch(Throwable t){
				throw new compiler_error(e.source_location,t.getMessage());}
			});
		c.finish();
	}
	private List<stmt>s;


	public static class stmt implements Serializable{
		public String source_location;
		public String txt;
		public int znxr;
		/**opcode*/public int opcode;
		public int reg_a;
		public int rd_d;
		public stmt(final source_reader r){source_location=r.hrs_location();}
		protected stmt(final source_reader r,final int op,final int ra,final int rd){source_location=r.hrs_location();this.opcode=op;this.reg_a=ra;this.rd_d=rd;}
		protected stmt(final source_reader r,final int op,final int ra,final int rd,final boolean flip_ra_rd){source_location=r.hrs_location();this.opcode=op;this.reg_a=rd;this.rd_d=ra;}
//		public stmt(){}
//		public stmt(String txt){this.txt=txt;}
		public void write_to(linker c){
			final int ir=znxr|opcode|((reg_a&15)<<8)|((rd_d&15)<<12);
			c.write(ir);
		}
		public void second_pass(program p){
			b.b.pl(source_location+" "+this);
		}
		final@Override public String toString(){return txt;}
		private static final long serialVersionUID=1;
	}
	public static class li extends stmt{
		private String data;
		public li(source_reader r)throws IOException{
			super(r,opli,0,r.reg());
			data=r.next_token_in_line();
		}
		public boolean is_integer(){
			try{Integer.parseInt(data);return true;}catch(Throwable t){return false;}
		}
		@Override public void write_to(linker c){
			super.write_to(c);
			final define def=c.p.defines.get(data);
			if(def!=null){
				data=def.val;
			}
			if(data.startsWith("&")){
				c.add_link(data.substring(1),source_location);
				c.write(0);
			}else{
				final int bit_width=16;
				final int i=Integer.parseInt(data,16);
				final int max=(1<<(bit_width-1))-1;
				final int min=-1<<(bit_width-1);
				if(i>max)throw new compiler_error(source_location,"number '"+data+"' out of "+bit_width+" bits range");
				if(i<min)throw new compiler_error(source_location,"number '"+data+"' out of "+bit_width+" bits range");
				c.write(i);
			}
			
		}
		private static final long serialVersionUID=1;
	}
	public static class inc extends stmt{
		public inc(source_reader r)throws IOException{
			super(r,opinc,0,r.reg());
		}
		private static final long serialVersionUID=1;
	}
//	final private static int reg(source_reader r)throws IOException{
//		final String s=r.next_token_in_line();
//		if(s==null)throw new program.compiler_error(r.hrs_location(),"expected register but found end of line");
//		if(s.length()!=1)throw new compiler_error(r.hrs_location(),"register name unknown '"+s+"'");
//		final char first_char=s.charAt(0);
//		final int reg=first_char-'a';
//		final int max=(1<<4)-1;
//		final int min=0;
//		if(reg>max||reg<min)throw new compiler_error(r.hrs_location(),"register '"+s+"' out range 'a' through 'p'");
//		return reg;
//	}
	final private static int num(source_reader r,int bit_width)throws IOException{
		final String s=r.next_token_in_line();
		if(s==null)throw new program.compiler_error(r.hrs_location(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)throw new compiler_error(r.hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)throw new compiler_error(r.hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){throw new compiler_error(r.hrs_location(),"can not translate number '"+s+"'");}
	}
//	final private static int ri(String s){
//		final char first_char=s.charAt(0);
//		final int reg=first_char-'a';
//		return reg;
//	}
	public static class st extends stmt{
		public st(source_reader r)throws IOException{
			super(r,opst,r.reg(),r.reg());
		}
		private static final long serialVersionUID=1;
	}
	public static class eof extends stmt{
		public eof(source_reader r)throws IOException{
			super(r);
			txt=". ffff";
		}
		@Override public void write_to(linker c){c.write(-1);}
		private static final long serialVersionUID=1;
	}
	public static class nxt extends stmt{
		public nxt(source_reader r)throws IOException{
			super(r,opnxt,0,0);
		}
		private static final long serialVersionUID=1;
	}
	public static class ret extends stmt{
		public ret(source_reader r)throws IOException{
			super(r,opret,0,0);
		}
		private static final long serialVersionUID=1;
	}
	public static class lp extends stmt{
		public lp(source_reader r)throws IOException{
			super(r,oplp,0,r.reg());
		}
		private static final long serialVersionUID=1;
	}
	public static class stc extends stmt{
		public stc(source_reader r)throws IOException{
			super(r,opstc,r.reg(),r.reg());
		}
		private static final long serialVersionUID=1;
	}
	public static class add extends stmt{
		public add(source_reader r)throws IOException{
			super(r,opadd,r.reg(),r.reg());
		}
		private static final long serialVersionUID=1;
	}
	public static class sub extends stmt{
		public sub(source_reader r)throws IOException{
			super(r,opsub,r.reg(),r.reg());
		}
		private static final long serialVersionUID=1;
	}
	public static class call extends stmt{
		String to;
		public call(source_reader r)throws IOException{
			super(r,opcall,0,0);
			to=r.next_token_in_line();
		}
		@Override public void write_to(linker c){
			c.add_link(to,source_location);
			super.write_to(c);
		}
		private static final long serialVersionUID=1;
	}
	public static class data_span extends stmt{
		public data_span(source_reader r)throws IOException{
			super(r);
			txt=consume_rest_of_line(r);
		}
		@Override public void write_to(linker c){
			try(final Scanner s=new Scanner(txt)){
				while(s.hasNext()){
					final String ss=s.next();
					final int d=Integer.parseInt(ss,16);
					c.write(d);
				}
			}
		}
		private static final long serialVersionUID=1;
	}
	public static class source_label extends stmt{
		public source_label(source_reader r,String nm){
			super(r);
			txt=nm;
		}
		@Override public void write_to(linker c){
			c.add_label(txt,source_location);
		}
		private static final long serialVersionUID=1;
	}
	public static class ld extends program.stmt{
		public ld(source_reader r)throws IOException{
			super(r,opld,r.reg(),r.reg(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class ldc extends program.stmt{
		public ldc(source_reader r)throws IOException{
			super(r,opldc,r.reg(),r.reg(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class tx extends program.stmt{
		public tx(source_reader r)throws IOException{
			super(r,optx,r.reg(),r.reg(),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class shf extends program.stmt{
		public shf(source_reader r)throws IOException{
			super(r,opshf,r.reg(),num(r,4),true);
		}
		private static final long serialVersionUID=1;
	}
	
	
	public static class data_int extends stmt{
		public String identifier;
		public String default_value;
		public data_int(source_reader r)throws IOException{
			super(r);
			identifier=r.next_identifier();
			default_value=r.next_token_in_line();
			if(default_value==null)default_value="0";
			txt="int "+identifier+" "+default_value;
		}
		@Override public void write_to(linker c){
			final int d=Integer.parseInt(default_value,16);
			c.add_label(identifier,source_location);
			c.write(d);
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
	private static String consume_rest_of_line(source_reader r)throws IOException{
		final StringBuilder sb=new StringBuilder();
		while(true){
			final int ch=r.read();
			if(ch==-1)break;
			if(ch=='\n'){r.unread(ch);break;}
			sb.append((char)ch);
		}
		return sb.toString();
	}
	
	public static class compiler_error extends RuntimeException{
		public String source_location;
		public String message;
		public compiler_error(stmt s,String message){
			this(s.source_location,message);
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