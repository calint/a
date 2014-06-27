package a.qa;
import b.*;
public class t102 extends a implements bin{static final long serialVersionUID=1;
	public String contenttype(){return "text/plain;charset=utf8";}
	public void to(final xwriter x)throws Throwable{
		x.pl("session coverage").nl();
		final session sn=req.get().session();
		x.pl(sn.path().toString());
		x.pl(sn.href());
		final path p=sn.path(getClass());
		x.pl(p.toString());
		x.pl(sn.inpath(p));
		final long bits=sn.bits();
		x.p(bits).nl();
		sn.bits(0x1248);
		x.p(sn.bits()).nl();
		x.pl(sn.bitshasany(0x0200)?"bitshasanyok":"");
		x.pl(sn.bitshasall(0x1248)?"bitshasallok":"");
		sn.bits(bits);
		x.p(sn.bits()).nl();

		sn.put("foo","bar");
		x.pl(sn.keyset().contains("foo")?"keysetputok":"");
		sn.save();
		sn.remove("foo");
		x.pl(!sn.keyset().contains("foo")?"keysetremoveok":"");

		try{final path root=b.path();sn.inpath(root);}catch(final Throwable ok){x.pl("inpathok");}
		x.pl(sn.inpath(sn.path()));
		x.pl("done");
	}
}
