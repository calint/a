package a.y.chs;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
public class pgn{
	private final Reader reader;
	//private final board board;
	//private int ply;
	private int state;
	private static final int state_header=0;
	private static final int state_header_name=1;
	private static final int state_header_value=2;
	private static final int state_header_end=3;
	private static final int state_body=4;
	private LinkedHashMap<String,String> lhm=new LinkedHashMap<String,String>();
	public pgn(final InputStream is,final board board){
		reader=new InputStreamReader(is);
		//this.board=board;
	}
	public void readHeader() throws IOException{
		StringBuilder hn=new StringBuilder();
		StringBuilder hv=new StringBuilder();
		int ch=reader.read();
		if(ch==-1)
			return;
		switch(state){
		case state_header:
			if(ch=='['){
				state=state_header_name;
			}else if(ch=='\n'){
				state=state_body;
			}
			break;
		case state_header_name:
			if(ch=='"'){
				state=state_header_value;
			}else{
				hn.append((char)ch);
			}
			break;
		case state_header_value:
			if(ch=='"'){
				state=state_header_end;
			}else{
				hv.append((char)ch);
			}
			break;
		case state_header_end:
			if(ch==']'){
				lhm.put(hn.toString().trim(),hv.toString().trim());
				state=state_header;
			}
			break;
		case state_body:
			break;
		}
	}
//	public void nextPly(){
//		this.board.getName();
//		ply++;
//	}
}
