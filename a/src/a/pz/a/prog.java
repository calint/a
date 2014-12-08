package a.pz.a;

import java.util.Map;
import a.pz.foo.block;
import a.pz.foo.statement;

final public class prog{
	final Map<String,statement> toc;
	final block code;
	public prog(Map<String,statement> toc,block code){
		this.toc=toc;
		this.code=code;
	}
}