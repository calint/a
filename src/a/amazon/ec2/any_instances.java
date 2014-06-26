package a.amazon.ec2;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import a.any.list.el;
import b.req;
import b.session;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

final public class any_instances implements el{
	private el pt;
	private String name="instances";
	public any_instances(final el parent){pt=parent;}
	public any_instances(final el parent,final String name){pt=parent;this.name=name;}
	@Override public el parent(){return pt;}
	@Override public String name(){return name;}
	@Override public String fullpath(){return name;}
	@Override public boolean isfile(){return false;}
	@Override public boolean isdir(){return true;}
	@Override public List<String>list(){return list(null);}
	@Override public List<String>list(final String query){
		ensure_instances_cache();
		final ArrayList<String>ls=new ArrayList<String>();
		for(final Instance o:instances_cache){
			final String nm=o.getInstanceId();
			if(query!=null&&nm.startsWith(query))continue;
			ls.add(nm);
//				x.p(o.getTags()+" "+o.getState()+" "+o.getInstanceId()+"\t"+o.getPublicIpAddress()+"\t"+o.getPublicDnsName()).nl();
		}
		return ls;
	}
	private void ensure_instances_cache(){
		final long t=System.currentTimeMillis();
		final long dt=t-instances_cache_ts;
		if(dt<instances_cache_ms&&instances_cache!=null)return;
		b.b.pl("amazon.ec2: refreshing instances cache, "+dt+" ms");
		instances_cache_ts=t;
		final session s=req.get().session();
		final String aws_secret_key=b.b.tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon key</a>");
		final String aws_access_key_id=b.b.tostr(s.get(key.ACCESS_KEY_ID),null);
//			final long t0=System.currentTimeMillis();
		final AmazonEC2Client ec=new AmazonEC2Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
//			final long t1=System.currentTimeMillis();
//			x.pl("time to create client in millis: "+(t1-t0));
		final DescribeInstancesResult di=ec.describeInstances();
//			final long t2=System.currentTimeMillis();
//			x.pl("time to describe instances in millis: "+(t2-t1));
		final List<Reservation>lsr=di.getReservations();
		instances_cache=new ArrayList<Instance>();
		for(final Reservation rr:lsr){
			for(final Instance o:rr.getInstances()){
				instances_cache.add(o);
//					x.p(o.getTags()+" "+o.getState()+" "+o.getInstanceId()+"\t"+o.getPublicIpAddress()+"\t"+o.getPublicDnsName()).nl();
			}
		}
	}
	private List<Instance>instances_cache;
	private long instances_cache_ts;
	public static int instances_cache_ms=5000;
	@Override public el get(String name){
//		System.out.println("get instance "+name);
//		final session s=req.get().session();
//		final String aws_secret_key=b.b.tostr(s.get(key.SECRET_KEY),null);
//		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon secret</a>");
//		final String aws_access_key_id=b.b.tostr(s.get(key.ACCESS_KEY_ID),null);
////		final long t0=System.currentTimeMillis();
//		final AmazonEC2Client ec=new AmazonEC2Client(new AWSCredentials(){
//			@Override public String getAWSSecretKey(){return aws_secret_key;}
//			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
//		});
//		
//		final DescribeInstancesRequest dir=new DescribeInstancesRequest()
//			.withInstanceIds(name);
//		final DescribeInstancesResult di=ec.describeInstances(dir);
//		final List<Reservation>lsr=di.getReservations();
//		final Reservation r=lsr.isEmpty()?null:lsr.get(0);
//		if(r==null)return null;
//		final List<Instance>ins=r.getInstances();
//		final Instance inst=ins.isEmpty()?null:ins.get(0);
		ensure_instances_cache();
		for(final Instance i:instances_cache){
			if(i.getInstanceId().equals(name))
				return new any_instance(this,i);
		}
		return null;
	}
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
	
	private static final long serialVersionUID=1;
}