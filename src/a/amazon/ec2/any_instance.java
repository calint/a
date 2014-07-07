package a.amazon.ec2;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import a.any.list.el;
import com.amazonaws.services.ec2.model.Instance;

final public class any_instance implements el{
	private el pt;
	private Instance ins;
	public any_instance(final el parent,final Instance i){pt=parent;ins=i;}
	@Override public el parent(){return pt;}
	@Override public String name(){return ins.getInstanceId();}
	@Override public String fullpath(){return ins.getPublicDnsName();}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return false;}
	@Override public List<String>list(){return null;}
	@Override public List<String>list(final String query){return null;}
	@Override public el get(String name){return null;}
	@Override public long size(){return 0;}
	@Override public long lastmod(){return 0;}
	@Override public String uri(){return null;}
	@Override public boolean exists(){return true;}
	@Override public void append(final String cs){throw new UnsupportedOperationException();}
	@Override public boolean rm(){throw new UnsupportedOperationException();}
	@Override public OutputStream outputstream(){throw new UnsupportedOperationException();}
	@Override public InputStream inputstream(){throw new UnsupportedOperationException();}
	
	@Override public boolean ommit_column_edit(){return true;}
	@Override public boolean ommit_column_lastmod(){return true;}
	@Override public boolean ommit_column_size(){return true;}
	@Override public boolean ommit_column_icon(){return false;}
	
	@Override public void foreach(String query,visitor v) throws Throwable{throw new UnsupportedOperationException();}
	
	private static final long serialVersionUID=1;
}