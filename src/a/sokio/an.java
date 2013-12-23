package a.sokio;
import java.io.*;
abstract class an implements Serializable{
	private static final long serialVersionUID=1;
	protected String name;
	protected String description;
	final public String name(){return name;}
	final public String description(){return description;}
	final public void description(final String s){description=s;}
	public String toString(){
		if(name!=null)return name;
		final String s=getClass().getName().replace('_',' ');
		final int i=s.lastIndexOf('$');
		if(i!=-1)s.substring(i+1);
		final int ii=s.lastIndexOf('.');
		if(ii==-1)return s;
		return s.substring(ii+1);
	}
}