package a.x;
import b.xwriter;
public final class jskeys{
	private xwriter x;
	public jskeys(xwriter w){this.x=w;}
	public void open(){x.pl("<script>if(!ui.keys)ui.keys=[];");}
	public void add(final String key,final String cmd){x.p("ui.keys['"+key+"']=\""+cmd+"\";");}
	public void close(){x.p("</script>");}
}
