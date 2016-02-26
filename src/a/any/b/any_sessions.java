package a.any.b;

import static b.b.isempty;
import static b.b.pl;
import static b.b.session;
import static b.b.sessionsids;
import static b.b.tostr;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import b.req;
import b.session;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

import a.any.list.el;
import a.any.list.el.visitor;

final public class any_sessions implements el{
	private el pt;
	private String name="sessions";
	public any_sessions(final el parent){pt=parent;}
	public any_sessions(final el parent,final String name){pt=parent;this.name=name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return name;}
	@Override public String fullpath(){return name;}
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
		sessionsids().stream().
			sorted((e1,e2)->e1.compareTo(e2)).
				forEach(e->{
					if(e.startsWith(q))v.visit(new any_session(this,e));
				});
	}
	@Override public el get(final String name){return new any_session(this,name);}

	
	private static final long serialVersionUID=1;
}