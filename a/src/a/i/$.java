package a.i;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import b.a;
import b.b;
public class $ extends a{
	static final long serialVersionUID=1;
	public static Font fontget(final String name,final float size) throws FileNotFoundException, FontFormatException, IOException{
		Font font=Font.createFont(Font.TRUETYPE_FONT,b.path("ttf/"+name+".ttf").inputstream());//? cache
		font=font.deriveFont(size);
		return font;
	}
	
}
