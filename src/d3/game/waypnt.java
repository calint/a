package d3.game;
import d3.p3;
final class waypnt{
	final p3 dst;
	double err;
	public waypnt(final p3 dst0,final double err0){
		dst=dst0;
		err=err0;
	}
	public final String toString(){
		return "wayp(dst,err)=("+dst+","+err+")";
	}
}
