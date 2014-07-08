package a.pczero;
import b.a;
import b.xwriter;
final public class regs extends a{
	static final long serialVersionUID=1;
	final public int size=16;
	private short[]r=new short[size];
	public void to(final xwriter x){
		x.el(this);
		x.p("registers:").nl();
		final String pad="0000";
		for(int i=0;i<r.length;){
			final String hex=Integer.toHexString(r[i++]);
			if(hex.length()<4)
				x.p(vintage.fld(pad,hex));
			else
				x.p(hex.substring(hex.length()-pad.length()));
			x.spc();
			if((i%4)==0)
				x.nl();
		}
		x.el_();
	}
	public void rst(){for(int i=0;i<r.length;i++)r[i]=0;}
	public short getinc(final int ri){return r[ri]++;}
	public short get(final int ri){return r[ri];}
	public void setr(final int ri,short d){r[ri]=d;}
	public void inc(final int ri){r[ri]++;}
}