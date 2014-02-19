package a.pczero;
import b.a;
import b.xwriter;
final public class ram extends a{
	static final long serialVersionUID=1;
	public final static int width=256;
	public final static int height=128;	
	final int scl=2;
	public final static int size=width*height;
	private short[]ram=new short[size];
	public ram(){rst();}
	static String labelrefresh="*";
	public void rst(){x=null;for(int i=0;i<ram.length;i++)ram[i]=0;}
	public void to(final xwriter x)throws Throwable{
		x.p("<canvas id=").p(id()).p(" width=").p(width*scl).p(" height=").p(height*scl).p("></canvas>");
	}
	public void x_rfh(final xwriter x,final String s){
		final String id=id();
		x.p("var d2=$('").p(id).p("').getContext('2d');");
		int cell=0;
		final int yw=height;//? size>>12;
		final int xw=width;
		for(int y=0;y<yw;y++){
			for(int xx=0;xx<xw;xx++){
				final short argb=ram[cell++];
				final String hex=Integer.toHexString(argb);
				x.p("d2.fillStyle='#"+vintage.fld("000",hex)+"';");
				x.pl("d2.fillRect("+xx*scl+","+y*scl+","+scl+","+scl+");");
//				x.pl("d2r(rgb,x,y,w,h)");
			}
//			x.flush();
		}
//		x.p("}");
	}
	public short get(final int addr){
		final int a;
//		if(addr>=ram.length){
//			a=addr%ram.length;
//		}else
			a=addr;
		return ram[a];
	}
	xwriter x;// if set updates to ram display are written as js
	public void set(final int addr,final int value){
		final int a;
//		if(addr>=ram.length){
//			a=addr%ram.length;
//		}else
			a=addr;
		ram[a]=(short)value;
		if(x==null)return;
		final short argb=(short)value;
		final String hex=Integer.toHexString(argb);
		final String id=id();
		x.p("{var d2=$('").p(id).p("').getContext('2d');");
		x.p("d2.fillStyle='#"+vintage.fld("000",hex)+"';");
		final int yy=a/width;
		final int xx=a%width;
		final int scl=2;
		x.p("d2.fillRect("+xx*scl+","+yy*scl+","+scl+","+scl+");");				
		x.pl("}");
	}
}
