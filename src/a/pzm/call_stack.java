package a.pzm;
import b.a;
import b.xwriter;
final public class call_stack extends a{
	public void to(final xwriter x){
		x.p("call stack:").p(Integer.toHexString(ix)).nl();
		for(int i=0;i<stk.length;){
			x.p(ide.fld("00000",Integer.toHexString(stk[i++]))).spc();
			if((i%4)==0)
				x.nl();
		}
	}
	public void push(final int v){
		stk[ix]=v;
		ix++;
	}
	public int pop(){
		ix--;
		return stk[ix];
	}
	public void rst(){
		ix=0;
		for(int i=0;i<stk.length;i++){
			stk[i]=0;
		}
	}
	public int top(){
		return stk[ix-1];
	}
	public int[]stk;
	private int ix=0;
	private static final long serialVersionUID=1;
}
