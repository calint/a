package a.pzm.bas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import b.xwriter;

final public class def_func extends def{
		public List<def_func_arg> args=new ArrayList<>();
		protected program code_block;
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
		@Override public void compile(program p){
			if(code_block!=null){
				code_block.compile(p);
				bin=code_block.bin;
			}
		}
		@Override public void link(program p){
			if(code_block!=null){
				code_block.link(p);
//				bin=code_block.bin;
			}
		}
		private static final long serialVersionUID=1;
	}