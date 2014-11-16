package a.civ;
import b.*;
class map extends a{
//	/..\__/
//	\__/..\
//	/..\__/
//	\__/..\
	public void to(xwriter x)throws Throwable{
		for(int i=0;i<(hi-1);i++){
			for(int j=0;j<(wi-1);j+=2){
				x.p("/").p(maptile(i,j)).p("\\__");
			}
			x.pl("/");
			for(int j=0;j<wi;j+=2){
				x.p("\\__/").p(maptile(i,j+1));
			}
			x.pl("\\");
		}
		x.nl();
	}
	public void exec(String ln)throws Throwable{
		if(ln.length()<2)return;
		final int row=ln.charAt(0)-'a';
		final int col=ln.charAt(1)-'1';
		data[row][col]="o ";
	}
	
	
	private String maptile(int i,int j){
		return data[i][j]==null?"  ":data[i][j];
	}
	
	final int hi=10,wi=10;
	private String[][]data=new String[hi+1][wi+1];
}