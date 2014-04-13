package a.medusa;

import a.medusa.medusa.reads;
import a.medusa.algebra.matrix;
import a.medusa.algebra.point;

final public class lazy_chain_matrix{
	lazy_chain_matrix parent;
	lazy_matrix local;
	
	final@reads matrix refresh_and_get(final int tick,final@reads point scale,final@reads point angle,final@reads point position){
		// if parent is null
		// if local matrix is refreshed
		// if parent matrix was refreshed
		if(parent.tick_refresh==parent_tick_refresh)return m;// matrix still valid
		// refresh matrix using parent and local matrix
		parent_tick_refresh=parent.tick_refresh;
		return m;
	}
	final public void set_parent(final lazy_chain_matrix m){
		parent=m;
		parent_tick_refresh=0;//? rollover issue
	}
	private int tick_refresh;//? rollover issues
	private int parent_tick_refresh;
	final private matrix m=new matrix();
}
