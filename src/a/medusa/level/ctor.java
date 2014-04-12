package a.medusa.level;

import a.medusa.medusa;
import a.medusa.player;
import a.medusa.sprite;
import a.medusa.medusa.plugin;

public class ctor implements plugin{
	final static glo_circle_xy circ=new glo_circle_xy();
	final static sprite spi=new sprite();
	@Override public void init(final medusa m)throws Throwable{
		circ.load();
		spi.load();

		player p;
		p=new player();
		p.glo(circ).xy(20,20).da(medusa.dtor(10));
		m.players_add(p);
		
		p=new player();
		p.glo(spi).xy(29,2);
		m.players_add(p);
		
		p=new player();
		p.glo(spi).xy(30,8);
		m.players_add(p);
		
		p=new player();
		p.glo(spi).xy(70,30);
		m.players_add(p);

		p=new player();
		p.glo(spi).xy(53,34);
		m.players_add(p);
	}
}
