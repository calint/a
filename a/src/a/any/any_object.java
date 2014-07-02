package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import a.any.list.el;

final public class any_object implements el{
	private el pt;
	private Serializable o;
	private String name;
	public any_object(final el parent,final Serializable o){pt=parent;this.o=o;}
	public any_object(final el parent,final Serializable o,final String name){pt=parent;this.o=o;this.name=name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return name!=null?name:Integer.toHexString(o.hashCode());}
	@Override public String fullpath(){return o.getClass().getName()+"@"+Integer.toHexString(o.hashCode());}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return true;}
	@Override public List<String>list(){return list(null);}
	@Override public List<String>list(final String query){
		final ArrayList<String>ls=new ArrayList<String>();
		for(final Field f:o.getClass().getFields()){
//			final int m=f.getModifiers();
//			if(!Modifier.isStatic(m))continue;
//			if(Modifier.isFinal(m))continue;
			final String nm=f.getName();
			if(query!=null&&!query.isEmpty()&&!nm.startsWith(query))continue;
			ls.add(f.getName());
		}
		return ls;
	}
	@Override public el get(String name){try{return new any_object_field(this,o,name);}catch(Throwable t){throw new Error(t);}}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}
	@Override public void append(String cs){throw new UnsupportedOperationException();}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}
	@Override public boolean ommit_column_edit(){return false;}
	@Override public boolean ommit_column_lastmod(){return true;}
	@Override public boolean ommit_column_size(){return true;}
	@Override public boolean ommit_column_icon(){return false;}

	@Override public void foreach(String query,visitor v)throws Throwable{
		Arrays.stream(o.getClass().getFields()).filter(e->{
//			final int m=e.getModifiers();
//			if(!Modifier.isStatic(m))return false;
//			if(Modifier.isFinal(m))return false;
			if(!e.getName().startsWith(query))return false;
			return true;
		}).sorted((e1,e2)->e1.getName().compareTo(e2.getName()))
			.forEach(e->v.visit(new any_object_field(this,o,e.getName())));
	}

	private static final long serialVersionUID=1;
}