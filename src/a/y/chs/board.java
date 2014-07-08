package a.y.chs;
import b.a;
import b.xwriter;
public class board extends a{
	private static final long serialVersionUID=1L;
	private static final int wihi=8;
	private final square[][]squares=new square[wihi][wihi];
	private boolean flipped;
	public board(){
		int k=0;
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<wihi;i++){
			for(int j=0;j<wihi;j++){
				sb.append((char)('a'+j));
				sb.append((char)('1'+i));
				String nm=sb.toString();
				sb.setLength(0);
				squares[i][j]=new square(this,nm,k%2);
				k++;
			}
			k++;
		}
	}
	public a chldq(final String id){return squares[id.charAt(1)-'1'][id.charAt(0)-'a'];}
	public void clearx(){
		for(final square[]r:squares)
			for(final square f:r)
				f.clearx();
	}
	public square square(final int rank,final int file){return squares[rank][file];}
	public void flip(){flipped=!flipped;}
	public void to(final xwriter x)throws Throwable{
		x.style().nl();
		x.p("body{text-align:center}");
		x.p("table.chs{margin-left:auto; margin-right:auto;background-color:#d0e4fe;border-collapse:collapse;text-align:center;}").nl();;
		x.p("table.chs td{vertical-align:middle;border:1px solid gray;width:47px;height:47px;}").nl();;
		x.p("table.chs td.blk{background-color:#808060;}").nl();;
		x.p("table.chs td.wht{background-color:#f8f8f8;}").nl();;
		x.p("table.chs .hilite{border:1px solid red;}").nl();;
		x.p("table.chs img{border:0px}").nl();
		x.style_().nl();;
		x.table("chs").nl();
		if(!flipped){
			for(int i=(wihi-1);i>=0;i--){
				x.tr();
				for(int j=0;j<wihi;j++){
					squares[i][j].to(x);
				}
				x.tr_().nl();
			}
		}else{
			for(int i=0;i<wihi;i++){
				x.tr();
				for(int j=(wihi-1);j>=0;j--){
					squares[i][j].to(x);
				}
				x.tr_().nl();
			}
		}
		x.table_();
	}
}
