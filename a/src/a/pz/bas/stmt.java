package a.pz.bas;

import java.io.Serializable;

public abstract class stmt implements Serializable{
	public String to_register;


	//	final static public class expr_func_call_arg extends expr{
//		stmt st;
//		public expr_func_call_arg(final program p) throws IOException{
//			super(p,null);
//			st=p.next_statement(false);
//			txt=st.toString();
////			txt=p.next_token_in_line();
//		}
//		@Override protected void compile(program p){
//			st.compile(p);
//		}
//		@Override protected void link(program p){
//			st.link(p);
//		}
//		private static final long serialVersionUID=1;
//	}
	protected String location_in_source;
	final public String source_location(){return location_in_source;}
	final public String source_location_line(){return location_in_source.split(":")[0];}
	public String txt;
	protected String type;
	public int[] bin;
	public int location_in_binary;
	public stmt(final program p){
		if(p!=null)
			location_in_source=p.location_in_source();
	}
	public stmt(final program p,final String txt){
		this(p);this.txt=txt;
	}
	protected void validate_references_to_labels(program r){}
	public void compile(program r){}
	public void link(program p){}
	public String toString(){return txt;}
	final public String txt(){return txt;}
	//		public void source_to(xwriter x){}
	private static final long serialVersionUID=1;
}