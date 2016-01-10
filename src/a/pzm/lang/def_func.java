package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class def_func extends def{
	private static final long serialVersionUID=1;
	final private String name,ws_before_name,ws_after_name,ws_after_params;
	final ArrayList<def_func_arg>argdefs=new ArrayList<>();
	final statement function_code;
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
			final def_func_arg arg=new def_func_arg(this,r);
			argdefs.add(arg);
		}
		ws_after_params=r.next_empty_space();
		function_code=statement.read(this,r);
		mark_end_of_source(r);
		r.toc.put("func "+name,this);
	}
	public String name(){return name;}
	@Override public void binary_to(xbin x){
//		x.def(name,this);
//		function_code.binary_to(x);
//		x.write(8);//ret // if last instr 4 set last instr 4+8
		return;
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("def").p(ws_before_name).p(name).p(ws_after_name).p("(");
		argdefs.forEach(e->e.source_to(x));
		x.p(")").p(ws_after_params);
		function_code.source_to(x);
	}
}