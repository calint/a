package a.pzm;

import b.a;
import b.xwriter;

final public class line_numbers extends a{
	public int focus_line=0;
	/**selected_line_no  0 is none*/public a sln;
	@Override public void to(xwriter x) throws Throwable{
		x.ul();
		for(int i=1;i<129;i++){
			if(i==focus_line){
				x.divo("","color:#800;font-weight:bold;background:yellow").p(Integer.toString(i)).div_();
			}else{
				x.li().p(i);
			}
		}
		x.ul_();
	}
	void xj_select_line(xwriter x,int line_no){
		final int selected_line_no=sln.toint();
		final String id=id();
		if(selected_line_no>0){//unselect
			x.p("var v=$('").p(id).p("').getElementsByTagName('li')[").p(selected_line_no-1).p("];if(v){v.className=v._classNameOld}").nl();
		}
		final int new_selected_line_no=line_no;
		sln.set(new_selected_line_no);
		if(new_selected_line_no>0){//select
			x.p("var v=$('").p(id).p("').getElementsByTagName('li')[").p(new_selected_line_no-1).p("];if(v){v._classNameOld=v.className;v.className='a'}").nl();
		}
	}

	private static final long serialVersionUID=1;
}