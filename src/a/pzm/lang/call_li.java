package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_li extends call{
	private static final long serialVersionUID=1;
	final public static int op=0;
	public call_li(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(b,annotations,"li",r);
	}
	@Override public void binary_to(xbin x){
		final int rdi=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int i=op|rdi<<14;
		x.write(apply_znxr_annotations_on_instruction(i),this);
		final expression imm=arguments.get(1);
		x.add_at_pre_link_evaluate(imm);
		x.write(0,imm);
	}
}