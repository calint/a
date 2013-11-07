package d2;
import java.util.Vector;
final class grids{
	private grid[]grids;
	private int rows;
	private double gridsize;
	private double gridsizeinv;
	private double xmin;
	private double ymin;
	private int ngrids;

	public grids(final double x0,final double y0,final double wihi,final int rows0){
		rows=rows0;
		ngrids=rows*rows;
		grids=new grid[ngrids];
		for(int i=0;i<ngrids;i++)
			grids[i]=new grid();
		gridsize=wihi/(double)rows;
		gridsizeinv=(double)rows/wihi;
	}

	public void getGridsForCircle(final double x,final double y,final double r,final Vector<grid>ret){//?
		int xx=(int)((x-xmin)*gridsizeinv);
		int yy=(int)((y-ymin)*gridsizeinv);
		ret.addElement(grids[yy*rows+xx]);
		boolean bool;
		if(x-gridsize*(double)xx<r)
			bool=true;
		else
			bool=false;
		boolean bool_6_;
		if(gridsize*(double)(xx+1)-x<r)
			bool_6_=true;
		else
			bool_6_=false;
		boolean bool_7_;
		if(y-gridsize*(double)yy<r)
			bool_7_=true;
		else
			bool_7_=false;
		boolean bool_8_;
		if(gridsize*(double)(yy+1)-y<r)
			bool_8_=true;
		else
			bool_8_=false;
		if(bool_7_) ret.addElement(grids[(yy-1)*rows+xx]);
		if(bool_8_) ret.addElement(grids[(yy+1)*rows+xx]);
		if(bool) ret.addElement(grids[yy*rows+xx-1]);
		if(bool_6_) ret.addElement(grids[yy*rows+xx+1]);
		if(bool&bool_8_) ret.addElement(grids[(yy+1)*rows+xx-1]);
		if(bool&bool_7_) ret.addElement(grids[(yy-1)*rows+xx-1]);
		if(bool_6_&bool_8_) ret.addElement(grids[(yy+1)*rows+xx+1]);
		if(bool_6_&bool_7_) ret.addElement(grids[(yy-1)*rows+xx+1]);
	}

	public void detectCollisionsAndClear(){
		for(final grid g:grids){
			g.detectcollision();
			g.clear();
		}
	}
}
