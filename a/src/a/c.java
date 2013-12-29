package a;
import a.x.*;
import b.*;
import static b.b.*;
import java.io.*;
import java.util.*;
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
		final OutputStream os=new osinc(x.outputstream(),p.parent(),null,this);
		try{p.to(os);}
		catch(final Throwable t){x.p(stacktrace(t));}
		finally{os.close();}
	}
	
	public static void date(final OutputStream os,final String a)throws Throwable{
		os.write(new Date().toString().getBytes());
	}
	public static void homeuri(final OutputStream os,final String a)throws Throwable{
		os.write(req.get().session().href().getBytes());
	}
	public static void imgtoascii(final OutputStream os,final String a)throws Throwable{
		new cli("jp2a "+path(a),os).wait_for_cli();
	}
	public static void fortune(final OutputStream os,final String a)throws Throwable{
		new cli("fortune",os).wait_for_cli();
	}
	public static void banner(final OutputStream os,final String a)throws Throwable{
		new cli("banner "+a,os).wait_for_cli();
	}
	public static void figlet(final OutputStream os,final String a)throws Throwable{
		new cli("figlet "+a,os).wait_for_cli();
	}
	public static void cowsay(final OutputStream os,final String a)throws Throwable{
		new cli("cowsay "+a,os).wait_for_cli();
	}
	final public static String version="1";
}
