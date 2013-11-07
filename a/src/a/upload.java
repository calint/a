package a;
import b.a;
import b.req;
import b.session;
import b.xwriter;
public class upload extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final session s=r.session();
//		x.br();
//		x.tago("object").attr("type","application/x-java-applet").attr("width","100%").attr("height","100%").tagoe().nl();
		x.tago("applet").attr("archive","/upload.jar").attr("code","applet.upload").attr("width","100%").attr("height","100%").tagoe().nl();
//		x.tago("param").attr("name","archive").attr("value","/upload.jar").tagoe().nl();
//		x.tago("param").attr("name","codebase").attr("value","/upload.jar").tagoe().nl();
//		x.tago("param").attr("name","code").attr("value","applet.upload").tagoe().nl();
		x.tago("param").attr("name","host").attr("value",r.host()).tagoe().nl();
		x.tago("param").attr("name","port").attr("value",r.port()).tagoe().nl();
		x.tago("param").attr("name","session").attr("value",s.id()).tagoe().nl();
//		x.tagEnd("object");
		x.tage("applet");
	}
}