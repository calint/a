package d3.game;
import d3.f3;
import d3.obj;
import d3.p3;
public class buildingcrp extends obj{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(buildingcrp.class);
	public static int frag_gens=2;
	public static double frag_pcount=5;
	public static double frag_prot=0.2;
	public static double frag_psize=0.1;
	public static double frag_pspeed=5;
	public static double frag_pspread=0.1;
	public static double health0=10;
	public buildingcrp(building b){
		super(b.world(),b.pos().y(b.pos().y/4),b.agl(),type_struc,f3,b.scale().y(b.scale().y/4),health0);
		p3 s=b.scale();
		$.mkfrags(this,(int)(frag_pcount*s.x*s.z),frag_psize*s.x*s.z,frag_pspread*s.z,s.z*frag_pspeed,s.z*frag_prot,frag_gens);
	}
}
