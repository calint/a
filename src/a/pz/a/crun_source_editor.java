package a.pz.a;
import static b.b.pl;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import b.a;
import b.xwriter;
final public class crun_source_editor extends a{
	int focusline;
	private int lstfocusline;
	//	public a brkpts;
	private Set<Integer> brkptsset=new HashSet<Integer>();
	public a src;
	public a resrc;
	public a sts;
	final public static class line_numbers extends a{
		public int focus_line=0;
		@Override public void to(xwriter x) throws Throwable{
			for(int i=1;i<100;i++)
				if(i==focus_line) x.divo("","color:#800;font-weight:bold;background:yellow").p(Integer.toString(i)).div_();
				else x.pl(Integer.toString(i));
		}

		private static final long serialVersionUID=1;
	}
	public line_numbers ln;
	public void to(final xwriter x) throws Throwable{
		x.spanh(sts,"","width:5em;color:#800;font-weight:bold").ax(this,"f3",""," crun ","a").nl();
		x.table().tr().td("","text-align:right;padding-right:.5em");
		x.el(ln);
		ln.to(x);
		x.el_();
		x.td();
		x.inptxtarea(src);
		x.td().el(resrc);
		x.table_();
	}
	public boolean isonbrkpt(final int srclno){
		return brkptsset.contains(srclno);
	}
	//	synchronized public void x_brk(xwriter x,String s)throws Throwable{
	//		final int lno=Integer.parseInt(s);
	//		if(brkptsset.contains(lno)){
	//			brkptsset.remove(lno);
	//			brkpts.set(brkptsset.toString());
	//			x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e.className=e._oldcls;");
	//			return;
	//		}
	//		brkptsset.add(lno);
	//		brkpts.set(brkptsset.toString());
	//		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e._oldcls=e.className;if(!e._oldcls)e._oldcls='';e.className='brk';");
	//	}
	public final static boolean ommit_compiling_source_from_disassembler=false;
	final public static class prog{
		final Map<String,statement> toc;
		final block code;
		public prog(Map<String,statement> toc,block code){
			this.toc=toc;
			this.code=code;
		}
	}
	synchronized public void x_f3(xwriter x,String s) throws Throwable{
		final reader r=new reader(src.reader());
		try{
			final block el=new block(this,"b",r);
			if(x==null) return;
			x.xu(sts.clr());
			el.source_to(x.xub(resrc,true,true));
			x.xube();
			ev(x,this,new prog(r.toc,el));
		}catch(Throwable t){
			b.b.log(t);
			if(x==null) return;
			x.pl("{var e=$('"+src.id()+"');e.selectionStart="+r.bm_nchar+";e.selectionEnd="+r.nchar+";}");
			x.xu(sts.set(t.getMessage()));
			//			x.xalert(t.getMessage());
		}
		//		final program p;
		//		try{
		//			p=new program(src.str());
		//			p.build();
		//			sts.set(p.program_length+" ");
		//			final int prev_focus_line=ln.focus_line;
		//			ln.focus_line=0;
		//			if(x!=null){
		//				if(prev_focus_line!=ln.focus_line) x.xu(ln);
		//				x.xu(sts);
		//			}
		//		}catch(Throwable t){
		//			log(t);
		//			sts.set(t.toString()+"\n");
		//			if(t instanceof compiler_error) ln.focus_line=((compiler_error)t).source_location_line();
		//			if(x!=null) x.xu(ln).xu(sts);
		//			return;
		//		}
		//		if(!ommit_compiling_source_from_disassembler){
		//			try{
		//				final program p2=new program(p.toString());
		//				p2.build();
		//				if(!p.is_binary_equal(p2)) throw new Error("not binary equivalent");
		//			}catch(Throwable t){
		//				log(t);
		//				if(x!=null) x.xalert("reverse compilation failed: "+t.toString());
		//				return;
		//			}
		//		}
		//		ev(x,this,p);
	}
	public void xfocusline(xwriter x){
		if(lstfocusline!=0) x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+lstfocusline+"];e.className=e._oldcls;");
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(focusline-1)+"];e._oldcls=e.className;e.className='stp';");
		lstfocusline=focusline;
	}
	private static final long serialVersionUID=11;

	public static final class reader{
		final public LinkedHashMap<String,statement> toc=new LinkedHashMap<>();
		private PushbackReader r;
		public int line=1,col=1,prevcol=1,nchar=0;
		public reader(Reader r){
			this.r=new PushbackReader(r,1);
		}
		private int read(){
			try{
				final int ch=r.read();
				last_read_char=ch;
				nchar++;
				if(ch=='\n'){
					line++;
					prevcol=col;
					col=1;
					return ch;
				}
				col++;
				return ch;
			}catch(IOException e){
				throw new Error(e);
			}
		}
		private void unread(final int ch){
			try{
				nchar--;
				if(ch!=-1) r.unread(ch);
				if(ch=='\n'){
					line--;
					col=prevcol;
					return;
				}
				col--;
				//				if(col==0)throw new Error();//?
			}catch(IOException e){
				throw new Error(e);
			}
		}
		public String next_empty_space(){
			final xwriter x=new xwriter();
			while(true){
				final int ch=read();
				if(Character.isWhitespace(ch)){
					x.p((char)ch);
					continue;
				}
				unread(ch);
				break;
			}
			return x.toString();
		}
		public String next_token(){
			final xwriter x=new xwriter();
			while(true){
				final int ch=read();
				if(ch==-1||Character.isWhitespace(ch)||ch=='{'||ch=='}'||ch=='('||ch==')'){
					unread(ch);
					break;
				}
				x.p((char)ch);
			}
			return x.toString();
		}
		public boolean is_next_char_block_open(){
			final int ch=read();
			if(ch=='{') return true;
			unread(ch);
			return false;
		}
		public boolean is_next_char_block_close(){
			final int ch=read();
			if(ch=='}') return true;
			unread(ch);
			return false;
		}
		public boolean is_next_char_expression_open(){
			final int ch=read();
			if(ch=='(') return true;
			unread(ch);
			return false;
		}
		public boolean is_next_char_expression_close(){
			final int ch=read();
			if(ch==')') return true;
			unread(ch);
			return false;
		}
		public int bm_line,bm_col,bm_nchar;
		public void bm(){
			bm_line=line;
			bm_col=col;
			bm_nchar=nchar;
		}
		public int last_read_char;
		public void unread_last_char(){
			unread(last_read_char);
		}
	}
	public static final class xbin{
		final public Map<String,statement> toc;
		public xbin(Map<String,statement> toc,final int[] dest){
			this.toc=toc;
			data=dest;
			//			defs=new LinkedHashMap<>();
			//			links=new LinkedHashMap<>();
			//			constants=new LinkedHashMap<>();
			//			functions=new LinkedHashMap<>();
		}
		public xbin def(final String name,def_func d){
			defs.put(name,ix);
			pl("func "+name+" at "+ix);
			return this;
		}
		public xbin data(final String name,def_data d){
			defs.put(name,ix);
			pl("data "+name+" at "+ix);
			return this;
		}
		public xbin write(final int d){
			data[ix++]=d;
			return this;
		}
		public xbin link_to_def(String name){
			links.put(ix,name);
			pl("ref at "+ix+" to "+name);
			return this;
		}
		public void link(){
			links.entrySet().forEach(me->{
				if(!defs.containsKey(me.getValue())) throw new Error("def not found: "+me.getValue());
				final int addr=defs.get(me.getValue());
				data[me.getKey()]|=(addr<<6);
				pl("linked "+me.getKey()+" to "+me.getValue());
			});
		}
		//		public xbin def_const(String name,def_const constant){
		//			pl("constant "+name+" "+constant);
		//			constants.put(name,constant);
		//			return this;
		//		}
		private LinkedHashMap<String,Integer> defs=new LinkedHashMap<>();
		private LinkedHashMap<Integer,String> links=new LinkedHashMap<>();
		//		private LinkedHashMap<String,def_const>constants;
		//		private LinkedHashMap<String,def>functions;
		private int[] data;
		private int ix;
		public int ix(){
			return ix;
		}
		public int data_location_in_binary_for_name(String src){
			final Integer i=defs.get(src);
			if(i==null)throw new Error("data not found: "+src);
			return i.intValue();
		}
	}
	public static class statement extends a{
		final private String ws;
		public statement(a pt,String nm,reader r){// {}  gives 0 length file
			super(pt,nm);
			ws=r.next_empty_space();
			r.bm();
		}
		public statement(a pt,String nm){// {}  gives 0 length file
			super(pt,nm);
			ws="";
//			r.bm();
		}
		public void source_to(xwriter x){
			x.p(ws);
		}
		public void binary_to(xbin x){}
		//		public void refactor_rename(xwriter x,String arg){}
		//		public void refactor_reorder_function_arguments(xwriter x,String arg){}
		private static final long serialVersionUID=1;
	}
	final public static class block extends statement{
		final private String ws_after_open_block,ws_trailing;
		public block(a pt,String nm,reader r){// {}  gives 0 length file
			super(pt,nm,r);
			if(!r.is_next_char_block_open()) throw new Error(r.line+":"+r.col+" expected {");
			int i=0;
			ws_after_open_block=r.next_empty_space();
			while(true){
				pl("line :"+i);
				if(r.next_empty_space().length()!=0) throw new Error();
				if(r.is_next_char_block_close()) break;
				final data d=new data(this,"i"+i++,r);
				datas.add(d);
			}
			ws_trailing=r.next_empty_space();
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p('{').p(ws_after_open_block);
			datas.forEach(e->e.source_to(x));
			x.p('}').p(ws_trailing);
		}
		@Override public void binary_to(xbin x){
			datas.forEach(e->e.binary_to(x));
		}

		final private ArrayList<data> datas=new ArrayList<>();
		private static final long serialVersionUID=1;
	}
	public static final class data extends statement{
		final private statement expr;
		public data(a pt,String nm,reader r){
			super(pt,nm,r);
			final String src=r.next_token();
			if("def".equals(src)){
				expr=new def(this,"e",r);
				return;
			}
			if(r.is_next_char_expression_open()){
				if("li".equals(src)){
					expr=new call_li(this,"e",r);
				}else if("st".equals(src)){
					expr=new call_st(this,"e",r);
				}else if("stc".equals(src)){
					expr=new call_stc(this,"e",r);
				}else if("lp".equals(src)){
					expr=new call_lp(this,"e",r);
				}else if("inc".equals(src)){
					expr=new call_inc(this,"e",r);
				}else if("add".equals(src)){
					expr=new call_add(this,"e",r);
				}else{
					expr=new call(this,"e",src,r);
				}
				return;
			}
			expr=new expression(this,"e",src,r);
		}
		@Override public void binary_to(xbin x){
			expr.binary_to(x);
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			expr.source_to(x);
		}
		private static final long serialVersionUID=1;
	}
	public static class call extends statement{
		private String name,ws_after_name,ws_trailing;
		protected ArrayList<expression> arguments;
		public call(a pt,String nm,String name,reader r){
			super(pt,nm,r);
			this.name=name;
			ws_after_name=r.next_empty_space();
			arguments=new ArrayList<>();
			while(true){
				if(r.is_next_char_expression_close()) break;
				final expression arg=new expression(pt,nm,r);
				arguments.add(arg);
			}
			ws_trailing=r.next_empty_space();
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p(name).p("(").p(ws_after_name);
			arguments.forEach(e->e.source_to(x));
			x.p(")").p(ws_trailing);
		}
		@Override public void binary_to(xbin x){
			final def_func d=(def_func)x.toc.get("func "+name);
			if(d==null) throw new Error("function not found: "+name);
			if(arguments.size()!=d.arguments.size()) throw new Error("number of arguments do not match: "+name);
			int i=0;
			for(expression e:arguments){
				final expression a=d.arguments.get(i++);
				final int rdi=a.src.charAt(0)-'a';
				final int in=0x0000|(0&15)<<8|(rdi&15)<<12;
				x.write(in);
				x.write(e.eval(x));
			}
			x.link_to_def(name);
			x.write(0x0010);//call
		}
		private static final long serialVersionUID=1;
	}
	final public static class def extends statement{
		final private String name,ws_after_name;
		final private statement e;
		public def(a pt,String nm,reader r){
			super(pt,nm,r);
			name=r.next_token();
			ws_after_name=r.next_empty_space();
			if(r.is_next_char_expression_open()){
				e=new def_func(this,name,name,r);
			}else if(r.is_next_char_block_open()){
				r.unread_last_char();
				e=new def_data(this,name,name,r);
			}else{
				e=new def_const(this,name,name,r);
			}
		}
		@Override public void binary_to(xbin x){
			e.binary_to(x);
		}
		@Override public void source_to(xwriter x){
			x.p("def");
			super.source_to(x);
			x.p(name);
			x.p(ws_after_name);
			e.source_to(x);
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_func extends statement{
		final private String name,ws_after_expr_close;
		final private ArrayList<expression> arguments=new ArrayList<>();
		final private block function_code;
		public def_func(a pt,String nm,String name,reader r){
			super(pt,nm,r);
			this.name=name;
			int i=0;
			while(true){
				if(r.is_next_char_expression_close()) break;
				final expression arg=new expression(this,""+i++,r);
				arguments.add(arg);
			}
			ws_after_expr_close=r.next_empty_space();
			function_code=new block(this,"c",r);
			r.toc.put("func "+name,this);
		}
		@Override public void binary_to(xbin x){
			x.def(name,this);
			function_code.binary_to(x);
			x.write(8);//ret // if last instr 4 set last instr 4+8
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p("(");
			arguments.forEach(e->e.source_to(x));
			x.p(")").p(ws_after_expr_close);
			function_code.source_to(x);
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_const extends statement{
		final private String ws_trailing;
		final private expression expr;
		public def_const(a pt,String nm,String name,reader r){
			super(pt,nm,r);
			expr=new expression(this,"e",r);
			ws_trailing=r.next_empty_space();
			r.toc.put("const "+name,this);
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			expr.source_to(x);
			x.p(ws_trailing);
		}
		private static final long serialVersionUID=1;
	}
	final public static class def_data extends statement{
		final private block data;
		final private String name;
		public def_data(a pt,String nm,String name,reader r){
			super(pt,nm,r);
			this.name=name;
			data=new block(this,"d",r);
			r.toc.put("data "+name,this);
		}
		@Override public void binary_to(xbin x){
			x.data(name,this);
			data.binary_to(x);
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			data.source_to(x);
		}
		private static final long serialVersionUID=1;
	}
	final public static class call_li extends call{
		public call_li(a pt,String nm,reader r){
			super(pt,nm,"li",r);
		}
		@Override public void binary_to(xbin x){
			//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
			final expression rd=arguments.get(0);
			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
			final int rdi=rd.src.charAt(0)-'a';
			final int i=rdi<<12;
			x.write(i);
			final expression imm=arguments.get(1);
			x.write(imm.eval(x));
		}
		private static final long serialVersionUID=1;
	}
	final public static class call_st extends call{
		public call_st(a pt,String nm,reader r){
			super(pt,nm,"st",r);
		}
		@Override public void binary_to(xbin x){
			//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
			final expression ra=arguments.get(0);
			if(ra.src.length()!=1) throw new Error("not a register: "+ra.src);
			final int rai=ra.src.charAt(0)-'a';
			if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
			final expression rd=arguments.get(1);
			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
			final int rdi=rd.src.charAt(0)-'a';
			if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
			final int i=0x00d8|(rai&15)<<8|(rdi&15)<<12;
			x.write(i);
		}
		private static final long serialVersionUID=1;
	}
	final public static class call_add extends call{
		public call_add(a pt,String nm,reader r){
			super(pt,nm,"add",r);
		}
		@Override public void binary_to(xbin x){
			//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
			final expression ra=arguments.get(0);
			if(ra.src.length()!=1) throw new Error("not a register: "+ra.src);
			final int rai=ra.src.charAt(0)-'a';
			if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
			final expression rd=arguments.get(1);
			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
			final int rdi=rd.src.charAt(0)-'a';
			if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
			final int i=0x00a0|(rai&15)<<8|(rdi&15)<<12;
			x.write(i);
		}
		private static final long serialVersionUID=1;
	}
	final public static class call_inc extends call{
		public call_inc(a pt,String nm,reader r){
			super(pt,nm,"inc",r);
		}
		@Override public void binary_to(xbin x){
			//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
			//			final expression ra=arguments.get(0);
			//			if(ra.src.length()!=1) throw new Error("not a register: "+ra.src);
			//			final int rai=ra.src.charAt(0)-'a';
			//			if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
			final expression rd=arguments.get(0);
			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
			final int rdi=rd.src.charAt(0)-'a';
			if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
			final int i=0x0200|(0&15)<<8|(rdi&15)<<12;
			//? inc reg imm4
			x.write(i);
		}
		private static final long serialVersionUID=1;
	}
	final public static class call_stc extends call{
		public call_stc(a pt,String nm,reader r){
			super(pt,nm,"stc",r);
		}
		@Override public void binary_to(xbin x){
			//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
			final expression ra=arguments.get(0);
			if(ra.src.length()!=1) throw new Error("not a register: "+ra.src);
			final int rai=ra.src.charAt(0)-'a';
			if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
			final expression rd=arguments.get(1);
			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
			final int rdi=rd.src.charAt(0)-'a';
			if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
			final int i=0x0040|(rai&15)<<8|(rdi&15)<<12;
			x.write(i);
		}
		private static final long serialVersionUID=1;
	}
	final public static class expression extends statement{
		final private String src,ws_after;
		public expression(a pt,String nm,reader r){
			super(pt,nm,r);
			src=r.next_token();
			ws_after=r.next_empty_space();
		}
		public expression(a pt,String nm,String src,reader r){
			super(pt,nm);
			this.src=src;
			ws_after=r.next_empty_space();
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p(src).p(ws_after);
		}
		@Override public void binary_to(xbin x){
			x.write(eval(x));
		}
		public int eval(xbin b){
			final def_const dc=(def_const)b.toc.get("const "+src);
			if(dc!=null){ return dc.expr.eval(b); }
			final def_data dd=(def_data)b.toc.get("data "+src);
			if(dd!=null){ return b.data_location_in_binary_for_name(src); }
			if(src.startsWith("0x")){
				try{
					return Integer.parseInt(src.substring(2),16);
				}catch(NumberFormatException e){
					throw new Error("not a hex: "+src);
				}
			}else if(src.startsWith("0b")){
				try{
					return Integer.parseInt(src.substring(2),2);
				}catch(NumberFormatException e){
					throw new Error("not a binary: "+src);
				}
			}else if(src.endsWith("h")){
				try{
					return Integer.parseInt(src.substring(0,src.length()-1),16);
				}catch(NumberFormatException e){
					throw new Error("not a hex: "+src);
				}
			}else{
				try{
					return Integer.parseInt(src);
				}catch(NumberFormatException e){
					throw new Error("not a number: "+src);
				}
			}
		}
		private static final long serialVersionUID=1;
	}
	final public static class call_lp extends statement{
		final private ArrayList<expression> arguments=new ArrayList<>();
		final private String ws_after_expression_open,ws_after_expression_closed;
		final private block loop_code;
		public call_lp(a pt,String nm,reader r){
			super(pt,nm,r);
			ws_after_expression_open=r.next_empty_space();
			int i=0;
			while(true){
				if(r.is_next_char_expression_close()) break;
				final expression arg=new expression(this,"e"+i++,r);
				arguments.add(arg);
			}
			ws_after_expression_closed=r.next_empty_space();
			//			if(!r.is_next_char_block_open())throw new Error("expected { for loop code");
			loop_code=new block(this,"b",r);
		}
		@Override public void binary_to(xbin x){
			//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
			final expression rd=arguments.get(0);
			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
			final int rdi=rd.src.charAt(0)-'a';
			if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
			final int rai=0,znxr=0;
			final int i=0x0100|znxr|(rai&15)<<8|(rdi&15)<<12;
			x.write(i);
			loop_code.binary_to(x);
			x.write(4);//nxt
		}
		@Override public void source_to(xwriter x){
			x.p("lp");
			super.source_to(x);
			x.p("(");
			x.p(ws_after_expression_open);
			arguments.forEach(e->e.source_to(x));
			x.p(")");
			x.p(ws_after_expression_closed);
			loop_code.source_to(x);
		}
		private static final long serialVersionUID=1;
	}

}
