package a.pz.bas;

import java.io.IOException;
import b.xwriter;

final public class def_struct_member extends def{
	private String default_value;
	public def_struct_member(program p) throws IOException{
		super(p);
		name=p.next_identifier();
		type=p.next_type_identifier();
		default_value=p.next_token_in_line();
		final xwriter x=new xwriter();
		x.p(name).spc().p(type).spc().p(default_value);
		txt=x.toString();
	}
	@Override public void validate_references_to_labels(program p) throws compiler_error{
		if(!p.typedefs.containsKey(type))
			throw new compiler_error(this,"type '"+type+"' not found in declared typedefs "+p.typedefs.keySet());
	}
	private static final long serialVersionUID=1;
}