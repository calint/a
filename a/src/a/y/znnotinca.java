package a.y;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import b.a;
import b.xwriter;
public class znnotinca extends a{
	static final long serialVersionUID=1L;
	public long t;
	public int pc;
	public int ir;
	public int reg[]= new int[0x00100];//256
	public int rom[]= new int[0x04000];//4k
	public byte ram[]=new byte[0x10000*4];//1m
	public a src;
	public a bin;
	public a run;
	public static class ob{
		public final static ob root=new ob("„",null,null,1);
		private String label;
		private ob left;
		private ob right;
		private int delay;
		public float pfail;
		public ob(final String label,final ob left,final ob right,final int delay){
			this.label=label;this.left=left;this.right=right;this.delay=delay;
			if(left!=null)left.right=this;
			if(right!=null)right.left=this;
		}
		protected int tick(final xwriter x,final int t)throws Throwable{
			x.p(t).p("…").p(label).p("…");
			final int tt=t+delay;
			if(right!=null)
				return right.tick(x,tt);
			return tt;
		}
		public ob left(){return left;}
		public final String toString(){return label;}
		public void to(final xwriter x)throws Throwable{
			x.p("…").p(label).nl();
			if(right!=null)right.to(x);
		}
	}
	public void to(final xwriter x)throws Throwable{
		x.code().pre();
		x.pl("notinca 32b");
		x.pl("    0     4    8        16       24       32");
		x.pl("   :zn jr     :    :    :        :        :");
		x.pl("   :--:--:----:----:----:--------:--------:");
		x.pl("lda:00:11:....:....:....:........:........:");
		x.pl("jif:..:11:....:....:....:........:........:");
		x.pl("jsr:..:10:....:....:....:........:........:");
		x.pl("ret:..:.1:....:....:....:........:........:");
		x.pl("   :..:0.:pixl:niah:shif:r......d:w......a:");
		x.pl("   :--:--:----:----:----:--------:--------:");
		x.pl("      loop imm nx load");
		x.pl("              not inc add hlf");
		x.pl("                store hilo inca flg    read write");
		x.pl("…………:……:");
		x.pl("… ifzn …");
		x.pl(" … branch …");
		x.pl("  … loop …");
		x.pl("  … read …");
		x.pl("   … alu …");
		x.pl("    … write …");
		x.pl("  … next …");
		x.pl("  … ret …");
		x.pl("     … done");
		x.pl("…………:……:");
		x.pl("            ");
		final ob ifzn=new ob("ifzn",ob.root,null,1);
		final ob branch=new ob("branch",ifzn,null,1);
		final ob loop=new ob("loop",branch,null,1);
		final ob read=new ob("read",loop,null,1);
		final ob alu=new ob("alu",read,null,1);
		final ob write=new ob("write",alu,null,1);
		final ob next=new ob("next",write,null,1);
		final ob ret=new ob("ret",next,null,1);
		new ob("done",ret,null,1);
		ob.root.to(x);
		ob.root.tick(x,0);
		x.nl();
		x.pl("source:");
		x.inputTextArea(src,"editor2").nl();
		x.ax(this,"c","compile").tag("hr").spanh(bin).tag("hr");
		x.ax(this,"r","run").tag("hr").spanh(run).tag("hr");
	}
	public void to(final OutputStream os,final int op)throws Throwable{
		os.write(ram);
		final BufferedImage bi=new BufferedImage(0x10,0x10,BufferedImage.TYPE_4BYTE_ABGR);
		final Graphics g=bi.getGraphics();
		g.drawImage(bi,0,0,null);
		ImageIO.write(bi,"png",os);
	}
	public void x_c(final xwriter x,final String s)throws Throwable{
		x.xu(bin.set(c(src.toString())));
	}
	public void x_r(final xwriter x,final String s)throws Throwable{
		x.xu(run.set(r(bin.toString())));
	}
	public static String c(final String src){
		return "…file\n\n"+src;
	}
	public static String r(final String bin){
		if(bin.startsWith("…file\n\n"))
			return bin.substring("…file\n\n".length());
		return bin;
	}
}
