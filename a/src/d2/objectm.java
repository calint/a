package d2;
public abstract class objectm extends object{
	protected double dx;
	protected double dy;
	protected double dagl;
	private double velocity;
	private double angleOfVelocity;

	public objectm(world w,double x,double y,double agl,double dx,double dy,double dagl){
		super(w,x,y,agl);
		this.dx=dx;
		this.dy=dy;
		this.dagl=dagl;
	}

	public void update(double d){
		super.update(d);
		updatePhysics(d);
		updateTheOccupiedGrids();
	}

	protected void updatePhysics(double dt){
		x+=dx*dt;
		y+=dy*dt;
		agl+=dagl*dt;
	}

	final public double dx(){return dx;}
	final public double dy(){return dy;}
	final public double dangle(){return dagl;}
	final public double velocity(){return velocity;}

	final public void angleandvelocity(double a,double v){
		angleOfVelocity=a;
		velocity=v;
		recalculate_dxdy();
	}

	final public void dangle(double aov){
		angleOfVelocity=aov;
		recalculate_dxdy();
	}

	final public void angle(double d){agl=d;}
	final public void velocity(double d){
		velocity=d;
		recalculate_dxdy();
	}

	final private void recalculate_dxdy(){
		dx=velocity*Math.cos(angleOfVelocity);
		dy=velocity*Math.sin(angleOfVelocity);
	}
}
