package d3;
import java.io.Serializable;
public final class p3 implements Cloneable,Serializable{
	public final static p3 axisx=new p3(1,0,0);
	public final static p3 axisy=new p3(0,1,0);
	public final static p3 axisz=new p3(0,0,1);
	private static final long serialVersionUID=1L;
	public double x;
	public double y;
	public double z;
	public p3(){}
	public p3(final double s){
		x=y=z=s;
	}
	public p3(final double x0,final double y0,final double z0){
		x=x0;
		y=y0;
		z=z0;
	}
	public p3(final/*const*/p3 p){
		x=p.x;
		y=p.y;
		z=p.z;
	}
	public p3(final/*const*/p3 p1,final/*const*/p3 p2){
		x=p2.x-p1.x;
		y=p2.y-p1.y;
		z=p2.z-p1.z;
	}
	public p3 abs(){
		if(x<0)
			x=-x;
		if(y<0)
			y=-y;
		if(z<0)
			z=-z;
		return this;
	}
	public void add(final double s){
		x+=s;
		y+=s;
		z+=s;
	}
	public p3 add(final/*const*/p3 p){
		x+=p.x;
		y+=p.y;
		z+=p.z;
		return this;
	}
	public p3 add(/*const*/final p3 p,final double dt){
		x+=p.x*dt;
		y+=p.y*dt;
		z+=p.z*dt;
		return this;
	}
	public p3 clone()/*const*/{
		return new p3(x,y,z);
	}
	public/*const*/double distance2_to(final/*const*/p3 p){
		double dx=p.x-x;
		double dy=p.y-y;
		double dz=p.z-z;
		return dx*dx+dy*dy+dz*dz;
	}
	public double dotprod(final p3 p)/*const*/{
		return p.x*x+p.y*y+p.z*z;
	}
	public boolean equals(/*const*/final p3 p)/*const*/{
		return (p.x==x)&&(p.y==y)&&(p.z==z);
	}
	public double magnitude()/*const*/{
		return Math.sqrt(x*x+y*y+z*z);
	}
	public double magnitude2()/*const*/{
		return x*x+y*y+z*z;
	}
	public p3 negate(){
		x=-x;
		y=-y;
		z=-z;
		return this;
	}
	public p3 norm(){
		return normalize(1.0);
	}
	public p3 normalize(final double length){
		double q=Math.sqrt(x*x+y*y+z*z);
		if(q==0){
			zero();
			return this;
		}
		double t=length/q;
		x=t*x;
		y=t*y;
		z=t*z;
		return this;
	}
	public p3 rot_axis_x(final double a){
		double ca=Math.cos(a);
		double sa=Math.sin(a);
		double ny=ca*y-sa*z;
		double nz=sa*y+ca*z;
		y=ny;
		z=nz;
		return this;
	}
	public p3 rot_axis_y(final double a){
		double ca=Math.cos(a);
		double sa=Math.sin(a);
		double nx=ca*x+sa*z;
		double nz=-sa*x+ca*z;
		x=nx;
		z=nz;
		return this;
	}
	public p3 rot_axis_z(final double a){
		double ca=Math.cos(a);
		double sa=Math.sin(a);
		double nx=ca*x-sa*y;
		double ny=sa*x+ca*y;
		x=nx;
		y=ny;
		return this;
	}
	public p3 scale(final double s){
		x*=s;
		y*=s;
		z*=s;
		return this;
	}
	public p3 set(final double x0,final double y0,final double z0){
		x=x0;
		y=y0;
		z=z0;
		return this;
	}
	public p3 set(final p3 p){
		x=p.x;
		y=p.y;
		z=p.z;
		return this;
	}
	public String toString()/*const*/{
		return new String("|"+x+" "+y+" "+z+"|");
	}
	public p3 vecprod(final p3 v1,final p3 v2){
		x=v1.y*v2.z-v1.z*v2.y;
		y=v1.z*v2.x-v1.x*v2.z;
		z=v1.x*v2.y-v1.y*v2.x;
		return this;
	}
	public final p3 vecto(/*const*/final p3 p){
		x=p.x-x;
		y=p.y-y;
		z=p.z-z;
		return this;
	}
	public p3 x(final double x0){
		x=x0;
		return this;
	}
	public p3 y(final double y0){
		y=y0;
		return this;
	}
	public p3 z(final double z0){
		z=z0;
		return this;
	}
	public p3 zero(){
		x=y=z=0;
		return this;
	}
	public p3 scale(final p3 p){
		x*=p.x;
		y*=p.y;
		z*=p.z;
		return this;
	}
}
