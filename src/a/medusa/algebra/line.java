package a.medusa.algebra;

import java.io.Serializable;

import a.medusa.medusa.takes;

public class line implements Serializable{
	public line(final@takes point p0,final@takes point p1){this.p0=p0;this.p1=p1;}
	
	private final point p0;
	private final point p1;
	
	private static final long serialVersionUID=1;
}
