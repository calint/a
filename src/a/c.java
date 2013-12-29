package a;
import a.x.*;
import b.*;
import static b.b.*;
import java.io.*;
public class c extends a implements bin,cacheable{static final long serialVersionUID=1;
	public String contenttype(){return "text/plain;charset=utf8";}
	
	public boolean cacheforeachuser(){return false;}
	public long lastmodupdms(){return 1000;}
	public String filetype(){return "txt";}
	public String lastmod(){return null;}

	public void to(final xwriter x)throws Throwable{
		final req r=req.get();
		final String qs=urldecode(r.query());
//		final path p=r.session().path(qs);
		final path p=path(qs);
		final OutputStream os=new osinc(x.outputstream(),p.parent(),null,null);
		try{p.to(os);}
		catch(final Throwable t){x.p(stacktrace(t));}
		finally{os.close();}
	}
}
