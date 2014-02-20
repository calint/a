package a.y.craftytrainer;
import static b.b.tobytes;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import b.a;
import b.xwriter;
public class $ extends a{
	static final long serialVersionUID=1;
	public a sts;
	public a dsp;
	public a diag;
	public a grph;
	public a hint;
	public a in1;
	public a devthr;{devthr.set(.8f);}
	final crafty cft=new crafty();
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css(sts,"color:green");
		x.css(in1,"border:1px dotted green;width:100%;height:200px");
		x.css(dsp,"border:1px dotted blue;display:block");
		x.css(grph,"display:block;");
		x.css(diag,"display:block;");
		x.css(hint,"border:1px dotted yellow;background:#f0fff0;display:block");
		x.css("table.chsboard","border:1px solid black");
		x.css("table.chsboard td","width:45px;height:45px;align:center;vertical-align:middle");
		x.css("table.chsboard td.wht","background:white");
		x.css("table.chsboard td.blk","background:#a0a0a0");
		x.css(devthr,"border:1px solid green;text-align:right;width:2em");
		x.styleEnd();
		x.pre();
		x.p("paste pgn below then ").ax(this,null,"•·scan").p(" for blunders using threshold ").inputFlt(devthr).nl();
		x.inputTextArea(in1,"in1").nl();
		x.output(sts).nl();
		x.output(grph);
		x.tag("figure");
		x.output(dsp).nl();
		x.span(diag);
		x.tag("figcaption");
		x.output(hint);
		x.tage("figcaption");
		x.tage("figure");
		x.nl().nl();
	}
	public void x_(final xwriter x,final String s)throws Throwable{
		final pgnscanner pgn=new pgnscanner(new Scanner(in1.toString()));
		final Map<String,String>hdr=pgn.readHeaders();
		hdr.clear();
		x.xu(sts,"");
		x.xu(dsp,"");
		x.xu(diag,"");
		x.xu(hint,"");
		x.xu(grph,"");
		final xwriter xds=new xwriter();
		float prvevf=0;
//		String prvcev="";
//		String prvmove="";
		cft.reset();
		int ply=0;
		boolean found=false;
		final float devthresh=devthr.toflt();
		while(true){
			final String move=pgn.readNextMove();
			if(move==null)break;
			ply++;
			if(ply%2==1)
				xds.p((ply>>1)+1).p(". ");
			xds.p(move).spc();
			if(ply%2!=1)
				xds.spc();
			final String cev=cft.move(move).trim();
			x.xu(sts,"ply "+ply+". "+move+": "+cev).flush();
			final String[]splt=cev.split("\\s+");
			final String ev=splt[1];
			if((ply%2)==1)
				x.xp(grph,((ply/2)+1)+". ");
			x.xp(grph,move+" ("+ev+") ");
			if((ply%2)!=1)
				x.xp(grph,"\n");
			if(ev.contains("Mat")){
				x.xalert("no blunder at threshold "+devthr);
//				x.xu(devthr.set(devthr.toflt()/2));
				x.xfocus(devthr);
				return;
			}
			final float evf=Float.parseFloat(ev);
			final float devf=evf-prvevf;
			if(Math.abs(devf)>devthresh){
				x.xu(dsp," "+(devf>=0?"white":"black")+" to move and gain "+Math.abs(devf));
				final xwriter board=new xwriter();
				cft.diagram(board.outputstream());
				final byte[]diagbytes=tobytes(board.toString());
				final Scanner scb=new Scanner(new InputStreamReader(new ByteArrayInputStream(diagbytes)));
				scb.nextLine();
				int lineno=0;
				boolean sqcolwh=true;
				final xwriter dg=new xwriter();
				dg.table("chsboard");
				while(scb.hasNextLine()){
					final String ln=scb.nextLine();
					if((lineno++%2)==1)continue;
					if(lineno==17)break;
					final Scanner scln=new Scanner(ln);
					scln.findInLine("\\|");
					dg.tr();
					for(int i=0;i<8;i++){
						final String cell=scln.findInLine("(\\s{3})|(\\<.*?\\>)|(\\-.*?\\-)|( . )");
						final boolean iswht=cell.substring(0,1).equals("-");
						final String piece=cell.substring(1,2).toLowerCase();
						dg.td(sqcolwh?"wht":"blk");
						final String img;
						if(piece.equals(" ")||piece.equals("."))
							img="";
						else
							img="<img src=/chs/45px-Chess_"+piece+(iswht?"l":"d")+"t45.svg.png>";
						sqcolwh=!sqcolwh;
						dg.p(img);
					}
					scln.close();
					sqcolwh=!sqcolwh;
				}
				scb.close();
				dg.tableEnd();
				x.xu(diag,dg.toString());
				final Scanner evsc=new Scanner(cev);
				evsc.next("\\-?\\d+\\.\\d+");
				evsc.next("\\-?\\d+\\.\\d+");
				x.xu(hint,"  hint: "+evsc.nextLine().trim());
				evsc.close();
				xds.flush();
				found=true;
				break;
			}
			prvevf=evf;
//			prvcev=cev;
//			prvmove=move;
		}
		x.xu(sts,"");
		if(!found){
			x.xu(dsp," no blunders found at evaluation threshhold "+devthresh+" with search depth "+cft.srchdpth+" ply");
			return;
		}
	}
	
	static class crafty{
//		public static String crafty="/Users/calin/Documents/workspace4/crafty/crafty";
		public static String crafty="crafty";
		public int srchdpth=10;
		private final Process p;
		private final OutputStream os;
		private final InputStream is;
		private final Scanner sc;
		public crafty(){try{
			final ProcessBuilder pb=new ProcessBuilder(crafty);
			pb.directory(new File("."));
			pb.redirectErrorStream(true);
			p=pb.start();
			is=p.getInputStream();
			os=p.getOutputStream();
			os.write(("log off\nsd "+srchdpth+"\nanalyze\n").getBytes());
			os.flush();
			sc=new Scanner(is);
			sc.findWithinHorizon("analyze\\.White\\(1\\): ",0);
		}catch(final Throwable t){throw new Error(t);}}
		public void reset()throws IOException{
			os.write("reset 1\n".getBytes());
			os.flush();
			scantillnextinput();			
		}
		public String move(final String mv)throws Throwable{
			os.write((mv+"\n").getBytes());
			os.flush();
			sc.findWithinHorizon("->",0);
			final String ln=sc.nextLine();
			scantillnextinput();
			return ln;
		}
		public void back()throws Throwable{
			os.write("\n".getBytes());
			os.flush();
			scantillnextinput();
		}
		public void diagram(final OutputStream out)throws Throwable{
			final PrintWriter pw=new PrintWriter(out,true);
			os.write("d\n".getBytes());
			os.flush();
			sc.nextLine();
			sc.nextLine();
			for(int i=0;i<8*2+2;i++){
				final String s1=sc.nextLine();
				pw.println(s1);
			}
			scantillnextinput();
		}
		private void scantillnextinput(){
			sc.findWithinHorizon("analyze\\.(White|Black)\\(\\d+\\): ",0);
		}
	}
	static class pgnscanner{
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
//				if(s1==null)break;
//				final String ln=sc.nextLine();
				final String s2=s1.trim();
				final String s3=s2.substring(1,s1.length()-1);
				final int ix=s1.indexOf(' ');
				final String hdrnm=s3.substring(0,ix).trim();
				final String s4=s3.substring(ix);
				final String hdrvl=s4.substring(1,s4.length()-1).trim();
				mp.put(hdrnm,hdrvl);
//				System.out.printf("%s %s\n",hdrnm,hdrvl);
//				if(hdrnm.equals(""))break;
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
//			final String s3=sc.findWithinHorizon("(\\{\\[%emt \\d+\\.\\d+\\]\\} )|\\s+",0);
//			final String s3=sc.findWithinHorizon("\\s+",0);
//			final String s4=sc.findWithinHorizon("\\s*(\\{.*?}\\s*)|\\s*",0);
			sc.findWithinHorizon("\\s*(\\{.*?}\\s*)|\\s*",0);
			blkmv=!blkmv;
			return s2;
		}
//		public String toString(){return null;}
	}
}
