package a.e;
import java.io.OutputStream;
public interface data{
	void to(final OutputStream os)throws Throwable;
	/** @return Long.MAX_VALUE is unknown size */
	long size()throws Throwable;
}
