package d3;
import java.io.IOException;
import java.io.InputStream;
public class xis{
	private InputStream is;
	public xis(InputStream is){
		this.is=is;
	}
	public double double_read() throws IOException{
		final StringBuilder sb=new StringBuilder(32);
		int chi=is.read();
		while(Character.isWhitespace(chi))
			chi=is.read();
		while(!Character.isWhitespace(chi)){
			sb.append((char)chi);
			chi=is.read();
		}
		return Double.parseDouble(sb.toString());
	}
	public int int_read() throws IOException{
		int num=0;
		int chi=is.read();
		while(Character.isWhitespace(chi))
			chi=is.read();
		while(Character.isDigit(chi)){
			num*=10;
			num+=chi-'0';
			chi=is.read();
		}
		return num;
	}
}
