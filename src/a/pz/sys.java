package a.pz;

import b.a;
import b.xwriter;

public class sys extends a{
	public void to(final xwriter x)throws Throwable{
		final acore z=(acore)pt(acore.class);
		x.div(this)
			.p("[").p(acore.fld("000",Integer.toHexString(z.c.program_counter))).p("]:").p(acore.fld("0000",Integer.toHexString(z.c.instruction_register))).spc().p(zntkns(z.c.zn_flags))
		.div_();
	}
	private static String zntkns(final int zn){
		if(zn==0){return"";}
		if(zn==1){return"z";}
		if(zn==2){return"n";}
		if(zn==3){return"p";}
		throw new Error();
	}
	private static final long serialVersionUID=1;
}