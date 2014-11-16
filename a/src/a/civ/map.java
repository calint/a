package a.civ;
import java.io.PushbackInputStream;

import b.a;
import b.xwriter;
public class map extends a{
//	/..\__/
//	\__/..\
//	/..\__/
//	\__/..\
	public void to(xwriter x)throws Throwable{
		final boolean rend_col_letters=true;
		
		x.p("  ");
		for(int j=0;j<wi;j+=2){
			x.p(" __ ").p("  ");
		}
		x.pl(" ");

		for(int i=0;i<hi;i++){
			x.p(""+(char)(i+'1')+" ");
			for(int j=0;j<(wi-1);j+=2){
				x.p("/").p(tile(i,j)).p("\\__");
			}
			x.pl(i==0?" ":"/");
			x.p("  ");
			for(int j=0;j<wi;j+=2){
				x.p("\\__/").p(tile(i,j+1));
			}
			x.pl("\\");
		}
		x.p("  ");
		for(int j=0;j<(wi-1);j+=2){
			x.p(j==0?" ":"/").p("  ").p("\\__");
		}
		x.pl("/");
		
		if(rend_col_letters){
			x.p("  ");
			for(int j=0;j<wi;j++){
				x.p(" "+(char)(j+'a')+" ");
			}
			x.nl();
		}
	}
	private String tile(int i,int j){
		final tile t=data[i][j];
		if(t==null)return"  ";
		return t.toString();
	}
	
	public void put(PushbackInputStream pis)throws Throwable{
		final int col=pis.read()-'a';
		final int row=pis.read()-'1';
		final int i=pis.read();
		data[row][col]=new tile(""+(char)i+" ");
	}
	public void remove(PushbackInputStream pis)throws Throwable{
		final int col=pis.read()-'a';
		final int row=pis.read()-'1';
		data[row][col]=null;
	}
	public void move(PushbackInputStream pis)throws Throwable{
		final int col=pis.read()-'a';
		final int row=pis.read()-'1';
		final tile t=data[row][col];
		data[row][col]=null;
		if(t==null)throw new Error("tile "+rowcol_to_str(row,col)+" is empty");

		final int a=pis.read();
		if(a=='-'){// ma1-e       qwe asd
			final int dir=pis.read();
			if(dir=='w'){data[row-1][col]=t;return;}
			if(dir=='s'){data[row+1][col]=t;return;}
			if(dir=='e'){data[row-1][col+1]=t;return;}
			
			throw new Error();
		}

		final int ncol=a-'a';
		final int nrow=pis.read()-'1';
		data[nrow][ncol]=t;
	}
	private static String rowcol_to_str(int row,int col){
		return (char)('a'+col)+""+(char)('1'+row);
	}
	public void clear(){
		for(int r=0;r<data.length;r++){
			for(int c=0;c<data[r].length;c++){
				data[r][c]=null;
			}
		}
	}




	public void put(String tile,tile t){
		final int col=tile.charAt(0)-'a';
		final int row=tile.charAt(1)-'1';
		data[row][col]=t;
	}
	public tile get(String tile){
		final int col=tile.charAt(0)-'a';
		final int row=tile.charAt(1)-'1';
		return data[row][col];
	}
	public tile take(String tile){
		final int col=tile.charAt(0)-'a';
		final int row=tile.charAt(1)-'1';
		final tile t=data[row][col];
		data[row][col]=null;
		return t;
	}
	
	
	private final int hi=8,wi=10;
	private tile[][]data=new tile[hi][wi];
}