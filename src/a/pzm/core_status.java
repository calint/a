package a.pzm;

import static a.pzm.ide.fld;

import java.util.Arrays;
import java.util.List;

import b.a;
import b.xwriter;

public class core_status extends a{
	public void to(final xwriter x)throws Throwable{
		final ide z=(ide)pt(ide.class);
		final int instr=z.cor.instruction;
		final List<Integer>spc_at=Arrays.asList(4,8,14);
		int bit=1;
		for(int i=0;i<20;i++){
			final boolean bit_is_set=(instr&bit)==bit;
			if(bit_is_set){
				x.p('o');
			}else{
				x.p('.');
			}
			if(spc_at.contains(i))
				x.spc();
			bit<<=1;
		}
		x.nl();
		x.p(zntkns(z.cor.flags)).spc().p("[").p(fld("0000",Integer.toHexString(z.cor.program_counter))).p("]:").p(fld("00000",Integer.toHexString(z.cor.instruction)));
	}
	private static String zntkns(final int zn){
//		if(zn==0){return"--";}
//		if(zn==1){return"z-";}
//		if(zn==2){return"-n";}
//		if(zn==3){return"zn";}
		if(zn==0){return"-";}
		if(zn==1){return"z";}
		if(zn==2){return"n";}
		if(zn==3){return"p";}
		throw new Error();
	}
	private static final long serialVersionUID=1;
}