package a;
import a.i.txt;
import b.a;
import b.b;
import b.req;
import b.xwriter;
public final class news extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x){
		String s=req.get().query();
		if(b.isempty(s))
			s="today was a good day";
		txt.tag(x,s,"tini",4,0x000000,0xffffff);
	}
}
