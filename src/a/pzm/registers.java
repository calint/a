package a.pzm;
import b.a;
import b.xwriter;
final public class registers extends a{
	public/*ref*/int[]ints;
	public void to(final xwriter x){
		x.p("fast memory:").nl();
		final String pad="00000";
		for(int i=0;i<ints.length;){
			final String hex=Integer.toHexString(ints[i++]);
			if(hex.length()<5)
				x.p(ide.fld(pad,hex));
			else
				x.p(hex.substring(hex.length()-pad.length()));
			x.spc();
			if((i%4)==0)
				x.nl();
		}
	}
	private static final long serialVersionUID=1;
}