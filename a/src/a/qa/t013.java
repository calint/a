package a.qa;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import b.a;
import b.b;
import b.path;
import b.xwriter;
public class t013 extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x) throws Throwable{
		final path p=b.path("/qa/t001.txt");
		if(!p.exists())throw new Error();
		final InputStream is=p.fileinputstream();
		final BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf8"));
		for(String line=br.readLine();line!=null;line=br.readLine()){
			x.p(line).nl();
		}
		is.close();
	}
}