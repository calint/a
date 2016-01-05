package a.pzm.lang;

import b.xwriter;

final public class compiler_error extends RuntimeException{
	public statement stmt;
	public compiler_error(statement s,String message,String message_info){
		super(message+": "+message_info);
		stmt=s;
	}
	@Override public String toString(){
		return new xwriter().p("line ").p(stmt.location_in_source()).spc().p(getMessage()).toString();
	}
//	public int source_location_line(){
//		return Integer.parseInt(source_location.split(":")[0]);
//	}
	private static final long serialVersionUID=1;
}