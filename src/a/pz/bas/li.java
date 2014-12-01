package a.pz.bas;

import java.io.IOException;

final public class li extends instr{
		private String data;
//		private int value;
		private constexpr ce;
		final public static int op=0x0000;
		public li(program r) throws IOException{
			super(r,0,op,null,r.next_token_in_line());
			data=r.next_token_in_line();
			txt="li "+rd+" "+data;
		}
		public li(program r,String reg,constexpr ce){
			super(r,0,op,null,reg);
			this.ce=ce;
			txt="li "+reg+" "+ce;
		}
		@Override public void compile(program p){
			bin=new int[]{znxr_ci__ra__rd__(),0};
		}
		@Override public void link(program p){
			if(ce!=null){
				bin[1]=ce.eval(p);
				return;
			}
			bin[1]=constexpr.from(p,data).eval(p);
//			
//			final def_const def=p.defines.get(data);
//			if(def!=null){
//				data=def.value;
//			}
//			if(data.startsWith("&")){
//				final String nm=data.substring(1);
//				final def_label l=p.labels.get(nm);
//				if(l==null)
//					throw new compiler_error(this,"label not found",nm);
//				value=l.location_in_binary;
//			}else{
//				try{
//					value=Integer.parseInt(data,16);
//				}catch(NumberFormatException e){
//					throw new compiler_error(this,"cannot parse number '"+data+"'");
//				}
//			}
//			final int bit_width=16;
//			//			final int i=Integer.parseInt(data,16);
//			final int max=(1<<(bit_width-1))-1;
//			final int min=-1<<(bit_width-1);
//			if(value>max)
//				throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
//			if(value<min)
//				throw new compiler_error(location_in_source,"number '"+data+"' out of "+bit_width+" bits range");
//			bin=new int[]{bin[0],value};
		}
		private static final long serialVersionUID=1;
	}