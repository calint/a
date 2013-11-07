package a.y;
import b.a;
import b.bin;
import b.xwriter;
public class rnd extends a implements bin{
	private static final long serialVersionUID=1;
	public String contenttype(){return "text/plain";}
	public void to(xwriter x)throws Throwable{
		while(true){
			x.p(Double.toString(Math.random())).nl();
			Thread.sleep(10);
		}
	}
}
