package d3;
final class hit{
	obj a;
	obj b;
	int pt;
	hit(obj a0,obj b0,int pt0){
		a=a0;
		b=b0;
		pt=pt0;
	}
	public final String toString(){
		return "hit(a,b)=("+a+","+b+")";
	}
}
