package a.pz;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

final public class program{
	public program(final source_reader r)throws IOException{
		s=new ArrayList<>();
		while(true){
			final int ch=r.read();
			if(ch==-1)break;
			r.unread(ch);
			final program.stmt st=stmt.read_next_stmt(r,labels);
			s.add(st);
		}
	}
	final public void write_to(rom r){
		final compiler c=new compiler(r);
		s.forEach(e->e.write_to(c));
		c.finish();
	}
	private List<program.stmt>s;
	final public String toString(){
		final StringBuilder sb=new StringBuilder();
		s.forEach(e->sb.append(e.toString()).append('\n'));
		return sb.toString();
	}
	final private Map<String,String>labels=new LinkedHashMap<>();

	public static class stmt{
		/**znxr*/public int znxr;
		/**opcode*/public int op;
		/**register a*/public int ra;
		/**register b*/public int rd;
//			/**nxt ret bits*/public boolean nxt,ret;
		public stmt(String s){this.txt=s;}
		protected stmt(int op,int ra,int rd){
			this.op=op;this.ra=ra;this.rd=rd;
		}
		protected stmt(int op,int ra,int rd,boolean flip_ra_rd){
			this.op=op;this.ra=rd;this.rd=ra;
		}
		protected String txt;
		public String toString(){
			final StringBuilder sb=new StringBuilder();
			if(txt==null){
				final int ir=znxr|op|((ra&15)<<8)|((rd&15)<<12);
				sb.append(". ").append(Integer.toHexString(ir));
				return sb.toString();
			}
			if((znxr&3)==1)sb.append("ifz ");
			if((znxr&3)==2)sb.append("ifn ");
			if((znxr&3)==3)sb.append("ifp ");
			sb.append(txt);
			if((znxr&4)==4)sb.append(" nxt");
			if((znxr&8)==8)sb.append(" ret");
			return sb.toString();
		}
		public void write_to(compiler c){
			final int ir=znxr|op|((ra&15)<<8)|((rd&15)<<12);
			c.write(ir);
		}
		public static program.stmt read_next_stmt(final source_reader r,final Map<String,String>labels)throws IOException{
			String tk="";
			while(true){
				skip_whitespace(r);
				tk=next_token_in_line(r);
				if(!tk.startsWith("#"))
					break;
				consume_rest_of_line(r);
			}
			if(tk.endsWith(":")){
				labels.put(tk,tk);
				return new label(tk.substring(0,tk.length()-1));
			}
			int znxr=0;
			switch(tk){
			case"ifz":{znxr=1;tk=next_token_in_line(r);break;}
			case"ifn":{znxr=2;tk=next_token_in_line(r);break;}
			case"ifp":{znxr=3;tk=next_token_in_line(r);break;}
			}
			if(tk.equals(".."))tk="eof";
			if(tk.equals("."))tk="data";
			final program.stmt s;
			try{
				final Class cls=Class.forName(program.class.getName()+"$"+tk);
				final Constructor ctor=cls.getConstructor(source_reader.class);
				s=(program.stmt)ctor.newInstance(r);
			}catch(Throwable t){
				throw new Error(r+" instruction '"+tk+"' not found");
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
	}
	public static class li extends program.stmt{
		private String data;
		public li(source_reader r)throws IOException{
			super(acore.opload,0,ri(next_token_in_line(r)));
			data=next_token_in_line(r);
		}
		@Override public void write_to(compiler c){
			super.write_to(c);
			if(data.startsWith(":")){
				c.add_link(data,"?");
				c.write(0);
			}else{
				c.write(Integer.parseInt(data,16));
			}
			
		}
	}
	public static class inc extends program.stmt{
		public inc(source_reader r)throws IOException{
			super(acore.opinc,0,ri(next_token_in_line(r)));
		}
	}
	final private static int ri(String s){
		final char first_char=s.charAt(0);
		final int reg=first_char-'a';
		return reg;
	}
	public static class st extends program.stmt{
		public st(source_reader r)throws IOException{
			super(acore.opst,ri(next_token_in_line(r)),ri(next_token_in_line(r)));
		}
	}
	public static class eof extends program.stmt{
		public eof(source_reader r)throws IOException{
			super(". ffff");
		}
		@Override public void write_to(compiler c){c.write(-1);}
	}
	public static class nxt extends program.stmt{
		public nxt(source_reader r)throws IOException{
			super(acore.opnxt,0,0);
		}
	}
	public static class ret extends program.stmt{
		public ret(source_reader r)throws IOException{
			super(acore.opret,0,0);
		}
	}
	public static class lp extends program.stmt{
		public lp(source_reader r)throws IOException{
			super(acore.oplp,0,ri(next_token_in_line(r)));
		}
	}
	public static class stc extends program.stmt{
		public stc(source_reader r)throws IOException{
			super(acore.opstc,ri(next_token_in_line(r)),ri(next_token_in_line(r)));
		}
	}
	public static class add extends program.stmt{
		public add(source_reader r)throws IOException{
			super(acore.opadd,ri(next_token_in_line(r)),ri(next_token_in_line(r)));
		}
	}
	public static class sub extends program.stmt{
		public sub(source_reader r)throws IOException{
			super(acore.opsub,ri(next_token_in_line(r)),ri(next_token_in_line(r)));
		}
	}
	public static class call extends program.stmt{
		String to;
		public call(source_reader r)throws IOException{
			super(acore.opcall,0,0);
			to=next_token_in_line(r);
		}
		@Override public void write_to(compiler c){
			c.add_link(to,"?");
			super.write_to(c);
		}
	}
	public static class data extends program.stmt{
		public data(source_reader r)throws IOException{
			super(consume_rest_of_line(r));
		}
		@Override public void write_to(compiler c){
			try(final Scanner s=new Scanner(txt)){
				while(s.hasNext()){
					final String ss=s.next();
					final int d=Integer.parseInt(ss,16);
					c.write(d);
				}
			}
		}
	}
	public static class label extends program.stmt{
		public label(String nm){
			super(nm);
		}
		@Override public void write_to(compiler c){
			c.add_label(toString(),"?");
		}
	}
	public static class ld extends program.stmt{
		public ld(source_reader r)throws IOException{
			super(acore.opld,ri(next_token_in_line(r)),ri(next_token_in_line(r)),true);
		}
	}
	public static class ldc extends program.stmt{
		public ldc(source_reader r)throws IOException{
			super(acore.opldc,ri(next_token_in_line(r)),ri(next_token_in_line(r)),true);
		}
	}
	public static class tx extends program.stmt{
		public tx(source_reader r)throws IOException{
			super(acore.opset,ri(next_token_in_line(r)),ri(next_token_in_line(r)),true);
		}
	}
	public static class shf extends program.stmt{
		public shf(source_reader r)throws IOException{
			super(acore.opshf,ri(next_token_in_line(r)),Integer.parseInt(next_token_in_line(r)),true);
		}
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
}