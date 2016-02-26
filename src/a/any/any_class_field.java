package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import a.any.list.el;
import b.a;
import b.b.conf;
import b.b.unit;
import b.xwriter;

final public class any_class_field implements el,el.el_column_value,el.el_column_value_editor{
	private el pt;
	private String clsnm;
	private String fldnm;
	public any_class_field(final el parent,final String class_name,final String field_name){pt=parent;clsnm=class_name;fldnm=field_name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return fldnm;}
	@Override public String fullpath(){return clsnm+"."+fldnm;}
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
		final Object o=Class.forName(clsnm).getField(fldnm).get(null);
		if(o==null)return;
		x.p(o.toString());
	}catch(Throwable t){throw new Error(t);}}
	@Override public a column_value_editor(){
		return new a_class_field_editor(clsnm,fldnm);
	}
	public static class a_class_field_editor extends a{
		private String clsnm;
		private String fldnm;
		public a_class_field_editor(final String class_name,final String field_name){clsnm=class_name;fldnm=field_name;}
		@Override public void to(xwriter x)throws Throwable{
			final Field f=Class.forName(clsnm).getField(fldnm);
			final Object o=f.get(null);
			if(o==null)clr();
			else set(o.toString());
			final conf ca=f.getAnnotation(conf.class);
			if(ca==null){
				x.p(str());
				return;
			}
			if(f.getType()==boolean.class)x.inp(this,"checkbox","font-weight:bold",null,this,null,null,this,null);
			else x.inp(this,"text","font-weight:bold",null,this,null,null,this,null);
			final unit ua=f.getAnnotation(unit.class);
			if(ua!=null)x.spc().p(ua.name());
			if(ca.reboot())x.p(" <span style=color:#c00>⎋</span>");
//			if(ca.reboot())x.nl().span("font-size:.3em;color:brown").p(" ⚠ ").spanEnd();
			final String note=ca.note();
			if(note.isEmpty())return;
			x.nl().span("font-size:.3em").p(note).span_();
		}
		public void x_(final xwriter x,final String a)throws Throwable{
			final Field f=Class.forName(clsnm).getField(fldnm);
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
