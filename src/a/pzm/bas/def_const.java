package a.pzm.bas;

import java.io.IOException;

final public class def_const extends def{
		public String value;
		public def_const(final program r) throws IOException{
			super(r);
			type=r.next_token_in_line();
			final def_type t=r.typedefs.get(type);
			if(t==null)
				throw new compiler_error(this,"type not found",type);
			name=r.next_token_in_line();
			final def_const d=r.defines.get(name);
			if(d!=null)
				throw new compiler_error(this,"define '"+name+"' already declared at "+d.location_in_source);
			if(!r.is_next_char_equals())
				throw new compiler_error(this,"expected format:  const int a=1");
			value=r.consume_rest_of_line();
			txt="const "+type+" "+name+"="+value;
		}
//		public int toInt(program p){
//			final constexpr ce=constexpr.from(p,value);
//			return ce.calc(p);
////			return Integer.parseInt(value,16);
//		}
		private static final long serialVersionUID=1;
	}