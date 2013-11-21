package a.ramvark.ab.qa;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import b.a;
import b.xwriter;
public class $ extends a{static final long serialVersionUID=1;
	private static class name implements Serializable{static final long serialVersionUID=1;
		public static name from(final String s){
			final name nm=new name();
			nm.str=s;
			return nm;
		}
		String str;
		public String toString(){return str;}
	}
	
	private static class birthdate implements Serializable{static final long serialVersionUID=1;
		public static birthdate from(final long ms){
			final birthdate bd=new birthdate();
			bd.ms=ms;
			return bd;
		}
		long ms;
		public String toString(){return sdf().format(new Date(ms));}
		public static SimpleDateFormat sdf(){return new SimpleDateFormat("yyyyMMdd");}
	}
	
	private static class fullname implements Serializable{static final long serialVersionUID=1;
		public static fullname from(String first,String middle,String last){
			final fullname fn=new fullname();
			fn.first=name.from(first);
			fn.middle=name.from(middle);
			fn.last=name.from(last);
			return fn;
		}
		name first;
		name middle;
		name last;
		public String toString(){return first+" "+middle+" "+last;}
	}

	private static class ssn implements Serializable{static final long serialVersionUID=1;
		public static ssn from(final String s){
			if(s.length()!=4)throw new Error("validation");
			final ssn o=new ssn();
			o.str=s;
			return o;
		}
		String str;
		public String toString(){return str;}
	}

	private static class person implements Serializable{static final long serialVersionUID=1;
		fullname fullname;
		birthdate birthdate;
		ssn ssn;
		public String toString(){return fullname+", "+birthdate+"-"+ssn;}
	}

	private static Collection<person>persons=Collections.synchronizedCollection(new LinkedList<person>());
	static{try{
		final ArrayList<name>surnames=new ArrayList<name>();
		final InputStream is=$.class.getResourceAsStream("names.sur.tbl");
		final Scanner sc=new Scanner(is);sc.nextLine();
		while(sc.hasNextLine()){surnames.add(name.from(sc.next()));sc.nextLine();}
		sc.close();
		
		final ArrayList<name>femnames=new ArrayList<name>();
		final InputStream is1=$.class.getResourceAsStream("names.fem.tbl");
		final Scanner sc1=new Scanner(is1);sc1.nextLine();
		while(sc1.hasNextLine()){femnames.add(name.from(sc1.next()));sc1.nextLine();}
		sc1.close();
		
		final ArrayList<name>malnames=new ArrayList<name>();
		final InputStream is2=$.class.getResourceAsStream("names.mal.tbl");
		final Scanner sc2=new Scanner(is2);sc2.nextLine();
		while(sc2.hasNextLine()){malnames.add(name.from(sc2.next()));sc2.nextLine();}
		sc2.close();
		
		final long t0=System.currentTimeMillis();
		final int n=100000;
		final StringBuilder sbssn=new StringBuilder(4);
		for(int i=0;i<n;i++){
			final person pn=new person();
			final ArrayList<name>aln=(b.b.rndint(0,2)==2?femnames:malnames);
			pn.fullname=fullname.from(aln.get(b.b.rndint(0,aln.size()-1)).toString(),aln.get(b.b.rndint(0,aln.size()-1)).toString(),surnames.get(b.b.rndint(0,surnames.size()-1)).toString());
			final int age=b.b.rndint(0,120);
//			System.out.println(age);
			final long msday=1000*60*60*24;
			final long msyear=msday*365;
			pn.birthdate=birthdate.from(t0-age*msyear+b.b.rndint(0,(int)msday));
			sbssn.setLength(0);
			for(int i1=0;i1<4;i1++)sbssn.append((char)('0'+b.b.rndint(0,10)));
			pn.ssn=ssn.from(sbssn.toString());
			persons.add(pn);
		}
		final long t1=System.currentTimeMillis();
		final long dt_ms=t1-t0;
		final String s=dt_ms+" ms, "+n*1000/dt_ms+" persons/s";
		b.b.log(new Throwable(s));
	}catch(final Throwable t){throw new Error(t);}}

	public void to(final xwriter x)throws Throwable{
		for(person pn:persons){
			x.pl(pn.toString());
		}
	}
}
