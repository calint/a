package a.qa;
import b.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
final public class t001 extends a{
	private static final long serialVersionUID=1;
	public void to(xwriter x)throws Throwable{
		x.pre();
		x.pl("serialize/deserialize tests");
		int i=1;for(final Object o:new Object[]{null,new String(),new String("a"),new String("ab"),new Integer(1),new Float(2),new Long(3),new Double(4),new Date(),new Boolean(true),new obj(),new num(1),new str(),new str(""),new str("a"),new str("ab"),new user(),new a(),new a(null,"",""),new a(null,"a","b"),new a(null,"a","bc")})
			serdeser(x,o," t"+i++);
		x.nl();
		x.pl("clone/declone tests");
		for(final obj o:new obj[]{new obj(),new num(1),new str(),new str(""),new str("a"),new str("ab")})
			clonedeclone(x,o," t"+i++);
	}
	public static class obj implements Serializable,Cloneable{
		private static final long serialVersionUID=1;
		public boolean equals(final Object o){if(!(o instanceof obj))return false;return true;}
		public Object clone()throws CloneNotSupportedException{return super.clone();}
		public String toString(){return "";}
	}
	public static class str extends obj{
		private static final long serialVersionUID=1;
		public final static str empty=new str();
		private String v;
		public str(){}
		public str(final String s){v=s;}
		public String toString(){return v;}
		public boolean equals(final Object o){
			if(!(o instanceof str))
				return false;
			final str os=(str)o;
			if(os.v==null)
				return v==null;
			return os.v.equals(v);
		}
		public void set(final String s){v=s;}
	}
	public static class num extends obj{
		private static final long serialVersionUID=1;
		private float v;
		public num(){}
		public num(final float f){v=f;}
		public String toString(){return Float.toString(v);}
		public boolean equals(final Object o){if(!(o instanceof num))return false;return ((num)o).v==v;}
	}
	public static class user extends obj{
		private static final long serialVersionUID=1;
		public final str name=new str("john doe");
		public final str hash=new str("0a0a0a0a0a0a0a0a0a0a0a0a0a0a0a0a");
		public final str email=new str("john.doe@domain.lol");
		public final num counter=new num(0);
		public user(){}
		public user(final String name,final String hash){
			this.name.set(name);
			this.hash.set(hash);
		}
		public String toString(){try{
			final xwriter x=new xwriter();
			boolean first=true;
			for(final Field f:getClass().getDeclaredFields()){
				if("serialVersionUID".equals(f.getName()))
						continue;
				final Object v=f.get(this);
				if(first)
					first=false;
				else
					x.p(";");
				x.p(f.getName()).p("=").p(v==null?"":v.toString());
			}
			return x.toString();
		}catch(Throwable t){throw new Error(t);}}
	}
	private void serdeser(final xwriter x,final Object o,final String prfx)throws IOException,ClassNotFoundException{
		final ByteArrayOutputStream baos=new ByteArrayOutputStream();
		final ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		x.p(prfx).p(": ").p(o==null?"null":o.getClass().getName()).p("(").p(o==null?"":o.toString()).p(")  ").p(baos.size()).p(" B").nl();
		final ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
		final ObjectInputStream ois=new ObjectInputStream(bais);
		final Object oo=ois.readObject();
		if(o==null&&oo==null)
			return;
		if(o!=null&&o.equals(oo))
			return;
		throw new Error(prfx+": "+o+" doesnotequal "+oo);
	}
	private void clonedeclone(final xwriter x,final obj o,final String prfx)throws CloneNotSupportedException{
		x.p(prfx).p(": ").p(o==null?"null":o.getClass().getName()).p("(").p(o==null?"":o.toString()).pl(")");
		final Object oo=o.clone();
		if(!o.equals(oo))
			throw new Error(prfx+": "+o+" doesnotequal "+oo);
		return;
	}
}
