package a.pzm;
import b.a;
import b.xwriter;
final public class sprite_editor extends a{
	public void to(final xwriter x){
		x.divo(this,"float");
		x.ul();
		int row=0;
		final String id=id();
		for(final int d:ints){
			x.li();
			x.p(ide.fld("00",Integer.toHexString(row)));
			x.tag("span",id+"-"+row+"-s").spc().tage("span");
			for(int k=0,bit=1<<20;k<20;bit>>=1){
				x.p("<a href=\"javascript:$x('").p(id).p("  ").p(row).p(" ").p(20-1-k).p("')\" id=").p(id).p("-").p(row).p("-").p(20-1-k).p(">");
				if((d&bit)==bit)
					x.p("o");
				else
					x.p(".");
				x.p("</a>").spc();
//				if(k==3||k==7||k==13)
//					x.spc();
				k++;
			}
			x.spc();
			final String wid=id();
			final int rowint=ints[row];
			final String rowinthex=Integer.toHexString(rowint);
			x.tago("span").attr("id",wid+"-"+row).tagoe().p(ide.fld("00000",rowinthex)).tage("span").nl();
			row++;
			if(row>=disppagenrows)
				break;
		}
		x.ul_();
		x.div_();
	}
	public void x_clr(xwriter x,String s)throws Throwable{
		for(int i=0;i<ints.length;i++)ints[i]=0;
		if(x==null)return;
		x.xu(this);
	}
	public void x_(xwriter x,String s){
		final String[]a=s.split(" ");
		final int row=Integer.parseInt(a[0]);
		final int bit=Integer.parseInt(a[1]);
		final int msk=1<<bit;
		int v=ints[row];
		final boolean on=(v&msk)==msk;
		if(on)v=v&~msk;
		else v|=msk;
		ints[row]=v;
		x.xu(id()+"-"+row+"-"+bit,on?".":"o");
		x.xu(id()+"-"+row,ide.fld("00000",Integer.toHexString(ints[row])));
	}
	public int disppagenrows=128;
	public int[]ints=new int[20];
	private static final long serialVersionUID=1;
}
