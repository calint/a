package a.craftytrainer;
import static b.b.tobytes;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
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
	public a crafty_bin_path;{crafty_bin_path.set("crafty");}
	public ficsgames fg;
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("body","margin:0 4em 0 4em;padding:0 4em 0 4em;box-shadow:0 0 17px rgba(0,0,0,.5);border-radius:1px");
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
		x.p("paste pgn below then ").ax(this,null,"•·scan").p(" for blunders using threshhold ").inputFlt(devthr).nl();
		x.inputTextArea(in1).nl();
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
		x.p("crafty bin path: ").input(crafty_bin_path,"text","border:1px solid #eee;width:15em",null,null,null,null,null,null);
		x.nl().nl();
		fg.to(x);
	}
	public void x_(final xwriter x,final String s)throws Throwable{
		final pgnreader pgn=new pgnreader(in1.toString());
		final Map<String,String>hdr=pgn.next_tags();
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
		crafty.crafty_path=crafty_bin_path.str();
		final crafty cft=new crafty();//? optional construct for custom path
		cft.reset();
		int ply=0;
		boolean found=false;
		final float devthresh=devthr.toflt();
		while(true){
			final String move=pgn.next_move();
			if(move==null)break;
			ply++;
			if(ply%2==1)xds.p((ply>>1)+1).p(". ");
			xds.p(move).spc();
			if(ply%2!=1)xds.spc();
			final String cev=cft.move(move);
			x.xu(sts,"ply "+ply+". "+move+": "+cev).flush();
			final String[]splt=cev.split("\\s+");
			final String ev=splt[1];
			if((ply%2)==1)x.xp(grph,((ply/2)+1)+". ");
			x.xp(grph,move+" ("+ev+") ");
			if((ply%2)!=1)x.xp(grph,"\n");
			if(ev.contains("Mat")){
				x.xalert("no blunder at threshold "+devthr);//x.xu(devthr.set(devthr.toflt()/2));
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
					try(final Scanner scln=new Scanner(ln)){
						scln.findInLine("\\|");
						dg.tr();
						for(int i=0;i<8;i++){
							final String cell=scln.findInLine("(\\s{3})|(\\<.*?\\>)|(\\-.*?\\-)|( . )");
							final boolean iswht=cell.substring(0,1).equals("-");
							final String piece=cell.substring(1,2).toLowerCase();
							dg.td(sqcolwh?"wht":"blk");
							final String img;
							if(piece.equals(" ")||piece.equals("."))img="";
							else img="<img src=/chs/45px-Chess_"+piece+(iswht?"l":"d")+"t45.svg.png>";
							sqcolwh=!sqcolwh;
							dg.p(img);
						}
					}
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
}
