package a.civ;
import b.a;
import b.xwriter;
public class techs extends a{
	public void to(xwriter x)throws Throwable{
		x.div(this);
		x.pl("discovered technologies:");
		x.pl("sharpstickin");
		x.pl("rockthrowin");
		x.pl("kickin");
		x.pl("hittin");
		x.pl("shoutin");
		x.pl("shootin");
		x.pl("rocketin");
		x.div_();
	}

	private static final long serialVersionUID=1;
}
