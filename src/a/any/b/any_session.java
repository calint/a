package a.any.b;

import static b.b.session;
import static b.b.tostr;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import a.any.any_object;
import a.any.list.el;
import b.session;

final public class any_session implements el{
	private el pt;
	private String session_id;
//	public any_session(final el parent){pt=parent;}
	public any_session(final el parent,final String name){pt=parent;this.session_id=name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return session_id;}
	@Override public String fullpath(){return session_id;}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return true;}
	@Override public List<String>list(){return list(null);}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}
	@Override public void append(String cs){throw new UnsupportedOperationException();}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}
	
	@Override public boolean ommit_column_edit(){return true;}
	@Override public boolean ommit_column_lastmod(){return true;}
	@Override public boolean ommit_column_size(){return true;}
	@Override public boolean ommit_column_icon(){return false;}	
	
	@Override public List<String>list(final String query){throw new UnsupportedOperationException();}
	@Override public void foreach(final String query,final visitor v)throws Throwable{
		final String q=tostr(query,"");
		final session s=session(session_id);
		s.keyset().
			stream().
				sorted((e1,e2)->e1.compareTo(e2)).
					forEach(e->{
						if(e.startsWith(q))
							v.visit(new any_session_kvp(this,session_id,e));
					});
		
	}
	@Override public el get(final String name){return new any_object(this,session(session_id).get(name),name);}
	
	private static final long serialVersionUID=1;
}