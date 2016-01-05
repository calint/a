package a.pzm.bas;



public final class constexpr_int extends constexpr{
	private int i;
	public constexpr_int(program p,int i){super(p,Integer.toHexString(i));this.i=i;}
	@Override public int eval(program p){return i;}
	private static final long serialVersionUID=1;
}