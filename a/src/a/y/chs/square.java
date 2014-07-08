package a.y.chs;
import b.a;
import b.xwriter;
public class square extends a{
	private static final long serialVersionUID=1L;
	private int color;
	private piece piece;
	private boolean highlight;
	public square(final board board,final String name,final int color){super(board,name);this.color=color;}
	public void clearx(){this.piece=null;}
	public piece getPiece(){return piece;}
	public void setPiece(final piece piece){this.piece=piece;}
	public void setHighlight(final boolean b){highlight=b;}
	public void to(final xwriter x)throws Throwable{
		final StringBuilder sb=new StringBuilder();
		sb.append(color==0?"blk":"wht");
		if(highlight)
			sb.append(" hilite");
		x.td(sb.toString());
		x.a("javascript:$x('"+id()+" clk')");
		if(piece!=null){
			x.rend(piece);
		}else{
			x.p("&nbsp;&nbsp;");
		}
		x.a_();
		x.td_();
	}
	public void x_clk(final xwriter x,final String s)throws Throwable{
		ev(x,this,null);
	}
}
