package a.pzm.lang;

final public class def_func_param extends def{
	public def_func_param(def_func parent,reader r){
		super(parent,new annotations(parent,r),r.next_token());
		r.set_location_in_source();
		mark_start_of_source(r);
		mark_end_of_source(r);
	}
	private static final long serialVersionUID=1;
}