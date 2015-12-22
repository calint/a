package a.spreadsheet;
import b.a;
import b.xwriter;
public class cell extends a{
	static final long serialVersionUID=1;
	public cell(spreadsheet c,String nm){
		super(c,nm);
	}
	public spreadsheet spreadsheat(){return (spreadsheet)pt();}
	public void to(final xwriter x)throws Throwable{
		final String s=toString();
		double d;
		boolean is_num;
		try{d=Double.parseDouble(s);is_num=true;}catch(NumberFormatException e){d=0;is_num=false;}
		x.p(is_num?Double.toString(d):"-");
	}
	public boolean refresh(xwriter x,String cellid)throws Throwable{
		return false;
	}
}
