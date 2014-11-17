package a.civ;
import java.io.*;//import java.io;
import b.a;
public class tape extends a{
	public String resource_path;
	public void reset(){
		is=getClass().getResourceAsStream(resource_path);
	}
	private InputStream is;
	public tape stream_next_file_name(final OutputStream os)throws IOException{
		while(true){
			final int ch=is.read();
			if(ch==-1)break;
			if(ch=='\n')break;
			os.write(ch);
		}
		return this;
	}
	public tape stream_next_file_content(OutputStream os){return this;}
	public String next_file_name(){return null;}
	public String next_file_content(){return null;}
	private static final long serialVersionUID=1;
}
