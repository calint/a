package a.pz;
import b.a;
import b.xwriter;
final public class rom extends a{
	static final long serialVersionUID=1;
	public final static int size=1024*8;
	private int disppagenrows=128;
	private int[]rom=new int[size];
	public void to(final xwriter x){
		x.div(this,"border float");
		x.style(this,"ul li.stp","background-color:#c88");
		x.p("   znxr ci.. aaaa dddd ").ax(this,"clr","x").p("   ").nl();
		x.ul();
//		x.style("el.rom el","background:#eea");
		int row=0;
		final String id=id();
		for(final int d:rom){
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
	String libgstp="yellow";
	public void x_clr(xwriter x,String s)throws Throwable{
		for(int i=0;i<rom.length;i++){
			rom[i]=0;
		}
		x.xuo(this);
	}
	public void x_(xwriter x,String s){
		final String[]a=s.split(" ");
		final int row=Integer.parseInt(a[0]);
		final int bit=Integer.parseInt(a[1]);
		final int msk=1<<bit;
		int v=rom[row];
		final boolean on=(v&msk)==msk;
		if(on)
			v=v&~msk;
		else
			v|=msk;
		rom[row]=v;
		x.xu(id()+"_"+row+"$"+bit,on?".":"o");
		x.xu(id()+"_"+row,zn.fld("0000",Integer.toHexString(rom[row])));
	}
	public int get(final int row){return rom[row];}
	public void set(final int row,final int value){rom[row]=value;}
//	public void save(final xwriter x)throws Throwable{
//		for(int k=0;k<rom.length;k++)
//			x.p(x1.fld("0000",Integer.toHexString(get(k)))).spc();
//	}
//	public void load(final path p)throws Throwable{
//		final Scanner sc=new Scanner(p.inputstream());
//		for(int k=0;k<rom.length&&sc.hasNextInt();k++){
//			rom[k]=sc.nextInt();
//		}
//		sc.close();
//	}
	public void rst(){
		lstfocusline=-1;
		for(int i=0;i<rom.length;i++)rom[i]=0;
	}
}
