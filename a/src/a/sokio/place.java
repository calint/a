package a.sokio;
import java.io.*;
interface place extends Serializable{
	String name();
	void name(final String s);//optional
	String description();
	void description(final String s);//optional
	
	place places_get(final String qry);
	place places_new(final String nm);
	void places_add(final place p);
	void places_foreach(final placevisitor v)throws Throwable;
	place places_enter(final $ so,final String qry);
	
	boolean things_isempty();
	int things_size();
	thing things_get(final String qry);
	void things_add(final thing o);
	void things_remove(final thing o);
	void things_foreach(final thingvisitor v)throws Throwable;
	
	int sokios_size();
	void sokios_add(final $ s);
	void sokios_remove(final $ s);
	void sokios_recv(final String msg,final $ exclude);	
	void sokios_foreach(final sokiovisitor v)throws Throwable;

	static interface placevisitor{boolean visit(final place p)throws Throwable;}
	static interface thingvisitor{boolean visit(final thing o)throws Throwable;}
	static interface sokiovisitor{boolean visit(final $ o)throws Throwable;}
}