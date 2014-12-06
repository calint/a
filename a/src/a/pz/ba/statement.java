package a.pz.ba;

import b.a;
import b.xwriter;

public class statement extends a{
	final private String ws;
	public statement(a pt,String nm,reader r){// {}  gives 0 length file
		super(pt,nm);
		ws=r.next_empty_space();
		r.bm();
	}
	public statement(a pt,String nm){// {}  gives 0 length file
		super(pt,nm);
		ws="";
		//			r.bm();
	}
	final @Override public void to(xwriter x) throws Throwable{
		source_to(x);
	}
	public void source_to(xwriter x){
		x.p(ws);
	}
	public void binary_to(xbin x){}
	//		public void refactor_rename(xwriter x,String arg){}
	//		public void refactor_reorder_function_arguments(xwriter x,String arg){}
	private static final long serialVersionUID=1;
}