package a.pz.bas;

import java.io.IOException;

final public class call extends instr{
	String label;
	final public static int op=0x0010;
	public call(program r) throws IOException{
		super(r,0,op,null,null);
		label=r.next_token_in_line();
		txt="call "+label;
	}
	@Override public void link(program p){
		def_label l=p.labels.get(label);
		if(l==null)
			throw new compiler_error(this,"label not found",label);
		final int a=l.location_in_binary;
		bin[0]|=(a<<6);
	}
	private static final long serialVersionUID=1;
}