package a.pzm;
import static b.b.pl;

import java.io.StringReader;

import a.pzm.lang.compiler_error;
import a.pzm.lang.reader;
import a.pzm.lang.statement;
import a.pzm.lang.xbin;
import b.a;
import b.req;
import b.xwriter;
final public class source_editor extends a{
	/**textarea*/public a src;
	//	public a resrc;
	public a sts;
	public line_numbers ln;
	public boolean ommit_compiling_source_from_disassembler=false;
	public statement code;
	public toc se;
	public boolean disp_compile_action=false;
	public void to(final xwriter x) throws Throwable{
//		x.style("def","font-weight:bold");//a name
//		x.style("fc","font-style: italic");//function name refered
//		x.style("fc","font-weight:bold");//a name
//		x.style("ac","color: gray");//assembler
//		x.style("ac","font-weight:bold");//a name
//		x.style("dr","font-style: italic");//data refered
//		x.style("dr","font-weight:bold");//a name
		x.spanh(sts,"","width:5em;color:#800;font-weight:bold");
		if(disp_compile_action)x.ax(this,"f3",""," compile ","c");
		x.nl();
		x.table().tr();
		x.td("","text-align:right;padding-right:.5em").el(ln).r(ln).el_();
		x.td().inptxtarea(src).focus(src);
		x.td().r(se);
		//		x.td().spaned(resrc);
		x.table_();
//		if(code!=null)
//				code.to(x);
	}
	xbin bin;
	synchronized public void x_f3(xwriter x,String s) throws Throwable{
		final String source=src.str();
		final reader r=new reader(new StringReader(source));
		try{
			code=statement.read(r);
			attach(code,"code");
			se.set_statement(code);
			se.set_textareaid(src.id());
			x.xuo(se);
//			System.out.println(generated_source.toString());
			if(!ommit_compiling_source_from_disassembler){
				final xwriter generated_source=new xwriter();
				code.source_to(generated_source);
				req.get().session().path("gen").writestr(generated_source.toString());
				req.get().session().path("org").writestr(source);
				if(!generated_source.toString().equals(source)){
					throw new Error("generated source differs from input");
				}
			}
			final prog p=new prog(r.toc,code);
			final int[]rom=new int[1024*1024];//?
			bin=new xbin(p.toc,rom);//? intbuffer.get() put()   pages then create_int_array()
			final int nregs_pre=bin.registers_available.size();
			pl("registers available "+bin.registers_available.size()+" "+bin.registers_available);
			code.binary_to(bin);
			bin.link();
			final int[]new_rom=new int[bin.ix()];
			System.arraycopy(rom,0,new_rom,0,bin.ix());
			final int nregs_aft=bin.registers_available.size();
			if(nregs_pre!=nregs_aft)
				throw new Error("available register count pre binary_to and after do not match: pre="+nregs_pre+"  after="+nregs_aft+"   "+bin.registers_available);
			pl("registers available "+bin.registers_available.size()+" "+bin.registers_available);
//			pl("*** toc");
//			p.toc.entrySet().forEach(me->{
//				pl(me.getKey());
//			});
			if(!bin.vspc().is_aliases_empty()){
				throw new Error("lingering aliases");
			}
			pl("*** done");
			if(x==null) return;
			x.xu(sts.clr(),code).flush();
			((ide)pt()).xj_update_focus_on_rom(x);
			ln.xj_select_line(x,0);
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
			ln.xj_select_line(x,Integer.parseInt(ix[0]));
			//			x.xalert(t.getMessage());
		}catch(Throwable t){
			b.b.log(t);
			if(x==null) return;
			x.xu(sts.set("line "+r.location_in_source()+": "+b.b.stacktrace(t)));//?
			//			x.xalert(t.getMessage());
		}
	}
	private static final long serialVersionUID=11;
}
