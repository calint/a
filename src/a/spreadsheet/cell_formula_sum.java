package a.spreadsheet;

import b.xwriter;

public class cell_formula_sum extends cell{
	int y0,x0,y1,x1;
	double last_refresh=0;
	public cell_formula_sum(spreadsheet c,String nm,int y0,int x0,int y1,int x1)throws Throwable{
		super(c,nm);
		this.y0=y0;this.y1=y1;this.x0=x0;this.x1=x1;
	}
	public boolean refresh(xwriter x,String cellid)throws Throwable{
		final int nrows=y1-y0;
		final int ncols=x1-x0;
		final spreadsheet sh=spreadsheat();
		double sum=0;
		for(int r=y0;r<y1;r++){
			for(int c=x0;c<x1;c++){
				final cell ce=sh.cells[r][c];
				sum+=ce.todbl();
			}	
		}
		final boolean changed=last_refresh!=sum;
		if(changed){
			last_refresh=sum;
			set(sum);
			if(x!=null)
				x.pl("$s('#"+id()+"','"+str()+"');");
		}
		return changed;
	}
	@Override
	public void to(xwriter x) throws Throwable {
		x.p("<span title='").p("=sum("+spreadsheet.cellnm(y0,x0)+" to "+spreadsheet.cellnm(y1-1,x1-1)+")").p("'>").p(toString()).span_();
	}
}
