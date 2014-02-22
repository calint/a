package a.y.j;
import b.xwriter;
public class javawriter{
	public javawriter pckg(final String packagename){
		x.p("package ").p(b.b.webobjpkg).p(packagename).p(";").nl();
		state=1;
		return this;
	}
	public javawriter uses(final String...packagenames){
//		if(state!=1)throw new IllegalStateException();
		for(final String pn:packagenames)
			x.p("import ").p(pn).p(".*;").nl();
		return this;
	}
	public javawriter defpckcls(){
		return clas(b.b.default_package_class);
	}
	public javawriter clas(final String nm){
		x.p("public class ").p(nm).p(" extends a{").nl();
		tabindex++;
		tab();x.p("static final long serialVersionUID=1;").nl();
		state=2;
		return this;
	}
	public javawriter has(final String...objects){
//		if(st!=1)throw new IllegalStateException("not in class");
		for(final String nm:objects){
			tab();x.p("public ").p(nm).spc().p(nm).p(";").nl();
		}
		return this;
	}
	public javawriter rend(){
//		if(st!=1)throw new IllegalStateException("not in class");
		tab();x.p("public void to(final xwriter x)throws Throwable{").nl();
		tabindex++;
		state=3;
		return this;
	}
	public javawriter rendend(){
//		if(st!=3)throw new IllegalStateException("not in function");
		tabindex--;
		tab();x.p("}").nl();
		state=2;
		return this;
	}
	public javawriter xfunc(final String nm){
//		if(st!=1)throw new IllegalStateException("not in class");
		tab();x.p("public void x_").p(nm).p("(final xwriter x,final String q)throws Throwable{").nl();
		tabindex++;
		state=3;
		return this;
	}
	public javawriter xfuncend(){
//		if(st!=3)throw new IllegalStateException("not in function");
		tabindex--;
		tab();x.p("}").nl();
		state=2;
		return this;
	}
	public javawriter clasend(){
//		if(st!=3)throw new IllegalStateException("not in function");
		tabindex--;
		tab();x.p("}").nl();
		state=1;
		return this;
	}
	
	public String toString(){return x.toString();}


	private xwriter x=new xwriter();
	private int state,tabindex;
	private void tab(){for(int i=0;i<tabindex;i++)x.tab();}
}
