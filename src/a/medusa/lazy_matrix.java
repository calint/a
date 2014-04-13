package a.medusa;

import a.medusa.medusa.reads;
import a.medusa.algebra.matrix;
import a.medusa.algebra.point;

final public class lazy_matrix{
	final@reads matrix refresh_and_get(final int tick,final@reads point scale,final@reads point angle,final@reads point position){
		if(scale.eq(scl)&&agl.eq(angle)&&pos.eq(position))
			return m;
//		Matrix.setIdentityM(mtx_model_world,0);
//		Matrix.translateM(mtx_model_world,0,x,y,z);
//		Matrix.rotateM(mtx_model_world,0,ax,1,0,0);
//		Matrix.rotateM(mtx_model_world,0,ay,0,1,0);
//		Matrix.rotateM(mtx_model_world,0,az,0,0,1);
//		Matrix.scaleM(mtx_model_world,0,sx,sy,sz);
		updated_at_tick=tick;
		return m;
	}

	private int updated_at_tick;
	final private matrix m=new matrix();
	final private point scl=new point();
	final private point agl=new point();
	final private point pos=new point();
}
