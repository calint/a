package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class def_func extends def{
	public def_func(statement parent,LinkedHashMap<String,String>annot,reader r,String name,String ws_bef_name,String ws_aft_name)throws Throwable{
		super(parent,annot);
		nm(name);
		ws_before_name=ws_bef_name;
		mark_start_of_source(r);
		this.name=name;
		ws_after_name=ws_aft_name;
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_expression_close())break;
			final def_func_param arg=new def_func_param(this,r);
			params.add(arg);
			if(r.is_next_char_expression_close())break;
			if(!r.is_next_char_comma())throw new compiler_error(arg,"expected comma after expression",arg.toString());
		}
		ws_after_params=r.next_empty_space();
		function_code=statement.read(this,r);
		mark_end_of_source(r);
		r.toc.put("func "+name,this);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("def").p(ws_before_name).p(name).p(ws_after_name).p("(");
		int i=params.size();
		for(final def_func_param e:params){
			e.source_to(x);
			i--;
			if(i!=0)x.p(',');
		}
		x.p(")").p(ws_after_params);
		function_code.source_to(x);
	}
	final ArrayList<def_func_param>params=new ArrayList<>();
	final statement function_code;
	final private String name,ws_before_name,ws_after_name,ws_after_params;
	private static final long serialVersionUID=1;
}