package a.any;

import b.b;
import b.req;

public class $ extends list{{

	final elroot l=new elroot(null,"any");
	path=root=l;
	l.add(new elpath(root,b.path(),"files"));
//	l.add(new elclass(root,list.class,"any"));
	l.add(new elclass(root,b.class,"server"));
	l.add(new elclass(root,req.class,"request"));
	l.add(new a.amazon.ec2.any_root(root,"amazon web"));
	
}static final long serialVersionUID=1;}
