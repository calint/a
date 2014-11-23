package d;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
public class cujo implements Serializable{
	public static void main(String[]args)throws IOException{
		System.out.println("cujo on port 8080");
		try(final ServerSocket ss=new ServerSocket(8080)){
			int served=0,requests=0;
			while(true){
				final Socket s=ss.accept();
				requests++;
				s.getOutputStream().write("HTTP/1.1 200 OK\r\nContent-Length: 11\r\n\r\nhello world".getBytes());
				s.close();
				System.out.println(s.getRemoteSocketAddress());
				served++;
				System.out.println(" • cujo req "+requests+" served "+served);
			}
		}
	}
	private static final long serialVersionUID=1;
}
