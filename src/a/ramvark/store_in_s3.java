package a.ramvark;

import static b.b.K;
import static b.b.pl;
import static b.b.tostr;
import static b.b.uri_to;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import a.amazon.key;
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

public class store_in_s3 implements store{
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{
		cstore.meters.creates++;
		final long t0=System.currentTimeMillis();
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		final String docid=cstore.mkdocid();
		e.did.set(docid);
		final long t1=System.currentTimeMillis();
		pl(this.getClass().getName()+" create "+cls+" with owner "+owner+"  "+(t1-t0)+" ms");
		return e;
	}
	@Override public void save(final itm e)throws Throwable{
		final AmazonS3Client s3=client();
		final long t0=System.currentTimeMillis();
		final String bucket=e.getClass().getName();
		final String key=e.did.str();
		final ByteArrayOutputStream bos=new ByteArrayOutputStream(K);
		e.save(bos);
		final byte[]ba=bos.toByteArray();
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
		pl(this.getClass().getName()+" save "+e.getClass()+" "+e.did+"  "+(t1-t0)+" ms");
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		final AmazonS3Client s3=client();		
		final long t0=System.currentTimeMillis();
		final String bucket=cls.getName();
		final String prefix=tostr(q,"");
		final ObjectListing ols=s3.listObjects(new ListObjectsRequest().withBucketName(bucket));
		for(final S3ObjectSummary sos:ols.getObjectSummaries()){
			final String did=sos.getKey();
			final itm e=load(cls,did);
			final String name=e.toString();
			if(!name.startsWith(prefix))continue;
			v.visit(e);
		}
		final long t1=System.currentTimeMillis();
		pl(this.getClass().getName()+" foreach "+cls.getName()+" using query "+q+"  "+(t1-t0)+" ms");
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		final AmazonS3Client s3=client();
		final long t0=System.currentTimeMillis();
		final String bucket=cls.getName();
		final S3Object ob=s3.getObject(new GetObjectRequest(bucket,did));
		final itm e=cls.newInstance();
		e.load(ob.getObjectContent());
		final long t1=System.currentTimeMillis();
		pl(this.getClass().getName()+" load "+cls.getName()+" "+did+" "+(t1-t0)+" ms");
		return e;
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		final AmazonS3Client s3=client();
		final long t0=System.currentTimeMillis();
		final String bucket=cls.getName();
		s3.deleteObject(bucket,did);
		final long t1=System.currentTimeMillis();
		pl(this.getClass().getName()+" delete "+cls.getName()+" "+did+" "+(t1-t0)+" ms");
	}
	
	private AmazonS3Client client(){
		final long t0=System.currentTimeMillis();
		final session s=req.get().session();
		final String aws_secret_key=tostr(s.get(key.SECRET_KEY),null);
		if(aws_secret_key==null)throw new Error("amazon key not in session\n try <a href="+uri_to(key.class)+">setting amazon key</a>");
		final String aws_access_key_id=tostr(s.get(key.ACCESS_KEY_ID),null);
		final AmazonS3Client s3=new AmazonS3Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return aws_secret_key;}
			@Override public String getAWSAccessKeyId(){return aws_access_key_id;}
		});
		final long t1=System.currentTimeMillis();
		pl(this.getClass().getName()+" created s3 client: "+(t1-t0)+" ms");
		return s3;
	}

}
