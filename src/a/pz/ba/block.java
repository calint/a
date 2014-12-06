package a.pz.ba;

import java.util.ArrayList;
import b.a;
import b.xwriter;

final public class block extends statement{
	final private String ws_after_open_block,ws_trailing;
	public block(a pt,String nm,reader r){// {}  gives 0 length file
		super(pt,nm,r);
		if(!r.is_next_char_block_open()) throw new Error(r.line+":"+r.col+" expected {");
		int i=0;
		ws_after_open_block=r.next_empty_space();
		while(true){
			//				pl("line :"+i);
			if(r.next_empty_space().length()!=0) throw new Error();
			if(r.is_next_char_block_close()) break;
			final data d=new data(this,"i"+i++,r);
			datas.add(d);
		}
		ws_trailing=r.next_empty_space();
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p('{').p(ws_after_open_block);
		datas.forEach(e->e.source_to(x));
		x.p('}').p(ws_trailing);
	}
	@Override public void binary_to(xbin x){
		datas.forEach(e->e.binary_to(x));
	}

	final private ArrayList<data> datas=new ArrayList<>();
	private static final long serialVersionUID=1;
}