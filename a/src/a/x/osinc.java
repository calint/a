package a.x;
import static b.b.*;

import java.io.*;
import java.util.*;

import b.*;
final public class osinc extends OutputStream{
	public final static byte token=(byte)'`';
	public osinc(final OutputStream os,final path root,final Map<String,Object>ctx,final Object ctxo){this.os=os;this.root=root;this.ctx=ctx;this.ctxo=ctxo;}
	public void write(final byte[]b,final int off,int len)throws IOException{
		int i=off,cpfromix=off,s=0,tknix=0;
		while(len-->0){
			if(b[i]=='\n')
				lineno++;
			switch(s){
			case 0:
				if(b[i]==token){
					tknix=i+1;
					s=1;
					os.write(b,cpfromix,i-cpfromix);
				}
				break;
			case 1:
				if(b[i]==token){
					final String tokenval=new String(b,tknix,i-tknix);
					final int i0=tokenval.indexOf(' ');
					final String nm;
					final String args;
					if(i0!=-1){
						nm=tokenval.substring(0,i0);
						args=tokenval.substring(i0+1);
					}else{
						nm=tokenval;
						args="";
					}
					process(tokenval,nm,args);
					s=0;
					cpfromix=i+1;
				}
				break;
			}
			i++;
		}
//		if(s==1);//? bug
		final int ln=i-cpfromix;
		if(ln==0)return;
		os.write(b,cpfromix,ln);
	}
	private void process(final String tokenval,final String nm,final String args)throws IOException{
		if(tokenval.startsWith("@")){
			root.get(tokenval.substring(1)).to(this);
			return;
		}
		try{ctxo.getClass().getMethod(nm,new Class[]{OutputStream.class,String.class}).invoke(ctxo,new Object[]{os,args});}catch(final Throwable t1){
			Object v=null;
			try{v=ctxo.getClass().getField(nm).get(ctxo);}catch(Throwable t2){
			try{v=ctxo.getClass().getMethod(nm).invoke(ctxo);}catch(Throwable t3){
			try{v=ctxo.getClass().getMethod(nm,new Class[]{String.class}).invoke(ctxo,new Object[]{args});}catch(Throwable t4){
			if(ctx!=null)v=ctx.get(tokenval);	
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

	private final OutputStream os;
	private final path root;
	private final Map<String,Object>ctx;
	private final Object ctxo;
	private int lineno=1;
}