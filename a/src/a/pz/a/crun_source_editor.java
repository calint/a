package a.pz.a;
import static b.b.pl;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
	public static class line_numbers extends a{
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
	synchronized public void x_f3(xwriter x,String s) throws Throwable{
		final reader r=new reader(src.reader());
		try{
			final block el=new block(this,"b",r);
			if(x==null) return;
			x.xu(sts.clr());
			el.source_to(x.xub(resrc,true,true));
			x.xube();
			ev(x,this,el);
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
		private PushbackReader r;
		public int line=1,col=1,prevcol=1,nchar=0;
		public reader(Reader r){
			this.r=new PushbackReader(r,1);
		}
		private int read(){
			try{
				final int ch=r.read();
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
	}
	public static final class xbin{
		public xbin(final int[] dest){
			data=dest;
			defs=new LinkedHashMap<>();
			links=new LinkedHashMap<>();
		}
		public xbin def(final String bm){
			defs.put(bm,ix);
			pl("def "+bm+" at "+ix);
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
				final int addr=defs.get(me.getValue());
				if(addr==0)throw new Error("def not found: "+me.getValue());
				data[me.getKey()]|=(addr<<6);
				pl("linked "+me.getKey()+" to "+me.getValue());
			});
		}
		private LinkedHashMap<String,Integer>defs;
		private LinkedHashMap<Integer,String>links;
		public int[] data;
		private int ix;
	}
	public static class el extends a{
		private String ws="";
		public el(a pt,String nm,reader r){// {}  gives 0 length file
			super(pt,nm);
			ws=r.next_empty_space();
			r.bm();
		}
		@Override public void to(xwriter x) throws Throwable{
			//			source_to(x);
		}
		public void source_to(xwriter x){
			x.p(ws);
		}
		public void binary_to(xbin x){}
		//		public void refactor_rename(xwriter x,String arg){}
		//		public void refactor_reorder_function_arguments(xwriter x,String arg){}
		private static final long serialVersionUID=1;
	}
	public static class block extends el{
		private String after_ws;
		public block(a pt,String nm,reader r){// {}  gives 0 length file
			super(pt,nm,r);
			if(!r.is_next_char_block_open()) throw new Error(r.line+":"+r.col+" expected {");
			int i=0;
			after_ws=r.next_empty_space();
			while(true){
				if(r.is_next_char_block_close()) break;
				final data d=new data(this,"i"+i,r);
				datas.add(d);
			}
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p('{').p(after_ws);
			datas.forEach(e->e.source_to(x));
			x.p('}');
		}
		@Override public void binary_to(xbin x){
			datas.forEach(e->e.binary_to(x));
		}

		private ArrayList<data> datas=new ArrayList<>();
		private static final long serialVersionUID=1;
	}
	public static final class data extends el{
		private String src;
		private int i;
		private String ws_after;
		private el expr;
		public data(a pt,String nm,reader r){
			super(pt,nm,r);
			src=r.next_token();
			if("def".equals(src)){
				expr=new def(this,"e",r);
			}else if(r.is_next_char_expression_open()){
				if("li".equals(src)){
					expr=new instruction_li(this,"e",r);
				}else if("st".equals(src)){
					expr=new instruction_st(this,"e",r);
				}else{
					expr=new function_call(this,"e",src,r);
				}
			}else if(src.startsWith("0x")){
				try{
					i=Integer.parseInt(src.substring(2),16);
				}catch(NumberFormatException e){
					throw new Error("not a hex: "+src);
				}
			}else if(src.startsWith("0b")){
				try{
					i=Integer.parseInt(src.substring(2),2);
				}catch(NumberFormatException e){
					throw new Error("not a binary: "+src);
				}
			}else if(src.endsWith("h")){
				try{
					i=Integer.parseInt(src.substring(0,src.length()-1),16);
				}catch(NumberFormatException e){
					throw new Error("not a hex: "+src);
				}
			}else{
				try{
					i=Integer.parseInt(src);
				}catch(NumberFormatException e){
					throw new Error("not a number: "+src);
				}
			}
			ws_after=r.next_empty_space();
		}
		@Override public void binary_to(xbin x){
			if(expr!=null){
				expr.binary_to(x);
				return;
			}
			x.write(i);
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			if(expr!=null){
				expr.source_to(x);
				x.p(ws_after);
				return;
			}
			x.p(src).p(ws_after);
		}
		private static final long serialVersionUID=1;
	}
	public static class function_call extends el{
		private String name,ws_after;
		protected ArrayList<expression> arguments;
		public function_call(a pt,String nm,String name,reader r){
			super(pt,nm,r);
			this.name=name;
			ws_after=r.next_empty_space();
			arguments=new ArrayList<>();
			while(true){
				if(r.is_next_char_expression_close()) break;
				final expression arg=new expression(pt,nm,r);
				arguments.add(arg);
			}
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p(name).p("(").p(ws_after);
			arguments.forEach(e->{
				e.source_to(x);
				//				x.spc();
				});
			x.p(")");
		}
		@Override public void binary_to(xbin x){
			x.link_to_def(name);
			x.write(0x0010);//call
		}
		private static final long serialVersionUID=1;
	}
	public static class def extends el{
		private String name,ws_after_token,ws_after_expr_close;
		protected ArrayList<expression> arguments;
		protected block code;
		public def(a pt,String nm,reader r){
			super(pt,nm,r);
			name=r.next_token();
			ws_after_token=r.next_empty_space();
			if(!r.is_next_char_expression_open()) throw new Error("expected ( and arguments declaration");
			arguments=new ArrayList<>();
			int i=0;
			while(true){
				if(r.is_next_char_expression_close()) break;
				final expression arg=new expression(this,""+i++,r);
				arguments.add(arg);
			}
			ws_after_expr_close=r.next_empty_space();
//			if(!r.is_next_char_block_open()) throw new Error("expected function code within { ... }");
			code=new block(this,"b",r);
		}
		@Override public void binary_to(xbin x){
			x.def(name);
			code.binary_to(x);
			x.write(8);//ret
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p("def").spc().p(name).p(ws_after_token).p("(");
			arguments.forEach(e->{
				e.source_to(x);
				});
			x.p(")").p(ws_after_expr_close);
			code.source_to(x);
		}
		private static final long serialVersionUID=1;
	}
	public static class instruction_li extends function_call{
		public instruction_li(a pt,String nm,reader r){
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
			x.write(imm.eval());
		}
		private static final long serialVersionUID=1;
	}
	public static class instruction_st extends function_call{
		public instruction_st(a pt,String nm,reader r){
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
	public static class expression extends el{
		private String ws_after;
		private String src;
		public expression(a pt,String nm,reader r){
			super(pt,nm,r);
			src=r.next_token();
			ws_after=r.next_empty_space();
		}
		@Override public void source_to(xwriter x){
			super.source_to(x);
			x.p(src).p(ws_after);
		}
		public int eval(){
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
}
