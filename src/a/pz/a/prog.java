package a.pz.a;

import java.util.Map;
import a.pz.ba.block;
import a.pz.ba.statement;

final public class prog{
	final Map<String,statement> toc;
	final block code;
	public prog(Map<String,statement> toc,block code){
		this.toc=toc;
		this.code=code;
	}
}