package a.pz;

import java.io.IOException;

final public class eof extends instr{
	public eof(program r) throws IOException{
		super(r);
		txt="..";
	}
	@Override public void compile(program p){
		bin=new int[]{-1};
	}
	private static final long serialVersionUID=1;
}