package a.pz;
import b.a;
import b.xwriter;
final public class regs extends a{
	static final long serialVersionUID=1;
	final public int size=16;
	private int[]r=new int[size];
	public void to(final xwriter x){
		x.div(this,"","border:1px dotted","");
		x.p("registers:").nl();
		final String pad="0000";
		for(int i=0;i<r.length;){
			final String hex=Integer.toHexString(r[i++]);
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
	public void rst(){for(int i=0;i<r.length;i++)r[i]=0;}
	public int getinc(final int ri){return r[ri]++;}
	public int get(final int ri){return r[ri];}
	public void setr(final int ri,int d){r[ri]=d;}
	public void inc(final int ri){r[ri]++;}
}