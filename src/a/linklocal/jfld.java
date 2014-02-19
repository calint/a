package a.linklocal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import b.a;
import b.xwriter;

public class jfld extends a{static final long serialVersionUID=1;
	private Class<?>cls;
	private String fieldnm;
	public jfld(a p,Class<?>cls,String fieldname){
		super(p,fieldname.replace('_','$'));
		this.cls=cls;
		this.fieldnm=fieldname;
	}
	public void to(xwriter x)throws Throwable{
		final Field f=cls.getField(fieldnm);
		final String s=b.b.tostr(f.get(null),"");
		if(Modifier.isFinal(f.getModifiers())){
			x.p(s);
			return;
		}
		final Class<?>type=f.getType();
		if(type==String.class){
			x.inputText(this,"line",this,null,s);
		}else if(type==int.class){
			x.inputText(this,"line",this,null,s);
		}else if(type==long.class){
			x.inputText(this,"line",this,null,s);
		}else if(type==boolean.class){
			x.inputText(this,"line",this,null,s);
		}else if(type==Set.class){
			@SuppressWarnings("unchecked")
			final Set<String>set=(Set<String>)f.get(null);
			final xwriter y=new xwriter();
			for(String ss:set)
				y.p(ss).p("; ");
			x.inputText(this,"line",this,null,y.toString());
		}else
			x.p(s);
	}
	public void x_(xwriter x,String q)throws Throwable{
		final Field f=cls.getField(fieldnm);
		final Class<?>type=f.getType();
		if(type==String.class){
			f.set(null,toString());
		}else if(type==int.class){
			f.set(null,Integer.parseInt(toString()));
		}else if(type==long.class){
			f.set(null,Long.parseLong(toString()));
		}else if(type==boolean.class){
			f.set(null,toString().startsWith("t"));
		}else if(type==Set.class){
			@SuppressWarnings("unchecked")
			final Set<String>set=(Set<String>)f.get(null);
			set.clear();
			final String[]ss=toString().trim().split(";");
			for(final String s:ss)
				set.add(s.trim());
		}
		x.xu(id(),f.get(null).toString());
		x.xfocus(this);
	}
}
