package a.pcmega;
import b.a;
import b.xwriter;
final public class regs extends a{
	static final long serialVersionUID=1;
	final public int size=16*4;
	private int[]r=new int[size];
	public void to(final xwriter x){
		x.el(this);
		x.p("registers:").nl();
		final String pad="00000";
		for(int i=0;i<r.length;i++){
			final String hex=Integer.toHexString(r[i]);
			if(hex.length()<pad.length())
				x.p(vintage.fld(pad,hex));
			else
				x.p(hex.substring(hex.length()-pad.length()));
			x.spc();
			if((i&0x3)==3)
				x.nl();
			if((i&0xf)==0xf)
				x.nl();
		}
//		x.nl();
		x.el_();
	}
	public void rst(){for(int i=0;i<r.length;i++)r[i]=0;}
	public int getinc(final int ri){return r[ri]++;}
	public int get(final int ri){return r[ri];}
	public void setr(final int ri,int d){r[ri]=d;}
	public void inc(final int ri){r[ri]++;}
}