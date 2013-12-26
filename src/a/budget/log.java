package a.budget;
import java.io.*;
import java.text.*;
import java.util.*;
import b.*;
//import static b.b.*;
public class log extends a{
	static final long serialVersionUID=1;
	public a s;//item
	public a t;//total price
	public a q;{q.set("1");}//qty
	public a d;{d.set(tostr(new Date()));}//date
	public a l;//output
	public a f;//filter
	public a fo;//filter rend
	public a ffr;//filter from
	public a fto;//filter to	
	final public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("html","font-size:2em;padding-left:4em;line-height:1.5em");
		x.css(s,"border:1px dotted green;padding:.5em;padding:.5em;margin:.5em;border-radius:.5em");
		x.css(t,"width:4em;align:right;border:1px dotted green;padding:.5em;margin:.5em;border-radius:.5em");
		x.css(q,"width:3em;border:1px dotted green;padding:.5em;margin:.5em;border-radius:.5em");
		x.css(d,"border:1px dotted green;padding:.5em;width:8em;padding:.5em;margin:.5em;border-radius:.5em");
//		x.css(fo,"float:right");
		x.css("hr","color:black;height:.5em");
		x.styleEnd();
//		x.pl(getClass().toString());
//		x.nl();
		x.table().tr().td();
		x.p("  item").inputText(s,null,this,"s").nl();
		x.p(" total").inputText(t,null,this,"s").nl();
		x.p("   qty").inputText(q,null,this,"s").nl();
		x.p("  date").inputText(d,null,this,"s").nl();
//		x.p("  time <input type=date name="+d.id()+" value=\""+d+"\">").inputText(d,null,this,"s").nl();
		x.nl().nl();
		
		x.td();
		
		x.el(fo);
		rend_filters(x);
		x.elend();
		x.hr();
		x.el(l);
		x.elend();
		x.p(" from ").inputText(ffr).nl().p("   to ").inputText(fto).nl();
		x.tableEnd();
	}
	private path path(){
		return req.get().session().path(getClass().getPackage().getName()).get("log");
	}
	public void ax_s(final xwriter x,final String[]p)throws Throwable{
		path().append(tologdatestr(parse(d.toString()))+" "+t.toint()+" "+q.toint()+" "+s,"\n");
		rend_log(x.xub(l,true,false));x.xube();
		x.xu(s.clr());
		x.xu(q.set("1"));
		x.xfocus(s);
	}
	private void rend_log(final xwriter x) throws IOException {
		final String fr=ffr.toString();
		final String to=fto.toString();
		final int datefieldlen=logdatefmt.length();
		path().to(new osnl(){final public void onnewline(final String line)throws Throwable{
			final String datestr=line.substring(0,datefieldlen);
			if(fr.length()!=0&&datestr.compareTo(fr)<0)return;
			if(to.length()!=0&&datestr.compareTo(to)>0)return;
			x.pl(line);
		}});
	}
	public void ax_li(final xwriter x,final String[]p)throws Throwable{
		f.set(p[2]);
		final Date d=new Date();
		switch(Integer.parseInt(p[2])){
		case 1://today
			x.xu(ffr.set(tologdatestr(d)));
			x.xu(fto.set(tologdatestr(d)));
			break;
		case 2://this week
			break;
		case 3://this month
			break;
		case 4://this year
			break;
		case 5://yesterday
			final Calendar cal=new GregorianCalendar();
			cal.setTime(d);
			cal.add(Calendar.DATE,-1);
			final Date yesterday=cal.getTime();
			x.xu(ffr.set(tologdatestr(yesterday)));
			x.xu(fto.set(tologdatestr(yesterday)));
			break;
		default:throw new Error(p[2]);
		}
		rend_filters(x.xub(fo,true,false));x.xube();
		rend_log(x.xub(l,true,false));x.xube();
	}
	private void rend_filters(final xwriter x){
		final int i=f.toint();
		if(i==1)x.p(" today");else x.ax(this,"li 1"," today");
		if(i==5)x.p(" yesterday");else x.ax(this,"li 5"," yesterday");
		if(i==2)x.p(" this-week");else x.ax(this,"li 2"," this-week");
		if(i==3)x.p(" month");else x.ax(this,"li 3"," month");
		if(i==4)x.p(" year");else x.ax(this,"li 4"," year");
	}
	
	final private static String inputdatefmt="yyyy-MM-dd";
	final private static String logdatefmt="yyyyMMdd";
	final private static Date parse(final String s){try{return new SimpleDateFormat(inputdatefmt).parse(s);}catch(final Throwable t){throw new Error(t);}}
//	final private static Date parse2(final String s){try{return new SimpleDateFormat(inputdate).parse(s);}catch(final Throwable t){return null;}}
	final private static String tostr(final Date d){return new SimpleDateFormat(inputdatefmt).format(d);}
	final private static String tologdatestr(final Date d){return new SimpleDateFormat(logdatefmt).format(d);}
}
