package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import a.any.list.el;
import a.any.list.el.visitor;
import b.a;
import b.b.conf;
import b.b.conf_reboot;
import b.b.unit;
import b.xwriter;

final public class elclassfield implements el,el.el_column_value,el.el_column_value_editor{
	private el pt;
	private Field fld;
	public elclassfield(final el parent,final Field f){pt=parent;fld=f;}
	@Override public el parent(){return pt;}
	@Override public String name(){return fld.getName();}
	@Override public String fullpath(){return fld.getDeclaringClass().getName()+"."+fld.getName();}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return false;}
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
	
	@Override public void column_value(final xwriter x){try{
		final Object o=fld.get(null);
		if(o==null)return;
		x.p(o.toString());
	}catch(Throwable t){throw new Error(t);}}
	@Override public a column_value_editor(){
		return new a_class_field_editor(fld);
	}
	public static class a_class_field_editor extends a{
		private Field f;
		public a_class_field_editor(final Field f){this.f=f;}
		@Override public void to(xwriter x)throws Throwable{
			final Object o=f.get(null);
			if(o==null)clr();
			else set(o.toString());
			final conf ca=f.getAnnotation(conf.class);
			final conf_reboot cra=f.getAnnotation(conf_reboot.class);
			if(ca==null&&cra==null){
				x.p(str());
				return;
			}
			if(f.getType()==boolean.class){
				x.input(this,"checkbox","font-weight:bold",null,this,null,null,this,null);
//				x.p("checkbox");
			}else
				x.input(this,"text","font-weight:bold",null,this,null,null,this,null);
			final unit ua=f.getAnnotation(unit.class);
			if(ua!=null){
				x.spc().p(ua.name());
			}
			if(cra!=null){
				x.pl(" <span style=color:#c00>⎋</span>");
			}
			if(ca!=null){
				final String note=ca.note();
				if(!note.isEmpty()){
					x.nl().span("font-size:.3em").p(note).spanEnd();
				}
			}else if(cra!=null){
				final String note=cra.note();
				if(!note.isEmpty()){
					x.span("font-size:.3em;color:brown").p("⚠ ").p(note).spanEnd();
				}
			}
		}
		public void x_(final xwriter x,final String a)throws Throwable{
			//? if exception, set prev value
//				x.xalert(f.toString()+"="+this);
			final Class<?>c=f.getType();
			if(c.isAssignableFrom(int.class)){
				f.set(null,Integer.parseInt(this.toString()));
				x.xu(this,f.get(null).toString());
				return;
			}
			if(c.isAssignableFrom(long.class)){
				f.set(null,Long.parseLong(this.toString()));
				x.xu(this,f.get(null).toString());
				return;
			}
			if(c.isAssignableFrom(float.class)){
				f.set(null,Float.parseFloat(this.toString()));
				x.xu(this,f.get(null).toString());
				return;
			}
			if(c.isAssignableFrom(double.class)){
				f.set(null,Double.parseDouble(this.toString()));
				x.xu(this,f.get(null).toString());
				return;
			}
			if(c.isAssignableFrom(boolean.class)){
				final String s=this.toString();
				final Boolean b="y".equals(s)||"yes".equals(s)||"true".equals(s)||"t".equals(s)||"1".equals(s)?Boolean.TRUE:Boolean.FALSE;
				f.set(null,b);
				set(b.toString());
				x.xu(this,f.get(null).toString());
				return;
			}
			{
				f.set(null,toString());
				x.xu(this,f.get(null).toString());
			}
//				throw new Error("unknown type "+c);
		}
		private static final long serialVersionUID = 1L;
	}
	@Override public boolean ommit_column_edit(){return false;}
	@Override public boolean ommit_column_lastmod(){return false;}
	@Override public boolean ommit_column_size(){return false;}
	@Override public boolean ommit_column_icon(){return false;}
	
	
	@Override public void foreach(String query,visitor v) throws Throwable{throw new UnsupportedOperationException();}

	private static final long serialVersionUID=1;
}
