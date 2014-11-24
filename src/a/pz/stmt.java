package a.pz;

import java.io.Serializable;

public class stmt implements Serializable{
	protected String location_in_source;
	protected String txt;
	protected String type;
	protected int[] bin;
	protected int location_in_binary;
	public stmt(final program p){
		if(p!=null)location_in_source=p.location_in_source();
	}
	protected void validate_references_to_labels(program r){}
	protected void compile(program r){}
	protected void link(program p){}
	public String toString(){return txt;}
	//		public void source_to(xwriter x){}
	private static final long serialVersionUID=1;
}