package a.pz.ba;

import static b.b.pl;
import java.util.LinkedHashMap;
import java.util.Map;

public final class xbin{
	final public Map<String,statement> toc;
	public xbin(Map<String,statement> toc,final int[] dest){
		this.toc=toc;
		data=dest;
		//			defs=new LinkedHashMap<>();
		//			links=new LinkedHashMap<>();
		//			constants=new LinkedHashMap<>();
		//			functions=new LinkedHashMap<>();
	}
	public xbin def(final String name,def_func d){
		defs.put(name,ix);
		pl("def func "+name+" at "+ix);
		return this;
	}
	public xbin def(final String name){
		defs.put(name,ix);
		pl("def "+name+" at "+ix);
		return this;
	}
	public xbin data(final String name,def_data d){
		defs.put(name,ix);
		pl("def data "+name+" at "+ix);
		return this;
	}
	public xbin write(final int d){
		data[ix++]=d;
		return this;
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
	public int ix(){
		return ix;
	}
	public int def_location_in_binary_for_name(String src){
		final Integer i=defs.get(src);
		if(i==null) throw new Error("def not found: "+src);
		return i.intValue();
	}
	private LinkedHashMap<Integer,expression> evals=new LinkedHashMap<>();		
	public void evaluate_pre_link(expression e){
		evals.put(ix,e);
	}
}