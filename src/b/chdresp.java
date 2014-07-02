package b;
import java.io.*;
import java.nio.*;
final class chdresp{
	private static final int hdrlencap=8*64;
	private path path;
	private cacheable cacheable;
	private long lastModified;
	private String lastModified_s;
	private long ts;
//	private long dt;
	private ByteBuffer byteBuffer;
	private int hdrinsertionix;
	private String contentType;
	private String key;
	private boolean isresource;
	chdresp(final path p)throws Throwable{path=p;validate(System.currentTimeMillis(),null);}
//	chdresp(final cacheable c,final String key){cacheable=c;this.key=key;dt=c.lastmodupdms();}
	chdresp(final cacheable c,final String key){cacheable=c;this.key=key;}
	public chdresp(final InputStream is)throws IOException{
		isresource=true;
		final ByteArrayOutputStream os=new ByteArrayOutputStream();
		b.cp(is,os);
		final byte[]ba=os.toByteArray();
		final long size=ba.length;
		lastModified=b.resources_lastmod;
		byteBuffer=ByteBuffer.allocateDirect(hdrlencap+(int)size);
		byteBuffer.put(req.h_http200);
		byteBuffer.put(req.h_content_length).put(Long.toString(size).getBytes());
		lastModified_s=b.tolastmodstr(lastModified);
		byteBuffer.put(req.h_last_modified).put(lastModified_s.getBytes());
		byteBuffer.put(req.hkp_connection_keep_alive);
		hdrinsertionix=byteBuffer.position();
		byteBuffer.put(req.ba_crlf2);
		byteBuffer.put(ba);
		byteBuffer.flip();
	}
	boolean ifnotmodsince(final String ifModSince){return ifModSince.equals(lastModified_s);}
	ByteBuffer byteBuffer(){return byteBuffer;}
	int hdrinsertionix(){return hdrinsertionix;}
	String contentType(){return contentType;}
	String lastModified(){return lastModified_s;}
	boolean validate(final long now,final String lm)throws Throwable{
		if(isresource)return true;
		if(cacheable!=null){validatecacheable(now,lm);return true;}
		
		if(now-ts<b.cache_files_validate_dt)return true;
		ts=now;
		if(!path.exists())return false;
		final long path_lastModified=path.lastmod();
		if(path_lastModified==lastModified)return true;
		final long path_len=path.size();
		byteBuffer=ByteBuffer.allocateDirect(hdrlencap+(int)path_len);
		byteBuffer.put(req.h_http200);
		byteBuffer.put(req.h_content_length).put(Long.toString(path_len).getBytes());
		if(contentType!=null)byteBuffer.put(req.h_content_type).put(contentType.getBytes());
		lastModified_s=b.tolastmodstr(path_lastModified);
		byteBuffer.put(req.h_last_modified).put(lastModified_s.getBytes());
		byteBuffer.put(req.hkp_connection_keep_alive);
		hdrinsertionix=byteBuffer.position();
		byteBuffer.put(req.ba_crlf2);
		path.to(byteBuffer);
		byteBuffer.flip();
		lastModified=path_lastModified;
		return true;
	}
	private void validatecacheable(final long now,final String lm)throws Throwable{
		if(isvalid(now))return;
		ts=now;
		contentType=cacheable.contenttype();
		lastModified_s=cacheable.lastmod();
//		if(lastModified_s==lm)return;
		if(lastModified_s!=null&&lastModified_s.equals(lm))return;
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(b.io_buf_B);
		((a)cacheable).to(new xwriter(baos));
		final byte[]ba=baos.toByteArray();
		if(b.cacheu_tofile)b.path(b.cacheu_dir+key+"."+cacheable.filetype()).writebb(ByteBuffer.wrap(ba));
		byteBuffer=ByteBuffer.allocate(256+ba.length);//? calcsize
		byteBuffer.put(req.h_http200);
		byteBuffer.put(req.h_content_length).put(Long.toString(baos.size()).getBytes());
		if(lastModified_s!=null)byteBuffer.put(req.h_last_modified).put(lastModified_s.getBytes());
		if(contentType!=null)byteBuffer.put(req.h_content_type).put(contentType.getBytes());
		byteBuffer.put(req.hkp_connection_keep_alive);
		hdrinsertionix=byteBuffer.position();
		byteBuffer.put(req.ba_crlf2);
		byteBuffer.put(ba);
		byteBuffer.flip();
	}
	boolean isvalid(final long now){return now-ts<cacheable.lastmodupdms();}
}
