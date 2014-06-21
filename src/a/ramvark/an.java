package a.ramvark;

import b.a;

public class an extends a{
	public int bits;
	public a set_bit(final int index_starting_at_zero,final boolean on){
		final int i1=1;
		final int i2=i1<<index_starting_at_zero;
		if(on){
			bits|=i2;
		}else{
			final int msk=-1^i2;
			bits&=msk;
		}
		return this;
	}
	public boolean has_bit(final int index_starting_at_zero){
		final int i1=1;
		final int i2=i1<<index_starting_at_zero;
		final boolean b=(bits&i2)!=0;
		return b;
	}
	
	private static final long serialVersionUID=1;
}
