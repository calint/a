package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import a.any.list.el;
import b.a;
import b.b.conf;
import b.b.unit;
import b.xwriter;

final public class any_object_field implements el,el.el_column_value,el.el_column_value_editor{
	private el pt;
	private Serializable ob;
	private String fldnm;
	public any_object_field(final el parent,final Serializable o,final String field_name){
		pt=parent;this.ob=o;fldnm=field_name;
		System.out.println("any: "+ob.getClass()+"."+fldnm);
	}
	@Override public el parent(){return pt;}
	@Override public String name(){return fldnm;}
	@Override public String fullpath(){return Integer.toHexString(ob.hashCode())+"."+fldnm;}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return ob instanceof Collection<?>;}
	@Override public List<String>list(){return null;}
	@Override public List<String>list(final String query){return null;}
	@Override public el get(String name){return null;}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}
	@Override public void append(String cs){throw new UnsupportedOperationException();}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}

	@Override public boolean ommit_column_edit(){return false;}
	@Override public boolean ommit_column_lastmod(){return false;}
	@Override public boolean ommit_column_size(){return false;}
	@Override public boolean ommit_column_icon(){return false;}
	
	
	@Override public void foreach(final String query,final visitor v)throws Throwable{
		((Collection<?>)ob).stream().
			filter(e->Integer.toHexString(e.hashCode()).startsWith(query)).
				forEach(e->v.visit(new any_object(this,(Serializable)e)));
	}
	
	@Override public void column_value(final xwriter x){try{
		final Object o=this.ob.getClass().getField(fldnm).get(this.ob);
		if(o==null)return;
		x.p(o.toString());
	}catch(Throwable t){throw new Error(t);}}
	
	@Override public a column_value_editor(){
		return new a_object_field_editor(ob,fldnm);
	}
	
	public static class a_object_field_editor extends a{
		private Serializable o;
		private String fldnm;
		public a_object_field_editor(final Serializable o,final String field_name){
			this.o=o;fldnm=field_name;
			System.out.println("any: "+o.getClass()+"."+fldnm);
		}
		@Override public void to(xwriter x)throws Throwable{
			final Field f=o.getClass().getField(fldnm);
			final Object oo=f.get(o);
			if(oo==null)clr();
			else set(oo.toString());
			final conf ca=f.getAnnotation(conf.class);
			if(ca==null||Modifier.isFinal(f.getModifiers())){
				x.p(str());
				return;
			}
			if(f.getType()==boolean.class){
				x.input(this,"checkbox","font-weight:bold",null,this,null,null,this,null);
//				x.p("checkbox");
			}else
				x.input(this,"text","font-weight:bold",null,this,null,null,this,null);
			final unit ua=f.getAnnotation(unit.class);
			if(ua!=null)x.spc().p(ua.name());
			if(ca.reboot())x.p(" <span style=color:#c00>*</span>");//⎋
			final String note=ca.note();
			if(note.isEmpty())return;
			x.span("font-size:.3em");
			if(ca.reboot())x.p("⚠ ");
			x.p(note).spanEnd();
		}
		public void x_(final xwriter x,final String a)throws Throwable{
			final Field f=o.getClass().getField(fldnm);
			//? if exception, set prev value
//				x.xalert(f.toString()+"="+this);
			final Class<?>c=f.getType();
			if(c.isAssignableFrom(int.class)){
				f.set(o,Integer.parseInt(this.toString()));
				x.xu(this,f.get(o).toString());
				return;
			}
			if(c.isAssignableFrom(long.class)){
				f.set(o,Long.parseLong(this.toString()));
				x.xu(this,f.get(o).toString());
				return;
			}
			if(c.isAssignableFrom(float.class)){
				f.set(o,Float.parseFloat(this.toString()));
				x.xu(this,f.get(o).toString());
				return;
			}
			if(c.isAssignableFrom(double.class)){
				f.set(o,Double.parseDouble(this.toString()));
				x.xu(this,f.get(o).toString());
				return;
			}
			if(c.isAssignableFrom(boolean.class)){
				final String s=this.toString();
				final Boolean b="y".equals(s)||"yes".equals(s)||"true".equals(s)||"t".equals(s)||"1".equals(s)?Boolean.TRUE:Boolean.FALSE;
				f.set(o,b);
				set(b.toString());
				x.xu(this,f.get(o).toString());
				return;
			}
			{
				f.set(o,toString());
				x.xu(this,f.get(o).toString());
			}
//				throw new Error("unknown type "+c);
		}
		private static final long serialVersionUID = 1L;
	}

	private static final long serialVersionUID=1;
}
