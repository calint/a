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
					final String fldnm;
					final String args;
					if(i0!=-1){
						fldnm=tokenval.substring(0,i0);
						args=tokenval.substring(i0+1);
					}else{
						fldnm=tokenval;
						args="";
					}
					if(tokenval.startsWith("@")){
						root.get(tokenval.substring(1)).to(this);
					}else if(fldnm.startsWith("!")){
						try{ctxo.getClass().getMethod(fldnm.substring(1),new Class[]{OutputStream.class,String.class}).invoke(ctxo,new Object[]{os,args});}catch(final Throwable t2){
							os.write(("•• error at line "+lineno+" while "+tokenval+" ••").getBytes());
						}
					}else{
						Object v;
						try{v=ctxo.getClass().getField(fldnm).get(ctxo);}catch(Throwable t){
						try{v=ctxo.getClass().getMethod(fldnm).invoke(ctxo);}catch(Throwable t3){
						try{v=ctxo.getClass().getMethod(fldnm,new Class[]{String.class}).invoke(ctxo,new Object[]{args});}catch(Throwable t2){
						v=ctx.get(tokenval);	
						}}}
						write(tobytes(tostr(v,"")));
					}
					s=0;
					cpfromix=i+1;
				}
				break;
			}
			i++;
		}
		if(s==1);//? bug
		final int ln=i-cpfromix;
		if(ln==0)
			return;
		os.write(b,cpfromix,ln);
	}
	public void write(final byte[]b)throws IOException{write(b,0,b.length);}
	public void write(final int ch)throws IOException{throw new UnsupportedOperationException();}

	private final OutputStream os;
	private final path root;
	private final Map<String,Object>ctx;
	private final Object ctxo;
	private int lineno=1;
}