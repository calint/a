package a.civ;

import java.io.OutputStream;

import a.civ.$.clickable;
import static b.b.*;

public class game{
	class unit{}
	class tile{}
	class map{
		public void refresh_console(OutputStream os)throws Throwable{
			final int hi=12,wi=8;
			final byte[]nl="\n".getBytes();
			for(int i=0;i<hi;i++){
				for(int j=0;j<wi;j++){
//					os.write("_".getBytes());
//					os.write("/.\\_".getBytes());
					os.write("/..\\__".getBytes());
				}
				os.write("/".getBytes());
				os.write(nl);
				for(int j=0;j<wi;j++){
//					os.write("_".getBytes());
//					os.write("\\_/.".getBytes());
					os.write("\\__/..".getBytes());
				}
				os.write("\\".getBytes());
				os.write(nl);
			}
			os.write(nl);
		}
	}
	class player{}
	
	
//	/..\__/
//	\__/..\
//	/..\__/
//	\__/..\
	
	
	@clickable public void reset(){}
	public void refresh_console(OutputStream os)throws Throwable{
		os.write(tobytes("turn: "+turn+"\n\b"));
		map.refresh_console(os);
		turn++;
	}
	
	private map map=new map();
	private int turn;
}
