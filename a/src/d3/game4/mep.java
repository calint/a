package d3.game4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import d3.p3;
import d3.world;
public class mep{
	public static void mk(String resource_path,world w,double offsetx,double offsety,double scl) throws IOException{
		InputStream is=mep.class.getResourceAsStream(resource_path);
		BufferedReader reader=new BufferedReader(new InputStreamReader(is,"utf8"));
		double scale=1;
		double startx=0;
		double startx_nl=0;
		double starty=0;
		String line=null;
		while(true){
			line=reader.readLine();
			if(line.isEmpty())
				break;
			int ix=line.indexOf(':');
			String key=line.substring(0,ix).trim();
			String value=line.substring(ix+1).trim();
			if(key.equals("startx"))
				startx=Double.parseDouble(value);
			else if(key.equals("starty"))
				starty=Double.parseDouble(value);
			else if(key.equals("scale"))
				scale=Double.parseDouble(value);
		}
		scale*=scl;
		startx_nl=startx;
		while(true){
			line=reader.readLine();
			if(line==null)
				break;
			startx=startx_nl;
			int nchars=line.length();
			for(int k=nchars-1;k>=0;k--){
				char ch=line.charAt(k);
				if(ch=='o'){
					ch='1';
				}
				if(ch=='.'){
					ch='0';
				}
				if(Character.isDigit(ch)){
					double size=(double)(ch-'0');
					if(size>0){
						double y=1;//scale;
						for(int i=0;i<size;i++){
							new cube(w,new p3(startx+offsetx,y,starty+offsety),new p3(scale,1,scale));
							y+=1;//scale*2;
						}
					}
				}
				if(ch=='N'){
					new notinca(w,new p3(startx+offsetx,10,starty+offsety));
				}
				startx+=scale*2;
			}
			starty+=scale*2;
		}
		reader.close();
	}
}
