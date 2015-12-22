package a.t3;
import b.a;
import b.xwriter;
public class system extends a{
	static final long serialVersionUID=1;
	public static int drive_size_megs=1024;
	public drive d[]=new drive[4];{for(int i=0;i<d.length;i++)d[i]=new drive(this,"~"+i);}
	@Override protected a chldq(String nm){
		if(!nm.startsWith("~"))
			return null;
		return d[Integer.parseInt(nm.substring("~".length()))];
	}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null)x.title("system");
		x.p("drive array, ").p(d.length).p(" drives, ").p(drive_size_megs*d.length>>10).p(" gig").nl();
		for(drive dr:d){
			x.r(dr).nl();
		}
	}
	public synchronized void x_index_lock(xwriter x,String s)throws Throwable{}
	public synchronized void x_index_clear(xwriter x,String s)throws Throwable{}
	public synchronized void x_index_add_word(xwriter x,String s)throws Throwable{}
	public synchronized void x_index_unlock(xwriter x,String s)throws Throwable{}
	public synchronized void x_index_search(xwriter x,String s)throws Throwable{}
}
