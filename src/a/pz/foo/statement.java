package a.pz.foo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class statement extends a{
	final public static LinkedHashMap<String,String> no_annotations=new LinkedHashMap<>();
	private static final long serialVersionUID=1;
	final protected String token;
	final private String ws_after;
	final private LinkedHashMap<String,String> annotations;
	final private String location_in_source;
	final protected block blk;
	public statement(a pt,String nm,LinkedHashMap<String,String> annotations,String loc,block b){
		super(pt,nm);
		this.annotations=annotations;
		token="";
		ws_after="";
		location_in_source=loc;
		blk=b;
	}
	public statement(a pt,String nm,LinkedHashMap<String,String> annotations,String loc,String token,block b){
		super(pt,nm);
		this.annotations=annotations;
		this.token=token;
		ws_after="";
		location_in_source=loc;
		blk=b;
	}
	public statement(a pt,String nm,LinkedHashMap<String,String> annotations,String token,reader r,block b){
		super(pt,nm,token);
		this.annotations=annotations;
		this.token=token;
		ws_after=r.next_empty_space();
		location_in_source=r.bm_line+":"+r.bm_col;
		blk=b;
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
	public static LinkedHashMap<String,String> read_annot(reader r){
		final LinkedHashMap<String,String> annotations=new LinkedHashMap<>();
		while(true){
			if(!r.is_next_char_annotation_open()) break;
			final String s=r.next_token();
			if(s.length()==0) throw new Error("unexpected empty token");
			final String ws=r.next_empty_space();
			annotations.put(s,ws);
		}
		return annotations;
	}

}