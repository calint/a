package a.cap;

import java.util.HashMap;

import a.cap.vm.varx;

final public class namespace{
	String name;
	HashMap<String,varx>vars=new HashMap<>();
	public namespace(String nm){name=nm;}
	@Override public String toString(){return name;}
}