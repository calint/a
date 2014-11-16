package a.civ;
import static b.b.*;
import b.*;

import java.lang.annotation.*;

final public class $ extends a{
	public game g=new game();
	public void to(final xwriter x)throws Throwable{
//		x.style().cssfont("tini","/ttf/monospace-typewriter.ttf").style_();
//		x.pl(g.toString());

//		x.pl("//		/.\\_/.");
//		x.pl("//		\\_/.\\_");
//		x.pl("//		/.\\_/.");
//		x.pl("//		\\_/.\\_");

//		Stream.of(g.getClass().getDeclaredMethods())
//			.filter(m->m.getDeclaredAnnotation(clickable.class)!=null)
//			.forEach(m->{x.p(m.getName());});
//		x.nl();
//		x.pl("- - - -- - -  -- -- -  -- - - - --  --  - -- -  - -- -- ---");
		
		x.style("html","text-align:center");
		x.ax(this).nl();
		x.output_holder(c);
//		x.style(c,"display:table;border:1px dotted black;margin-left:auto;margin-right:auto");
//		x.script();
//		x_(x,"");
//		x.script_();
		x.nl(2);
	}

	public synchronized void x_(xwriter x,String arg)throws Throwable{
		g.refresh_console(x.xub(c,true,false).outputstream());
		x.xube();	
	}

	public a c;//console
	public @Retention(RetentionPolicy.RUNTIME)@interface clickable{}
	static final long serialVersionUID=1;
}