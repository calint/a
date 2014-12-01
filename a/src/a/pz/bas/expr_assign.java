package a.pz.bas;

import java.io.IOException;
import a.pz.compiler_error;
import a.pz.instr;
import a.pz.ld;
import a.pz.ldc;
import a.pz.li;
import a.pz.program;
import a.pz.tx;
import b.xwriter;

final public class expr_assign extends expr{
		String rh;
		boolean is_ld;
		boolean is_ldc;
		public expr_assign(final program p,final String register) throws IOException{
			super(p,register);
//			if(!p.is_register_allocated(register))
//				throw new compiler_error(this,"var '"+register+"' has not been declared");
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
		}
		@Override public void compile(program p){
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
		@Override public void link(program p){
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
			final int i=constexpr.from(p,rh).eval(p);
			bin[1]=i;
		}
		private static final long serialVersionUID=1;
	}