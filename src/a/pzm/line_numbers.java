package a.pzm;

import b.a;
import b.xwriter;

final public class line_numbers extends a{
	public int focus_line=0;
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
		x.script();
		x.p("var v=$('"+id()+"').getElementsByTagName('li')[1];if(v){v._classNameOld=v.className;v.className='a'}");
//		x.p("var v=$('"+id()+"').getElementsByTagName('li')[1];if(c){v.className=v._classNameOld}");
		x.script_();
	}

	private static final long serialVersionUID=1;
}