package a.any.b;

import static b.b.session;
import static b.b.tostr;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import a.any.any_object;
import a.any.list.el;

final public class any_session_kvp implements el{
	private el pt;
	private String session;
	private String key;
//	public any_session(final el parent){pt=parent;}
	public any_session_kvp(final el parent,final String session,final String key){pt=parent;this.session=session;this.key=key;}
	@Override public el parent(){return pt;}
	@Override public String name(){return key;}
	@Override public String fullpath(){return key;}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return true;}
	@Override public List<String>list(){return list(null);}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}
	@Override public boolean ommit_column_edit(){return true;}
	@Override public boolean ommit_column_lastmod(){return true;}
	@Override public boolean ommit_column_size(){return true;}
	@Override public boolean ommit_column_icon(){return false;}	
	@Override public List<String>list(final String query){throw new UnsupportedOperationException();}
	@Override public void foreach(final String query,final visitor v)throws Throwable{throw new UnsupportedOperationException();}

	@Override public void append(String cs){throw new UnsupportedOperationException();}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}
	@Override public el get(final String name){throw new UnsupportedOperationException();}
	
	private static final long serialVersionUID=1;
}