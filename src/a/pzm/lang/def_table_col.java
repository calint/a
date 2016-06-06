package a.pzm.lang;

final public class def_table_col extends statement{
	public def_table_col(statement parent,annotations annot,reader r)throws Throwable{
		super(parent,annot,r.next_token(),r);
		if(token.name.length()==0)throw new compiler_error(this,"unexpected empty token","");
		mark_end_of_source(r);
	}
	private static final long serialVersionUID=1;
}