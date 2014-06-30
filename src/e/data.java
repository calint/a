package e;

import java.io.OutputStream;

public interface data{
	void to(OutputStream os)throws Throwable;
	long size()throws Throwable;// Long.MAX_VALUE == unknown
}
