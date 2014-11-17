package a.civ;
import java.io.*;//import java.io;
import a.x.osvoid;
import b.a;
import b.xwriter;
public class atape extends a{
	public String resource_path="data.tape";
	public void rewind(){
		is=getClass().getResourceAsStream(resource_path);
		if(is==null)throw new Error("3 "+resource_path);
	}
	private InputStream is;
	{rewind();}
	public atape stream_next_file_name(final OutputStream os)throws IOException{
		final byte[]cha=new byte[1];
		while(true){
			final int ch=is.read();
			if(ch==-1)throw new Error("1");
			if(ch=='\n')break;
			cha[0]=(byte)ch;//overcomeswritingarraysonly
			os.write(cha);
		}
		return this;
	}
	public atape stream_next_file_content(final OutputStream os)throws IOException{
		boolean last_ch_was_nl=false;
		final byte[]cha=new byte[1];
		while(true){
			final int ch=is.read();
			if(ch==-1)throw new Error("2");
			if(ch=='\n')if(last_ch_was_nl)break;else last_ch_was_nl=true;else last_ch_was_nl=false;
			cha[0]=(byte)ch;
			os.write(cha);
		}
		return this;
	}
	
	@Override public void to(final xwriter x) throws Throwable{
//		x.ax(this,"rst"," :: reset");
		x.ax(this,"ls"," :: list");
		x.ax(this,"d"," :: print");
		x.nl();
		x.divr(d);
	}
	/**display*/public a d;
//	synchronized public void x_rst(xwriter x,String s){reset();}
	synchronized public void x_ls(xwriter y,String s)throws IOException{
		rewind();
		final xwriter x=y.xub(d,true,false);
		final OutputStream os=x.outputstream();
		try{while(true){
			stream_next_file_name(os);
			x.nl();
			stream_next_file_content(osvoid.i);
		}}catch(Error r){}
		y.xube();
	}
	synchronized public void x_d(xwriter y,String s)throws IOException{
		rewind();
		final xwriter x=y.xub(d,true,false);
		final OutputStream os=x.outputstream();
		try{while(true){
			stream_next_file_name(os);
			x.nl();
			stream_next_file_content(os);
		}}catch(Error r){}
		y.xube();
	}
	private static final long serialVersionUID=1;
}
