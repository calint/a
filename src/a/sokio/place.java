package a.sokio;
import java.io.*;
public interface place extends Serializable{
	String description();
	void description(final String s);//optional
	
	place places_get(final String qry);
	void places_add(final place o);
	void places_foreach(final place.visitor v)throws Throwable;
	place places_enter(final $ so,final String qry);
	
	boolean things_isempty();
	int things_size();
	anything things_get(final String qry);
	void things_add(final anything o);
	void things_remove(final anything o);
	void things_foreach(final place.visitor v)throws Throwable;
	
	int sokios_size();
	void sokios_add(final $ s);
	void sokios_remove(final $ s);
	void sokios_recv(final String msg,final $ exclude);	
	void sokios_foreach(final place.sokiovisitor v)throws Throwable;

	static public interface visitor{boolean visit(final place p)throws Throwable;}
	static public interface sokiovisitor{boolean visit(final $ o)throws Throwable;}
}