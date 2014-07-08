package a.amazon;

import b.a;
import b.req;
import b.session;
import b.xwriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;

public class key0 extends a{
	public final static String ACCESS_KEY_ID="__aws_access_key_id";
	public final static String SECRET_KEY="__aws_secret_key";
	public a accesskey;
	public a secretkey;
	public a output;
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("input","border:1px dotted green;padding:.5em");
		x.css(accesskey,"width:13em");
		x.css(secretkey,"width:25em");
		x.style_();
		x.p("aws.amazon.com   access key ").inputText(accesskey);
		x.p(" secret key ").inputText(secretkey).spc().ax(this).spc();
		x.output_holder(output);
		
	}
	public void x_(final xwriter y,final String a)throws Throwable{
		y.xu(output,"").flush();
		final xwriter x=y.xub(output,true,true);
		final String aws_secret_key=secretkey.toString();
		final String aws_access_key_id=accesskey.toString();
		try{
			final long t0=System.currentTimeMillis();
			new AmazonEC2Client(new AWSCredentials(){
				@Override public String getAWSSecretKey(){return aws_secret_key;}
				@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
			});
			final long t1=System.currentTimeMillis();
			x.pl("verified in "+(t1-t0)+" ms");
			final session s=req.get().session();
			s.put(SECRET_KEY,aws_secret_key);
			s.put(ACCESS_KEY_ID,aws_access_key_id);
			//? use instance from session, cache client
		}catch(Throwable t){
			y.xube();
			throw new Error(t);
		}
		y.xube();
	}
	
	static final long serialVersionUID=1;
}
