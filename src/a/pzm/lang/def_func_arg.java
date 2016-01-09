package a.pzm.lang;

final public class def_func_arg extends expression{
	private static final long serialVersionUID=1;
	public def_func_arg(def_func parent,reader r){
		super(parent,null,r,null,null);
	}
	@Override public void binary_to(xbin x){
		return;
	}
}