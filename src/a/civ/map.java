package a.civ;
import java.util.Arrays;

import b.*;
class map extends a{
//	/..\__/
//	\__/..\
//	/..\__/
//	\__/..\
	public void to(xwriter x)throws Throwable{
		for(int i=0;i<(hi-1);i++){
			for(int j=0;j<(wi-1);j+=2){
				x.p("/").p(tile(i,j)).p("\\__");
			}
			x.pl("/");
			for(int j=0;j<wi;j+=2){
				x.p("\\__/").p(tile(i,j+1));
			}
			x.pl("\\");
		}
		x.nl();
	}
	private String tile(int i,int j){
		return data[i][j]==null?"  ":data[i][j];
	}
	
	final int hi=10,wi=10;
	private String[][]data=new String[hi+1][wi+1];
	public void put(int row,int col,String str){
		data[row][col]=str;
	}
	public void remove(int row,int col){
		data[row][col]=null;
	}
	public String take(int row,int col){
		final String s=data[row][col];
		if(s==null)throw new Error("nothing to take at "+(char)(row+'a')+(char)(col+'1'));
		data[row][col]="  ";
		return s;
	}
	public void clear(){
		for(int r=0;r<data.length;r++){
			for(int c=0;c<data[r].length;c++){
				data[r][c]=null;
			}
		}
	}
}