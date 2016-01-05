package a.pzm.bas;

import b.xwriter;

final public class compiler_error extends RuntimeException{
	public String source_location;
	public String message;
	public compiler_error(stmt s,String message){
		this(s.location_in_source,message);
	}
	public compiler_error(stmt s,String msg,String offender){
		this(s.location_in_source,msg+": "+offender);
	}
	public compiler_error(String source_location,String message){
		super(source_location+" "+message);
		this.source_location=source_location;
		this.message=message;
	}
	@Override public String toString(){
		return new xwriter().p("line ").p(source_location).spc().p(message).toString();
	}
	public int source_location_line(){
		return Integer.parseInt(source_location.split(":")[0]);
	}
	private static final long serialVersionUID=1;
}