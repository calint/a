package a.pz;
import b.a;
import b.xwriter;
final public class rom extends a{
	public void to(final xwriter x){
		x.div(this,"float panel");
		x.p("   znxr ci.. aaaa dddd ").ax(this,"clr","x").p("   ").nl();
		x.ul();
		int row=0;
		final String id=id();
		for(final int d:bits){
			x.li();
			x.p(zn.fld("00",Integer.toHexString(row)));
			x.tag("span",id+"_"+row+"_s").spc().tage("span");
			for(int k=0,bit=1;k<16;bit<<=1){
				x.p("<a href=\"javascript:$x('").p(id).p("  ").p(row).p(" ").p(k).p("')\" id=").p(id).p("_").p(row).p("$").p(k).p(">");
				if((d&bit)==bit)
					x.p("o");
				else
					x.p(".");
				x.p("</a>");
				if(++k%4==0)
					x.spc();
			}
			final String wid=id();
			final int rowint=get(row);
			final String rowinthex=Integer.toHexString(rowint);
			x.tago("span").attr("id",wid+"_"+row).tagoe().p(zn.fld("0000",rowinthex)).tage("span").nl();
			row++;
			if(row>=disppagenrows)
				break;
		}
		x.ul_();
		x.div_();
	}
	int focusline=-1;
	private int lstfocusline=focusline;
	void xfocusline(xwriter x){
		if(lstfocusline!=-1){
			final String js="var e=$('"+id()+"').getElementsByTagName('li')["+lstfocusline+"];e.className=e._oldcls;";
			x.pl(js);
		}
		if(focusline!=-1){
			lstfocusline=focusline;
			final String js="var e=$('"+id()+"').getElementsByTagName('li')["+focusline+"];e._oldcls=e.className;e.className='stp';";
			x.pl(js);
		}
	}
	public void x_clr(xwriter x,String s)throws Throwable{
		for(int i=0;i<bits.length;i++){
			bits[i]=0;
		}
		x.xuo(this);
	}
	public void x_(xwriter x,String s){
		final String[]a=s.split(" ");
		final int row=Integer.parseInt(a[0]);
		final int bit=Integer.parseInt(a[1]);
		final int msk=1<<bit;
		int v=bits[row];
		final boolean on=(v&msk)==msk;
		if(on)
			v=v&~msk;
		else
			v|=msk;
		bits[row]=v;
		x.xu(id()+"_"+row+"$"+bit,on?".":"o");
		x.xu(id()+"_"+row,zn.fld("0000",Integer.toHexString(bits[row])));
	}
	public int get(final int row){return bits[row];}
	public void set(final int row,final int value){bits[row]=value;}
	public void rst(){
		lstfocusline=-1;
		for(int i=0;i<bits.length;i++)bits[i]=0;
	}
	
	
//	final public static int size=1024*8;
	private int disppagenrows=128;
	public int[]bits;
	private static final long serialVersionUID=1;
}
