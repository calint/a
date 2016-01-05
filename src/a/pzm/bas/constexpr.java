package a.pzm.bas;



public abstract class constexpr extends stmt{
//		public constexpr(final program p){super(p);}
		public constexpr(program p,String txt){super(p);this.txt=txt;}
		public abstract int eval(final program p);

		public static constexpr from(program p,String src){
			// &dots
			if(src.startsWith("&")){
				final String nm=src.substring(1);
				final def_label dl=p.labels.get(nm);
				if(dl==null)
					throw new compiler_error(p.location_in_source(),"label not found: "+nm);
				final int i=dl.location_in_binary;
				return new constexpr_int(p,i);
			}
				
			// linewi
			final def_const dc=p.defines.get(src);
			if(dc!=null)
				return constexpr.from(p,dc.value);
			
			// linewi-wi
			final int i1=src.lastIndexOf('-');
			final int i2=src.lastIndexOf('+');
			if(i1!=-1&&i2==-1       )return new constexpr_add(p,src.substring(0,i1),src.substring(i1+1),true);
			if(i1!=-1&&i2!=-1&&i1>i2)return new constexpr_add(p,src.substring(0,i1),src.substring(i1+1),true);
			if(i2!=-1&&i1==-1       )return new constexpr_add(p,src.substring(0,i2),src.substring(i2+1),false);
			if(i2!=-1&&i1!=-1&&i2>i1)return new constexpr_add(p,src.substring(0,i2),src.substring(i2+1),false);
			try{return new constexpr_int(p,Integer.parseInt(src,16));}catch(Throwable t){throw new compiler_error("","not a hex: "+src);}
		}
		private static final long serialVersionUID=1;
	}