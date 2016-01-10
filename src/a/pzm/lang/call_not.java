package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_not extends call{
	private static final long serialVersionUID=1;
	final public static int op=0x60;
	public call_not(statement parent,LinkedHashMap<String,String>annotations,reader r){
		super(parent,annotations,"not",r);
	}
	@Override public void binary_to(xbin x){
		final String rd=arguments.get(0).token;
		if(!x.is_alias_declared(rd))
			throw new compiler_error(arguments.get(0),"var '"+rd+"' not declared",x.register_aliases.toString());

		final int rdi=x.register_index_for_alias(this,rd);
		final int zni=apply_znxr_annotations_on_instruction(op|(rdi&63)<<14);
		x.write(zni);
	}
}