package a.bigfile;

import b.a;

public class blob extends a{
	public blob(){}
	public blob(storage p,String key,String value){super(p,key,value);}
	/** @heavy */public long size_in_bytes(){
		return str().getBytes().length;
	}
}
