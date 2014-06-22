package a.x;
import b.xwriter;
public final class jskeys implements AutoCloseable{
	private xwriter x;
	public jskeys(xwriter w){this.x=w;x.pl("<script>ui.keys=[];");}
//	public void open(){}
	public void add(final String key,final String cmd){x.p("ui.keys['"+key+"']=\""+cmd+"\";");}
	public void close(){x.p("</script>");}
}
