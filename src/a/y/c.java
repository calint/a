package a.y;
import static b.b.tobytes;
import static b.b.tostr;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class c extends a {
	static final long serialVersionUID=1;
	private path pathroot(){return req.get().session().path("");}
	private a[]menu=new a[3];
	{for(int n=0;n<menu.length;n++){
		menu[n]=new a(this,"0"+n);
	}}
	protected a chldq(final String id){
		if(id.charAt(0)=='0')
			return menu[Integer.parseInt(id)];
		return super.chldq(id);
	}
	public void to(final xwriter x)throws Throwable{
		final String qry=req.get().query();
		final String[]qparts=b.isempty(qry)?new String[0]:qry.split("/");
		x.pre();
		x.span(menu[0]).nl();
		x.span(menu[1]).nl();

		path p=pathroot();
		//p.assureacess
		x.script();
		int i=0;
		for(final String s:qparts){
			updlvl(x,i++,p,s);
			p=p.get(s);
			//p.assureacess
		}
		x.scriptEnd();
		x.div("editor2");
		if(p.isfile())try{
			final Map<String,Object>ctx=new HashMap<String,Object>();
			ctx.put("blank","blank••");
			p.to(new osinc(x.outputstream(),p.parent(),ctx,this));}catch(Throwable t){
			x.pl(b.stacktraceline(t));
			}
		else{
			x.script();
			updlvl(x,i++,p,"");
			x.scriptEnd();
		}
		x.divEnd();
			
	}
	private void updlvl(final xwriter x,final int lvl,final path pth,final String sel){
		final xwriter y=new xwriter();
		final session ses=req.get().session();
		for(final String s:pth.list()){
			y.p(" • ");
			if(s.equals(sel))
				y.p("[").p(s).p("]");
			else
				y.p("<a href=c?").p(b.urlencode(ses.inpath(pth.get(s)))).p(">").p(s).aEnd();
		}
		x.xu(menu[lvl],y.toString());
	}
	
	static class osinc extends OutputStream{
		private final OutputStream os;
		private final path root;
		private final Map<String,Object>ctx;
		private final Object ctxo;
		public osinc(final OutputStream os,final path root,final Map<String,Object>ctx,final Object ctxo){this.os=os;this.root=root;this.ctx=ctx;this.ctxo=ctxo;}
		public void write(final int ch)throws IOException{throw new UnsupportedOperationException();}
		public void write(final byte[]b)throws IOException{write(b,0,b.length);}
		final public static byte token=(byte)'`';
		private int lineno=1;
		public void write(final byte[]b,final int off,int len) throws IOException{
			int i=off,cpfromix=off,s=0,tknmix=0;
			while(len-->0){
				if(b[i]=='\n')
					lineno++;
				switch(s){
				case 0:
					if(b[i]==token){
						tknmix=i+1;
						s=1;
						os.write(b,cpfromix,i-cpfromix);
					}
					break;
				case 1:
					if(b[i]==token){
						final String tokenval=new String(b,tknmix,i-tknmix);
						final int i0=tokenval.indexOf(' ');
						final String fldnm;
						final String args;
						if(i0!=-1){
							fldnm=tokenval.substring(0,i0);
							args=tokenval.substring(i0+1);
						}else{
							fldnm=tokenval;
							args="";
						}
						if(tokenval.startsWith("@")){
							final path pathinc=root.get(tokenval.substring(1));
							pathinc.to(this);
						}else if(fldnm.startsWith("!")){
							try{ctxo.getClass().getMethod(fldnm.substring(1),new Class[]{OutputStream.class,String.class}).invoke(ctxo,new Object[]{os,args});}catch(Throwable t2){
								os.write(("•• error at line "+lineno+" while "+tokenval+" ••").getBytes());
							}
						}else{
							Object v;
							try{v=ctxo.getClass().getField(fldnm).get(ctxo);}catch(Throwable t){
							try{v=ctxo.getClass().getMethod(fldnm).invoke(ctxo);}catch(Throwable t3){
							try{v=ctxo.getClass().getMethod(fldnm,new Class[]{String.class}).invoke(ctxo,new Object[]{args});}catch(Throwable t2){
							v=ctx.get(tokenval);	
							}}}
							write(tobytes(tostr(v,"")));
						}
						s=0;
						cpfromix=i+1;
					}
					break;
				}
				i++;
			}
			if(s==1);//? bug
			if((i-cpfromix)==0)
				return;
			os.write(b,cpfromix,i-cpfromix);
		}
	}
	
	
	public String name="blankø";
	public String funcu(String s){return "fun¢u"+s;}
	public static String func2(){return "func2ti";}
	public static void funcu3(OutputStream os,String args)throws IOException{
		os.write(b.tobytes("hello world ("+args+")"));
	};
	public void funcu4(OutputStream os,String args)throws IOException{
		os.write(b.tobytes("2hello world ("+args+")"));
	};
}
