package a.civ;

import java.io.OutputStream;


//import a.civ.$.clickable;
import static b.b.*;

public class game{
	class unit{}
	class tile{}
	class map{
		public void refresh_console(OutputStream os)throws Throwable{
			data[0][0]='1';
			data[0][1]='2';
			data[0][2]='3';
			data[0][3]='4';
			
			data[1][0]='a';
			data[1][1]='b';
			data[1][2]='c';
			data[1][3]='d';
			
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
	
	
//	@clickable public void reset(){}
	public void refresh_console(OutputStream os)throws Throwable{
		os.write(tobytes("turn: "+turn+"\n\b"));
		map.refresh_console(os);
		turn++;
	}
	
	private map map=new map();
	private int turn;
}
