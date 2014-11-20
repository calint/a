package a.pz;
import b.a;
import b.xwriter;
final public class registers extends a{
	public int[]bits;
	public void to(final xwriter x){
		x.div(this);
		x.p("registers:").nl();
		final String pad="0000";
		for(int i=0;i<bits.length;){
			final String hex=Integer.toHexString(bits[i++]);
			if(hex.length()<4)
				x.p(acore.fld(pad,hex));
			else
				x.p(hex.substring(hex.length()-pad.length()));
			x.spc();
			if((i%4)==0)
				x.nl();
		}
		x.div_();
	}
	private static final long serialVersionUID=1;
}