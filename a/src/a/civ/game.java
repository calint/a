package a.civ;

import java.io.IOException;
import java.io.OutputStream;

import a.civ.$.clickable;

public class game{
	class unit{}
	class tile{}
	class map{}
	class player{}
	
	
	
	@clickable public void reset(){}
	public void refresh_console(OutputStream os){
		try{os.write("bmp width height depth".getBytes());}catch(IOException e){throw new Error(e);}
	}
}
