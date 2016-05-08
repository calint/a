package a.ramvark;
import a.ramvark.ab.puppet;
import a.ramvark.ab.ramvark;
import b.xwriter;
public @ls(cls=puppet.class)class refs extends lst{
	
	protected void rendhead(final xwriter x){
		x.ax(this,"refnsel","+").spc();
		super.rendhead(x);
		//		x.nl();
//		x.tag("span",this);
	}
	
	public synchronized void x_refnsel(final xwriter x,final String s)throws Throwable{
		final lst l=new ramvark();
		l.elem_refn_adding=this;
		l.label="select";
		ev(x,this,l);
	}
	
static final long serialVersionUID=1;}
