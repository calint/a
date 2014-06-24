package a.ramvark.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import b.a;
import b.xwriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

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
		x.p(" secret key ").inputText(secretkey).spc();
		x.ax(this).nl();
		x.output(output);
	}
	public void x_(final xwriter x,final String a)throws Throwable{
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
	private void main(final String[]args,final xwriter x)throws Throwable{
		x.pl("amazon s3").flush();
		x.pl("creating client").flush();
		final AmazonS3 s3=new AmazonS3Client(new AWSCredentials(){
			@Override public String getAWSSecretKey(){return args[0];}
			@Override public String getAWSAccessKeyId(){return args[1];}
		});
		final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
		
		String bucketName = "my-first-s3-bucket-" + UUID.randomUUID();
		String key = "MyObjectKey";
		
		x.pl(" • creating bucket").flush();
		s3.createBucket(bucketName);
		x.pl(" • listing buckets").flush();
		for(Bucket bucket:s3.listBuckets()){
			x.p(" - ").pl(bucket.getName()).flush();
		}
		x.pl(" • put object").flush();
		s3.putObject(new PutObjectRequest(bucketName,key,createSampleFile()));
		x.pl(" • get object").flush();
		final S3Object object=s3.getObject(new GetObjectRequest(bucketName,key));
		final BufferedReader reader=new BufferedReader(new InputStreamReader(object.getObjectContent()));
		while(true){
			final String line=reader.readLine();
			if(line==null)break;
			x.p("    ").pl(line).flush();
		}
		x.pl(" • list objects").flush();
		final ObjectListing objectListing=s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("My"));
		for(S3ObjectSummary objectSummary:objectListing.getObjectSummaries()){
			x.p(" - ").p(objectSummary.getKey()).spc().p("(size = ").p(objectSummary.getSize()).pl(")");
		}
		x.pl(" • delete object").flush();
		s3.deleteObject(bucketName,key);
		x.pl(" • delete bucket").flush();
		s3.deleteBucket(bucketName);
		x.pl(" • done").flush();
	}
	private static File createSampleFile()throws IOException{
		File file=File.createTempFile("aws-java-sdk-",".txt");
		file.deleteOnExit();
		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write("abcdefghijklmnopqrstuvwxyz\n");
		writer.write("01234567890112345678901234\n");
		writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
		writer.write("01234567890112345678901234\n");
		writer.write("abcdefghijklmnopqrstuvwxyz\n");
		writer.close();
		return file;
	}
	
	static final long serialVersionUID=1;
}
