package a.y;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import b.a;
import b.sock;
import b.sockio;
import b.thdwatch;

public class gloxnet extends a implements sock{
	private static final long serialVersionUID=1;
	
	private final static int pklen=32;
	private final static int nplayers=2;
	private static ByteBuffer bbkeys=ByteBuffer.allocate(pklen*nplayers);
	private static List<gloxnet>clients=new ArrayList<gloxnet>(nplayers);
	private static int playerix;
	private static int count;
	
	private sockio sio;
	private int state;
	private ByteBuffer bbin;
	private ByteBuffer bbout;	
	public op sockinit(final Map<String,String>hdrs,final sockio sio)throws Throwable{
		System.out.println(this+" init "+sio);
		this.sio=sio;
		synchronized(clients){
			bbout=bbkeys.slice();
			bbin=bbkeys.slice();
			bbin.position(playerix*pklen).mark().limit(playerix*pklen+pklen);
			clients.add(this);
			playerix=clients.size();
		}
		return read();
	}
	public op read() throws Throwable{
		System.out.println(this+" read "+sio);
		while(true){
			final int c=sio.read(bbin);
			if(c>0)thdwatch.output+=c;
			if(bbin.remaining()==0){bbin.reset();break;}
			if(c==-1)return op.close;
			if(c==0)return op.read;
		}
		switch(state){
//		case 0:
//			System.out.println(this+" player #"+(count+1)+" of "+nplayers+": "+new String(bbin.array(),bbin.arrayOffset(),bbin.remaining()));
//			synchronized(gloxnet.class){
//				count++;
//				if(count<nplayers)
//					return op.wait;
//				count=0;
//				for(final gloxnet g:clients)
//					g.notif();
//				return op.cont;
//			}
		case 0:
		case 1:
			synchronized(gloxnet.class){
				count++;
				if(count<nplayers)
					return op.wait;
				count=0;
				for(final gloxnet g:clients)
					g.notif();
				return op.noop;
			}
		default:throw new IllegalStateException();
		}
	}
	public op write() throws Throwable{
		System.out.println(this+" write "+sio);
		while(true){
			final int c=sio.write(bbout);
			System.out.println(new String(bbout.array()));
			if(c>0)thdwatch.output+=c;
			if(bbout.remaining()==0)break;
			if(c==-1)return op.close;
			if(c==0)return op.write;
		}
		bbout.flip();
		return op.read;
	}
	@Override
	public void onconnectionlost() throws Throwable {
		// TODO Auto-generated method stub
		
	}
	private void notif(){state=1;sio.reqwrite();}
}
