package a.civ;
import java.io.*;//import java.io;
import b.a;
public class atape extends a{
	public String resource_path;
	public void reset(){
		is=getClass().getResourceAsStream(resource_path);
	}
	private InputStream is;
	public atape stream_next_file_name(final OutputStream os)throws IOException{
		while(true){
			final int ch=is.read();
			if(ch==-1)break;
			if(ch=='\n')break;
			os.write(ch);
		}
		return this;
	}
	public atape stream_next_file_content(OutputStream os)throws IOException{
		boolean last_line_empty=false;
		while(true){
			final int ch=is.read();
			if(ch==-1)break;
			if(ch=='\n'){
				if(last_line_empty)break;
				else last_line_empty=true;
			}else last_line_empty=false;
			os.write(ch);
		}
		return this;
	}
	private static final long serialVersionUID=1;
}