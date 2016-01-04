package a.pcmega;
import b.a;
import b.xwriter;
public class calls extends a{
	static final long serialVersionUID=1;
	final int size=16;
	private int[]stk=new int[size];
	private int ix=0;
	public void to(final xwriter x){
		x.el(this);
		x.p("call stack:").p(Integer.toHexString(ix)).nl();
		for(int i=0;i<stk.length;){
			x.p(vintage.fld("00000",Integer.toHexString(stk[i++]))).spc();
			if((i%4)==0)
				x.nl();
		}
		x.el_();
	}
	public void push(final int v){
		stk[ix]=v;
		ix++;
	}
	public int pop(){
		ix--;
		return stk[ix];
	}
	public void rst(){ix=0;for(int i=0;i<stk.length;i++){
		stk[i]=0;
	}}
	public int top(){return stk[ix-1];}
}
