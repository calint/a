package a.pz;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;

final public class program implements Serializable{
	

//	public program(final String cs)throws IOException,compiler_error{this(new StringReader(cs));}
//	public program(final Reader rr)throws IOException,compiler_error{
//		new reader(rr,this);
//		s=new ArrayList<>();
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)break;
//			r.unread(ch);
//			final stmt st=read_next_statement_from(r);
//			s.add(st);
//		}
//		s.forEach(e->e.validate_references_to_labels(this));
//		s.forEach(e->e.compile(this));
//		int pc=0;
//		for(final stmt ss:s){
//			ss.location_in_binary=pc;
//			if(ss.bin!=null)pc+=ss.bin.length;
//		}
//		s.forEach(e->e.link(this));
//	}
//	public void disassemble_to(xwriter x){
//		s.forEach(e->x.pl(e.toString()));
//	}
//	public String toString(){
//		final xwriter x=new xwriter();
//		disassemble_to(x);
//		return x.toString();
//	}

	
	
	
//	final public Map<String,define_const>defines=new LinkedHashMap<>();
//	final public Map<String,define_typedef>typedefs=new LinkedHashMap<>();
//	final public Map<String,define_struct>structs=new LinkedHashMap<>();
//	final public Map<String,define_label>labels=new LinkedHashMap<>();
//	private stmt read_next_statement_from(final reader r)throws IOException,compiler_error{
//		String tk="";
//		while(true){
//			r.skip_whitespace();
//			tk=r.next_token_in_line();
//			if(tk==null)return new eof(r);
//			if(tk.equals("const")){
//				final define_const s=new define_const(r);
//				defines.put(s.name,s);
//				return s;
//			}
//			if(tk.equals("typedef")){
//				final define_typedef s=new define_typedef(r);
//				typedefs.put(s.name,s);
//				return s;
//			}
//			if(tk.equals("struct")){
//				final define_struct s=new define_struct(r);
//				structs.put(s.name,s);
//				return s;
//			}
//			if(tk.equals("var")){
//				final define_var s=new define_var(r);
////				structs.put(s.name,s);
//				return s;
//			}
//			if(tk.startsWith(":")){
//				final define_label s=new define_label(r,tk.substring(1));
//				labels.put(s.name,s);
//				r.consume_line();
//				return s;
//			}
//			final define_typedef td=typedefs.get(tk);
//			if(td!=null){
//				final define_data_int s=new define_data_int(r);
//				labels.put(s.name,s);
//				return s;
//			}
//			if(tk.equals(".")){
//				final define_data s=new define_data(r);
//				r.consume_line();
//				return s;
//			}
//			if(tk.equals("..")){
//				final eof s=new eof(r);
//				r.consume_line();
//				return s;
//			}
//			if(tk.startsWith("//")){
//				r.consume_line();
//				continue;
//			}
//			break;
//		}
//		int znxr=0;
//		switch(tk){
//		case"ifz":{znxr=1;tk=r.next_token_in_line();break;}
//		case"ifn":{znxr=2;tk=r.next_token_in_line();break;}
//		case"ifp":{znxr=3;tk=r.next_token_in_line();break;}
//		}
//		final stmt s;
//		try{
//			s=(stmt)Class.forName(program.class.getName()+"$"+tk).getConstructor(reader.class).newInstance(r);
//		}catch(InvocationTargetException t){
//			if(t.getCause()instanceof compiler_error)throw(compiler_error)t.getCause();
//			throw new compiler_error(r.hrs_location(),t.getCause().toString());
//		}catch(InstantiationException|IllegalAccessException|NoSuchMethodException t){
//			throw new compiler_error(r.hrs_location(),t.toString());
//		}catch(ClassNotFoundException t){
//			throw new compiler_error(r.hrs_location(),"unknown instruction '"+tk+"'");
//		}catch(Throwable t){
//			throw new compiler_error(r.hrs_location(),t.toString());
//		}
//		if(!(s instanceof define_data)){
//			while(true){
//				final String t=r.next_token_in_line();
//				if(t==null)break;
//				if("nxt".equalsIgnoreCase(t)){znxr|=4;continue;}
//				if("ret".equalsIgnoreCase(t)){znxr|=8;continue;}
//				if(t.startsWith("//")){r.consume_line();break;}
//				throw new Error("3 "+t);
//			}
//			s.znxr=znxr;
//		}
//		r.consume_line();
//		return s;
//	}
//	/**writes binary*/
//	final public void zap(int[]rom){//? arraycopybinary
//		for(int i=0;i<rom.length;i++)rom[i]=-1;
//		int pc=0;
//		for(final stmt ss:s){
//			if(ss.bin==null)continue;
//			final int c=ss.bin.length;
//			System.arraycopy(ss.bin,0,rom,pc,c);
//			pc+=c;
//		}

//		final linker c=new linker(this,rom);
//		s.forEach(e->{
//			try{
//				e.write_to(c);
//			}catch(compiler_error ee){
//				throw ee;
////			}catch(InvocationTargetException ite){
////				throw new compiler_error(e.source_location,ite.getCause().getMessage());
//			}catch(NumberFormatException t){
//				throw new compiler_error(e.source_location,t.getMessage());
//			}catch(Throwable t){
//				throw new compiler_error(e.source_location,t.getMessage());}
//			});
//		c.finish();
//	}
//	public List<stmt>s;

	

	
//	private static void skip_whitespace(source_reader r)throws IOException{
//		while(true){
//			final int ch=r.read();
//			if(Character.isWhitespace(ch))continue;
//			if(ch==-1)return;
//			r.unread(ch);
//			return;
//		}
//	}
//	private static void skip_whitespace_on_same_line(source_reader r)throws IOException{
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)return;
//			if(ch=='\n'){r.unread(ch);return;}
//			if(Character.isWhitespace(ch))continue;
//			r.unread(ch);
//			return;
//		}
//	}
//	private static String next_token_in_line(source_reader r)throws IOException{
//		skip_whitespace_on_same_line(r);
//		final StringBuilder sb=new StringBuilder();
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)break;
//			if(ch=='\n'){r.unread(ch);break;}
//			if(Character.isWhitespace(ch))break;
//			sb.append((char)ch);
//		}
//		skip_whitespace_on_same_line(r);
//		if(sb.length()==0)return null;
//		return sb.toString();
//	}
//	private static String consume_rest_of_line(source_reader r)throws IOException{
//		final StringBuilder sb=new StringBuilder();
//		while(true){
//			final int ch=r.read();
//			if(ch==-1)break;
//			if(ch=='\n'){r.unread(ch);break;}
//			sb.append((char)ch);
//		}
//		return sb.toString();
//	}
	
	private static final long serialVersionUID=1;
}