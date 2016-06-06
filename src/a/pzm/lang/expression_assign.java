package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

public class expression_assign extends expression{
	private static final long serialVersionUID=1;
	private expression expr_to_assign_to_token;
	protected expression_assign(statement parent,LinkedHashMap<String,String>annot,String varname,reader r){
		super(parent,annot,varname,r,varname);
		mark_start_of_source(r);
		expr_to_assign_to_token=expression.read(this,r.read_annotation(),varname,r);
		mark_end_of_source(r);
	}
	@Override public void binary_to(xbin x){		
		if(x.vspc().is_declared(expr_to_assign_to_token.token)){// tx
			final int rai=x.vspc().get_register_index(this,token);
			final int rdi=x.vspc().get_register_index(this,expr_to_assign_to_token.token);
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
}