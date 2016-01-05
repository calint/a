package a.pzm.bas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import b.xwriter;

final public class data extends stmt{
	private List<String> data;
	public data(program r) throws IOException{
		super(r);
		data=new ArrayList<>();
		while(true){
			final String t=r.next_token_in_line();
			if(t==null)
				break;
			data.add(t);
		}
		final xwriter x=new xwriter().p(". ");
		data.forEach(e->x.spc().p(e));
		txt=x.toString();
	}
	@Override public void compile(program p){
		bin=new int[data.size()];
		int i=0;
		for(final String s:data){
			bin[i++]=Integer.parseInt(s,16);
		}
	}
	private static final long serialVersionUID=1;
}