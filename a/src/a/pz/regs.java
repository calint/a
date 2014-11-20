package a.pz;
import b.a;
import b.xwriter;
final public class regs extends a{
//	final public int size=16;
	public int[]bits;
	public void to(final xwriter x){
		x.div(this);
		x.p("registers:").nl();
		final String pad="0000";
		for(int i=0;i<bits.length;){
			final String hex=Integer.toHexString(bits[i++]);
			if(hex.length()<4)
				x.p(zn.fld(pad,hex));
			else
				x.p(hex.substring(hex.length()-pad.length()));
			x.spc();
			if((i%4)==0)
				x.nl();
		}
		x.div_();
	}
	public void rst(){for(int i=0;i<bits.length;i++)bits[i]=0;}
	public int getinc(final int ri){return bits[ri]++;}
	public int get(final int ri){return bits[ri];}
	public void setr(final int ri,int d){bits[ri]=d;}
	public void inc(final int ri){bits[ri]++;}
	
	private static final long serialVersionUID=1;
}