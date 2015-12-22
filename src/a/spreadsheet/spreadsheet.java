package a.spreadsheet;
import b.a;
import b.xwriter;
public class spreadsheet extends a{
	static final long serialVersionUID=1;
	public cell cells[][]=new cell[16][16];
	private static String cellname(int row,int col){
		return "cells~"+row+"~"+col;	
	}
	public spreadsheet()throws Throwable{
		cells[0][0]=new cell_number(this,cellname(0,0),1);
		cells[1][0]=new cell_number(this,cellname(1,0),2);
		cells[0][1]=new cell_number(this,cellname(0,1),4);
		cells[1][1]=new cell_number(this,cellname(1,1),8);
		cells[2][0]=new cell_formula_sum(this,cellname(2,0),3,0,4,1);
		cells[3][0]=new cell_formula_sum(this,cellname(3,0),0,0,2,2);
		refresh(null,null);
	}
	@Override protected a chldq(String nm){
		if(nm.startsWith("cells~")){
			final String yx=nm.substring("cells~".length());
			final int delim=yx.indexOf("~");
			final String y=yx.substring(0,delim);
			final String x=yx.substring(delim+1);
			final int yi=Integer.parseInt(y);
			final int xi=Integer.parseInt(x);
			final cell c=cells[yi][xi];
			return c;
		}
		return super.chldq(nm);
	}
	public final static String cellnm(int y,int x){
		return (char)('a'+x)+""+(y+1);
	}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null)x.title("spreadsheet");
		x.style(".t","border:1px solid green");
		x.style(".t td","padding:5px;border:1px dotted blue");
		x.table("t").tr().td();
		for(int col=0;col<cells[0].length;col++){
			x.td().p((char)('a'+col));
		}
		for(int row=0;row<cells.length;row++){
			x.tr().td().p(row+1);
			final String id=id();
			for(int col=0;col<cells[0].length;col++){
				final String cn=cellnm(row,col);
				final cell c=cells[row][col];
				if(c==null)continue;
				final String cid=c.id();
				x.p("<td id=#").p(cid).p(" onclick=\"").axjs(id(),"c",cid).p("\">");
				c.to(x);
			}
			x.nl();
		}
		x.table_();
	}
	public void x_c(xwriter x,String s)throws Throwable{
		cells[0][0].set(16);
		x.xu(cells[0][0]);
		refresh(x,s);
	}
	public void refresh(xwriter x,String cellid)throws Throwable{
		int iter=10;
		while(true){
			System.out.println(" *** iter="+iter);
			boolean refreshed_cell=false;
			for(int row=0;row<cells.length;row++){
				for(int col=0;col<cells[0].length;col++){
					final cell c=cells[row][col];
					if(c==null)continue;
					if(c.refresh(x,cellid)){
						if(!refreshed_cell)
							refreshed_cell=true;
					}
				}
			}
			if(!refreshed_cell)
				return;
			iter--;
			if(iter==0)throw new Throwable("refresh iterated");
		}
	}
}
