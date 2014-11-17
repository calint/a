package a.civ;
import b.*;
public class map extends a{
//	   /..\   
//	/..\__/..\
//	\__/..\__/
//	/..\__/..\
//	\__/..\__/
//     \__/   
	public void to(xwriter x)throws Throwable{
		final boolean rend_col_letters=true;
		x.p("  ");
		for(int j=0;j<wi;j+=2){
			x.p(" __ ").p("  ");
		}
		x.pl(" ");
		for(int i=0;i<hi;i++){
			x.p(""+(char)(i+'1')+" ");
			for(int j=0;j<(wi-1);j+=2){
				x.p("/").p(tile_str(i,j)).p("\\__");
			}
			x.pl(i==0?" ":"/");
			x.p("  ");
			for(int j=0;j<wi;j+=2){
				x.p("\\__/").p(tile_str(i,j+1));
			}
			x.pl("\\");
		}
		x.p("  ");
		for(int j=0;j<(wi-1);j+=2){
			x.p(j==0?" ":"/").p("  ").p("\\__");
		}
		x.pl("/");
		
		if(rend_col_letters){
			x.p("  ");
			for(int j=0;j<wi;j++){
				x.p(" "+(char)(j+'a')+" ");
			}
			x.nl();
		}
	}
	private String tile_str(int i,int j){
		final tile t=data[i][j];
		if(t==null)return"  ";
		return t.get_map_abbrv();
	}
	public void clear(){
		for(int r=0;r<data.length;r++){
			for(int c=0;c<data[r].length;c++){
				data[r][c]=null;
			}
		}
	}
	public void put_unit(final String tileid,final unit u){
		final int col=tileid.charAt(0)-'a';
		final int row=tileid.charAt(1)-'1';
		tile t=data[row][col];
		if(t==null){
			t=new tile(this,tileid);
			data[row][col]=t;
		}
		t.u.add(u);
	}
	public tile get_tile(String id){
		final int col=id.charAt(0)-'a';
		final int row=id.charAt(1)-'1';
		return data[row][col];
	}
	public unit take(String tile){
		final int col=tile.charAt(0)-'a';
		final int row=tile.charAt(1)-'1';
		final tile t=data[row][col];
		if(t==null)throw new Error();
		final unit u=t.u.get_first();
		t.u.clear();
		return u;
	}
	private final int hi=8,wi=10;
	private tile[][]data=new tile[hi][wi];
	private static final long serialVersionUID=1;
}