package a.ramvark;

import static b.b.tostr;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import a.amazon.ec2.key;
import b.req;
import b.session;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class store_in_s3 implements store{
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{
		cstore.meters.creates++;
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		final String docid=cstore.mkdocid();
		e.did.set(docid);
		return e;
	}
	@Override public void save(final itm e)throws Throwable{
		final long t0=System.currentTimeMillis();
		final session s=req.get().session();
		final String aws_secret_key=tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon key</a>");
		final String aws_access_key_id=tostr(s.get(key.ACCESS_KEY_ID),null);
		final AmazonS3Client s3=new AmazonS3Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
//		final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		s3.setRegion(usWest2);
		
		final String bucket=e.getClass().getName();
		final String key=e.did.str();
		final ByteOutputStream bos=new ByteOutputStream(b.b.K);
		e.save(bos);
		final byte[]ba=bos.getBytes();
		final ObjectMetadata omd=new ObjectMetadata();
		omd.setLastModified(new Date());
		omd.setContentLength(ba.length);
		omd.setContentEncoding("text/plain");
//		omd.setContentType("");
		final ByteArrayInputStream bis=new ByteArrayInputStream(ba);
		while(true)
			try{
				s3.putObject(new PutObjectRequest(bucket,key,bis,omd));
				break;
			}catch(final AmazonS3Exception ex){
				if(ex.getErrorCode().equals("NoSuchBucket")){
					s3.createBucket(bucket);
				}
			}
		final long t1=System.currentTimeMillis();
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		final long t0=System.currentTimeMillis();
		final session s=req.get().session();
		final String aws_secret_key=tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon key</a>");
		final String aws_access_key_id=tostr(s.get(key.ACCESS_KEY_ID),null);
		final AmazonS3Client s3=new AmazonS3Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
		
		final String bucket=cls.getName();
		final String prefix=tostr(q,"");
		final ObjectListing ols=s3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(prefix));
		for(final S3ObjectSummary sos:ols.getObjectSummaries()){
			final String did=sos.getKey();
			final itm e=load(cls,did);
			if(!e.toString().startsWith(prefix))continue;
			v.visit(e);
		}
		final long t1=System.currentTimeMillis();
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		final long t0=System.currentTimeMillis();
		final session s=req.get().session();
		final String aws_secret_key=tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon key</a>");
		final String aws_access_key_id=tostr(s.get(key.ACCESS_KEY_ID),null);
		final AmazonS3Client s3=new AmazonS3Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
		
		final String bucket=cls.getName();
		final S3Object object=s3.getObject(new GetObjectRequest(bucket,did));
		final itm e=cls.newInstance();
		e.load(object.getObjectContent());
		final long t1=System.currentTimeMillis();
		return e;
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		final long t0=System.currentTimeMillis();
		final session s=req.get().session();
		final String aws_secret_key=tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href=/amazon.ec2.key>setting amazon key</a>");
		final String aws_access_key_id=tostr(s.get(key.ACCESS_KEY_ID),null);
		final AmazonS3Client s3=new AmazonS3Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
		
		final String bucket=cls.getName();
		s3.deleteObject(bucket,did);
		final long t1=System.currentTimeMillis();
	}
}
