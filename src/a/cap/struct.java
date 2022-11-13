package a.cap;

import java.util.LinkedList;

import a.cap.vm.stmt;
import a.cap.vm.varx;

final public class struct{
		String name;
		final LinkedList<struct.slot>slots=new LinkedList<>();
//		final LinkedList<struct.slot>attrs=new LinkedList<>();
//		final LinkedList<struct.slot>funcs=new LinkedList<>();
		public struct(String name){this.name=name;}//autoset
		final public struct.slot find_function_or_break(String funcnm){return find_function(funcnm,true);}
		final public struct.slot find_function(String funcnm,boolean break_if_not_found){
			for(struct.slot s:slots)
				if(s.isfunc)
					if(s.name.equals(funcnm))
						return s;
			if(break_if_not_found)throw new Error("cannot find function '"+funcnm+"' in struct '"+name+"'");
			return null;
		}
		final @Override public String toString(){return name+"{"+slots+"}";}
		final public String name(){return name;}
		final public static class slot{
			String tn="";// string from source  i.e. 'int i' 'string s'
			String name="";// decoded from tn  i.e.  'i'     's'
			String type="";//                        'int'   'string'
//			String args="";//when field this is default value
			String struct_member_default_value="";
			String func_source="";//function source
			stmt stm;
			boolean isfunc;
			boolean isctor;
			boolean ispointer;
			final LinkedList<varx>argsvar=new LinkedList<>();
			public slot(String type_and_name,boolean func){tn=type_and_name;isfunc=func;decode_tn();}
			final public int argument_count(){return argsvar.size();}
			final public String name(){return name;}
			final @Override public String toString(){
				final StringBuilder sb=new StringBuilder();
				sb.append(type).append(" ").append(name);
				if(isfunc){
					sb.append("(");
					sb.append(args_to_string());
					sb.append(")");
				}
				return sb.toString();
			}
			final public String args_to_string(){
				final StringBuilder sb=new StringBuilder();
				for(varx v:argsvar){
					sb.append(v.type()).append(" ").append(v.code).append(",");
				}
				final int len=sb.length();
				if(len>0)sb.setLength(len-1);
				return sb.toString();
			}
			final public String args_declaration_to_string(){
				final StringBuilder sb=new StringBuilder();
				for(varx v:argsvar){
					sb.append(v.type()).append(",");
				}
				final int len=sb.length();
				if(len>0)sb.setLength(len-1);
				return sb.toString();
			}
			private void decode_tn(){
				//  get func name from i.e. 'const int*func'   'const int func'
				final int i1=tn.lastIndexOf('*');
				final int i2=tn.lastIndexOf(' ');
//				if(i1==-1&&i2==-1){isctor=true;name="";type=tn;return;}
				if(i1==-1&&i2==-1){
					if(isfunc){
						name=tn;type="void";
					}else{
						name=type=tn;
						if(name.equals("int"))name="i";
//						if(name.equals("float"))name="data";
					}
				}else if(i1>i2){
					name=tn.substring(i1+1);
					type=tn.substring(0,i1+1);
				}
				else{
					name=tn.substring(i2+1);
					type=tn.substring(0,i2);
				}
				ispointer=type.endsWith("*");
				if(!isfunc&&struct_member_default_value.length()==0){// set default value
					struct_member_default_value="0";
				}
			}
//			final public boolean is_pointer(){return ispointer;}
		}
		final public slot inherits_from(){
			final slot s=slots.peekLast();
			return s;
		}
	}