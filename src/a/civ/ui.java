package a.civ;

import java.io.OutputStream;

public interface ui{
	interface display{void refresh_console(OutputStream os);}
	interface keyboard{void keyboard_();}
}
