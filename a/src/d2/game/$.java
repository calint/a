package d2.game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import d2.object;
import d2.viewport;
import d2.world;
public class $ extends world{
	protected ship shp;
	protected int level;
	protected int maxobjects;
	protected Image background;
	final protected int nsupplies=11;
	protected Color[]supplycolor;
	protected String[]supplyname;
	final protected boolean[]key=new boolean[20];
	protected Font myFont;
	protected Font myFontBig;
	protected Font myFontVeryBig;

	public void init(final Image img){
		background=img;
	}
	public $(){
		super(0,0,200,5);
		final String fontnm="monospace";
		myFont=Font.getFont(fontnm);
		myFontBig=myFont==null?null:myFont.deriveFont(17);
		myFontVeryBig=myFont==null?null:myFont.deriveFont(21);
		viewport.center(100,100);
		viewport.zoom(.7);
		supplycolor=new Color[nsupplies];
		supplyname=new String[nsupplies];
		supplycolor[0]=Color.yellow;
		supplyname[0]="Fuel";
		supplycolor[1]=Color.cyan;
		supplyname[1]="(j) Missile";
		supplycolor[2]=Color.pink;
		supplyname[2]="(h) Back-missile";
		supplycolor[3]=Color.magenta;
		supplyname[3]="(k) Shotgun";
		supplycolor[4]=Color.red;
		supplyname[4]="(y) Cluster";
		supplycolor[5]=Color.green;
		supplyname[5]="(i) Mine";
		supplycolor[6]=Color.orange;
		supplyname[6]="(u) Sprincler";
		supplycolor[7]=Color.white;
		supplyname[7]="(b) Reverse-thrust";
		supplycolor[8]=new Color(100,140,30);
		supplyname[8]="(g) Shield";
		supplycolor[9]=new Color(0,140,30);
		supplyname[9]="(n) Controlled cluster";
		supplycolor[10]=new Color(150,150,150);
		supplyname[10]="Energi";
		reset();
	}

	public Color getSuppliesColor(int i){return supplycolor[i];}
	public int getNbrSupplies(){return nsupplies;}
	public void setLevel(int i){reset();warpToLevel(i);}
	public void warpToLevel(int i){
		level=i;
		shp.shoot(ship.SHIELD,0);
		makeAsteroids(level*2);
		add(new label(this,0,0,100,100,"Level "+level,myFontVeryBig,Color.GREEN,3));
	}
	protected void makeAsteroids(int i){
		for(int j=0;j<i;j++){
			Math.random();
			double d=3.141592653589793*Math.random()*2.0;
			double d_1_=10.0*(1.0+Math.random())*Math.cos(d);
			double d_2_=10.0*(1.0+Math.random())*Math.sin(d);
			add(new asteroid(this,10.0,20.0+Math.random()*160.0,20.0,d_1_,d_2_,(3.141592653589793*(2.0*Math.random()-1.0)),3.141592653589793*(Math.random()-0.5)));
		}
	}

	public void reset(){
		super.reset();
		asteroid.asteroidsAlive=0;
		add(shp=new ship(this,100,100,-1.5707963267948966,0,0,0));
	}

	protected void onkeyb(int ky,boolean prsd){
		switch(ky){
		case 106:key[0]=prsd;break;
		case 104:key[4]=prsd;break;
		case 107:key[5]=prsd;break;
		case 121:key[6]=prsd;break;
		case 117:key[8]=prsd;break;
		case 98:key[9]=prsd;break;
		case 103:key[10]=prsd;break;
		case 110:key[11]=prsd;break;
		case 105:key[7]=prsd;break;
		case 32:key[1]=prsd;break;
		case 97:key[2]=prsd;break;
		case 115:key[3]=prsd;break;
		case '1':warpToLevel(1);break;
		}
	}

	protected void devineIntervention(double dt){
		final double s=.0001;
		if(Math.random()*dt<s){
			int i=(int)(Math.random()*(double)(nsupplies-1));
			this.add(new supplies(this,Math.random()*150.0,Math.random()*150.0,Math.random()*20.0,(Math.random()*3.141592653589793*2.0)-3.141592653589793,i,supplycolor[i]));
		}
		if(Math.random()*dt<.00001*(double)level){
			double d=Math.random()*150.0;
			this.add(new cruiser(this,d,0.0,shp));
			this.add(new label(this,d,0.0,(shp.x()-d)*0.15,shp.y()*0.15,"Incoming cruiser!!",myFontBig,Color.red,4.0));
		}
		if(asteroid.asteroidsAlive==0&&level!=0){
			warpToLevel(++level);
			for(int i=0;i<5;i++){
				int j=(int)(Math.random()*(double)getNbrSupplies());
				Color color=getSuppliesColor(j);
				this.add(new supplies(this,shp.x()+(Math.random()-0.5)*5.0,shp.y()+(Math.random()-0.5)*15.0,Math.random()*15.0,Math.random()*3.141592653589793*2.0,j,color));
			}
		}
		if(shp.isalive()==false){
			this.add(new label(this,shp.x(),shp.y(),(100.0-shp.x())*0.5,(100.0-shp.y())*0.5,"Ship destroyed.",myFontBig,new Color(10,20,30),2.0));
			this.add(shp=new ship(this,100,100,-1.5707963267948966,0,0,0.0));
			shp.shoot(ship.SHIELD,dt);
		}
		handleKeyboard(dt);
	}

	protected void handleKeyboard(double dt){
		if(key[0]) shp.shoot(ship.MISSILE,dt);
		if(key[4]) shp.shoot(ship.BACK,dt);
		if(key[5]) shp.shoot(ship.SHOTGUN,dt);
		if(key[6]) shp.shoot(ship.CLUSTER,dt);
		if(key[7]) shp.shoot(ship.SPINING,dt);
		if(key[8]) shp.shoot(ship.SHOOTING,dt);
		if(key[10]) shp.shoot(ship.SHIELD,dt);
		if(key[11]) shp.shoot(ship.CONTROLLED,dt);
		if(key[9])shp.thrust(-.5,dt);
		else if(key[1])shp.thrust(.5,dt);
		else shp.noThrust();
		
		if(key[2])shp.turn(Math.PI,dt);
		else if(key[3])shp.turn(-Math.PI,dt);
		else shp.turn(0,dt);
	}
	boolean paintbackground=true;
	public void paint(Graphics graphics){
		if(size().width!=dim.width||size().height!=dim.height){
			dim=size();
			if(dim.width!=dim.height)dim.width=dim.height=Math.min(dim.width,dim.height);
//			resize(dim);
			viewport.setScreenSize(dim.width,dim.height);
			myFont=new Font("TimesRoman",0,(int)viewport.scaleY(4.0));
			myFontBig=new Font("TimesRoman",2,(int)viewport.scaleY(6.0));
			myFontVeryBig=new Font("TimesRoman",2,(int)viewport.scaleY(10.0));
		}
		viewport.setTheta(shp.angle());
		if(paintbackground)
			graphics.drawImage(background,0,0,dim.width,dim.height,null);
		paintSupplies(graphics,viewport);
		for(final object o:objects)
			o.paint(graphics,viewport);
	}

	protected void paintSupplies(Graphics g,viewport vp){
		int i=(int)vp.scaleY(5.0);
		int i_4_=0;
		int i_5_=vp.x(40.0);
		int i_6_=100;
		g.setFont(myFont);
		for(int i_7_=0;i_7_<nsupplies;i_7_++){
			if(shp.mySupplies[i_7_]>0){
				g.setColor(supplycolor[i_7_]);
				i_6_=vp.y(40.0+(double)(i_4_*5));
				g.fillRect(i_5_,i_6_,(int)vp.scaleX((double)shp.mySupplies[i_7_])>>1,i-1);
				g.setColor(Color.black);
				g.drawString(supplyname[i_7_],i_5_,i_6_+i-2);
				i_4_++;
			}
		}
		g.setColor(Color.black);
		g.drawString(new String("Objects  :"+objects.size()),i_5_,i_6_-20);
		g.drawString(new String("Asteroids:"+asteroid.asteroidsAlive),i_5_,i_6_-40);
	}
}
