package a.pz.a;
import a.x.jskeys;
import b.a;
import b.xwriter;
final public class clare extends a{
	final public int ncores=4;
	public clare()throws Throwable{
		for(int i=0;i<ncores;i++)z.add(new acore());// ncores times z add new zn
		z.stream().forEach(z->z.bi.set(acore.bit_panels));
		z.rend_dut=false;
	}
	@Override public void to(xwriter x)throws Throwable{
		final String id=id();
		if(pt()==null){
			x.tag("style")
				.css("html","background:#111;color:#080")
				.css("body","line-height:1.4em;width:80em;margin-left:auto;margin-right:auto;padding:3em 4em 0 8em;display:block;box-shadow:0 0 17px rgba(0,0,0,.5)")
				.css("a","color:#008")
//				.css(".panel","border:1px dotted;border-radius:.5em;padding:0 .2em 0 .2em")
				.css(".float","float:left")
				.css(".textleft","text-align:left")
				.css(".floatclear","clear:both")
				.css(".stp","background-color:#020")
				.css(".brk","background-color:#060")
//				.css(ajaxsts,"position:absolute;bottom:0;right:0")
			.style_();
			try(final jskeys jskeys=new jskeys(x)){
				jskeys.add("cS","$x('"+id+" s')");//? x.axstr(id,func,param):"$x('..','... ...');
				jskeys.add("cK","alert('info')");
			}
			x.p("<title>clare</title>");
		}
		z.get_first().logo_to(x);
		x.span(ajaxsts).r(z);
	}
	/**cores*/public alist<acore>z;
	/**builtinajaxstatus*/public a ajaxsts;{ajaxsts.set("idle");}
	private static final long serialVersionUID=1;
}
