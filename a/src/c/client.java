package c;
import java.io.*;
import java.nio.channels.*;
import java.util.*;
public final class client{
	public client(final String host,final int port){this.host=host;this.port=port;count++;}
	public void close()throws Throwable{if(conn !=null){
		conn.close();
		conn =null;}if(--count==0)on=false;}
	public void get(final String uri,final conn.oncontent oncontent,final conn.ongetdone ongetdone)throws Throwable{
		if(conn !=null){
			conn.get(uri,null,oncontent,ongetdone);return;}
		conn =new conn(this,host,port,()-> conn.get(uri,null,oncontent,ongetdone));
	}
	public void cookie(final String cookie){this.cookie=cookie;}
	public String cookie(){return cookie;}
	public void websock(final String uri,final client.onwebsockconnect onwebsockconnect)throws Throwable{
		conn =new conn(this,host,port,()->
			conn.get(uri,new kvps().put("Upgrade","websocket").put("Connection","upgrade").put("Sec-WebSocket-Key","x3JJHMbDL1EzLkh9GBhXDw==").put("Sec-WebSocket-Version","13"),null,()->{
				//. checkrepliedkey
				conn.mode_websock();
				onwebsockconnect.onwebsockconnect();
			})
		);
	}
	public void recv(final String s,final conn.onwebsock onwebsockframe)throws Throwable{
		conn.websock_sendframe(s,onwebsockframe);}
	public client pl(final String s){c.out.println(s);return this;}
	static Selector selector(){return sel;}

	final private String host;
	final private int port;
	private String cookie;
	private conn conn;

	private static int count;
	private static boolean on=true;

	public static void loop(){
		final long t0_ms=System.currentTimeMillis();
		while(on)try{
			sel.select();
			metrs.select++;
			final Iterator<SelectionKey> it=sel.selectedKeys().iterator();
			while(it.hasNext()){
				metrs.ioevent++;
				final SelectionKey sk=it.next();
				it.remove();
				sk.interestOps(0);
				final conn cn=(conn)sk.attachment();
				if(sk.isConnectable()){metrs.iocon++;cn.onconnect();continue;}
				if(sk.isReadable()){metrs.ioread++;cn.onread();continue;}
				if(sk.isWritable()){metrs.iowrite++;cn.onwrite();}
			}
		}catch(final Throwable e){
			if(e instanceof ClosedChannelException)continue;
			if(e instanceof ClosedSelectorException)break;
			if(e instanceof CancelledKeyException)continue;
			throw new Error(e);
		}
		final long dt_ms=System.currentTimeMillis()-t0_ms;
		if(c.pstats)System.out.println(c.nclients+" req, "+dt_ms+" ms, "+c.nclients*1000/dt_ms+" req/s, "+(((metrs.output+metrs.input)*1000/dt_ms)>>10)+" KB/s");
	}
	private static Selector sel;static{try{sel=Selector.open();}catch(final IOException t){throw new Error(t);}}

	public static interface onwebsockconnect{void onwebsockconnect()throws Throwable;}
}