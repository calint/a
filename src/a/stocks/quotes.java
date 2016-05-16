package a.stocks;

import java.util.LinkedHashMap;

import b.a;

public final class quotes extends a{
	public quotes(){set("quotes");}
	public /*readonly*/LinkedHashMap<String,quotes>get(){return null;}
	public interface ifc{void quotes_onchange(stock s,money from,money to)throws Throwable;}
	public ifc qi;
	private static final long serialVersionUID=1;
}
