package a.linklocal;
import java.util.LinkedList;
import b.a;
import b.req;
import b.session;
import b.xwriter;
public class keys extends a{
	static final long serialVersionUID=1;
	public a bits;
	{bits.set(req.get().session().bits());}
	public void to(final xwriter x)throws Throwable{
		x.p("access control bits ").inptxt(bits,this,null,null);
	}
	public void x_(final xwriter x,final String s){
		final session ses=req.get().session();
		ses.bits(bits.tolong());
		
		//? 
		final LinkedList<String>ls=new LinkedList<String>();
		for(final String key:ses.keyset())if(key.startsWith("/"))ls.add(key);
		for(final String key:ls)ses.remove(key);
	}
}
