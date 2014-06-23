package a.any;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import a.any.list.el;

final public class elroot implements el{
	private el pt;
	private List<el>ls;
	private String nm;
	public elroot(final el parent,final String name){
		pt=parent;
		this.nm=name;
		ls=new ArrayList<el>();
	}
	@Override public el get(String name){
		for(final el e:ls){
			final String nm=e.name();
			if(nm.equals(name))return e;
		}
		return null;
	}
	@Override public String name(){return nm;}
	@Override public boolean isdir(){return true;}
	@Override public boolean isfile(){return false;}
	@Override public List<String>list(){//? stream
		final List<String>l=new ArrayList<String>();
		for(final el e:ls){
			l.add(e.name());
		}
		return l;
	}
	@Override public List<String>list(final String query){
		final List<String>l=new ArrayList<String>();
		for(final el e:ls){
			final String nm=e.name();
			if(!nm.startsWith(query))continue;
			l.add(e.name());
		}
		return l;
	}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}
	@Override public void append(String cs){throw new UnsupportedOperationException();}
	@Override public String fullpath(){return nm;}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public el parent(){return pt;}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}
	
	final public elroot add(final el e){ls.add(e);return this;}
	
	
	
	@Override public boolean ommit_column_edit(){return true;}
	@Override public boolean ommit_column_lastmod(){return true;}
	@Override public boolean ommit_column_size(){return true;}
	@Override public boolean ommit_column_icon(){return false;}
	
	private static final long serialVersionUID=1;
}