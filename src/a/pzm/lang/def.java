package a.pzm.lang;

import a.pzm.lang.reader.token;

public class def extends statement{
	public static def read(statement parent,annotations annot,token deftkn,reader r)throws Throwable{
		final token identtkn=r.next_token();
		if(r.is_next_char_expression_open())
			return new def_func(parent,annot,deftkn,identtkn,r);
		
		if(r.is_next_char_block_open()){
			r.set_location_cue();
			return new def_data(parent,annot,deftkn,identtkn,r);
		}
		
		if(r.is_next_char_struct_open())
			return new def_table(parent,annot,deftkn,identtkn,r);
		
		return new def_const(parent,annot,deftkn,identtkn,r);
	}

	public def(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	private static final long serialVersionUID=1;
}