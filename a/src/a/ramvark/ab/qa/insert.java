package a.ramvark.ab.qa;
import java.util.concurrent.atomic.AtomicInteger;

import a.ramvark.cstore;
import a.ramvark.itm;
import a.ramvark.ab.puppet;
import b.a;
import b.xwriter;
public class insert extends a{static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		// browser quirk fix
		for(int n=0;n<8;n++)x.p("                                                                ");
		x.pl("");
		x.pl("insert test");
		x.flush();
		final int npuppets=10_000;
		final int nthreads=10;
		final int nthreads_step=2;
		final Thread[]threads=new Thread[nthreads];
		final AtomicInteger ai=new AtomicInteger();
		final Runnable r=new Runnable(){@Override public void run(){try{
			for(int k=0;k<npuppets;k++){
				final itm e=cstore.create(puppet.class,null);
				e.set("puppet "+ai.incrementAndGet());
				cstore.save(e);
			}
		}catch(final Throwable t){throw new Error(t);}}};
		for(int j=1;j<=nthreads;j+=nthreads_step){
			final long t0=System.currentTimeMillis();
			for(int k=0;k<j;k++){
				final Thread t=new Thread(r);
				threads[k]=t;
				t.start();
			}
			for(int k=0;k<j;k++){
				threads[k].join();
			}
			final long t1=System.currentTimeMillis();
			final long dt0=t1-t0;
			x.pl("inserts:"+npuppets*j+"   threads:"+j+"    ms:"+dt0+"    "+npuppets*j*1000/dt0+" inserts/second");
			x.flush();
			//		System.out.println(dt0);
		}
	}
}
