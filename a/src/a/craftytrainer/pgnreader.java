package a.craftytrainer;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

class pgnreader{
		private PushbackReader pr;
		public pgnreader(final Reader r){pr=new PushbackReader(r,1);}
		public pgnreader(final String s){pr=new PushbackReader(new StringReader(s),1);}
		public Map<String,String>readTags()throws IOException{
			final LinkedHashMap<String,String>tags=new LinkedHashMap<String,String>();
			while(true){
				skip_white_space(pr);
				final int ch=pr.read();
				if(ch==-1){return null;}
				if(ch=='1'){pr.unread(ch);break;}//no headers
				if(ch!='[')throw new Error("expected [ for tag start");
				//read tagname
				final tagname tn=new tagname(pr);
				skip_white_space(pr);
				final tagvalue tv=new tagvalue(pr);
				tags.put(tn.toString(),tv.toString());
			}
			return tags;
		}
		public String readNextMove()throws IOException{
			skip_white_space(pr);
			if(!blkmv){
				final StringBuilder sb=new StringBuilder();
				while(true){
					final int ch=pr.read();
					if(ch==-1)break;
					if(Character.isWhitespace(ch))break;
					sb.append((char)ch);
				}
				final String s=sb.toString();
				if(s.equals("1-0")||s.equals("0-1")||s.equals("1/2-1/2")){
					blkmv=false;
					return null;//end of game
				}
				skip_white_space(pr);
			}
			blkmv=!blkmv;
			final move mv=new move(pr);
			skip_white_space(pr);
			final int ch=pr.read();
			if(ch=='{')new comment(pr);
			else if(ch!=-1)pr.unread(ch);
			final String mvstr=mv.toString();
			if(mvstr.equals("1-0")||mvstr.equals("0-1")||mvstr.equals("1/2-1/2")){
				blkmv=false;
				return null;//end of game
			}
			return mv.toString();
		}

		private boolean blkmv;
		private static void skip_white_space(PushbackReader pr)throws IOException{
			while(true){
				final int ch=pr.read();
				if(ch==-1)break;
				if(Character.isWhitespace(ch))continue;
				pr.unread(ch);
				break;
			}
		}
		private static class tagname{
			final String s;
			tagname(Reader r)throws IOException{
				final StringBuilder sb=new StringBuilder();
				while(true){
					final int ch=r.read();
					if(ch==-1)break;
					if(ch==' ')break;
					sb.append((char)ch);
				}
				s=sb.toString();
			}
			public String toString(){return s;}
		}	
		private static class tagvalue{
			final String s;
			tagvalue(Reader r)throws IOException{
				final StringBuilder sb=new StringBuilder();
				if(r.read()!='"')throw new Error("expected \"");
				while(true){
					final int ch=r.read();
					if(ch==-1)break;
					if(ch=='\"')break;
					sb.append((char)ch);
				}
				s=sb.toString();
				if(r.read()!=']')throw new Error("expected ]");
			}
			public String toString(){return s;}
		}
		private static class move{
			final String s;
			move(Reader r)throws IOException{
				final StringBuilder sb=new StringBuilder();
				while(true){
					final int ch=r.read();
					if(ch==-1)break;
					if(Character.isWhitespace(ch))break;
					sb.append((char)ch);
				}
				s=sb.toString();
			}
			public String toString(){return s;}
		}
		private static class comment{
			final String s;
			comment(Reader r)throws IOException{
				final StringBuilder sb=new StringBuilder();
				while(true){
					final int ch=r.read();
					if(ch==-1)break;
					if(ch=='}')break;
					sb.append((char)ch);
				}
				s=sb.toString();
			}
			public String toString(){return s;}
		}

}