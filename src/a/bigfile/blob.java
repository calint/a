package a.bigfile;

import b.a;

public class blob extends a{
	public blob(){}
	public blob(a pt,String key,String value){super(pt,key,value);}
	/** @heavy */public long size_in_bytes(){
		return str().getBytes().length;
	}
	private static final long serialVersionUID=1;
}
