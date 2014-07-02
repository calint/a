package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import a.any.list.el;

final public class any_object implements el{
	private el pt;
	private Serializable o;
	private String name;
	public any_object(final el parent,final Serializable o){pt=parent;this.o=o;}
	public any_object(final el parent,final Serializable o,final String name){pt=parent;this.o=o;this.name=name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return name!=null?name:Integer.toHexString(o.hashCode());}
	@Override public String fullpath(){return name;}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return true;}
	@Override public List<String>list(){return list(null);}
	@Override public List<String>list(final String query){
		final List<String>ls=Arrays.stream(o.getClass().getFields()).filter(e->{
//			final int m=e.getModifiers();
//			if(!Modifier.isStatic(m))return false;
//			if(Modifier.isFinal(m))return false;
			if(query!=null&&!query.isEmpty()&&!e.getName().startsWith(query))return false;
			return true;
		}).map(e->e.getName()).collect(Collectors.toList());
		return ls;
	}
	
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
			if(query!=null&&!query.isEmpty()&&!e.getName().startsWith(query))return false;
			return true;
		}).sorted((e1,e2)->e1.getName().compareTo(e2.getName()))
			.forEach(e->{
				final Class<?>type=e.getType();
				final boolean iscol=Collection.class.isAssignableFrom(type);
				final boolean isobj=!(String.class.isAssignableFrom(type)||boolean.class.isAssignableFrom(type)||int.class.isAssignableFrom(type)||long.class.isAssignableFrom(type)||float.class.isAssignableFrom(type)||double.class.isAssignableFrom(type));
				System.out.println(e+"   "+isobj);
				final el elem;
				final Object ev;
				try{ev=e.get(o);}catch(Throwable t){throw new Error(t);}
				if(iscol)elem=new any_collection(this,(Collection)ev,e.getName());
				else if(isobj)elem=new any_object(this,(Serializable)ev,e.getName());
				else elem=new any_object_field(this,o,e.getName());
				v.visit(elem);
			});
	}
	@Override public el get(final String name){try{
		final Field f=o.getClass().getField(name);
		final Object fv=f.get(o);
		if(fv instanceof Collection)return new any_collection(this,(Collection)fv,f.getName());
		return new any_object(this,(Serializable)fv,f.getName());
//		return new any_object_field(this,o,name);
	
	}catch(Throwable t){throw new Error(t);}}

	private static final long serialVersionUID=1;
}