package a.pcmega.x;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;
import javax.swing.JFrame;
import javax.swing.JPanel;
import a.pczero.ram;
public final class display{
	private ram ram;
	public display connect(final ram ram){this.ram=ram;return this;}
	
	final int wi=256,hi=128;
	final JFrame frame;
	final JPanel panel;
	private int scale=4;
	public static void main(String[] args){
		new display(null);
	}
	private BufferedImage bmp;
	public display(final ram ram){
		this.ram=ram;	        
		bmp=new BufferedImage(wi,hi,BufferedImage.TYPE_USHORT_565_RGB);
		frame=new JFrame("display");
		frame.setSize(wi*scale,hi*scale);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		panel=(JPanel)frame.getContentPane();
		frame.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){refresh();}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
		frame.addComponentListener(new ComponentAdapter(){
			public void componentResized(final ComponentEvent e){
				refresh();
			}
		});
	}
	public void refresh(){
		final short[]pxls=((DataBufferUShort)bmp.getRaster().getDataBuffer()).getData();
		for(int i=0;i<pxls.length;i++){
				final short argb=ram.get(i);
				final int b=argb   &0xf;
				final int g=argb>>4&0xf;
				final int r=argb>>8&0xf;
				pxls[i]=(short)((r<<11)|((g<<7)|(b<<1)));
		}
		final Graphics2D g=(Graphics2D)panel.getGraphics();
		g.drawImage(bmp,0,0,frame.getWidth(),frame.getHeight(),null);
		frame.toFront();
	}
}