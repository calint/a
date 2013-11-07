import java.io.*;
public class bench{
	public static void main(final String[]args)throws Throwable{
		for(final String a:args)System.out.println(a);
		final BufferedReader br=new BufferedReader(new FileReader("bench.log"));
		final String server="",path="",clients="",completed_req,failed_req,reqsec="";
		for(String line=br.readLine();line!=null;line=br.readLine()){
			//System.out.println(line);
			line=line.trim();
			if(line.startsWith("Server Software:")){
				final String[]ss=line.split(":");
				server=ss.length>1?ss[1].trim():"noname";
				continue;
			}
			if(line.startsWith("Document Path:")){
				path=line.split(":")[1].trim();
				continue;			
			}
			if(line.startsWith("Concurrency Level:")){
				clients=line.split(":")[1].trim();
				continue;			
			}
			if(line.startsWith("Requests per second:")){
				reqsec=line.split(":")[1].trim().split(" ")[0].trim();
				continue;			
			}
			if(line.startsWith("100%")){
				System.out.println(server+"\t"+clients+"\t"+reqsec+"\t"+path);			
			}
		}
		br.close();
	}
}
