package a.pzm.lang;

import static b.b.pl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import b.xwriter;

public final class xbin{
	final public static class varspace{
		final public static class allocated_var{
			public statement declared_at;
			public String bound_to_register;
			public int register_index;
			public String name;
			public String toString(){
				return name+":"+register_index;
			}
		}
		private varspace pt;//parent
		private String nm;//name
		private LinkedHashMap<String,allocated_var>vars;
		private xbin xb;//xbin
		public varspace(xbin b,varspace parent,String name){
			pt=parent;
			nm=name;
			xb=b;
			vars=new LinkedHashMap<>();
		}
		public int alloc_var(statement stmt,String name){
			final allocated_var e=vars.get(name);
			if(e!=null)
				throw new compiler_error(stmt,"var '"+name+"' declared at line "+e.declared_at.source_lineno(),vars.keySet().toString());
			final allocated_var v=new allocated_var();
			v.declared_at=stmt;
			v.bound_to_register=xb.allocate_register(stmt);
			v.name=name;
			v.register_index=xb.register_index_for_name(v.bound_to_register);
			vars.put(name,v);
			aliases.put(name,name);
			return v.register_index;
		}
		public int get_register_index(statement stmt,final String name){
			String nm=name;
			while(true){
				final String alias=aliases.get(nm);
				if(alias.equals(nm))
					break;
				nm=alias;
			}
			final allocated_var v=vars.get(nm);
			if(v==null)
				throw new compiler_error(stmt,"'"+name+"' is not declared",vars.keySet().toString());
			return v.register_index;
		}
		public void free_var(statement stmt,String name){
			final allocated_var v=vars.remove(name);
			if(v==null)
				throw new compiler_error(stmt,"var '"+name+"' is not declared",vars.keySet().toString());
			xb.free_register(stmt,v.bound_to_register);
			aliases.remove(v.name);
		}
		public String toString(){
			return nm+vars.keySet().toString()+aliases.toString();
		}
		public boolean is_declared(String name){
			return aliases.containsKey(name);
		}
		private LinkedHashMap<String,String>aliases=new LinkedHashMap<>();
		public void alias(statement stmt,String alias,String var){
			if(vars.containsKey(alias))
				throw new compiler_error(stmt,"alias '"+alias+"' is a var",vars.toString());
			if(aliases.containsKey(alias))
				throw new compiler_error(stmt,"alias '"+alias+"' exists",vars.toString());
			aliases.put(alias,var);
		}
		public void unalias(statement stmt,String alias){
			if(!aliases.containsKey(alias))
				throw new compiler_error(stmt,"alias '"+alias+"' not declared",vars.values().toString());
			aliases.remove(alias);
		}
		public boolean is_aliases_empty(){return aliases.isEmpty();}
	}
	public int nregisters=1<<6;
	public final LinkedList<String>registers_available=new LinkedList<>();
	{
//		for(String s:"a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" "))
//			registers_available.add(s);
		for(int i=0;i<nregisters;i++){
			registers_available.add("r"+i);
		}
	}
	public int register_index_for_name(String regnm){
		return Integer.parseInt(regnm.substring(1));
	}

	final public varspace vspc=new varspace(this,null,"/");
	final public Map<String,statement> toc;
	//		public xbin def_const(String name,def_const constant){
	//			pl("constant "+name+" "+constant);
	//			constants.put(name,constant);
	//			return this;
	//		}
	private LinkedHashMap<String,Integer>defs=new LinkedHashMap<>();
	private LinkedHashMap<Integer,String>calls=new LinkedHashMap<>();
	private LinkedHashMap<Integer,String>lis=new LinkedHashMap<>();
	//		private LinkedHashMap<String,def_const>constants;
	//		private LinkedHashMap<String,def>functions;
	private int[]data;
	private statement[]data_to_statement;
	private int ix;
	private LinkedHashMap<Integer,expression>evals=new LinkedHashMap<>();
	public xbin(Map<String,statement> toc,final int[] dest){
		this.toc=toc;
		data=dest;
		data_to_statement=new statement[data.length];
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
//	public xbin def(final String name,def_func d){
//		defs.put(name,ix);
//		pl("def func "+name+" at "+ix);
//		return this;
//	}
	public int def_location_in_binary_for_name(String src){
		final Integer i=defs.get(src);
		if(i==null)throw new Error("def not found: "+src);
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
	public xbin linker_add_call(String name){
		calls.put(ix,name);
		pl("link call at "+ix+" to "+name);
		return this;
	}
	public xbin linker_add_li(String name){
		lis.put(ix,name);
		pl("link li at "+ix+" to "+name);
		return this;
	}
	public xbin write(final int d,statement stmt_binary_belongs_to){
		data[ix]=d;
		data_to_statement[ix]=stmt_binary_belongs_to;
		ix++;
		return this;
	}
	public String allocate_register(statement at_statement){
		if(registers_available.isEmpty())throw new compiler_error(at_statement,"out of registers","");
		return registers_available.remove(0);
	}
//	private void allocate_register(statement at_statement,String name){
//		if(registers_available.isEmpty()) throw new compiler_error(at_statement,"out of registers","");
//		if(!registers_available.contains(name))throw new compiler_error(at_statement,"register not available",name);
//		if(!registers_available.remove(name))throw new Error();
//		pl(ix+" allocate register "+name);
//	}
	final LinkedHashMap<String,String>register_aliases=new LinkedHashMap<String,String>();
//	public boolean is_alias_declared(final String alias){return register_aliases.containsKey(alias);}
//	public void alias_register(statement stmt,String token,String reg){
//		if(register_aliases.containsKey(token))throw new compiler_error(stmt,"var '"+token+"' is already used",register_aliases.toString());
//		register_aliases.put(token,reg);
//		pl(ix+" register alias "+reg+"   "+token);
//	}
//	public void unalias_register(statement stmt,String token){
//		if(!register_aliases.containsKey(token))throw new compiler_error(stmt,"alias not found: "+token+"   "+register_aliases,"");
//		register_aliases.remove(token);
//		pl(ix+" unregister alias "+token);
//	}
	public void free_register(statement stmt,String e){
		pl(ix+" free register "+e);
		if(registers_available.contains(e))
			throw new compiler_error(stmt,"register '"+e+"' already freed",registers_available.toString());
		registers_available.add(0,e);
	}
//	public String register_for_alias(String alias){
//		return register_aliases.get(alias);
//	}
//	public int register_index_for_alias(statement stmt,String alias){
//		String regnm=register_for_alias(alias);
//		if(regnm==null)
//			throw new compiler_error(stmt,"var '"+alias+"' not declared",register_aliases.keySet().toString());
//		final int rdi=register_index_for_name(regnm);
//		//? magicnum
//		if(rdi<0||rdi>63) throw new Error("destination register out of range");
//		return rdi;
//	}
//	public void unalloc(statement stmt,String alias){
//		final String r=register_for_alias(alias);
//		if(r==null)throw new compiler_error(stmt,"alias not registered",alias);
//		unalias_register(stmt,alias);
//		free_register(stmt,r);
//	}
//	public String get_register_for_alias(String token){
//		return register_aliases.get(token);
//	}
	public statement statement_for_address(int addr){
		return data_to_statement[addr];
	}
	public String toString(){
		final xwriter x=new xwriter();
		x.p(vspc.toString());
		return x.toString();
	}
}