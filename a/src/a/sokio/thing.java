package a.sokio;
class thing extends splace implements Cloneable{
	private static final long serialVersionUID=1;
	protected splace place;
	protected String aan;
//	protected int bits;
//	final public boolean iscontainer(){return(bits&1)==1;}
//	final public void iscontainer(final boolean b){if(b)bits|=1;else bits&=-1-1;}
	public Object clone(){//? deepcopy
		try{return super.clone();}catch(CloneNotSupportedException e){throw new Error(e);}}
	
	final public String aanname(){
		if(aan==null||aan.length()==0)
			return toString();
		return aan+" "+toString();
	}
}