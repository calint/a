package a.y;
import b.a;
import b.xwriter;
public class sine extends a {
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		final int n=128;
		x.pl("sin:");
		x.p(". ").p(Integer.toHexString(n)).nl();
		x.p(". ");
		for(int i=0;i<n;i++){
			final double dd=Math.PI*2*i/n;
			final short d=(short)(dd*Short.MAX_VALUE);
			String s=Integer.toHexString(d).trim();
			if(s.length()>4){
				s=s.substring(s.length()-4);
			}
			if(s.length()<4){
				s="0000".substring(s.length())+s;
			}
			x.p(s).spc();
		}
		x.nl();
	}
}
