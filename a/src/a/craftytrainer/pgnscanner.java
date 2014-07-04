package a.craftytrainer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

class pgnscanner{
		private Scanner sc;
		public pgnscanner(final Scanner sc){this.sc=sc;}
//		[Site "FICS freechess.org"]
//		...
//		[Result "1-0"]
		public Map<String,String>readHeaders(){
			final Map<String,String>mp=new LinkedHashMap<String,String>();
			while(true){
				final String s1=sc.findWithinHorizon("\\[.*\\]|^$",0);
				if(s1==null||s1.length()==0)break;
				final String s2=s1.trim();
				final String s3=s2.substring(1,s1.length()-1);
				final int ix=s1.indexOf(' ');
				final String hdrnm=s3.substring(0,ix).trim();
				final String s4=s3.substring(ix);
				final String hdrvl=s4.substring(1,s4.length()-1).trim();
				mp.put(hdrnm,hdrvl);
			}
			return mp;
		}
		private boolean blkmv;
		public String readNextMove(){
//			1. e4 e5 2. Nf3 Nc6 
//			final String s3=sc.findWithinHorizon("\\d+",0);
			final String ptrneom="\\{(Black|White) (checkmated|resigns|forfeits on time)\\}";
			if(!blkmv){
				//\\{White resigns\\}|\\{Black resigns\\}
				final String s1=sc.findWithinHorizon("(\\d+\\.\\s*)|"+ptrneom,0);
				if(s1==null)return null;
				if(s1.matches(ptrneom))return null;
			}
			final String s2=sc.findWithinHorizon("[\\w+\\-\\+]+|"+ptrneom,0);
			if(s2.matches(ptrneom))return null;
			sc.findWithinHorizon("\\s*(\\{.*?}\\s*)|\\s*",0);
			blkmv=!blkmv;
			return s2;
		}
//		public String toString(){return null;}
	}