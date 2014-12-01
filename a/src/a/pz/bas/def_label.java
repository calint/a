package a.pz.bas;



final public class def_label extends def{
	public def_label(program p,String nm){
		super(p);
		name=nm;
		final def_label d=p.labels.get(name);
		if(d!=null)
			throw new compiler_error(this,"label '"+name+"' already declared at "+d.location_in_source);
		txt=":"+nm;
	}
	private static final long serialVersionUID=1;
}