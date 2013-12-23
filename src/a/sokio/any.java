package a.sokio;

import java.io.Serializable;

public class any implements Serializable{
	private static final long serialVersionUID=1;
	protected String name;
	protected String description;
	final public String name(){return name;}
	final public String description(){return description;}
	public String toString(){
		if(name!=null)return name;
		final String s=getClass().getName().replace('_',' ');
		final int i=s.lastIndexOf('$');
		if(i==-1)return s;
		return s.substring(i+1);
	}
}