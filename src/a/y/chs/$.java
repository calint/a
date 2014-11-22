package a.y.chs;
import b.a;
import b.xwriter;
public class $ extends a{
	static final long serialVersionUID=1;
	public board brd;
	public a movefld;
	private square fromsq;
	public void x_rst(final xwriter x,final String s){board_setup();x.xreload();}
	public void x_clr(final xwriter x,final String s){brd.clearx();x.xreload();}
	public void x_mov(final xwriter x,final String s){
		//		x.x_alert(movefld.toString());
		//		movefld.x_setValue(x,"");
		final String m=movefld.toString().trim();
		if(m.length()==0)
			return;
		final char pc=m.charAt(0);
		final StringBuilder sb=new StringBuilder();
		int i=m.indexOf('x');
		final boolean takes=(i!=-1);
		if(takes){
			sb.append("takes ");
		}
		i=m.indexOf('+');
		final boolean check=(i!=-1);
		if(check){
			sb.append("check ");
		}
		i=m.indexOf('#');
		final boolean mate=(i!=-1);
		if(mate){
			sb.append("mate ");
		}
		i=m.indexOf('=');
		final boolean promotion=(i!=-1);
		if(promotion){
			sb.append("promotion ");
		}
		if(m.equals("O-O")){
			sb.append("kingside castling");
		}
		if(m.equals("O-O-O")){
			sb.append("queenside castling");
		}
		final boolean pawnmove=Character.isLowerCase(pc);
		if(pawnmove){
			sb.append("pawn move");
		}else{
			sb.append(pc+" piece move ");
		}
		String destsq;
		if(takes){
			destsq=m.substring(2,4);
		}else if(!pawnmove){
			destsq=m.substring(1,3);
		}else{
			destsq=m.substring(0,2);
		}
		sb.append("  destsq  ").append(destsq);
		if(pawnmove){
			if(!takes){				
			}else{}
		}
		x.xalert(sb.toString());
		movefld.set("");
		x.xreload();
	}
	public void x_flp(final xwriter x,final String s){brd.flip();x.xreload();}
	private void board_setup(){
		brd.clearx();
		brd.square(0,0).setPiece(new piecerook(1));
		brd.square(0,1).setPiece(new pieceknight(1));
		brd.square(0,2).setPiece(new piecebishop(1));
		brd.square(0,3).setPiece(new piecequeen(1));
		brd.square(0,4).setPiece(new pieceking(1));
		brd.square(0,5).setPiece(new piecebishop(1));
		brd.square(0,6).setPiece(new pieceknight(1));
		brd.square(0,7).setPiece(new piecerook(1));
		for(int k=0;k<8;k++){
			brd.square(1,k).setPiece(new piecepawn(1));
		}
		brd.square(7,0).setPiece(new piecerook(0));
		brd.square(7,1).setPiece(new pieceknight(0));
		brd.square(7,2).setPiece(new piecebishop(0));
		brd.square(7,3).setPiece(new piecequeen(0));
		brd.square(7,4).setPiece(new pieceking(0));
		brd.square(7,5).setPiece(new piecebishop(0));
		brd.square(7,6).setPiece(new pieceknight(0));
		brd.square(7,7).setPiece(new piecerook(0));
		for(int k=0;k<8;k++){
			brd.square(6,k).setPiece(new piecepawn(0));
		}
	}
	public void to(final xwriter x)throws Throwable{
		x.r(brd);
		x.style().nl();
		x.p("input.movefld{width:80px;border:1px dotted black;}").nl();
		x.style_().nl();
		x.ax(this,"clr","::clear").p(" ");
		x.ax(this,"rst","::reset").p(" ");
		x.ax(this,"flp","::flip").br();
		x.inptxt(movefld,this,"mov","movefld");
		x.focus(movefld);
		x.ax(this,"mov","::move").p(" ");
	}
	public void ev(final xwriter x,final a fromwt,final Object o)throws Throwable{
		if(fromwt instanceof square){
			final square sq=(square)fromwt;
			if(fromsq!=null){
				fromsq.setHighlight(false);
				final piece p=fromsq.getPiece();
				if(p!=null){
					fromsq.clearx();
					sq.clearx();
					sq.setPiece(p);
				}
				fromsq=null;
			}else{
				fromsq=(square)fromwt;
				fromsq.setHighlight(true);
			}
			x.xreload();
		}else
			super.ev(x,fromwt,o);
	}
}
