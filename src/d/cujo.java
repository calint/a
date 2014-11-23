package d;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
public class cujo implements Serializable{
	public static class req{
		BufferedReader bir;
		OutputStream os;
		InputStream is;
		Socket so;
		void read_header()throws IOException{
			first_line_in_request=bir.readLine();
			while(!isempty(bir.readLine()));
			final String[]lns=first_line_in_request.split("\\s+");
			request_uri=lns[1];
			final String[]qsa=request_uri.split("\\?");
			requested_path=qsa[0];
			query_string=qsa.length==1?"":qsa[1];
		}
		String first_line_in_request,request_uri,requested_path,query_string;
	}
	public static void main(String[]args)throws IOException{
		pl("cujo");
		try(final ServerSocket ss=new ServerSocket(8080)){
			pl("  on port 8080");
			int served=0,requests=0;
			while(true){
				try{
					requests++;
					final req r=new req();
					r.so=ss.accept();
					r.os=r.so.getOutputStream();
					r.is=r.so.getInputStream();
					r.bir=new BufferedReader(new InputStreamReader(r.is));
					r.read_header();
					final String clsnm="d"+r.requested_path.replace('/','.');
					pl(clsnm);
					try{Class.forName(clsnm).getConstructor(req.class).newInstance(r);}catch(Throwable t){
						r.os.write("HTTP/1.1 500 Error\r\n\r\n".getBytes());
						r.so.close();
						t.printStackTrace();
					}
			//		pl(s.getRemoteSocketAddress());
					served++;
				}catch(Throwable e){e.printStackTrace();}
				pl(" • cujo req "+requests+" served "+served);
			}
		}
	}
	public static boolean isempty(String s){return s==null||s.length()==0;}
	public static void pl(Object o){System.out.println(o.toString());}
	private static final long serialVersionUID=1;
}
