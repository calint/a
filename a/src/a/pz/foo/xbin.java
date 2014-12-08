package a.pz.foo;

import static b.b.pl;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public final class xbin{
	final public Map<String,statement> toc;
	//		public xbin def_const(String name,def_const constant){
	//			pl("constant "+name+" "+constant);
	//			constants.put(name,constant);
	//			return this;
	//		}
	private LinkedHashMap<String,Integer> defs=new LinkedHashMap<>();
	private LinkedHashMap<Integer,String> calls=new LinkedHashMap<>();
	private LinkedHashMap<Integer,String> lis=new LinkedHashMap<>();
	//		private LinkedHashMap<String,def_const>constants;
	//		private LinkedHashMap<String,def>functions;
	private int[] data;
	private int ix;
	private LinkedHashMap<Integer,expression> evals=new LinkedHashMap<>();
	public xbin(Map<String,statement> toc,final int[] dest){
		this.toc=toc;
		data=dest;
		//			defs=new LinkedHashMap<>();
		//			links=new LinkedHashMap<>();
		//			constants=new LinkedHashMap<>();
		//			functions=new LinkedHashMap<>();
	}
	public xbin data(final String name,def_data d){
		defs.put(name,ix);
		pl("def data "+name+" at "+ix);
		return this;
	}
	public xbin def(final String name){
		defs.put(name,ix);
		pl("def "+name+" at "+ix);
		return this;
	}
	public xbin def(final String name,def_func d){
		defs.put(name,ix);
		pl("def func "+name+" at "+ix);
		return this;
	}
	public int def_location_in_binary_for_name(String src){
		final Integer i=defs.get(src);
		if(i==null) throw new Error("def not found: "+src);
		return i.intValue();
	}
	public void at_pre_link_evaluate(expression e){
		evals.put(ix,e);
	}
	public int ix(){
		return ix;
	}
	public void link(){
		evals.entrySet().forEach(me->{
			final int pc=me.getKey();
			final expression ev=me.getValue();
			final int value=ev.eval(this);
			data[pc]=value;
		});
		calls.entrySet().forEach(me->{
			if(!defs.containsKey(me.getValue())) throw new Error("def not found: "+me.getValue());
			final int addr=defs.get(me.getValue());
			data[me.getKey()]|=(addr<<6);
			pl("linked call at "+me.getKey()+" to "+me.getValue());
		});
		lis.entrySet().forEach(me->{
			if(!defs.containsKey(me.getValue())) throw new Error("def not found: "+me.getValue());
			final int addr=defs.get(me.getValue());
			data[me.getKey()]=addr;
			pl("linked li at "+me.getKey()+" to "+me.getValue());
		});
	}
	//		public xbin back(){
	//			ix--;
	//			return this;
	//		}
	public xbin link_call(String name){
		calls.put(ix,name);
		pl("link call at "+ix+" to "+name);
		return this;
	}
	public xbin link_li(String name){
		lis.put(ix,name);
		pl("link li at "+ix+" to "+name);
		return this;
	}
	public xbin write(final int d){
		data[ix++]=d;
		return this;
	}

	public final LinkedList<String> registers_available=new LinkedList<>();
	{
		for(String s:"a b c d e f g h i j k l m n o p".split(" "))
			registers_available.add(s);
	}
	public String allocate_register(statement at_statement){
		if(registers_available.isEmpty()) throw new compiler_error(at_statement,"out of registers","");
		return registers_available.remove(0);
	}
//	private void allocate_register(statement at_statement,String name){
//		if(registers_available.isEmpty()) throw new compiler_error(at_statement,"out of registers","");
//		if(!registers_available.contains(name))throw new compiler_error(at_statement,"register not available",name);
//		if(!registers_available.remove(name))throw new Error();
//		pl(ix+" allocate register "+name);
//	}
	final LinkedHashMap<String,String>register_aliases=new LinkedHashMap<String,String>();
	public void alias_register(String token,String reg){
		if(register_aliases.containsKey(token))throw new Error("alias "+token+" is "+register_aliases);
		register_aliases.put(token,reg);
		pl(ix+" register alias "+reg+"   "+token);
	}
	public void unalias_register(statement stmt,String token){
		if(!register_aliases.containsKey(token))throw new compiler_error(stmt,"alias not found: "+token+"   "+register_aliases,"");
		register_aliases.remove(token);
		pl(ix+" unregister alias "+token);
	}
	public void free_register(String e){
		pl(ix+" free register "+e);
		registers_available.add(0,e);
	}
	public String register_for_alias(String alias){
		return register_aliases.get(alias);
	}
	public int register_index_for_alias(statement stmt,String alias){
		 String regnm=register_for_alias(alias);
		if(regnm==null)
//			regnm=alias;
			throw new compiler_error(stmt,"alias '"+alias+"' not found in "+register_aliases,alias);
		if(regnm.length()!=1)
		 throw new compiler_error(stmt,"not a register",regnm);
		final int rdi=regnm.charAt(0)-'a';
		if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
		return rdi;
	}
	public void unalloc(statement stmt,String alias){
		final String r=register_for_alias(alias);
		if(r==null)throw new compiler_error(stmt,"alias not registered",alias);
		unalias_register(stmt,alias);
		free_register(r);
	}
	public String get_register_for_alias(String token){
		return register_aliases.get(token);
	}
}