package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class statement extends a{
	final public static LinkedHashMap<String,String> no_annotations=new LinkedHashMap<>();
	private static final long serialVersionUID=1;
	final private String token;
	final private String ws_after;
	final private LinkedHashMap<String,String> annotations;
	final private String location_in_source;
	public statement(a pt,String nm,LinkedHashMap<String,String> annotations,String loc){
		super(pt,nm);
		this.annotations=annotations;
		token="";
		ws_after="";
		location_in_source=loc;
	}
	public statement(a pt,String nm,LinkedHashMap<String,String> annotations,String token,reader r){
		super(pt,nm);
		this.annotations=annotations;
		this.token=token;
		ws_after=r.next_empty_space();
		location_in_source=r.bm_line+":"+r.bm_col;
//		r.bm();
	}
	public void binary_to(xbin x){}
	public void source_to(xwriter x){
		annotations.entrySet().forEach(me->x.p('@').p(me.getKey()).p(me.getValue()));
		x.p(token).p(ws_after);
	}
	final @Override public void to(xwriter x) throws Throwable{
		source_to(x);
	}
	public boolean has_annotation(String src){
		return annotations.containsKey(src);
	}
	public String location_in_source(){
		return location_in_source;
	}

}