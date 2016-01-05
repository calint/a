package a.pzm;

import java.util.Map;

import a.pzm.lang.block;
import a.pzm.lang.statement;

final public class prog{
	final Map<String,statement> toc;
	final block code;
	public prog(Map<String,statement> toc,block code){
		this.toc=toc;
		this.code=code;
	}
}