package a.bureaucrat;

import java.util.LinkedHashMap;
import java.util.Map;

import b.a;
import b.xwriter;

public class archive extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
	x.pl("archive");
	for(final Map.Entry<String,form>e:forms.entrySet()){
		x.ax(this,e.getKey());
	}
}
	private static Map<String,form>forms=new LinkedHashMap<String,form>();
}