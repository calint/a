package a.e;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

final public class packet implements Serializable{
	final static int BIT_FIRST_PACK=1;
	final static int BIT_LAST_PACK=2;
	final static int BIT_HAS_SIZE=3;
	final static int BIT_HAS_FROM=4;
	final static int BIT_HAS_TO=5;

	final byte bits;
	final data dat;
	final byte[]to;
	final byte[]from;
	final byte[]size;//ascii stored
	public packet(final byte bits,final data d){this.bits=bits;dat=d;size=null;to=null;from=null;}
	public packet(final byte bits,final data d,final byte[]to){this.bits=bits;dat=d;size=null;this.to=to;from=null;}
	public packet(final byte bits,final data d,final byte[]to,final byte[]from){this.bits=bits;dat=d;size=null;this.to=to;this.from=from;}
	public packet(final byte bits,final data d,final byte[]from,final boolean broadcast){this.bits=bits;dat=d;size=null;to=null;this.from=from;}

	public void to(final OutputStream os)throws IOException{}
//	public void to(final SocketChannel sc)throws IOException{}
//	public void to(final FileChannel sc)throws IOException{}
	
	private static final long serialVersionUID=1;
}
