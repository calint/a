package a.cap;

import java.util.LinkedList;

import a.cap.vm.stmt;
import a.cap.vm.var;

final public class struct{
		String name;
		LinkedList<struct.slot>slots=new LinkedList<>();
		public struct(String name){this.name=name;}//autoset
		final public struct.slot find_function_or_break(String funcnm){
			for(struct.slot s:slots)
				if(s.name.equals(funcnm))
					return s;
			throw new Error("cannot find function '"+funcnm+"' in struct '"+name+"'");
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
			final LinkedList<var>argsvar=new LinkedList<>();
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
				for(var v:argsvar){
					sb.append(v.type()).append(" ").append(v.code).append(",");
				}
				final int len=sb.length();
				if(len>0)sb.setLength(len-1);
				return sb.toString();
			}
			final public String args_declaration_to_string(){
				final StringBuilder sb=new StringBuilder();
				for(var v:argsvar){
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
					if(isfunc){name=tn;type="void";}
					else{name=tn;type="int";}
				}else if(i1>i2){name=tn.substring(i1+1);type=tn.substring(0,i1+1);}
				else{name=tn.substring(i2+1);type=tn.substring(0,i2);}
				ispointer=type.endsWith("*");
				if(!isfunc&&struct_member_default_value.length()==0){// set default value
					struct_member_default_value="0";
				}
			}
//			final public boolean is_pointer(){return ispointer;}
		}
	}