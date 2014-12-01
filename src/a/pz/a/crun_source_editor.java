package a.pz.a;
import static b.b.log;
import java.util.HashSet;
import java.util.Set;
import a.pz.bas.compiler_error;
import a.pz.bas.program;
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
	public a sts;
	public static class line_numbers extends a{
		public int focus_line=0;
		@Override public void to(xwriter x) throws Throwable{
			for(int i=1;i<100;i++)
				if(i==focus_line)
					x.divo("","color:#800;font-weight:bold;background:yellow").p(Integer.toString(i)).div_();
				else
					x.pl(Integer.toString(i));
		}
		
		private static final long serialVersionUID=1;
	}
	public line_numbers ln;
	public void to(final xwriter x) throws Throwable{
		x.spanh(sts,"","width:5em;color:#800;font-weight:bold").ax(this,"f3","","crun ","a").nl();
		x.table().tr().td("","text-align:right;padding-right:.5em");
		x.el(ln);
		ln.to(x);
		x.el_();
		x.td();
		x.inptxtarea(src);
		x.table_();
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
		final program p;
		try{
			p=new program(src.str());
			p.build();
			sts.set(p.program_length+" ");
			final int prev_focus_line=ln.focus_line;
			ln.focus_line=0;
			if(x!=null){
				if(prev_focus_line!=ln.focus_line)
					x.xu(ln);
				x.xu(sts);
			}
		}catch(Throwable t){
			log(t);
			sts.set(t.toString()+"\n");
			if(t instanceof compiler_error)
				ln.focus_line=((compiler_error)t).source_location_line();
			if(x!=null)x.xu(ln).xu(sts);
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
