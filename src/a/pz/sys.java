package a.pz;
import b.a;
import b.xwriter;
public class sys extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		final zn c=(zn)pt(zn.class);
		x.div(this)
			.p(zntkns(c.zn)).spc().p("[").p(zn.fld("000",Integer.toHexString(c.pc))).p("]:").p(zn.fld("0000",Integer.toHexString(c.ir)))
		.div_();
	}
	private String zntkns(final int zn){
		if(zn==0){return "";}
		if(zn==1){return "z";}
		if(zn==2){return "n";}
		if(zn==3){return "p";}
		throw new Error();
	}
}