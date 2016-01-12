package a.pzm.lang;

import static b.b.pl;

import java.util.ArrayList;
import java.util.Iterator;
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
		public varspace parent(){return pt;}
		public int alloc_var(statement stmt,String name){
			final allocated_var e=vars.get(name);
			if(e!=null)
				throw new compiler_error(stmt,"var '"+name+"' declared at line "+e.declared_at.source_lineno(),vars.keySet().toString());
			final allocated_var v=new allocated_var();
			v.declared_at=stmt;
			v.bound_to_register=xb.alloc_register(stmt);
			v.name=name;
			v.register_index=xb.get_register_index_for_name(v.bound_to_register);
			vars.put(name,v);
//			aliases.put(name,name);
			return v.register_index;
		}
		public int get_register_index(statement stmt,final String name){
			final String alias=aliases.get(name);
			if(alias!=null){// aliased to previous varspace
				return pt.get_register_index(stmt,alias);
			}
			final allocated_var v=vars.get(name);
			if(v==null&&pt!=null&&"block".equals(nm))
				return pt.get_register_index(stmt,name);
			if(v==null)
				throw new compiler_error(stmt,"'"+name+"' is not declared",this.toString());
			return v.register_index;
		}
		public void free_var(statement stmt,String name){
			final allocated_var v=vars.remove(name);
			if(v==null)
				throw new compiler_error(stmt,"var '"+name+"' is not declared",this.toString());
			xb.free_register(stmt,v.bound_to_register);
//			aliases.remove(v.name);
		}
		public String toString(){
			return nm+aliases.toString()+vars.values().toString();
		}
		public boolean is_declared(String name){
			final boolean yes=aliases.containsKey(name)||vars.containsKey(name);
			if(yes)return true;
			if(pt!=null&&"block".equals(nm))
				return pt.is_declared(name);
			return false;
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
	public varspace push_block(){
		return vspc=new varspace(this,vspc,"block");
	}
	public varspace push_func(){
		return vspc=new varspace(this,vspc,"func");
	}
	public varspace pop(statement stmt){
//		if(vspc.aliases!=null&&!vspc.aliases.isEmpty())throw new Error();
//		if(vspc.vars!=null&&!vspc.vars.isEmpty())throw new Error();
		
		if(vspc.aliases!=null){
			final ArrayList<String>aliases_names=new ArrayList<>();
			aliases_names.forEach(nm->vspc.unalias(stmt,nm));
		}
		if(vspc.vars!=null){
			final ArrayList<String>var_names=new ArrayList<>();
			vspc.vars.keySet().forEach(name->var_names.add(name));
			var_names.forEach(nm->vspc.free_var(stmt,nm));
		}
		return vspc=vspc.pt;
	}
	public varspace vspc(){return vspc;}
	public int nregisters=1<<6;
	public final LinkedList<String>registers_available=new LinkedList<>();
	{
//		for(String s:"a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" "))
//			registers_available.add(s);
		for(int i=0;i<nregisters;i++){
			registers_available.add("r"+i);
		}
	}
	public int get_register_index_for_name(String regnm){
		try{
			return Integer.parseInt(regnm.substring(1));
		}catch(NumberFormatException e){
			throw new Error(e);
		}
	}

	final public Map<String,statement>toc;
	private varspace vspc=new varspace(this,null,"/");
	private LinkedHashMap<String,Integer>defs=new LinkedHashMap<>();
	private LinkedHashMap<Integer,String>calls=new LinkedHashMap<>();
	private LinkedHashMap<Integer,String>lis=new LinkedHashMap<>();
	private int[]data;
	private statement[]data_to_statement;
	private int ix;
	private LinkedHashMap<Integer,expression>evals=new LinkedHashMap<>();
	public int maxregalloc;
	public statement maxregalloc_at_statement;
	public xbin(Map<String,statement> toc,final int[] dest){
		this.toc=toc;
		data=dest;
		data_to_statement=new statement[data.length];
	}
	public xbin def(final String name){
		defs.put(name,ix);
		return this;
	}
	public int def_location_in_binary_for_name(String name){
		final Integer i=defs.get(name);
		if(i==null)throw new Error("def not found: "+name);
		return i.intValue();
	}
	public void add_at_pre_link_evaluate(expression e){
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
//			pl("eval at "+me.getKey()+" to "+me.getValue());
		});
		calls.entrySet().forEach(me->{
			if(!defs.containsKey(me.getValue()))throw new Error("def not found: "+me.getValue());
			final int addr=defs.get(me.getValue());
			data[me.getKey()]|=(addr<<6);
			pl("linked call at "+me.getKey()+" to "+me.getValue());
		});
		lis.entrySet().forEach(me->{
			if(!defs.containsKey(me.getValue()))throw new Error("def not found: "+me.getValue());
			final int addr=defs.get(me.getValue());
			data[me.getKey()]=addr;
			pl("linked li at "+me.getKey()+" to "+me.getValue());
		});
	}
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
	public String alloc_register(statement at_statement){
		if(registers_available.isEmpty())throw new compiler_error(at_statement,"out of registers","");
		final int nallocated=nregisters-registers_available.size();
		if(nallocated>maxregalloc){
			maxregalloc=nallocated;
			maxregalloc_at_statement=at_statement;
		}
		return registers_available.remove(0);
	}
	public void free_register(statement stmt,String register_name){
//		pl(ix+" free register "+e);
		if(registers_available.contains(register_name))
			throw new compiler_error(stmt,"register '"+register_name+"' already freed",registers_available.toString());
		registers_available.add(0,register_name);
	}
	public statement statement_for_address(int addr){
		return data_to_statement[addr];
	}
	public String toString(){
		final xwriter x=new xwriter();
		x.p(vspc.toString());
		return x.toString();
	}
	public void write_op(final statement stmt,final int op,final int rai,final int rdi){
		int znxr=0;
		if(stmt.has_annotation("ifp"))znxr|=3;
		if(stmt.has_annotation("ifz"))znxr|=1;
		if(stmt.has_annotation("ifn"))znxr|=2;
		if(stmt.has_annotation("nxt"))znxr|=4;
		if(stmt.has_annotation("ret"))znxr|=8;
		final int i=op|znxr|(rai&63)<<8|(rdi&63)<<14;
		write(i,stmt);
	}
}