package a.pzm.lang;
import static b.b.pl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import b.xwriter;
public final class xbin implements Serializable{
	final public static class varspace{
		final public static class allocated_var implements Serializable{
			public statement declared_at;
			public String bound_to_register;
			public int register_index;
			public String name;
			public boolean is_const;
			public String toString(){
				return name+":"+register_index;
			}
			private static final long serialVersionUID=1;
		}
		private varspace pt;//parent
		private String nm;//name
		private LinkedHashMap<String,allocated_var>vars=new LinkedHashMap<>();
		private LinkedHashMap<String,String>aliases=new LinkedHashMap<>();
		private xbin xb;//xbin
		public varspace(xbin b,varspace parent,String name){pt=parent;nm=name;xb=b;}
		public varspace parent(){return pt;}
		public int alloc_var(statement stmt,String name){
			final allocated_var var=vars.get(name);
			if(var!=null)throw new compiler_error(stmt,"var '"+name+"' declared at line "+var.declared_at.source_lineno(),vars.keySet().toString());
			final allocated_var allocated_var=new allocated_var();
			allocated_var.declared_at=stmt;
			allocated_var.bound_to_register=xb.alloc_register(stmt);
			allocated_var.name=name;
			allocated_var.register_index=xb.get_register_index_for_name(allocated_var.bound_to_register);
			allocated_var.is_const=stmt.has_annotation("const");
			vars.put(name,allocated_var);
			return allocated_var.register_index;
		}
		public void assert_var_writable(final statement stmt,final String name,List<String>declarations){
			if(declarations==null){
				declarations=new ArrayList<>();
			}
			final String alias=aliases.get(name);
			if(alias!=null){// linked to outter varspace through param
				def_func df=null;
				statement s=stmt.parent_statement();
				while(true){
					if(s==null)break;
					if(s instanceof def_func){
						df=(def_func)s;
						break;
					}
					s=s.parent_statement();
				}
				if(df!=null){// function call
					for(def_func_param dfp:df.params){// find argument
						if(dfp.token.name.equals(name)){// i.e.  inc(@const tick)
							final boolean isconst=dfp.has_annotation("const");
							declarations.add("at line "+dfp.source_location_start.split(":")[0]+" "+(isconst?"const":"writable"));
							if(isconst){
								throw new compiler_error(stmt,name+" is const",declarations.toString());
							}else
								break;
						}
					}
				}
				if(pt!=null){// recurse
					pt.assert_var_writable(stmt,alias,declarations);
					return;
				}
			}
			final allocated_var var=vars.get(name);
			if(var!=null){
				if(var.is_const){
					declarations.add("at line "+var.declared_at.source_location_start.split(":")[0]+" const");
					throw new compiler_error(stmt,"cannot write to const '"+name+"'",declarations.toString());
				}
				return;
			}
			if(pt!=null&&"block".equals(nm)){
				pt.assert_var_writable(stmt,name,declarations);
			}
		}
//		public boolean is_var_const(final statement stmt,final String name){
//			final allocated_var var;
//			final String alias=aliases.get(name);
//			if(alias!=null){// linked to outter varspace through param
//				statement s=stmt.parent_statement();
//				def_func df=null;
//				while(true){
//					if(s==null)break;
//					if(s instanceof def_func){
//						df=(def_func)s;
//						break;
//					}
//					s=s.parent_statement();
//				}
//				if(df!=null){// function call
//					for(def_func_param dfp:df.params){// find argument
//						if(dfp.token.name.equals(name)){// i.e.  inc(@const tick)
//							if(dfp.has_annotation("const"))
//								return true;
//							else
//								break;
//						}
//					}
//					
//				}
//				if(pt!=null){// recurse
//					return pt.is_var_const(stmt,alias);
//				}
//			}
//			var=vars.get(name);
//			if(var!=null)
//				return var.is_const;
//			if(pt!=null&&"block".equals(nm))
//				return pt.is_var_const(stmt,name);
//			throw new compiler_error(stmt,"var '"+name+"' not found","");
//		}
		public int get_register_index(statement stmt,final String name){
			final String alias=aliases.get(name);
			if(alias!=null)return pt.get_register_index(stmt,alias);// aliased to previous varspace
			final allocated_var v=vars.get(name);
			if(v==null&&pt!=null&&"block".equals(nm))return pt.get_register_index(stmt,name);// block
			if(v==null)throw new compiler_error(stmt,"'"+name+"' is not found",this.toString());
			return v.register_index;
		}
		public void free_var(statement stmt,String name){
			final allocated_var v=vars.remove(name);
			if(v==null)throw new compiler_error(stmt,"var '"+name+"' is not allocated",this.toString());
			xb.free_register(stmt,v.bound_to_register);
		}
		public String toString(){return nm+aliases.toString()+vars.values().toString();}
		public boolean is_declared(String name){
			final boolean yes=aliases.containsKey(name)||vars.containsKey(name);
			if(yes)return true;
			if(pt!=null&&"block".equals(nm))
				return pt.is_declared(name);
			return false;
		}
		public void alias(statement stmt,String alias,String var){
			if(vars.containsKey(alias))throw new compiler_error(stmt,"'"+alias+"' is a var declared at x",vars.toString());
			if(aliases.containsKey(alias))throw new compiler_error(stmt,"'"+alias+"' is an alias declared at x",vars.toString());
			aliases.put(alias,var);
		}
		public void unalias(statement stmt,String alias){
			if(!aliases.containsKey(alias))throw new compiler_error(stmt,"alias '"+alias+"' not found",vars.values().toString());
			aliases.remove(alias);
		}
		public boolean is_aliases_empty(){return aliases.isEmpty();}
	}
	public varspace push_block(){return vspc=new varspace(this,vspc,"block");}
	public varspace push_func(){return vspc=new varspace(this,vspc,"func");}
	public varspace pop(statement stmt){
		if(vspc.aliases!=null){
			final ArrayList<String>aliases_names=new ArrayList<>();
			for(final String nm:aliases_names)vspc.unalias(stmt,nm);
		}
		if(vspc.vars!=null){
			final ArrayList<String>var_names=new ArrayList<>();
			for(final String name:vspc.vars.keySet())var_names.add(name);
			for(final String nm:var_names)vspc.free_var(stmt,nm);
		}
		return vspc=vspc.pt;
	}
	public varspace vspc(){return vspc;}
	public int nregisters=1<<6;
	public final LinkedList<String>registers_available=new LinkedList<>();
				{for(int i=0;i<nregisters;i++)registers_available.add("r"+i);}
	public int get_register_index_for_name(String regnm){try{return Integer.parseInt(regnm.substring(1));}catch(NumberFormatException e){throw new Error(e);}}
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
	public xbin(Map<String,statement>toc,final int[]dest){this.toc=toc;data=dest;data_to_statement=new statement[data.length];}
	public xbin def(final String name){defs.put(name,ix);return this;}
	public int def_location_in_binary_for_name(String name){
		final Integer i=defs.get(name);
		if(i==null)throw new Error("def not found: "+name);
		return i.intValue();
	}
	public void at_link_eval(expression e){evals.put(ix,e);}
	public int ix(){return ix;}
	public void link(){
		for(final Map.Entry<Integer,expression>me:evals.entrySet()){
			final int pc=me.getKey();
			final expression ev=me.getValue();
			final int value=ev.eval(this);
			data[pc]=value;
		}
		for(final Map.Entry<Integer,String>me:calls.entrySet()){
			if(!defs.containsKey(me.getValue()))throw new Error("def not found: "+me.getValue());
			final int addr=defs.get(me.getValue());
			data[me.getKey()]|=(addr<<6);
			pl("linked call at "+me.getKey()+" to "+me.getValue());
		}
		for(final Map.Entry<Integer,String>me:lis.entrySet()){
			if(!defs.containsKey(me.getValue()))throw new Error("def not found: "+me.getValue());
			final int addr=defs.get(me.getValue());
			data[me.getKey()]=addr;
			pl("linked li at "+me.getKey()+" to "+me.getValue());
		}
	}
	public xbin linker_add_call(String name){calls.put(ix,name);pl("link call at "+ix+" to "+name);return this;}
	public xbin linker_add_li(String name){lis.put(ix,name);pl("link li at "+ix+" to "+name);return this;}
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
		if(registers_available.contains(register_name))throw new compiler_error(stmt,"register '"+register_name+"' already freed",registers_available.toString());
		registers_available.add(0,register_name);
	}
	public statement statement_for_address(int addr){return data_to_statement[addr];}
	public String toString(){return new xwriter().p(vspc.toString()).toString();}
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
	private static final long serialVersionUID=1;
}