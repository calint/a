package a.pz.bas;



abstract public class expr extends stmt{
	public expr(program p,String to_register){
		super(p);
		this.to_register=to_register;
	}
	private static final long serialVersionUID=1;
}