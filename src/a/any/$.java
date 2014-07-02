package a.any;

import a.c;
import a.amazon.ec2.any_root;
import a.any.b.any_sessions;
import b.a;
import b.a_ajaxsts;
import b.b;
import b.req;
import b.xwriter;

public class $ extends a{
	public a_ajaxsts ajaxsts;{ajaxsts.set("idle");}//application status
	public list ls;
	public $(){
		final any_menu l=new any_menu(null,"any");
		ls.root_and_path(l,l);
//		path=root=l;
		l.add(new any_path(l,b.path(),"files"));
	//	l.add(new elclass(root,list.class,"any"));
		l.add(new any_class(l,b.class,"server"));
		l.add(new any_class(l,req.class,"request"));
		l.add(new any_class(l,c.class,"cincos"));
		l.add(new any_sessions(l,"users"));
		l.add(new any_root(l,"amazon web"));
	}
	
	@Override public void to(xwriter x) throws Throwable{
		x.style(ajaxsts,"position:absolute;left:1em;top:4em;text-align:right");
		ajaxsts.to(x);
		ls.to(x);
	}

static final long serialVersionUID=1;}
