package d3;
import java.io.Serializable;
class pa2 implements Serializable{
	private static final long serialVersionUID=1L;
	int n;
	int x[];
	int y[];
	public pa2(int x[],int y[],int n){
		this.x=x;
		this.y=y;
		this.n=n;
	}
}
