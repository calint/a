package a;
import java.io.*;
import java.util.*;
import a.x.*;
import b.*;
public class hello extends a{
	private static final long serialVersionUID=1;
	private static String hr="\n-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --\n";
	public void to(xwriter x) throws Throwable{
		x.p("<pre><code>");
		x.p("          session: ").p(req.get().session().href()).nl();
		b.stats_to(x.outputstream());
		x.p("           server: ");
		final OutputStream s=new osltgt(x.outputstream());
		new cli("sh",s).p("uname -a").exit();
		x.p("      server home: ");
		new cli("sh",s).p("pwd").exit();
		x.p("        home size: ");
		new cli("sh",s).p("du -sh .").exit();
		x.p("          classes: ");
		new cli("sh",s).p("du -sh jar/").exit();
		x.p(hr).nl();
		new cli("sh",s).p("free -l").exit();
		x.p(hr).nl();
		new cli("sh",s).p("df -h").exit();
		x.p(hr).nl();
		new cli("sh",s).p("ifconfig").exit();
		x.p(hr).nl();
		new cli("sh",s).p("iwconfig").exit();
		x.p(hr).nl();
		for(final Iterator<?>i=System.getProperties().entrySet().iterator();i.hasNext();x.pl(i.next().toString()));
		x.p(hr).nl();
		new cli("sh",s).p("sysctl -A").exit();
		x.p(hr).nl();
		new cli("sh",s).p("ps axjf").exit();
	}
 }
