package a.medusa.level;

import a.medusa.glo_circle_xy;
import a.medusa.medusa;
import a.medusa.player;
import a.medusa.sprite;
import a.medusa.medusa.medusa_ctor;

public class ctor implements medusa_ctor {
	@Override public void init(final medusa m)throws Throwable{
		final sprite spi=new sprite();
		spi.load();
		
		player sp=new player();
		sp.glo(spi).xy(29,2);
		m.players_all.add(sp);
		
		sp=new player();
		sp.glo(spi).xy(30,8);
		m.players_all.add(sp);
		
		sp=new player();
		sp.glo(spi).xy(70,30);
		m.players_all.add(sp);

		sp=new player();
		sp.glo(spi).xy(53,34);
		m.players_all.add(sp);

		sp=new player();
		final glo_circle_xy circ=new glo_circle_xy();
		circ.load();
		sp.glo(circ).xy(20,20).da(medusa.dtor(10));
		m.players_all.add(sp);
	}
}
