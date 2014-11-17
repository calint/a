package a.civ;
import b.*;
public class tile extends a{
	public tile(final map m,final String id){super(m,id);}
	void map_abbrv_to(final xwriter x){
		final unit e=u.get_first();
		if(e==null){x.p("  ");return;}
		final StringBuilder sb=new StringBuilder();
		sb.append(e.toString());
		while(sb.length()<2)sb.append(' ');
		sb.setLength(2);
		x.p(sb.toString());
		return;
	}
	public alist<unit>u;
	private static final long serialVersionUID=1;
}