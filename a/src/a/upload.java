package a;
import b.a;
import b.req;
import b.xwriter;
public class upload extends a{
	static final long serialVersionUID=1;
	final public void to(final xwriter x) throws Throwable{
//		x.br();
//		x.tago("object").attr("type","application/x-java-applet").attr("width","100%").attr("height","100%").tagoe().nl();
//		x.tago("applet").attr("archive","/upload.jar").attr("code","applet.upload").attr("width","100%").attr("height","100%").tagoe().nl();
		x.tago("applet").attr("codebase","/bin").attr("code","applet.upload").attr("width","100%").attr("height","100%").tagoe().nl();
//		x.tago("param").attr("name","archive").attr("value","/upload.jar").tagoe().nl();
//		x.tago("param").attr("name","codebase").attr("value","/upload.jar").tagoe().nl();
//		x.tago("param").attr("name","code").attr("value","applet.upload").tagoe().nl();
		x.tago("param").attr("name","host").attr("value",host()).tagoe().nl();
		x.tago("param").attr("name","port").attr("value",port()).tagoe().nl();
		x.tago("param").attr("name","session").attr("value",session()).tagoe().nl();
		x.tago("param").attr("name","rootpath").attr("value",rootpath()).tagoe().nl();
//		x.tagEnd("object");
		x.tage("applet");
	}
	protected String host(){return req.get().host();}
	protected int port(){return req.get().port();}
	protected String session(){return req.get().session().id();}
	protected String rootpath(){return "";}
}