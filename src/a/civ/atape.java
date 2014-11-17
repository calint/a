package a.civ;
import java.io.*;//import java.io;
import b.a;
import b.xwriter;
public class atape extends a{
	public String resource_path="data.tape";
	public void reset(){
		is=getClass().getResourceAsStream(resource_path);
	}
	private InputStream is;
	public atape stream_next_file_name(final OutputStream os)throws IOException{
		while(true){
			final int ch=is.read();
			if(ch==-1)throw new Error("1");
			if(ch=='\n')break;
			os.write(ch);
		}
		return this;
	}
	public atape stream_next_file_content(final OutputStream os)throws IOException{
		boolean last_ch_was_nl=false;
		while(true){
			final int ch=is.read();
			if(ch==-1)throw new Error("2");
			if(ch=='\n')if(last_ch_was_nl)break;else last_ch_was_nl=true;else last_ch_was_nl=false;
			os.write(ch);
		}
		return this;
	}
	
	@Override public void to(xwriter x) throws Throwable{
		x.ax(this,"ls","list");
		while(true){}
	}
	private static final long serialVersionUID=1;
}
