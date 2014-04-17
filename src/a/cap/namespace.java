package a.cap;

import java.util.HashMap;

import a.cap.vm.var;

final public class namespace{
	String name;
	HashMap<String,var>vars=new HashMap<>();
	public namespace(String nm){name=nm;}
	@Override public String toString(){return name;}
}