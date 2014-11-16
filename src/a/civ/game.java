package a.civ;

import java.io.OutputStream;

import a.civ.$.clickable;

public class game{
	class unit{}
	class tile{}
	class map{}
	class player{}
	
	
	
	@clickable public void reset(){}
	public void refresh_console(OutputStream os)throws Throwable{
		os.write("bmp width height depth".getBytes());
	}
}
