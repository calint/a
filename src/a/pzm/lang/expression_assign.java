package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.xwriter;

public class expression_assign extends expression{
	protected expression_assign(statement parent,annotations annot,token destvar,reader r){
		super(parent,annot,destvar,r,destvar.name);
		mark_start_of_source(r);
		expr_to_assign_to_token=expression.read(this,new annotations(parent,r),destvar.name,r);
		mark_end_of_source(r);
	}
	@Override public void binary_to(xbin x){		
		if(x.vspc().is_declared(expr_to_assign_to_token.token.name)){// tx
			final int rai=x.vspc().get_register_index(this,token.name);
			final int rdi=x.vspc().get_register_index(this,expr_to_assign_to_token.token.name);
			x.write_op(this,call_tx.op,rai,rdi);
			return;
		}
		
		if(destreg!=null){// li
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_li.op,0,rdi);
			x.at_link_eval(this);
			x.write(0,this);
			return;
		}
		
		// constant / data
		x.at_link_eval(this);
		x.write(0,this);
	}
	public int eval(xbin b){
		return expr_to_assign_to_token.eval(b);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("=");
		expr_to_assign_to_token.source_to(x);
	}
	private expression expr_to_assign_to_token;
	private static final long serialVersionUID=1;
}