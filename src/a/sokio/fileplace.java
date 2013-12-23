package a.sokio;
import java.util.*;
import b.*;
public class fileplace implements place{
	static final long serialVersionUID=1;
	final private path p;
	public fileplace(final path p){this.p=p;}
	public String toString(){return p.name();}
	public String description(){try{return p.readstr();}catch(final Throwable t){throw new Error(t);}}

	private List<place>places(){
		final List<place>dir=new LinkedList<place>();
		for(final String s:p.list()){
			final path pp=p.get(s);
			dir.add(new fileplace(pp));
		}
		return dir;
	}
	public void places_foreach(final place.visitor v)throws Throwable{
		for(final place p:places())
			if(!v.visit(p))break;
	}
	public boolean things_isempty(){return true;}
	public int things_size(){return 0;}
	public void things_foreach(final place.visitor v)throws Throwable{}
	public thing things_get(final String qry){return null;}
	public void things_add(final thing o){}
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
			if(pp.name().startsWith(qry))return new fileplace(pp);
		}
		return null;
	}
	public void places_add(final place o){
		final path f=p.get(o.toString());
		if(f.exists())return;
		final String txt=o.description();
		if(txt==null||txt.length()==0){
			try{f.mkfile();}catch(Throwable t){throw new Error(t);}
			return;
		}
		try{f.append(txt);}catch(Throwable t){throw new Error(t);}
	}
	public place places_enter(final $ so,final String qry){
		place dest=places_get(qry);
		if(dest==null)dest=things_get(qry);
		if(dest==null)return null;
		so.place().sokios_remove(so);
		so.place().sokios_recv(this+" departed to "+dest,so);
		dest.sokios_recv(this+" arrived from "+so.place(),so);
		dest.sokios_add(so);
		so.path_push(dest);
		return dest;
	}
}