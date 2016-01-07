package a.pzm;
import static b.b.pl;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import a.pzm.lang.compiler_error;
import a.pzm.lang.reader;
import a.pzm.lang.statement;
import a.pzm.lang.xbin;
import b.a;
import b.req;
import b.xwriter;
final public class source_editor extends a{
	public a src;
	//	public a resrc;
	public a sts;
	public line_numbers ln;
	public boolean ommit_compiling_source_from_disassembler=false;
	private statement code;
	public void to(final xwriter x) throws Throwable{
		x.style("def","font-weight:bold");//a name
//		x.style("fc","font-style: italic");//function name refered
		x.style("fc","font-weight:bold");//a name
		x.style("ac","color: gray");//assembler
//		x.style("ac","font-weight:bold");//a name
//		x.style("dr","font-style: italic");//data refered
		x.style("dr","font-weight:bold");//a name
		x.spanh(sts,"","width:5em;color:#800;font-weight:bold").ax(this,"f3",""," crun ","a").nl();
		x.table().tr().td("","text-align:right;padding-right:.5em");
		x.el(ln);
		ln.to(x);
		x.el_();
		x.td();
		x.inptxtarea(src);
		x.focus(src);
		//		x.td().spaned(resrc);
		x.table_();
//		if(code!=null)
//				code.to(x);
	}
	synchronized public void x_f3(xwriter x,String s) throws Throwable{
		final String source="{"+src.str()+"}";
		final reader r=new reader(new StringReader(source));
		try{
			code=new statement(this,"b",null,"",r);// root statement
			if(!ommit_compiling_source_from_disassembler){
				final xwriter generated_source=new xwriter();
				code.source_to(generated_source);
				if(!generated_source.toString().equals(source)){
					req.get().session().path("gen").writestr(generated_source.toString());
					req.get().session().path("org").writestr(source);
					throw new Error("generated source differs from input");
				}
			}
			final prog p=new prog(r.toc,code);
			final int[]rom=new int[1024*1024];
			final xbin b=new xbin(p.toc,rom);
			final int nregs_pre=b.registers_available.size();
			pl("registers available "+b.registers_available.size()+" "+b.registers_available);
			code.binary_to(b);
			b.link();
			final int[]new_rom=new int[b.ix()];
			System.arraycopy(rom,0,new_rom,0,b.ix());
			final int nregs_aft=b.registers_available.size();
			if(nregs_pre!=nregs_aft)
				throw new Error("available register count pre binary_to and after do not match: pre="+nregs_pre+"  after="+nregs_aft+"   "+b.registers_available);
			pl("registers available "+b.registers_available.size()+" "+b.registers_available);
//			pl("*** toc");
//			p.toc.entrySet().forEach(me->{
//				pl(me.getKey());
//			});
			pl("*** done");
			if(x==null) return;
			x.xu(sts.clr(),code);
			//			el.source_to(x.xub(resrc,true,true));x.xube();
			ev(x,this,new_rom);
		}catch(compiler_error t){
			b.b.log(t);
			if(x==null)return;
			final String[]ix=t.stmt.location_in_source().split(":");
			final String[]ixe;
			if(b.b.isempty(t.stmt.location_in_source_end())){
				ixe=ix;
			}else{
				ixe=t.stmt.location_in_source_end().split(":");
			}
			x.pl("{var e=$('"+src.id()+"');e.selectionStart="+Integer.parseInt(ix[2])+";e.selectionEnd="+Integer.parseInt(ixe[2])+";}");
			x.xu(sts.set("line "+ix[0]+":"+" "+t.getMessage()));
			//			x.xalert(t.getMessage());
		}catch(Throwable t){
			b.b.log(t);
			if(x==null) return;
			x.xu(sts.set("line "+r.location_in_source()+": "+t.getMessage()));
			//			x.xalert(t.getMessage());
		}
	}
	private static final long serialVersionUID=11;
}
