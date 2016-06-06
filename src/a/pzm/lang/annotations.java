package a.pzm.lang;

import java.util.List;

import a.pzm.lang.reader.token;
import b.a;
import b.xwriter;

final public class annotations extends a{
	public annotations(statement stmt,reader r){
		ws_pre=r.next_empty_space();
		ls=r.read_annotation();
	}
	@Override public void to(xwriter x){
		x.p(ws_pre);
		if(ls!=null)for(token tk:ls){
			x.p(tk.ws_pre).p('@').p(tk.name).p(tk.ws_post);
//			tk.to(x);
		}
	}
	private final String ws_pre;
	private final List<token>ls;
	private static final long serialVersionUID=1;
	public boolean has_annotation(String tag){
		if(ls==null)return false;
		for(token tk:ls){
			if(tk.name.equals(tag))return true;
		}
		return false;
	}
	public int count(){if(ls==null)return 0;return ls.size();}
	public token get(int index){if(ls==null)return null;return ls.get(index);}
	public List<token>list(){return ls;}
}