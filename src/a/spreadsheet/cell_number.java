package a.spreadsheet;

public class cell_number extends cell{
	private double d;
	public cell_number(spreadsheet c,String nm,double d){
		super(c,nm);
		this.d=d;
		set(d);
	}
}
