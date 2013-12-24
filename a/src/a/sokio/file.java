package a.sokio;
import java.util.*;

import b.*;
final class file implements place{
	static final long serialVersionUID=1;
	final private path p;
	public file(final path p){this.p=p;}
	public String toString(){return p.name();}
	public String name(){return p.name();}
	public void name(final String s){p.rename(p.parent().get(s));}
	public String description(){try{return p.readstr();}catch(final Throwable t){throw new Error(t);}}
	final public void description(final String s){try{p.writestr(s);}catch(final Throwable t){throw new Error(t);}}
	private List<place>places(){
		final List<place>dir=new LinkedList<place>();
		for(final String s:p.list()){
			final path pp=p.get(s);
			dir.add(new file(pp));
		}
		return dir;
	}
	public void places_foreach(final place.placevisitor v)throws Throwable{for(final place p:places())if(!v.visit(p))break;}
	public boolean things_isempty(){return true;}
	public int things_size(){return 0;}
	public void things_foreach(final place.thingvisitor v)throws Throwable{}
	public thing things_get(final String qry){return null;}
	public void things_add(final thing o){
		if(!p.isdir())throw new Error(this+" is not a container");
		final path f=p.get(o.toString());
		final String d=o.description();
		try{
			if(d!=null)f.writestr(d);else f.mkfile();
		}catch(final Throwable t){throw new Error(t);}
	}
	public void things_remove(final thing o){}

	public int sokios_size(){return 0;}
	public void sokios_foreach(place.sokiovisitor v)throws Throwable{}
	public void sokios_add(final $ s){}
	public void sokios_remove(final $ s){}
	public void sokios_recv(final String msg,final $ exclude){}

	public boolean places_isempty(){return p.list().length==0;}
	public place places_get(final String qry){
		for(final String s:p.list()){
			final path pp=p.get(s);
			if(pp.name().startsWith(qry))return new file(pp);
		}
		return null;
	}
	public void places_add(final place o){
		final path f=p.get(o.toString());
		try{f.mkdirs();}catch(final Throwable t){throw new Error(t);}
	}
	public place places_enter(final $ so,final String qry){
		place dest=places_get(qry);
		if(dest==null)dest=things_get(qry);
		if(dest==null)return null;
		so.moveto(dest);
		return dest;
	}
}