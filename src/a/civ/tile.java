package a.civ;
import b.*;
public class tile extends a{
	public tile(final map m,final String id){super(m,id);}
	String get_map_abbrv(){
		final StringBuilder sb=new StringBuilder();
		final unit e=u.get_first();
		if(e==null){return"  ";}
		sb.append(u.nm());
		b.pl(u.toString());
		while(sb.length()<2)sb.append(' ');
		sb.setLength(2);
		return sb.toString();
	}
	public alist<unit>u;
	private static final long serialVersionUID=1;
}