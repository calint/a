package a.amazon.ec2;

import static b.b.isempty;
import static b.b.pl;
import static b.b.tostr;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import a.any.list.el;
import b.req;
import b.session;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

final public class any_instances2 implements el{
	private el pt;
	private String name="instances";
	public any_instances2(final el parent){pt=parent;}
	public any_instances2(final el parent,final String name){pt=parent;this.name=name;}
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
	
	@Override public List<String>list(final String query){
		ensure_instances_cache();
		if(isempty(query))return instances_cache.parallelStream().map(Instance::getInstanceId).collect(Collectors.toList());
		return instances_cache.parallelStream().filter(i->i.getInstanceId().startsWith(query)).map(Instance::getInstanceId).collect(Collectors.toList());
//		final ArrayList<String>ls=new ArrayList<String>();
//		for(final Instance o:instances_cache){
//			final String nm=o.getInstanceId();
//			if(query!=null&&!nm.startsWith(query))continue;
//			ls.add(nm);
//				x.p(o.getTags()+" "+o.getState()+" "+o.getInstanceId()+"\t"+o.getPublicIpAddress()+"\t"+o.getPublicDnsName()).nl();
//		}
//		return ls;
	}
	private void ensure_instances_cache(){
		//todo loadlog
		if(instances_cache!=null)return;
		final long t0=System.currentTimeMillis();
		final session s=req.get().session();
		final String aws_secret_key=tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon key</a>");
		final String aws_access_key_id=tostr(s.get(key.ACCESS_KEY_ID),null);
		final AmazonEC2Client ec=new AmazonEC2Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
		final DescribeInstancesResult di=ec.describeInstances();
		final List<Reservation>lsr=di.getReservations();
		instances_cache=new ArrayList<Instance>();
		for(final Reservation rr:lsr){
			for(final Instance o:rr.getInstances()){
				instances_cache.add(o);
			}
		}
		final long t1=System.currentTimeMillis();
		pl("load instances cache in millis: "+(t1-t0));
	}
	@Override public el get(final String name){
		ensure_instances_cache();
		final List<Instance>ils=instances_cache.stream().parallel().filter(i->i.getInstanceId().equals(name)).collect(Collectors.toList());
		if(ils.isEmpty())return null;
		return new any_instance(this, ils.get(0));
//		for(final Instance i:instances_cache){
//			if(i.getInstanceId().equals(name))
//				return new any_instance(this,i);
//		}
//		return null;
	}
	private List<Instance>instances_cache;
	
//	@Override public List<a> actions(){
//		return null;
//	}
	
	@Override public void foreach(final String query,final visitor v)throws Throwable{
		ensure_instances_cache();
		final String q=tostr(query,"");
		instances_cache.stream().sorted((e1,e2)->e1.getInstanceId().compareTo(e2.getInstanceId())).forEach(e->{
			if(e.getInstanceId().startsWith(q))
				v.visit(new any_instance(this,e));
		});
	}

	
	private static final long serialVersionUID=1;
}