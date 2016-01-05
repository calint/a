package a.pzm.a;

import static a.pzm.a.acore.fld;

import b.a;
import b.xwriter;

public class core_status extends a{
	public void to(final xwriter x)throws Throwable{
		final acore z=(acore)pt(acore.class);
		x.p(zntkns(z.cor.flags)).spc().p("[").p(fld("000",Integer.toHexString(z.cor.program_counter))).p("]:").p(fld("0000",Integer.toHexString(z.cor.instruction)));
	}
	private static String zntkns(final int zn){
		if(zn==0){return"--";}
		if(zn==1){return"z-";}
		if(zn==2){return"-n";}
		if(zn==3){return"zn";}
		throw new Error();
	}
	private static final long serialVersionUID=1;
}