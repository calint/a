package a.x;
import static b.b.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import b.*;
final public class osinc extends OutputStream{
	public final static byte token=(byte)'`';
	public osinc(final OutputStream os,final path root,final Map<String,Object>ctx,final Object ctxo){this.os=os;this.root=root;this.ctx=ctx;this.ctxo=ctxo;}
	public void write(final byte[]b,final int off,final int len)throws IOException{
		int i=off,cpfromix=off,n=len;
		while(n-->0){
			if(b[i]=='\n')lineno++;
			switch(st){
			case 0:
				if(b[i]==token){st=1;os.write(b,cpfromix,i-cpfromix);}
				break;
			case 1:
				final byte bt=b[i];
				if(bt==token){
					st=0;
					cpfromix=i+1;
					tokenbb.flip();
					final String token=new String(tokenbb.array(),0,tokenbb.limit(),strenc);
					tokenbb.clear();
					process(token);
					break;
				}
				tokenbb.put(bt);
				break;
			}
			i++;
		}
		if(st==1)return;
		final int ln=i-cpfromix;
		if(ln==0)return;
		os.write(b,cpfromix,ln);
	}
	private void process(final String token)throws IOException{
		if(token.startsWith("@")){
			root.get(token.substring(1)).to(this);
			return;
		}
		final int i0=token.indexOf(' ');
		final String nm;
		final String args;
		if(i0!=-1){
			nm=token.substring(0,i0);
			args=token.substring(i0+1);
		}else{
			nm=token;
			args="";
		}
		try{ctxo.getClass().getMethod(nm,new Class[]{OutputStream.class,String.class}).invoke(ctxo,new Object[]{os,args});}catch(final Throwable t1){
			Object v=null;
			try{v=ctxo.getClass().getField(nm).get(ctxo);}catch(Throwable t2){
			try{v=ctxo.getClass().getMethod(nm).invoke(ctxo);}catch(Throwable t3){
			try{v=ctxo.getClass().getMethod(nm,new Class[]{String.class}).invoke(ctxo,new Object[]{args});}catch(Throwable t4){
			if(ctx!=null)v=ctx.get(token);	
			}}}
			if(v==null){
				os.write(("•• error at line "+lineno+", "+nm+" not found in "+ctxo.getClass()+" ••").getBytes());
				return;
			}
			write(tobytes(tostr(v,"")));
		}
	}
	public void write(final byte[]b)throws IOException{write(b,0,b.length);}
	public void write(final int ch)throws IOException{throw new UnsupportedOperationException();}

	private int st;
	private int lineno=1;
	private final OutputStream os;
	private final path root;
	private final Map<String,Object>ctx;
	private final Object ctxo;
	private final ByteBuffer tokenbb=ByteBuffer.allocate(128);
}