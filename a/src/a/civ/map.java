package a.civ;
import static b.b.tobytes;
import java.io.OutputStream;
import b.a;
import b.xwriter;
class map extends a{
	public void to(xwriter x)throws Throwable{
		for(int i=0;i<hi;i++){
			for(int j=0;j<wi;j++){
				data[i][j]=String.valueOf(i+1)+String.valueOf((char)('a'+j));
			}
		}
		
		final OutputStream os=x.outputstream();
		final byte[]nl="\n".getBytes();
		for(int i=0;i<(hi-1);i++){
			for(int j=0;j<(wi-1);j+=2){
				os.write(tobytes("/"+maptile(i,j)+"\\__"));
			}
			os.write("/".getBytes());
			os.write(nl);
			for(int j=0;j<wi;j+=2){
				os.write(tobytes("\\__/"+maptile(i,j+1)));
			}
			os.write("\\".getBytes());
			os.write(nl);
		}
		os.write(nl);
	}
	private String maptile(int i,int j){
		return data[i][j]==null?"  ":data[i][j];
	}
	
	final int hi=10,wi=10;
	String[][]data=new String[hi+1][wi+1];
}