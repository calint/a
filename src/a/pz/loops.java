package a.pz;
import b.a;
import b.xwriter;
public class loops extends a{
	public void to(final xwriter x){
		x.div(this,"","border:1px dotted","");
		x.p("loop stack:").p(Integer.toHexString(ix)).nl();
		for(int i=0;i<size;){
			x.p(zn.fld("0000",Integer.toHexString(stkadr[i]))).p(":");
			x.p(zn.fld("0000",Integer.toHexString(stkctr[i]))).spc();
			i++;
			if((i%2)==0)
				x.nl();
		}
		x.div_();
	}
	public void push(final int addr,final int counter){
		stkadr[ix]=addr;
		stkctr[ix]=counter;
		ix++;
	}
	public void pop(){
		ix--;
	}
	public void rst(){
		for(int i=0;i<size;i++){
			stkctr[i]=0;
			stkadr[i]=0;
		}
		ix=0;
	}
	public boolean nxt(){
		stkctr[ix-1]--;
		return stkctr[ix-1]==0;
	}
	public int adr(){
		return stkadr[ix-1];
	}
	final static int size=8;
	private int[]stkctr=new int[size];
	private int[]stkadr=new int[size];
	private int ix=0;
	private static final long serialVersionUID=1;
}
