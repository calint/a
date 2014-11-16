package a.civ;
import java.io.*;
import static b.b.*;
import b.*;
public class game extends a{
	class unit extends a{}
	class tile extends a{}
	class map extends a{
		public void to(xwriter x)throws Throwable{
			data[0][0]='1';
			data[0][1]='2';
			data[0][2]='3';
			data[0][3]='4';
			
			data[1][0]='a';
			data[1][1]='b';
			data[1][2]='c';
			data[1][3]='d';
			final OutputStream os=x.outputstream();
			final byte[]nl="\n".getBytes();
			for(int i=0;i<hi;i++){
				for(int j=0;j<wi;j+=2){
					os.write(tobytes("/"+maptile(i,j)+" \\__"));
				}
				os.write("/".getBytes());
				os.write(nl);
				for(int j=0;j<wi;j+=2){
					os.write(tobytes("\\__/"+maptile(i,j+1)+" "));
				}
				os.write("\\".getBytes());
				os.write(nl);
			}
			os.write(nl);
		}
		private String maptile(int i,int j) {
			return data[i][j]==0?" ":(""+(char)data[i][j]);
		}
		
		final int hi=8,wi=10;
		int[][]data=new int[hi+1][wi+1];
	}
	class player{}
	
	
//	/..\__/
//	\__/..\
//	/..\__/
//	\__/..\
	
	public void to(xwriter x)throws Throwable{
		x.pl("turn: "+turn+"\n\b");
		map.to(x);
		turn++;
	}
	
	private map map=new map();
	private int turn;
}
