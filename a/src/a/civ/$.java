package a.civ;
import static b.b.*;
import b.*;

import java.lang.annotation.*;
import java.util.stream.Stream;

final public class $ extends a{
	public game g=new game();
	public void to(final xwriter x)throws Throwable{
//		x.pl(g.toString());
		Stream.of(g.getClass().getDeclaredMethods())
			.filter(m->m.getDeclaredAnnotation(clickable.class)!=null)
			.forEach(m->{x.p(" * ").p(m.getName());});
		x.nl().pl("- - - -- - -  -- -- -  -- - - - --  --  - -- -  - -- -- ---");
		g.refresh_console(x.outputstream());
	}

	public @Retention(RetentionPolicy.RUNTIME)@interface clickable{}
	static final long serialVersionUID=1;
}