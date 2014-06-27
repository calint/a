package a.qa;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.*;
import java.util.*;
final public class uploader{
	public static int chunksize=256*1024;
	public static void main(final String[]a)throws Throwable{
		if(a.length<5){
			System.out.println("try: java "+uploader.class.getName()+" localhost 8888 sessionid uploaddir file.txt q");
			System.out.println(". .. java "+uploader.class.getName()+" localhost 8888 sessionid \"\" dir");
			return;
		}
		final boolean silent=a.length>5&&"q".equals(a[5]);
		final uploader upl=new uploader();
		upl.perfstart();
		try{upl.send(a[0],Integer.parseInt(a[1]),a[2],a[3],new File(a[4]),null,silent?null:new sts());}catch(final Throwable t){t.printStackTrace();System.exit(1);}
		upl.perfstop();
		if(!silent)System.out.println("\ndone in "+upl.dt+" ms, "+upl.kbps+" kB/s, "+upl.totalfiles+" files, "+(upl.totalbytes>>10)+" KB");
	}
	public synchronized void send(final String host,final int port,final String session,final String dir,final File fl,final String md5sum,final sts st)throws Throwable{
		if(cancelled)return;
		final String md5=md5sum==null?"-":md5sum;
		final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss.SSS");
		final SocketChannel sc=sock(host,port);
		final ByteBuffer bb=ByteBuffer.allocate(512);// dir.len+fl.name.len+x
		final String dateupd=df.format(fl.lastModified());
		bb.put("POST ".getBytes()).put(urlenc(dir,fl.getName())).put(" HTTP/1.1\r\n".getBytes());
		bb.put("Host: ".getBytes()).put(host.getBytes()).put("\r\n".getBytes());
		bb.put("Cookie: i=".getBytes()).put(session.getBytes()).put("\r\n".getBytes());//? if reused sock
		if(fl.isDirectory()){
			bb.put("Content-Type: dir;".getBytes()).put(dateupd.getBytes()).put("\r\n\r\n".getBytes());
			bb.flip();
			if(st!=null)st.sts("directory " + fl.toString());
			while(bb.hasRemaining())sc.write(bb);
			bb.clear();
			final int c=sc.read(bb);
			if(c==-1)throw new Error("eos");
			bb.flip();
			final String resp=new String(bb.array(),0,bb.limit());
			if(!resp.startsWith("HTTP/1.1 204"))throw new Error(""+c);
//			System.out.println();
//			System.out.println("****"+resp);
			for(final File f:fl.listFiles()){
				boolean go=true;
				while(go)
					try{
						send(host,port,session,dir.replace(File.separatorChar,'/')+"/"+fl.getName(),f,md5,st);
						go=false;
					}catch(final Throwable t){
						t.printStackTrace();
						System.out.println(t.getMessage());
						if("eos".equals(t.getMessage())){
							System.out.println("#### eos. retry");
							socks.clear();
						}else throw new Error(t);
					}
			}
			return;
		}
		final FileChannel fc=new FileInputStream(fl).getChannel();
		final long file_len=fc.size();
		bb.put("Content-Type: file;".getBytes()).put(dateupd.getBytes()).put("\r\n".getBytes());
		bb.put("Content-Length: ".getBytes()).put(Long.toString(file_len).getBytes()).put("\r\n\r\n".getBytes());
		bb.flip();
//		System.out.println();
//		System.out.println(new String(bb.array(),0,bb.limit()));
		while(bb.hasRemaining())sc.write(bb);
		totalfiles++;
		long file_pos=0;
		long file_rem=file_len;
		while(true){
			if(cancelled)break;
			if(file_rem==0)break;
			final long n=file_rem>chunksize?chunksize:file_rem;
			final long c=fc.transferTo(file_pos,n,sc);
			if(c==-1)break;
			if(c==0)continue;
			file_pos+=c;file_rem-=c;totalbytes+=c;
			if(st!=null)st.sts(totalfiles+" files, "+(totalbytes>>10)+" KB, xxx KB/s, "+(int)(file_pos*100/file_len)+"% "+fl.getName()+" ("+file_rem+")B");
		}
		fc.close();
		bb.clear();
		if(!sc.isOpen())throw new Error("socket channel not open");
		final int c=sc.read(bb);
		if(c==-1)throw new Error("eos");
		bb.flip();
		final String resp=new String(bb.array(),0,bb.limit());
		if(!resp.startsWith("HTTP/1.1 204"))throw new Error("error code");
//		System.out.println();
//		System.out.println("****"+resp);
		if(st!=null)st.flush();
		//? read 204 No Content
	}
	private byte[]urlenc(final String dir,final String nm)throws UnsupportedEncodingException{
		final String pth;
		if(dir==null||dir.length()==0||".".equals(dir)){
			pth="/"+nm;
		}else{
			pth=dir+"/"+nm;
		}
//		System.out.println(pth);
		final String[]pa=pth.split("/");
		final StringBuilder encoded=new StringBuilder(64);
		for(final String s:pa){
			if(s.length()==0)continue;
			encoded.append("/");
			encoded.append(URLEncoder.encode(s,"UTF-8"));
		}
		return encoded.toString().getBytes();
//		return URLEncoder.encode(dir+"/"+fl.getName(),"UTF-8").getBytes();
	}
	private SocketChannel sock(final String host,final int port)throws Throwable{
		final String key=host+":"+port;
		SocketChannel sc=socks.get(key);
		if(sc==null){
			sc=SocketChannel.open();
			if(!sc.connect(new InetSocketAddress(host,port)))throw new Error("could not connect to "+host+":"+port);
			socks.put(key,sc);
		}
		if(!sc.isConnected())throw new Error("socket disconnected");//? newsock
		return sc;
	}
	public void perfstart(){t0=System.currentTimeMillis();totalbytes=0;}
	public void perfstop(){dt=System.currentTimeMillis()-t0;kbps=(int)(totalbytes/dt);}
	public boolean cancelled(){return cancelled;}
	public void cancel(){cancelled=true;synchronized(socks){socks.clear();}}//? socks.close

	private Map<String,SocketChannel>socks=new HashMap<String,SocketChannel>();
	private boolean cancelled;
	private long totalbytes;
	private int totalfiles;
	private int kbps;
	private long t0;
	private long dt;

	final static private class sts{
		private String sts;
		private long t;
		public long updt=100;
		public sts(){}
		public sts(final long update_intervall_ms){this.updt=update_intervall_ms;}
		public final void sts(final String s){
			sts=s;
			final long tnow=System.currentTimeMillis();
			if(tnow-t>updt){
				t=tnow;
				flush();
			}
		}
		public void flush(){System.out.print('\r');System.out.print(sts);}
	}
}