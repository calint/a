package a.craftytrainer;

import static b.b.K;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import b.a;
import b.b;
import b.path;
import b.req;
import b.xwriter;
import c.client;

final public class ficsgames extends a{
	public a pl;//player name
	public a af;//query date
	public a ot;//output
	@Override public void to(xwriter x)throws Throwable{
		x.el(this);
		x.p("player: ").inputText(pl).p("  from date: ").input(af,"date",null,null,null,null,null,null,null).ax(this).nl().output(ot);
		x.elend();
	}

	public void x_(xwriter y,String s)throws Throwable{
		final xwriter x=y.xub(ot,true,true);
		final pgnreader ps=query(pl.str(),af.str());
		while(true){
			x.nl().nl();
			final Map<String,String>tags=ps.readTags();
			if(tags==null)break;
			x.pl(tags.toString());
			String mv=ps.readNextMove();
			while(mv!=null){
				x.p(mv).spc();
				mv=ps.readNextMove();
			}
			x.nl().nl();
		}
		y.xube();
	}
	
//	http://ficsgames.org/cgi-bin/search.cgi?player=ctenitchi&action=Finger

	public pgnreader query(final String player_name,final String after_date)throws Throwable{
		//	http://ficsgames.org/cgi-bin/search.cgi?white=ctenitchi&colors=1&date-sel-after=2013&date-sel-after-mm=07&date-sel-after-dd=04&dlgamesnomtimes=Download+(no+movetimes)
		if(player_name==null||player_name.isEmpty())throw new Error("must enter playername");
		final String[]ad=after_date.split("\\-");
		try(final client c=new client("ficsgames.org",80)){
			final StringBuilder sb=new StringBuilder();
			sb.append("/cgi-bin/search.cgi?white=").append(player_name).append("&date-sel-after=").append(ad[0]).append("&date-sel-after-mm=").append(ad[1]).append("&date-sel-after-dd=").append(ad[2]).append("&colors=1&dlgamesnomtimes=Download+(no+movetimes)");
			final String uri=sb.toString();
			
			final ByteArrayOutputStream bos=new ByteArrayOutputStream(8*K);//? ybuffer
			try{c.get(uri,bos::write,c::close);}catch(Throwable t){t.printStackTrace();}
			c.run();
			
			final byte[]ba=bos.toByteArray();
			try(final ZipInputStream zis=new ZipInputStream(new ByteArrayInputStream(ba))){
				final ZipEntry ze=zis.getNextEntry();
				final path p=req.get().session().path(getClass()).get(uri);
				try(final OutputStream pos=p.outputstream()){b.cp(zis,pos);}
				return new pgnreader(p.reader());
			}
		}
	}
	
	private static final long serialVersionUID=1L;
}
