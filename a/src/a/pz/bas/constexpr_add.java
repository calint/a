package a.pz.bas;



public final class constexpr_add extends constexpr{
		private String lhs,rhs;
		private boolean neg;
		public constexpr_add(program p,String lhs,String rhs,boolean neg){
			super(p,lhs+(neg?"-":"")+rhs);this.lhs=lhs;this.rhs=rhs;this.neg=neg;
//			txt=lhs+(neg?"-":"")+rhs;
		}
		@Override public int eval(program p){
			final constexpr lh=from(p,lhs);
			final constexpr rh=from(p,rhs);
			final int lhi=lh.eval(p);
			final int rhi=rh.eval(p);
			if(neg)
				return lhi-rhi;
			return lhi+rhi;
		}
		private static final long serialVersionUID=1;
	}