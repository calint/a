import java.util.*;
import java.text.*;
public class stress_parser{public static void main(String[]args)throws Throwable{
	Scanner sc=new Scanner(System.in);
	int cr=0,fr=0,tr=0;
	float duration_s=0,rps=0;
	int nclients=0,ntesters=0;
	long nbytes=0;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd---HH:mm:ss-");
	Date t0=null,t1=null;
	while(sc.hasNextLine()){
		String ln=sc.nextLine().trim();
		if(ln.startsWith("Complete requests:")){
			String[]a=ln.split("\\s+");
			int i=Integer.parseInt(a[2]);
			cr+=i;
			continue;
		}
		if(ln.startsWith("start time:")){
			String[]a=ln.split("\\s+");
			String t=a[2];
			t0=sdf.parse(t);
			continue;
		}
                if(ln.startsWith("end time:")){ 
                        String[]a=ln.split("\\s+");
                        String t=a[2];
                        t1=sdf.parse(t);
                        continue;
                }
                if(ln.startsWith("Total transferred:")){ 
                        String[]a=ln.split("\\s+");
                        nbytes+=Long.parseLong(a[2]);
			ntesters++;
                        continue;
                }
                if(ln.startsWith("Failed requests:")){
                        String[]a=ln.split("\\s+");
                        fr+=Integer.parseInt(a[2]);
                        continue;
                }
                if(ln.startsWith("Time taken for tests:")){
                        String[]a=ln.split("\\s+");
                        duration_s+=Float.parseFloat(a[4]);
                        continue;
                }
                if(ln.startsWith("clients:")){
                        String[]a=ln.split("\\s+");
                        nclients=Integer.parseInt(a[1]);
                        continue;
                }
                if(ln.startsWith("Requests per second:")){
                        String[]a=ln.split("\\s+");
                        rps+=Float.parseFloat(a[3]);
                        continue;
                }
                if(ln.startsWith("Transfer rate")){
                        String[]a=ln.split("\\s+");
                        tr+=Float.parseFloat(a[2]);
                        continue;
                }
		if(ln.startsWith("•"))continue;
		System.out.println(ln);
	}
	long dt_ms=t1.getTime()-t0.getTime();
	int dt_s=(int)(dt_ms/1000);
//	System.out.println("duration: "+(int)duration_s+" seconds");
//	System.out.println("test time: "+dt_s+" seconds");
	System.out.println("requests per second: "+rps);
	System.out.println("transfer rate: "+tr+" KB/s");
        System.out.println("total transfer: "+nbytes+" bytes  "+(nbytes>>10)+" KB   "+(nbytes>>20)+" MB");
	System.out.println("total requests: "+cr);
}}
