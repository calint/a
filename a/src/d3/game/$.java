package d3.game;
import java.util.LinkedList;
import java.util.List;
import d3.applet;
import d3.cfg;
import d3.obj;
import d3.p3;
import d3.player;
import d3.setup;
import d3.window;
import d3.world;
public final class $ implements d3.setup{
	public final static void mkfrags(obj o,int nfrags,double size,double spread,double speed,double rot,int gens){
		for(int n=0;n<nfrags;n++)
			new frag(o.world(),o.pos(),size,spread,speed,rot,gens);
	}
	List<atg> atgls;
	List<atg> add_atgarray(world w,p3 pos,int count,double xdisp,double zdisp){
		atgls=new LinkedList<atg>();
		for(int n=-count;n<count;n++){
			atg a=new atg(w,pos.x+n*xdisp,pos.z+Math.abs(n)*zdisp,0,new ai());
			atgls.add(a);
		}
		for(atg a:atgls)
			a.ai().trgbits(obj.type_vehicle);
		return atgls;
	}
	void add_city(world w){
		// -- the front wall
		new pillar(w,-410,-300,new p3(0,0,0),2,3,12);
		for(int x=-400;x<=400;x+=20){
			if(x==0){
				new gate(w,new p3(x,0,-300),new p3());
				new pillar(w,x+10,-300,new p3(0,0,0),2,3,12);
			}else{
				// new gate(w, new p3d(x, 0, -300), new p3d());
				new wall(w,x,-300,new p3(0,0,0),8,2,10);
				new pillar(w,x+10,-300,new p3(0,0,0),2,3,12);
			}
		}
		// -- the right wall
		new pillar(w,-414,-295,new p3(0,Math.PI/2,0),2,3,12);
		for(int y=-285;y<=400;y+=20){
			new wall(w,-414,y,new p3(0,Math.PI/2,0),8,2,10);
			new pillar(w,-414,y+10,new p3(0,Math.PI/2,0),2,3,12);
		}
		// -- the left wall
		new pillar(w,414,-295,new p3(0,-Math.PI/2,0),2,3,12);
		for(int y=-285;y<=400;y+=20){
			new wall(w,414,y,new p3(0,-Math.PI/2,0),8,2,10);
			new pillar(w,414,y+10,new p3(0,-Math.PI/2,0),2,3,12);
		}
		// -- the back wall
		new pillar(w,-410,410,new p3(0,Math.PI,0),2,3,12);
		for(int x=-400;x<=400;x+=20){
			new wall(w,x,410,new p3(0,Math.PI,0),8,2,10);
			new pillar(w,x+10,410,new p3(0,Math.PI,0),2,3,12);
		}
		// -- the "South-west" area
		for(int y=-255;y<-130;y+=30){
			for(int x=385;x>40;x-=30){
				new building(w,x,y,5,5,5);
			}
		}
		for(int y=-225;y<-130;y+=30){
			new building(w,25,y,5,5,5);
		}
		new building(w,25,-255,5,5,5); // TOWER
		new building(w,25,-105,5,5,5); // TOWER
		for(int x=385;x>130;x-=30)
			new building(w,x,-75,5,5,5);
		for(int x=385;x>100;x-=30)
			new building(w,x,-105,5,5,5);
		for(int y=0;y>-50;y-=40){
			for(int x=150;x<360;x+=40){
				new building(w,x,y,10,10,10);
			}
		}
		new building(w,385,-45,5,5,5);
		new building(w,385,5,5,5,5);
		// -- the "South-east" area
		for(int x=-75;x>-100;x-=20){
			new building(w,x,-255,5,5,5);
			new building(w,x,-235,5,5,5);
			new building(w,x,-225,5,5,5);
			new building(w,x,-205,5,5,5);
			new building(w,x,-195,5,5,5);
			new building(w,x,-175,5,5,5);
			new building(w,x,-165,5,5,5);
			new building(w,x,-145,5,5,5);
			new building(w,x,-135,5,5,5);
			new building(w,x,-115,5,5,5);
		}
		for(int x=-235;x>-260;x-=20){
			new building(w,x,-255,5,5,5);
			new building(w,x,-235,5,5,5);
			new building(w,x,-225,5,5,5);
			new building(w,x,-205,5,5,5);
			new building(w,x,-195,5,5,5);
			new building(w,x,-175,5,5,5);
			new building(w,x,-165,5,5,5);
			new building(w,x,-145,5,5,5);
			new building(w,x,-135,5,5,5);
			new building(w,x,-115,5,5,5);
		}
		for(int y=-115;y<-90;y+=20){
			new building(w,-115,y,5,5,5);
			new building(w,-125,y,5,5,5);
			new building(w,-145,y,5,5,5);
			new building(w,-155,y,5,5,5);
			new building(w,-175,y,5,5,5);
			new building(w,-185,y,5,5,5);
			new building(w,-205,y,5,5,5);
			new building(w,-215,y,5,5,5);
		}
		new building(w,-115,-255,5,5,5);
		new building(w,-125,-255,5,5,5);
		new building(w,-145,-255,5,5,5);
		new building(w,-155,-255,5,5,5);
		new building(w,-175,-255,5,5,5);
		new building(w,-185,-255,5,5,5);
		new building(w,-205,-255,5,5,5);
		new building(w,-215,-255,5,5,5);
		new building(w,-235,-95,5,5,5);
		new building(w,-245,-95,5,5,5);
		new building(w,-255,-95,5,5,5);
		new building(w,-255,-105,5,5,5);
		for(int y=-230;y<-130;y+=90){
			for(int x=-120;x>-220;x-=30){
				new building(w,x,y,10,10,10);
			}
		}
		new building(w,-120,-170,10,10,10);
		new building(w,-120,-200,10,10,10);
		new building(w,-210,-170,10,10,10);
		new building(w,-210,-200,10,10,10);
		for(int y=-225;y<-130;y+=30){
			new building(w,-25,y,5,5,5);
		}
		new building(w,-25,-105,5,5,5); // TOWER
		new building(w,-25,-255,5,5,5); // TOWER
		for(int y=-245;y<60;y+=30){
			for(int x=-295;x>-350;x-=50){
				new building(w,x,y,5,5,5);
			}
		}
		for(int y=-35;y<20;y+=30){
			for(int x=-115;x>-270;x-=30){
				new building(w,x,y,5,5,5);
			}
		}
		new building(w,-375,-5,5,5,5);
		new building(w,-375,-35,5,5,5);
		// -- the "North-east" area
		// -- the left wall
		new pillar(w,55,405,new p3(0,-Math.PI/2,0),2,3,12);
		for(int y=395;y>185;y-=20){
			new wall(w,55,y,new p3(0,-Math.PI/2,0),8,2,10);
			new pillar(w,55,y-10,new p3(0,-Math.PI/2,0),2,3,12);
		}
		// -- the front wall
		new pillar(w,-410,180,new p3(0,0,0),2,3,12);
		for(int x=-400;x<=50;x+=20){
			if((x==0)||(x==-320)){
				new gate(w,new p3(x,0,180),new p3());
				new pillar(w,x+10,180,new p3(0,0,0),2,3,12);
			}else{
				new wall(w,x,180,new p3(0,0,0),8,2,10);
				new pillar(w,x+10,180,new p3(0,0,0),2,3,12);
			}
		}
		// -- the inner front wall
		new pillar(w,-50,260,new p3(0,0,0),2,3,12);
		for(int x=-40;x<=50;x+=20){
			if(x==0){
				new gate(w,new p3(x,0,260),new p3());
				new pillar(w,x+10,260,new p3(0,0,0),2,3,12);
			}else{
				new wall(w,x,260,new p3(0,0,0),8,2,10);
				new pillar(w,x+10,260,new p3(0,0,0),2,3,12);
			}
		}
		// the inner right wall
		for(int y=255;y>190;y-=20){
			new wall(w,-54,y,new p3(0,Math.PI/2,0),8,2,10);
			new pillar(w,-54,y-10,new p3(0,Math.PI/2,0),2,3,12);
		}
		for(int y=215;y<260;y+=20){
			for(int x=-135;x>-240;x-=20){
				new building(w,x,y,5,5,5);
			}
		}
		new building(w,-255,215,5,5,5);
		new building(w,-255,255,5,5,5);
		new building(w,-265,245,5,5,5);
		new building(w,-275,235,5,5,5);
		for(int y=235;y<380;y+=20){
			for(int x=-355;x>-380;x-=20){
				new building(w,x,y,5,5,5);
			}
		}
		for(int y=290;y<380;y+=40){
			for(int x=-140;x>-230;x-=40){
				new building(w,x,y,10,10,10);
			}
		}
		for(int x=0;x>-90;x-=40){
			new building(w,x,360,10,10,10);
		}
		for(int x=20;x>-70;x-=40){
			new building(w,x,300,10,10,10);
		}
		new building(w,35,275,5,5,5);
		new building(w,35,285,5,5,5);
		new building(w,-35,275,5,5,5);
		new building(w,-35,285,5,5,5);
		// the middle area
		for(int y=90;y<140;y+=40){
			for(int x=30;x<80;x+=40){
				new building(w,x,y,10,10,10);
			}
		}
		for(int y=90;y<140;y+=40){
			for(int x=-30;x>-80;x-=40){
				new building(w,x,y,10,10,10);
			}
		}
		for(int x=-105;x>-230;x-=30){
			new building(w,x,125,5,5,5);
		}
		for(int x=-245;x>-340;x-=30){
			new building(w,x,125,5,5,5);
		}
		for(int x=-195;x>-260;x-=30){
			new building(w,x,25,5,5,5);
		}
		new building(w,-135,25,5,5,5);
		new building(w,-105,65,5,5,5);
		new building(w,-105,95,5,5,5);
		new building(w,-135,95,5,5,5);
		new building(w,-195,95,5,5,5);
		new building(w,-225,55,5,5,5);
		// -- the "corridor"
		for(int i=0;i<=50;i+=10){
			new building(w,-205-i,115-i,5,5,5);
			new building(w,-235-i,115-i,5,5,5);
		}
		new building(w,-265,55,5,5,5);
		new building(w,-275,45,5,5,5);
		new building(w,-285,35,5,5,5);
		new building(w,-305,105,5,5,5);
		new building(w,-305,85,5,5,5);
		new building(w,-335,105,5,5,5);
		new building(w,-335,85,5,5,5);
		// -- the circle
		new building(w,-55,-95,5,5,5);
		new building(w,-85,-85,5,5,5);
		new building(w,-105,-65,5,5,5);
		new building(w,-105,25,5,5,5);
		new building(w,-85,45,5,5,5);
		new building(w,-55,55,5,5,5);
		new building(w,-25,65,5,5,5);
		new building(w,55,-95,5,5,5);
		new building(w,85,-85,5,5,5);
		new building(w,105,-65,5,5,5);
		new building(w,105,25,5,5,5);
		new building(w,85,45,5,5,5);
		new building(w,55,55,5,5,5);
		new building(w,25,65,5,5,5);
		new building(w,115,-5,5,5,5);
		new building(w,115,-35,5,5,5);
	}
	void add_city_defence(world w){
		new atg(w,-165,-185,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-165,65,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-295,165,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-345,165,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-295,215,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-345,215,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,25,225,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-25,225,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,15,335,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-15,335,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-255,295,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-255,365,Math.PI,new ai().trgbits(obj.type_vehicle));
		new atg(w,-305,325,Math.PI,new ai().trgbits(obj.type_vehicle));
	}
	void add_ornaments(world w,p3 p,double wi,double stp){
		for(double x=-wi;x<wi;x+=stp)
			for(double y=-wi;y<wi;y+=stp)
				new pillar(w,p.x+x,p.z+y,new p3(),1.83,1.83,1.83*7);
	}
	void add_recons(world w,int napc,p3 pos,double p0){
		double x=pos.x;
		double z=pos.z;
		for(int n=0;n<napc;n++){
			new recon(w,x,z,obj.oclock((int)w.rand(0,12)),new ai().go(new p3(0,1,0)));
			x=w.rand(-500.0,500.0);
			z-=recon.scl.z*p0;
		}
	}
	void add_ufos(world w,p3 p,p3 d,int count){
		ufo ufo=null;
		for(int n=0;n<count;n++)
			ufo=new ufo(w,p.x+n*d.x,p.z+n*d.z,p.y+n*d.y,obj.oclock(0),obj.oclock(0),new ai().trgbits(obj.type_vehicle));
		w.player(ufo);
	}
	public void do_setup(applet app,setup.env env){
		world w=new world(-1024,-1024,2048,8,-4.812);
		new screen(w,new p3(),new p3(-Math.PI/3,0,0));
		//		vehicle v=new ufo(w,0,-20,100,Math.PI);
		env.plrs=new player[1];
		env.plrs[0]=new recon(w,0,-5,0,null);
		w.player(env.plrs[0]);
		scene_city(w);
		scene_reconstorm(w,128);
		add_ufos(w,new p3(0,110,80),new p3(-60,+1,-15),5);
		//		
		//		scene_zkifc(w);
		//		add_atgarray(w, new p3d(0,0,-200), 10, 3.75, 2);
		//		scene_solo(w);
		//				scene_solo_ufo(w);
		//				scene_solo_atg(w);
		//						new building(w,0,-100,20,7,10);
		// add_ornaments(w, new p3d(-100, 0, -400), 10, 10);
		//		 scene_gunnersystem(w, new p3d(), 100, 100);
		// add_city(w);
		//		new building(w,0,0,1,1,1);
		//		new building(w,10,0,0.7,0.3,1.83);
		//		new building(w,20,0,2,2,2);
		//		new building(w,30,0,4,4,4);
		//		w.player(new ufo(w,0,10,0,0));
		//		recon r=new recon(w,0,0,0);
		//		new player(r).go(new p3d(100,0,100));
		//		w.player(r);
		window wn=new window(w,app.getSize(),cfg.scr_dist,(obj)env.plrs[0],new p3(0,11,11),new p3(-Math.PI/180*9,0,0),2,-1.0,1024.0,10.0,2048/16);
		p3 light=new p3(0,0,-1).rot_axis_x(-Math.PI/4).rot_axis_y(-Math.PI/4);
		wn.light(light);
		env.wld=w;
		env.win=wn;
	}
	void scene_city(world w){
		add_city(w);
		add_city_defence(w);
		add_ufos(w,new p3(100,100,0),new p3(100,0,100),1);
	}
	void scene_gunnersystem(world w,p3 p,double rx,double rz){
		new ufo(w,p.x-rx,p.z-rz,100,0,0,new ai().go(new p3(p.x+rx,100,p.z+rz)));
		atg a=new atg(w,p.x,p.z,Math.PI/2,new ai().trgbits(obj.type_vehicle));
		w.player(a);
	}
	void scene_reconstorm(world w,int count){
		add_recons(w,count,new p3(0,0,-800),2);
		add_atgarray(w,new p3(0,0,-400),1,50,-20);
		w.player(atgls.get(atgls.size()>>1));
	}
	void scene_solo(world w){
		recon recon=new recon(w,100,0,0,null);
		w.player(recon);
	}
	void scene_solo_atg(world w){
		atg a=new atg(w,0,0,0,null);
		w.player(a);
	}
	void scene_solo_ufo(world w){
		ufo ufo=new ufo(w,0,0,10,0,0,null);
		w.player(ufo);
	}
	void scene_zkifc(world w){
		new d3.game.d3zk(w,new p3(0,0,-20),new p3(),new p3(),new p3(),d3zk.f3d,new p3(1,1,1),obj.type_strucm,Double.MAX_VALUE,Double.MAX_VALUE,true);
		ufo ufo=new ufo(w,0,0,7,0,0,null);
		w.player(ufo);
	}
}
