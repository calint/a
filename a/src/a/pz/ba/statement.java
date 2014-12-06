package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class statement extends a{
	final public static LinkedHashMap<String,String> no_annotations=new LinkedHashMap<>();
	private static final long serialVersionUID=1;
	final private String ws_before;
	final LinkedHashMap<String,String> annotations;
	public statement(a pt,String nm){
		super(pt,nm);
		ws_before="";
		annotations=no_annotations;
	}
	public statement(a pt,String nm,LinkedHashMap<String,String> annotations,reader r){
		super(pt,nm);
		this.annotations=annotations;
		ws_before=r.next_empty_space();
		r.bm();
	}
	public void binary_to(xbin x){}
	public void source_to(xwriter x){
		annotations.entrySet().forEach(me->x.p(me.getKey()).p(me.getValue()));
		x.p(ws_before);
	}
	final @Override public void to(xwriter x) throws Throwable{
		source_to(x);
	}
	public boolean has_annotation(String src){
		return annotations.containsKey(src);
	}

}