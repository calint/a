package a.chex;

import b.a;
import b.xwriter;

public final class board extends a{
	public final static int ROWS=8;
	public final static int COLS=8;
	
	private square[][]s=new square[ROWS][COLS];
	{
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				s[r][c]=new square();
			}
		}
	}
	@Override public void to(xwriter x)throws Throwable{
//		x.p(getClass().toString());
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				s[r][c].to(x);
			}
			x.nl();
		}
	}
	public square get_square(int row,int col){
		return s[row][col];
	}
	private static final long serialVersionUID=1;
}
