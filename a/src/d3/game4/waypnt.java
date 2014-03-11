package d3.game4;
import d3.p3;
final class waypnt{
	p3 dst;
	double err;
	public waypnt(p3 dst0,double err0){
		dst=dst0;
		err=err0;
	}
	public final String toString(){
		return "wayp(dst,err)=("+dst+","+err+")";
	}
}
