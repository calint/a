package a.pzm.a;
import a.pzm.core;
import b.a;
import b.xwriter;
final public class loop_stack extends a{
	public core core;
	public void to(final xwriter x){
		final int ix=core.loop_stack_index;
		x.p("loop stack:").p(Integer.toHexString(ix)).nl();
		for(int i=0;i<core.loop_stack_address.length;){
			x.p(acore.fld("00000",Integer.toHexString(core.loop_stack_address[i]))).p(":");
			x.p(acore.fld("00000",Integer.toHexString(core.loop_stack_counter[i]))).spc();
			i++;
			if((i%2)==0)
				x.nl();
		}
	}
	private static final long serialVersionUID=1;
}
