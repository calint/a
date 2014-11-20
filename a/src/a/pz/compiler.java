package a.pz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class compiler{
	private rom r;
	private int i;
	public compiler(rom r){this.r=r;}
	public void write(int instruction){r.set(i++,instruction);}
	public void add_label(String name,String location_in_source){
		if(labels.containsKey(name))throw new Error("label '"+name+"' already declared. "+labels.get(name));
		final compiler.label_link m=new label_link();
		m.binloc=i;
		m.srcloc=location_in_source;
		m.link_to=name;
		labels.put(name,m);
	}
	private final static class label_link{
		String link_to;
		String srcloc;
		int binloc;
		public String toString(){return srcloc+" ["+Integer.toHexString(binloc)+"]";}
	}
	private final Map<String,compiler.label_link>labels=new LinkedHashMap<>();
	public void add_link(String to_label,String location_in_source){
		final compiler.label_link m=new label_link();
		m.binloc=i;
		m.srcloc=location_in_source;
		m.link_to=to_label;
		links.add(m);
	}
	private final List<compiler.label_link>links=new ArrayList<>();
	public void finish(){
		links.forEach(e->{
			final compiler.label_link ll;
			if(e.link_to.startsWith(":"))ll=labels.get(e.link_to.substring(1));
			else ll=labels.get(e.link_to);
			if(ll==null)throw new Error(e.srcloc+" '"+e.link_to+"' not found");
			final int a=ll.binloc;
			final int d=r.get(e.binloc);
			final int c;
			if((d&0b010000)==0b010000){//? hackish
				c=d|(a<<6);
			}else{//? assuming data
				c=a;
			}
			r.set(e.binloc,c);
		});
	}
}