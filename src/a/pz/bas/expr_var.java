package a.pz.bas;

import java.io.IOException;
import a.pz.compiler_error;
import a.pz.li;
import a.pz.program;
import a.pz.stmt;
import b.xwriter;

final public class expr_var extends expr{
	public String rh;
	public expr_var(final program p) throws IOException{
		super(p,p.next_token_in_line());
		p.allocate_register(this,to_register);
		if(p.is_next_char_equals())
			rh=p.next_token_in_line();
		if(!p.is_next_char_end_of_line()&&!p.is_next_char_end_of_file())
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
	@Override public void compile(program p){
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
	@Override public void link(program p){
		if(rh==null)
			return;
		final constexpr ce=constexpr.from(p,rh);
		final int i=ce.eval(p);
		bin[1]=i;
	}
	private static final long serialVersionUID=1;
}