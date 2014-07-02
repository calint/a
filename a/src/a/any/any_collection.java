package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import a.any.list.el;

final public class any_collection implements el{
	private el pt;
	private Collection<?>col;
	private String name;
	public any_collection(final el parent,final Collection<?>c){pt=parent;col=c;}
	public any_collection(final el parent,final Collection<?>c,final String name){pt=parent;col=c;this.name=name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return name!=null?name:col.getClass().getName()+"@"+Integer.toHexString(col.hashCode());}
	@Override public String fullpath(){return name();}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return true;}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}

	@Override public List<String>list(){throw new UnsupportedOperationException();}
	@Override public List<String>list(final String query){throw new UnsupportedOperationException();}
	@Override public void append(String cs){throw new UnsupportedOperationException();}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}

	@Override public boolean ommit_column_edit(){return false;}
	@Override public boolean ommit_column_lastmod(){return true;}
	@Override public boolean ommit_column_size(){return true;}
	@Override public boolean ommit_column_icon(){return false;}

	@Override public void foreach(String query,visitor v)throws Throwable{
		col.stream().filter(e->{
			if(!Integer.toHexString(e.hashCode()).startsWith(query))return false;
			return true;
		}).sorted((e1,e2)->Integer.toHexString(e1.hashCode()).compareTo(Integer.toHexString(e2.hashCode())))
			.forEach(e->v.visit(new any_object(this,(Serializable)e,e.toString())));
	}
	@Override public el get(String name){
		final int hash_code=Integer.parseInt(name,16);
		final Object ob=col.parallelStream().filter(o->o.hashCode()==hash_code).findFirst();
		final el e=new any_object(this,(Serializable)ob);
		return e;
	}

	private static final long serialVersionUID=1;
}