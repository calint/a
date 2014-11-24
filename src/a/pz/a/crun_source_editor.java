package a.pz.a;
import static b.b.log;
import java.util.HashSet;
import java.util.Set;
import a.pz.program;
import b.a;
import b.xwriter;
final public class crun_source_editor extends a{
	int focusline;
	//	private int lstfocusline=-1;
	//	public a brkpts;
	private Set<Integer> brkptsset=new HashSet<Integer>();
	//	void xfocusline(xwriter x){
	//		if(lstfocusline!=-1)x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lstfocusline-1)+"];e.className=e._oldcls;");
	//		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(focusline-1)+"];e._oldcls=e.className;e.className='stp';");
	//		lstfocusline=focusline;
	//	}
	public a src;
	//	public a sts;
	public void to(final xwriter x) throws Throwable{
		x.ax(this,"f3","crun ").nl().inptxtarea(src);
	}
	public boolean isonbrkpt(final int srclno){
		return brkptsset.contains(srclno);
	}
	//	synchronized public void x_brk(xwriter x,String s)throws Throwable{
	//		final int lno=Integer.parseInt(s);
	//		if(brkptsset.contains(lno)){
	//			brkptsset.remove(lno);
	//			brkpts.set(brkptsset.toString());
	//			x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e.className=e._oldcls;");
	//			return;
	//		}
	//		brkptsset.add(lno);
	//		brkpts.set(brkptsset.toString());
	//		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e._oldcls=e.className;if(!e._oldcls)e._oldcls='';e.className='brk';");
	//	}
	public final static boolean ommit_compiling_source_from_disassembler=false;
	synchronized public void x_f3(xwriter x,String s) throws Throwable{
		final program p=new program(src.str());
		try{
			p.build();
		}catch(Throwable t){
			log(t);
			if(x!=null)
				x.xalert(t.toString());
			return;
		}
		if(!ommit_compiling_source_from_disassembler){
			try{
				final program p2=new program(p.toString());
				p2.build();
				if(!p.is_binary_equal(p2))
					throw new Error("not binary equivalent");
			}catch(Throwable t){
				log(t);
				if(x!=null)
					x.xalert("reverse compilation failed: "+t.toString());
				return;
			}
		}
		//? compare-first-frame-ram-for-better-ok
		ev(x,this,p);
	}
	private static final long serialVersionUID=11;
}
