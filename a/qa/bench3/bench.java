import java.io.*;
import java.util.*;
import java.util.Scanner;
public class bench{public static void main(final String[]args)throws Throwable{
	String server="",clients="",failedreq="",reqsec="";
	for(final Scanner sc=new Scanner(System.in);sc.hasNextLine();){
		final String line=sc.nextLine().trim();
		if(line.startsWith("Server Software:")){
			final String[]ss=line.split(":");
			server=ss.length>1?ss[1].trim():"noname";
			continue;
		}
		if(line.startsWith("Concurrency Level:")){
			clients=line.split(":")[1].trim();
			continue;			
		}
        if(line.startsWith("Failed requests:")){
            failedreq=line.split(":")[1].trim();
            continue;
        }
		if(line.startsWith("Requests per second:")){
			reqsec=line.split(":")[1].trim().split(" ")[0].trim();
			continue;			
		}
		if(line.startsWith("100%")){
            if("0".equals(failedreq))
			    System.out.println(server+"\t"+clients+"\t"+reqsec);
		}
	}
}}
