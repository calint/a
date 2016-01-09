package a.pzm.lang;

import b.xwriter;

final public class def_func_arg extends def{
	final private String ws_leading,ws_trailing;
	public def_func_arg(def_func parent,reader r){
		super(parent,null);
		ws_leading=r.next_empty_space();
		r.set_location_in_source();
		mark_start_of_source(r);
		token=r.next_token();
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
	}
	@Override public void binary_to(xbin x){
		return;
	}
	@Override public void source_to(xwriter x){
		x.p(ws_leading).p(token).p(ws_trailing);
	}
	private static final long serialVersionUID=1;
}