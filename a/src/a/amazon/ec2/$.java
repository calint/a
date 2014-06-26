package a.amazon.ec2;

import java.util.List;

import b.a;
import b.xwriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

public class $ extends a{
	public a accesskey;
	public a secretkey;
	public a output;
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("input","border:1px dotted green;padding:.5em");
		x.css(accesskey,"width:13em");
		x.css(secretkey,"width:25em");
		x.styleEnd();
		x.p("aws.amazon.com   access key ").inputText(accesskey);
		x.p(" secret key ").inputText(secretkey).spc().nl();
		x.p("  instance ").ax(this,"c","• run").spc().ax(this,"l","• list").nl();
		x.output(output);
	}
	public void x_c(final xwriter x,final String a)throws Throwable{
//		for(int i=0;i<100;i++)
			x.xu(output,"").flush();
		final xwriter y=x.xub(output,true,true);
		final String aws_secret_key=secretkey.toString();
		final String aws_access_key_id=accesskey.toString();
		try{
			main(new String[]{aws_secret_key,aws_access_key_id},y);
		}catch(Throwable t){
			x.xube();
			throw new Error(t);
		}
		x.xube();
	}
	public void x_l(final xwriter y,final String a)throws Throwable{
//		for(int i=0;i<100;i++)
			y.xu(output,"").flush();
		final xwriter x=y.xub(output,true,true);
		final String aws_secret_key=secretkey.toString();
		final String aws_access_key_id=accesskey.toString();
		try{
			final long t0=System.currentTimeMillis();
			final AmazonEC2Client ec=new AmazonEC2Client(new AWSCredentials(){
				@Override public String getAWSSecretKey(){return aws_secret_key;}
				@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
			});
			final long t1=System.currentTimeMillis();
			x.pl("time to create client in millis: "+(t1-t0));
			final DescribeInstancesResult di=ec.describeInstances();
			final long t2=System.currentTimeMillis();
			x.pl("time to describe instances in millis: "+(t2-t1));
			final List<Reservation>lsr=di.getReservations();
			for(final Reservation rr:lsr){
				for(final Instance o:rr.getInstances()){
					x.p(o.getTags()+" "+o.getState()+" "+o.getInstanceId()+"\t"+o.getPublicIpAddress()+"\t"+o.getPublicDnsName()).nl();
				}
			}
		}catch(Throwable t){
			y.xube();
			throw new Error(t);
		}
		y.xube();
	}
	private void main(final String[]args,final xwriter x)throws Throwable{
		x.pl("amazon s3");
		final AmazonEC2Client ec=new AmazonEC2Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return args[0];}
			@Override public String getAWSAccessKeyId(){return args[1];}
		});
		x.nl().nl().pl("run instances");
		final RunInstancesRequest ri=new RunInstancesRequest()
			.withImageId("ami-8e8a70e6")
			.withInstanceType("t1.micro")
			.withMinCount(1)
			.withMaxCount(1)
			.withKeyName("ramvark-keypair")
			.withSecurityGroups("allopen");
		x.pl(ri.toString());
		final RunInstancesResult ris=ec.runInstances(ri);
		x.pl(ris.toString());
		x.pl("");
		final Reservation r=ris.getReservation();
		for(final Instance o:r.getInstances()){
			x.pl(o.getInstanceId()+" "+o.getPublicIpAddress()+" "+o.getPublicIpAddress());
		}
		
		x.nl().nl().pl("describe instances");
//		final DescribeInstancesRequest dir=new DescribeInstancesRequest();
		final DescribeInstancesResult di=ec.describeInstances();
		final List<Reservation>lsr=di.getReservations();
		for(final Reservation rr:lsr){
			for(final Instance o:rr.getInstances()){
				x.pl(o.getInstanceId()+" "+o.getPublicIpAddress()+" "+o.getPublicIpAddress());
			}
		}
	}
	
	static final long serialVersionUID=1;
}
