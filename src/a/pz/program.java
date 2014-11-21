package a.pz;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

	public program(final String cs)throws IOException,error{this(new StringReader(cs));}
	public program(final Reader rr)throws IOException,error{
		final source_reader r=new source_reader(rr);
		s=new ArrayList<>();
		while(true){
			final int ch=r.read();
			if(ch==-1)break;
			r.unread(ch);
			final stmt st=read_next_statement_from(r);
			s.add(st);
		}
	}
	private static stmt read_next_statement_from(final source_reader r)throws IOException,error{
		String tk="";
		while(true){
			skip_whitespace(r);
			tk=next_token_in_line(r);
			if(!tk.startsWith("#"))break;
			consume_rest_of_line(r);
		}
		if(tk.endsWith(":"))return new label(tk.substring(0,tk.length()-1));
		int znxr=0;
		switch(tk){
		case"ifz":{znxr=1;tk=next_token_in_line(r);break;}
		case"ifn":{znxr=2;tk=next_token_in_line(r);break;}
		case"ifp":{znxr=3;tk=next_token_in_line(r);break;}
		}
		if(tk.equals(".."))tk="eof";
		if(tk.equals("."))tk="data";
		final stmt s;
		try{
			s=(stmt)Class.forName(program.class.getName()+"$"+tk).getConstructor(source_reader.class).newInstance(r);
			s.source_location=r.hrs_location();
		}catch(InvocationTargetException t){
			if(t.getCause()instanceof error)throw(error)t.getCause();
			throw new error(r.hrs_location(),t.getCause().toString());
		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
			throw new error(r.hrs_location(),t.toString());
		}catch(ClassNotFoundException t){
			throw new error(r.hrs_location(),"unknown instruction '"+tk+"'");
		}catch(Throwable t){
			throw new error(r.hrs_location(),t.toString());
		}
		if(!(s instanceof data)){
			while(true){
				final String t=next_token_in_line(r);
				if(t==null)break;
				if("nxt".equalsIgnoreCase(t)){znxr|=4;continue;}
				if("ret".equalsIgnoreCase(t)){znxr|=8;continue;}
				if(t.startsWith("#")){consume_rest_of_line(r);break;}
				throw new Error("3 "+t);
			}
			s.znxr=znxr;
		}
		final int eos=r.read();
		if(eos!='\n'&&eos!=-1)throw new Error(r+" expected end of line or end of file");
		return s;
	}
	/**writes binary to param*/
	final public void zap(int[]ints){
		final rom_writer c=new rom_writer(ints);
		s.forEach(e->{try{e.write_to(c);}catch(error ee){throw ee;}catch(Throwable t){throw new error(e.source_location,t.getMessage());}});
		c.finish();
	}
	private List<stmt>s;


	public static class stmt implements Serializable{
		public String source_location;
		/**znxr*/public int znxr;
		/**opcode*/public int op;
		/**register a*/public int ra;
		/**register b*/public int rd;
		public stmt(String txt){this.txt=txt;}
		protected stmt(int op,int ra,int rd){this.op=op;this.ra=ra;this.rd=rd;}
		protected stmt(int op,int ra,int rd,boolean flip_ra_rd){this.op=op;this.ra=rd;this.rd=ra;}
		public void write_to(rom_writer c){
			final int ir=znxr|op|((ra&15)<<8)|((rd&15)<<12);
			c.write(ir);
		}
		protected String txt;
		private static final long serialVersionUID=1;
	}
	public static class li extends stmt{
		private String data;
		public li(source_reader r)throws IOException{
			super(opli,0,reg(r));
			data=next_token_in_line(r);
		}
		@Override public void write_to(rom_writer c){
			super.write_to(c);
			if(data.startsWith(":")){
				c.add_link(data,source_location);
				c.write(0);
			}else{
				final int bit_width=16;
				final int i=Integer.parseInt(data,16);
				final int max=(1<<(bit_width-1))-1;
				final int min=-1<<(bit_width-1);
				if(i>max)throw new error(source_location,"number '"+data+"' out of "+bit_width+" bits range");
				if(i<min)throw new error(source_location,"number '"+data+"' out of "+bit_width+" bits range");
				c.write(i);
			}
			
		}
		private static final long serialVersionUID=1;
	}
	public static class inc extends stmt{
		public inc(source_reader r)throws IOException{
			super(opinc,0,reg(r));
		}
		private static final long serialVersionUID=1;
	}
	final private static int reg(source_reader r)throws IOException{
		final String s=next_token_in_line(r);
		if(s==null)throw new program.error(r.hrs_location(),"expected register but found end of line");
		if(s.length()!=1)throw new error(r.hrs_location(),"register name unknown '"+s+"'");
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		final int max=(1<<4)-1;
		final int min=0;
		if(reg>max||reg<min)throw new error(r.hrs_location(),"register '"+s+"' out range 'a' through 'p'");
		return reg;
	}
	final private static int num(source_reader r,int bit_width)throws IOException{
		final String s=next_token_in_line(r);
		if(s==null)throw new program.error(r.hrs_location(),"expected number but found end of line");
		try{
			final int i=Integer.parseInt(s);
			final int max=(1<<(bit_width-1))-1;
			final int min=-1<<(bit_width-1);
			if(i>max)throw new error(r.hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			if(i<min)throw new error(r.hrs_location(),"number '"+s+"' out of "+bit_width+" bits range");
			return i;
		}catch(NumberFormatException e){throw new error(r.hrs_location(),"can not translate number '"+s+"'");}
	}
//	final private static int ri(String s){
//		final char first_char=s.charAt(0);
//		final int reg=first_char-'a';
//		return reg;
//	}
	public static class st extends stmt{
		public st(source_reader r)throws IOException{
			super(opst,reg(r),reg(r));
		}
		private static final long serialVersionUID=1;
	}
	public static class eof extends stmt{
		public eof(source_reader r)throws IOException{
			super(". ffff");
		}
		@Override public void write_to(rom_writer c){c.write(-1);}
		private static final long serialVersionUID=1;
	}
	public static class nxt extends stmt{
		public nxt(source_reader r)throws IOException{
			super(opnxt,0,0);
		}
		private static final long serialVersionUID=1;
	}
	public static class ret extends stmt{
		public ret(source_reader r)throws IOException{
			super(opret,0,0);
		}
		private static final long serialVersionUID=1;
	}
	public static class lp extends stmt{
		public lp(source_reader r)throws IOException{
			super(oplp,0,reg(r));
		}
		private static final long serialVersionUID=1;
	}
	public static class stc extends stmt{
		public stc(source_reader r)throws IOException{
			super(opstc,reg(r),reg(r));
		}
		private static final long serialVersionUID=1;
	}
	public static class add extends stmt{
		public add(source_reader r)throws IOException{
			super(opadd,reg(r),reg(r));
		}
		private static final long serialVersionUID=1;
	}
	public static class sub extends stmt{
		public sub(source_reader r)throws IOException{
			super(opsub,reg(r),reg(r));
		}
		private static final long serialVersionUID=1;
	}
	public static class call extends stmt{
		String to;
		public call(source_reader r)throws IOException{
			super(opcall,0,0);
			to=next_token_in_line(r);
		}
		@Override public void write_to(rom_writer c){
			c.add_link(to,source_location);
			super.write_to(c);
		}
		private static final long serialVersionUID=1;
	}
	public static class data extends stmt{
		public data(source_reader r)throws IOException{
			super(consume_rest_of_line(r));
		}
		@Override public void write_to(rom_writer c){
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
	public static class label extends stmt{
		public label(String nm){
			super(nm);
		}
		@Override public void write_to(rom_writer c){
			c.add_label(txt,source_location);
		}
		private static final long serialVersionUID=1;
	}
	public static class ld extends program.stmt{
		public ld(source_reader r)throws IOException{
			super(opld,reg(r),reg(r),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class ldc extends program.stmt{
		public ldc(source_reader r)throws IOException{
			super(opldc,reg(r),reg(r),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class tx extends program.stmt{
		public tx(source_reader r)throws IOException{
			super(optx,reg(r),reg(r),true);
		}
		private static final long serialVersionUID=1;
	}
	public static class shf extends program.stmt{
		public shf(source_reader r)throws IOException{
			super(opshf,reg(r),num(r,4),true);
		}
		private static final long serialVersionUID=1;
	}
	private static void skip_whitespace(source_reader r)throws IOException{
		while(true){
			final int ch=r.read();
			if(Character.isWhitespace(ch))continue;
			if(ch==-1)return;
			r.unread(ch);
			return;
		}
	}
	private static void skip_whitespace_on_same_line(source_reader r)throws IOException{
		while(true){
			final int ch=r.read();
			if(ch==-1)return;
			if(ch=='\n'){r.unread(ch);return;}
			if(Character.isWhitespace(ch))continue;
			r.unread(ch);
			return;
		}
	}
	private static String next_token_in_line(source_reader r)throws IOException{
		skip_whitespace_on_same_line(r);
		final StringBuilder sb=new StringBuilder();
		while(true){
			final int ch=r.read();
			if(ch==-1)break;
			if(ch=='\n'){r.unread(ch);break;}
			if(Character.isWhitespace(ch))break;
			sb.append((char)ch);
		}
		skip_whitespace_on_same_line(r);
		if(sb.length()==0)return null;
		return sb.toString();
	}
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
	
	public static class error extends RuntimeException{
		public String source_location;
		public String message;
		public error(String source_location,String message){this.source_location=source_location;this.message=message;}
		@Override public String toString(){return "line "+source_location.split(":")[0]+": "+message;}
		private static final long serialVersionUID=1;
	}

	
	private static final long serialVersionUID=1;
}