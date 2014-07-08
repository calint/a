package a.linklocal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import b.a;
import b.xwriter;

public class $ extends a{static final long serialVersionUID=1;public void to(xwriter x)throws Throwable{
	x.pre();
	x.ax(this,"clruche","::clear-uri-cache").spc();
	x.ax(this,"clrfche","::clear-file-cache").spc();
	x.nl();
	ls.clear();
	int lblw=0;
	for(Field f:b.b.class.getFields()){
		if(f.getName().length()>lblw)
			lblw=f.getName().length();
	}
	for(Field f:b.b.class.getFields()){
		final a fld=new jfld(this,b.b.class,f.getName());
		ls.add(fld);
		x.spc(lblw-f.getName().length()).p(f.getName()).p(": ").r(fld).nl();
	}
}
	private List<a>ls=new ArrayList<a>();
	protected a chldq(String id){
		for(a a:ls)
			if(a.nm().equals(id))
				return a;
		return super.chldq(id);
	}
	public void x_clruche(xwriter x,String s){x.xalert("todo: clear uri cache");}
}
