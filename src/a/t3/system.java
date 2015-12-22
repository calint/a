package a.t3;
import b.a;
import b.xwriter;
public class system extends a{
	static final long serialVersionUID=1;
	public static int drive_size_megs=1024;
	public drive d[]=new drive[16];{for(int i=0;i<d.length;i++)d[i]=new drive(this,i+"");}
	@Override protected a chldq(String nm){
		if(nm.startsWith("d")){
			return d[Integer.parseInt(nm.substring(1))];
		}
		return super.chldq(nm);
	}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null)x.title("one peta byte file ― system");
		x.p("drive array, ").p(d.length).p(" drives, ").p(drive_size_megs*d.length>>10).p(" terra bytes").nl();
		for(drive dr:d){
			x.r(dr).nl();
		}
		x.pl("[o] distribute for search on all drives");
		x.pl("[ ] distribute using index             ");
	}
}
