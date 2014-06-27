package a.qa;
import java.nio.*;
import b.*;
// coverage test for path
public class t100 extends a implements bin{static final long serialVersionUID=1;
	public String contenttype(){return "text/plain;charset=utf8";}
	public void to(final xwriter x)throws Throwable{
		x.pl("path tests").nl();
		final path p1=b.path("qa/t100.tmp");
		x.pl(p1.toString());
		x.p(p1.uri()).nl();
		x.p(p1.type()).nl();
		x.p(p1.lastmod()).nl();
		x.p(p1.ishidden()?"hidden":"").nl();
		x.p(b.tostr(p1.parent(),"")).nl();

		final String s="file size fit for long numbers, names, subjects, etcڀํ";
		p1.writestr(s);
		final String scmp=p1.readstr();
		final boolean ok=s.equals(scmp);
		x.p("path").spc().p(p1.toString()).spc().p("B").p(p1.size()).spc().pl(ok?"ok":"notok");
		x.flush();
		if(!ok)throw new Error();
		//Thread.sleep(2000);
		//if(perftest){
		//	final ByteBuffer bb1=ByteBuffer.wrap(b.tobytes(s));
		//	final int n1=10000;
		//	for(int t=10;t<=n1;t*=10){
		//		final long t0_ms=System.currentTimeMillis();
		//		for(int i=0;i<t;i++){
		//			p1.writebb(bb1);
		//			bb1.flip();
		//		}
		//		final long t1_ms=System.currentTimeMillis();
		//		final long dt_ms=t1_ms-t0_ms;
		//		x.pl("wrote "+bb1.remaining()+" B to path "+t+" times in "+dt_ms+" ms, "+(dt_ms==0?"inf":t*1000/dt_ms)+" times/s, "+(dt_ms==0?"inf":(((long)bb1.remaining()*t*1000/dt_ms)>>10))+" KB/s");
		//		x.flush();
		//	}
		//	x.nl();
		//}
		p1.rm(new sts(){public void flush() throws Throwable{}public void setsts(final String s) throws Throwable{
			x.pl(s);
		}});

		final path p2=b.path("qa/t001.txt");
		x.p("path").spc().p(p2.toString()).spc().p(p2.size()).spc().p("B").nl();
		final ByteBuffer bb2=p2.readbb();
		final path p2cp=b.path("qa/t100.tmp");
		p2cp.writebb(bb2);
		p2cp.to(x);
		p2cp.rm();
		x.pl(p2.isin(b.path())?"isinpathok":"");
		//if(perftest){
		//	final int n2=1000;
		//	for(int t=10;t<=n2;t*=10){
		//		x.p("wrote "+(bb2.remaining()>>10)+" KB to path "+t+" times in ").flush();
		//		final long t0_ms=System.currentTimeMillis();
		//		for(int i=0;i<t;i++){
		//			p1.writebb(bb2);
		//			bb2.flip();
		//		}
		//		final long t1_ms=System.currentTimeMillis();
		//		final long dt_ms=t1_ms-t0_ms;
		//		x.pl(dt_ms+" ms, "+(dt_ms==0?"inf":t*1000/dt_ms)+" times/s, "+(dt_ms==0?"inf":(((long)bb2.remaining()*t*1000/dt_ms)>>10))+" KB/s");
		//		x.flush();
		//	}
		//	x.nl();
		//}
		final path p3=b.path("qa/t100.tmp");
		p3.mkfile();
		p3.append(s);
		p3.append(s,"\n");
		p3.append(new String[]{s,"1"+s},"\n");
		p3.to(x);
		p3.to(x.outputstream());
		x.nl().nl();

		final path p3dir=b.path("qa/t100.dir");
		x.p("cp ").p(p3.toString()).p(" ").p(p3dir.toString()).nl();
		p3.copyto(p3dir);
		final path p4=p3dir.get(p3.name());
		x.p("check ").pl(p4.toString());
		x.pl(p4.exists()?"exists":"");
		x.pl(p4.lastmod()==p3.lastmod()?"lastmodok":"");
		x.pl(p4.ishidden()==p3.ishidden()?"ishiddenok":"");
		p4.to(x);
		p3dir.apply(new path.visitor(){public boolean visit(final path p) throws Throwable{
			x.p("visiting: ").pl(p.toString());
			p.to(x);
			return false;
		}});
		p3dir.rm();
		p3.rm();


		x.pl("done");
	}
	//private boolean perftest;
}
