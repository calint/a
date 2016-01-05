package a.pzm.bas;



abstract public class def extends stmt{
	protected String name;
	public String type;
	public def(program p){super(p);}
	public def(program p,String type,String name){super(p);this.type=type;this.name=name;}
	final public String name(){return name;}
	@Override public void compile(program p){}
	@Override public void link(program p){}
	private static final long serialVersionUID=1;
}