package a.civ;
import b.a;
import b.xwriter;
public class techs extends a{
	public void to(xwriter x)throws Throwable{
		x.divo(this);
		x.pl("discovered technologies:");
//		x.pl("bombin");
//		x.pl("shootin");
//		x.pl("rocketin");
		x.pl("sharp sticks");
		x.pl("rocks");
		x.div_();
	}
	private static final long serialVersionUID=1;
}
