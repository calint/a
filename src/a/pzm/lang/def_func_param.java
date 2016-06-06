package a.pzm.lang;

import b.xwriter;

final public class def_func_param extends def{
	public def_func_param(def_func parent,reader r){
		super(parent,r.read_annotation());
		ws_leading=r.next_empty_space();
		r.set_location_in_source();
		mark_start_of_source(r);
		token=r.next_token();
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
	}
	@Override public void source_to(xwriter x){super.source_to(x);x.p(ws_leading).p(token).p(ws_trailing);}
	final private String ws_leading,ws_trailing;
	private static final long serialVersionUID=1;
}