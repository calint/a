package a.pzm;

import java.util.Map;

import a.pzm.lang.statement;

final public class prog{
	final Map<String,statement>toc;
	final statement stmt;
	public prog(Map<String,statement>toc,statement stmt){
		this.toc=toc;
		this.stmt=stmt;
	}
}