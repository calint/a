package a.civ;
import java.io.*;//import java.io;
import b.a;
public class tape extends a{
	public String resource_path;
	public void rewind(){}
	public String next_file_name(){return null;}
	public String next_file_content(){return null;}
	public tape stream_next_file_name(OutputStream os){return this;}
	public tape stream_next_file_content(OutputStream os){return this;}
	private static final long serialVersionUID=1;
}
