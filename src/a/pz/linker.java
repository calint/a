package a.pz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import a.pz.program.compiler_error;

final class linker{
	private int[]ints;
	private int i;
	final program p;
	public linker(program p,int[]ints){this.p=p;this.ints=ints;}
	public void write(int instruction){
		ints[i++]=instruction;
	}
	public void add_label(String name,String location_in_source){
		if(labels.containsKey(name))throw new Error("label '"+name+"' already declared. "+labels.get(name));
		final linker.label_link m=new label_link();
		m.at_binary_location=i;
		m.at_source_location=location_in_source;
		m.link_to=name;
		labels.put(name,m);
	}
	private final static class label_link{
		String link_to;
		String at_source_location;
		int at_binary_location;
		public String toString(){return at_source_location+" ["+Integer.toHexString(at_binary_location)+"]";}
	}
	private final Map<String,linker.label_link>labels=new LinkedHashMap<>();
	public void add_link(String to_label,String location_in_source){
		final linker.label_link m=new label_link();
		m.at_binary_location=i;
		m.at_source_location=location_in_source;
		m.link_to=to_label;
		links.add(m);
	}
	private final List<linker.label_link>links=new ArrayList<>();
	public void finish()throws compiler_error{
		links.forEach(e->{
			final linker.label_link ll;
			if(e.link_to.startsWith(":"))ll=labels.get(e.link_to.substring(1));
			else ll=labels.get(e.link_to);
			if(ll==null)throw new compiler_error(e.at_source_location,"label '"+e.link_to+"' not found");
			final int a=ll.at_binary_location;
			final int d=ints[e.at_binary_location];
			final int c;
			if((d&0b010000)==0b010000){//? hackish
				c=d|(a<<6);
			}else{//? assuming data
				c=a;
			}
			ints[e.at_binary_location]=c;
		});
	}
}