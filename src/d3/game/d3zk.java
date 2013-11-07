package d3.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import d3.f3;
import d3.objm;
import d3.p3;
import d3.window;
import d3.world;
public class d3zk extends objm{
	private static final long serialVersionUID=1L;
	public static f3 f3d=null;
	static class alu{}
	static class blu{}
	static class ifu{}
	static class mvu{
		public final static int op_pushmul=1,op_pop=2,op_mvr=4,op_mvrt=8;
		float[] mtx=new float[4*4];
		byte op;
		float[] vecs=new float[2*4];
		//		private float[]stk=new float[256*4*4];
		//		private int stki;
	}//matrix vector unit
	static class rfu{}
	static class rru{
		public final static int op_load=1,op_store=2;
		int addr0_19;
		float[] data=new float[2*4];
		byte op;
	}//ram read unit
	static class vau{}//vector acceleration unit;//instruction fetch unit
	static class vdu{}//vector display unit;//branch and loop unit
	static class vpu{}//vector projection unit;//register fetch unit
	final static int BITV_CZ=1,BITV_CN=2,BITV_CALL=4,BITV_RET=8,BITV_NXT=0x10;
	public final static int K=1024;;//arithmetic logic unit
	int acc,dat,adr;
	int[] gpr=new int[256];
	int ip;
	int ir;
	int[] ram=new int[1*K*K];
	int[] ram_argb=new int[2*256*256];// (z,argb) 256KB
	float[] ram_xyzw=new float[64*K*4];// (x,y,z,w) 1MB
	int[] rom=new int[4*K];
	int[] stkip=new int[256];
	byte stkipi;
	int[] stklp=new int[256];
	int[] stklpa=new int[256];
	int stklpi;
	byte[] stkzn=new byte[256];
	int vram[]=new int[0x200*0x200];
	byte zn;
	{
		for(int n=0;n<vram.length;n++)
			vram[n]=n;
	}
	public d3zk(world w,p3 p,p3 a,p3 dp,p3 da,f3 ph,p3 s,int type0,double hlth0,double lftm0,boolean hoverer0){
		super(w,p,a,dp,da,ph,s,type0,hlth0,lftm0,hoverer0);
	}
	public void notinca(display d){}
	public void rend_clp(Graphics g,window w,p3 lht,int clp){
		super.rend_clp(g,w,lht,clp);
		rend_hud(g,w,lht);
	}
	public void rend_hud(Graphics g,window w,p3 lht){
		BufferedImage bi=new BufferedImage(0x100,0x100,BufferedImage.TYPE_INT_ARGB_PRE);
		WritableRaster r=bi.getRaster();
		r.setPixels(0,0,0x100,0x100,vram);
		g.drawImage(bi,0x80,0x80,null);
		g.setColor(Color.black);
		g.drawString(getClass().getName(),0x80,0x78);
	}
	public void rend_noclp(Graphics g,window w,p3 lht){
		super.rend_noclp(g,w,lht);
		rend_hud(g,w,lht);
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		ir=rom[ip];
		int irzn=ir&3;
		if(irzn!=0)
			if((irzn&zn)==0){
				ip++;
				return;
			}
		if((ir&BITV_CALL)!=0){
			stkip[stkipi]=ip;
			stkzn[stkipi]=zn;
			stkipi++;
			ip=ir&0x0fffffff;
			return;
		}
		boolean f1=false;
		if((ir&BITV_NXT)!=0)
			if(stklp[stklpi]--!=0)
				ip=stklpa[stklpi];
			else{
				stklpi--;
				ip++;
				if((ir&BITV_RET)!=0){
					ip=stkip[--stkipi];
					zn=stkzn[stkipi];
					f1=true;
				}
			}
		if(!f1)
			if((ir&BITV_RET)!=0){
				ip=stkip[--stkipi];
				zn=stkzn[stkipi];
			}
		int op=ir&0x0e000000;
		int im=ir&0x00ffffff;
		if(op==0x02000000){
			stklpi++;
			stklp[stklpi]=im;
			stklpa[stklpi]=ip;
		}
		if(op==0x04000000){
			stklpi++;
			stklp[stklpi]=gpr[im];
			stklpa[stklpi]=ip;
		}
		if(op==0x06000000){
			gpr[0xf]=im;
			return;
		}
		if(op==0x08000000){
			ip+=im;
			return;
		}
		//clear,load,add,half,store
		if((op&0x00100000)!=0)
			acc=0;
		if((op&0x00200000)!=0){
			dat=ram[gpr[im&0x10]];
		}else
			dat=gpr[im&0x10];
		if((op&0x00400000)!=0)
			acc+=dat;
		if((op&0x00800000)!=0)
			acc>>=1;
		if((op&0x00010000)!=0)
			ram[gpr[im&0x10]]=acc;
		if((im&0x10)!=0)
			gpr[im&0x10]++;
	}
};
//d3zk - dizzy k megadotti pipeline
//
//  | w|   
// r|__|
//
//ram - random access memory read write 32B/T 
//adr0..20  data0..255  op(read,write)
//
//mvu - matrix vector unit          clr
//mtx0..511  vecs0..255  op(mulpush,pop,mvr,mvrt)
//stk0..511 x 256              
//
//vpu - vector projection unit
//vp0..127  vecs0..255  op(scale,zproj,zflp,displace)
//(xs,ys,xo,yo) (x*xs/z+xo,y*ys/z+yo,z,argb)
//      
//vdu - vector display unit  pzram 1024x512x64b (x,y,z,argb)
//clp0..63  vecs0..255  clp(lft,rht,top,btm,fnt,bck)
//(x0,y0,x1,y1)
//
//vau - vector acceleration unit
//dt0..31    vecs0..255  
//
//28b notinca computer
//
//          |loopi
//8b zncr ex|loop
//          |skp
//          |imm
//          
//13b ld0..2 neg add hlf dbl st0..2 inc0..2
//4b mvu
//2b vpu
//1b vdu
//0b vau
//
//acc (x,y,z,w)
// zen notincah 40b 256 
//         not inc add hlf
// zn crxm ih ra------ rb------ rc------ niah----   
// :: :::: :: -------- imm24--- -------- --------
// :: :::: :: imm----- -------- -------- --------
// -- c--- -- -------- -------- -------- -------- .  call  imm
// -- cr00 00 -------- -------- -------- -------- .  loop  imm        for(int k=0;k<imm;k++)
// -- cr10 00 -------- -------- -------- -------- .  lp    ra      rb rc   for(int a=ra,b=rb,c=rc;a<b;a+=c)
// -- cr11 01 -------- -------- -------- -------- .  lda   imm        r[0xa]=imm32
// -- cr11 10 -------- -------- -------- -------- .  ldr   ra imm24     r[ra]=imm24
// -- cr11 11 -------- -------- -------- -------- .  cp    ra      rb rc
// -- cr0m 00 -------- -------- -------- -------- .  ld    *ra     rb rc   rc=*(ra+(rb<<2))
// -- cr0m i0 -------- -------- -------- -------- .  ld    *ra++   rb rc
// -- cr1m 00 -------- -------- -------- -------- .  st    *ra     rb rc   *(ra+(rb<<2))=rc
// -- cr1m i0 -------- -------- -------- -------- .  st    *ra++   rb rc
// -- cr1m ih -------- -------- -------- -------- .  st    *ra++   rb rc   chr
// -- cr1m 0h -------- -------- -------- -------- .  st    *ra     rb rc   chr
// -- ---m -- -------- -------- -------- -------- .  niah  *ra     rb rc
// -- ---m i- -------- -------- -------- -------- .  niah  *ra++   rb rc
// z- ---- -- -------- -------- -------- -------- z  niah  ra      rb rc
// -n ---- -- -------- -------- -------- -------- n  niah  ra      rb rc   nxt
// zn -r-- -- -------- -------- -------- -------- p  niah  ra      rb rc   ret
// -- -rx- -- -------- -------- -------- -------- .  niah  ra      rb rc   nxt   ret
// zn crx- lsib niah ra1 ra2
class display{}
