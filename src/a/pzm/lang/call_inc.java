package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_inc extends call{
	private static final long serialVersionUID=1;
	public call_inc(a pt,String nm,LinkedHashMap<String,String>annotations,reader r,statement b){
		super(pt,nm,b,annotations,"inc",r);
	}
	@Override public void binary_to(xbin x){
		final String rd=arguments.get(0).token;
		if(!x.is_register_alias_exists(rd))
			throw new compiler_error(arguments.get(0),"var '"+rd+"' not declared",x.register_aliases.toString());

		final int rdi=x.register_index_for_alias(this,rd);
		final int zni=apply_znxr_annotations_on_instruction(0x0200|(rdi&63)<<14);
		//? inc reg imm4
		x.write(zni);
	}
}