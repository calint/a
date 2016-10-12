package a.chex;

import b.a;
import b.xwriter;

public final class square extends a{
	private piece p;
//	{
//		p=new pawn();
//	}
	@Override public void to(xwriter x)throws Throwable{
//		x.p("|");
		if(p!=null){
			p.to(x);
		}else{
			x.p(". ");
		}
//		x.p("]");
	}
	public void set_piece(pawn pc){p=pc;}
	
	
	private static final long serialVersionUID=1;
}
