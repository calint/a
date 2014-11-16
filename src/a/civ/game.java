package a.civ;

import java.io.OutputStream;

import a.civ.$.clickable;

public class game{
	class unit{}
	class tile{}
	class map{
		public void refresh_console(OutputStream os)throws Throwable{
			os.write("••••\n••••\n••••\n••••".getBytes());
		}
	}
	class player{}
	
	
	
	@clickable public void reset(){}
	public void refresh_console(OutputStream os)throws Throwable{
		map.refresh_console(os);
	}
	
	private map map=new map();
}
