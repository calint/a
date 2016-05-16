package a.stocks;

import java.util.LinkedHashMap;

import b.a;

public final class trader extends a{
	public LinkedHashMap<String,stock>stocks;
	public LinkedHashMap<String,money>accounts;
	public trader(){set("trader");}
	public void buy_stock(String stock,int amt)throws Throwable{}
	public void sell_stock(String stock,int amt)throws Throwable{}
	public money get_balance(){return null;}
	private static final long serialVersionUID=1;
}
