package a.amazon;

import b.a;
import b.req;
import b.session;
import b.xwriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;

public class key extends a{
	public final static String ACCESS_KEY_ID="__aws_access_key_id";
	public final static String SECRET_KEY="__aws_secret_key";
	public a key;
	public a output;
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("input","border:1px dotted green;padding:.5em");
		x.css(key,"border:1px dotted green;padding:.5em;width:40em;height:3em");
		x.style_();
		x.p("paste rootkey.cvs from aws.amazon.com below:").nl();
		x.inputTextArea(key);
		x.nl().ax(this,null," :: validate");
		x.spc(3).output_holder(output);
		
	}
	public void x_(final xwriter y,final String a)throws Throwable{
		y.xu(output,"").flush();
		final xwriter x=y.xub(output,true,true);
		final String s1=key.toString();
		final String[]s2=s1.split("\\r?\\n");
		final String aws_access_key_id=s2[0].split("=")[1];
		final String aws_secret_key=s2[1].split("=")[1];
		try{
			final long t0=System.currentTimeMillis();
			new AmazonEC2Client(new AWSCredentials(){
				@Override public String getAWSSecretKey(){return aws_secret_key;}
				@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
			});
			final long t1=System.currentTimeMillis();
			x.pl("validated in "+(t1-t0)+" ms");
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
