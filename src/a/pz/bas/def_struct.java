package a.pz.bas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import a.pz.program;
import b.xwriter;

final public class def_struct extends def{
	private List<def_struct_member> fields;
	public def_struct(final program p) throws IOException{
		super(p);
		name=p.next_identifier();
		fields=new ArrayList<>();
		while(true){
			p.skip_whitespace_on_same_line();
			if(p.is_next_char_end_of_line())
				break;
			final def_struct_member f=new def_struct_member(p);
			fields.add(f);
		}
		final xwriter x=new xwriter();
		x.p("struct").spc().p(name);
		fields.forEach(f->x.spc().p(f.toString()));
		txt=x.toString();
	}
	@Override protected void validate_references_to_labels(program p){
		fields.forEach(e->e.validate_references_to_labels(p));
	}
	private static final long serialVersionUID=1;
}