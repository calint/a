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
		return data[i][j]==null?"  ":data[i][j];
	}
	
	private final int hi=8,wi=10;
	private String[][]data=new String[hi+1][wi+1];
	public void put(PushbackInputStream pis)throws Throwable{
		final int col=pis.read()-'a';
		final int row=pis.read()-'1';
		final int i=pis.read();
		data[row][col]=(char)i+" ";
	}
	public void remove(PushbackInputStream pis)throws Throwable{
		final int col=pis.read()-'a';
		final int row=pis.read()-'1';
		data[row][col]=null;
	}
	public void move(PushbackInputStream pis)throws Throwable{
		final int col=pis.read()-'a';
		final int row=pis.read()-'1';
		final String s=data[row][col];
		data[row][col]=null;
		if(s==null)throw new Error("tile "+rowcol_to_str(row,col)+" is empty");

		final int ncol=pis.read()-'a';
		final int nrow=pis.read()-'1';
		data[nrow][ncol]=s;
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




	public void put(String tile,String content){
		final int col=tile.charAt(0)-'a';
		final int row=tile.charAt(1)-'1';
		data[row][col]=content;
	}
	public String get(String tile){
		final int col=tile.charAt(0)-'a';
		final int row=tile.charAt(1)-'1';
		return data[row][col];
	}
	
	static class tile{String str;}
}