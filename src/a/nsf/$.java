package a.nsf;

import static b.b.K;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import b.a;
import b.xwriter;

public class $ extends a{
	private final ByteBuffer bb=ByteBuffer.allocate(64*K);
	private final static class bbreader{
		private ByteBuffer bb;
		bbreader(final ByteBuffer bb){this.bb=bb;}
		void to(final xwriter x)throws Throwable{
			x.pl(bb.toString());
		}
	}
	private final static class bbwriter{
		private final xwriter x;
		private LinkedList<String>stk_ns=new LinkedList<>();
		bbwriter(final xwriter x){this.x=x;}
		bbwriter ns(final String name)throws Throwable{
			x.p(name).p('{');
			stk_ns.addFirst(name);
			return this;
		}
		bbwriter ct(final String content)throws Throwable{
			x.p(content);
			stk_ns.removeFirst();
			x.p('}');
			return this;
		}
		bbwriter ct(final ByteBuffer content)throws Throwable{
//			x.p("");
			stk_ns.removeFirst();
			x.p('}');
			return this;
		}
		bbwriter nsc(){
			stk_ns.removeFirst();
			x.p('}');
			return this;
		}
		bbwriter done(){
			for(int i=0,n=stk_ns.size();i<n;i++){
				x.p('}');
			}
			return this;
		}
	}
	@Override public void to(final xwriter x)throws Throwable{
		final bbwriter bbw=new bbwriter(x);
		x.pl("name{space{file 1{content}file 2{content}}}");
		bbw.ns("system").ns("directory").ns("files").ns("1").ct("content1").ns("2").ct("content2").done();
//		bbw.nsc();
//		bbw.nsc();
	}
}
