package d;

import static d.cujo.pl;
import java.io.IOException;
import java.io.Serializable;
import d.cujo.req;

public class hello implements Serializable{
	public hello(){}
	public hello(req r)throws IOException{
		pl(this);
		r.os.write("HTTP/1.1 200 OK\r\nContent-Length: 11\r\n\r\nhello world".getBytes());
		r.so.close();
	}
	private static final long serialVersionUID=1;
}
