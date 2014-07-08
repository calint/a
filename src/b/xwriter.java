package b;
import static b.b.tobytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public final class xwriter{
	public static String encquot(final String text){if(text==null)return"";return text.replaceAll("\"","&quot;");}
	private final OutputStream os;
	public xwriter(final OutputStream os){this.os=os;}
	public xwriter(){os=new ByteArrayOutputStream();}
	public OutputStream outputstream(){return os;}
	public xwriter p(final String s){if(s==null)return this;try{os.write(tobytes(s));}catch(final IOException e){throw new Error(e);}return this;}
	public xwriter nl(){return p('\n');}
	public xwriter nl(final int number_of_newlines){for(int i=0;i<number_of_newlines;i++)p('\n');return this;}
	public xwriter p(final byte n){return p(Byte.toString(n));}
	public xwriter p(final char n){return p(Character.toString(n));}
	public xwriter p(final int n){return p(Integer.toString(n));}
	public xwriter p(final float n){return p(Float.toString(n));}
	public xwriter p(final long n){return p(Long.toString(n));}
	public xwriter p(final double n){return p(Double.toString(n));}
	public xwriter pl(final String s){return p(s).nl();}
	public xwriter tag(final String name){return p("<").p(name).p(">");}
	public xwriter tag(final String name,final String id){return p("<").p(name).p(" id=").p(id).p(">");}
	public xwriter tago(final String name){return p("<").p(name);}
//	public xwriter tago(final String name,final String id){return p("<").p(name).p(" id=").p(id);}
	public xwriter attrdef(final a e){final String wid=e.id();return attr("id",wid);}
	public xwriter attr(final String name,final int value){return p(" ").p(name).p("=").p(value);}
	public xwriter attr(final String name,final String value){return p(" ").p(name).p("=\"").p(xwriter.encquot(value)).p("\"");}
	public xwriter attr(final String name){return p(" ").p(name);}
	public xwriter tagoe(){return p(">");}
//	public xwriter tagoec(){return p("/>");}
	public xwriter tage(final String name){return p("</").p(name).p(">");}
	public xwriter a(final String href){return tago("a").attr("href",href).tagoe();}
	public xwriter aEnd(){return tage("a");}
	public xwriter a(final String href,final String txt){return a(href).p(txt).aEnd();}
	public xwriter ax(final a e,final String func){return ax(e,func,func);}
	public xwriter ax(final a e,final String func,final String html){
		final String wid=e.id();
		p("<a href=\"javascript:").axjs(wid,func,"").p("\">").p(html).p("</a>");
		return this;
	}
	public xwriter ax(final a a,final String func,final String param,final String html){
		final String wid=a.id();
		p("<a href=\"javascript:").axjs(wid,func,param).p("\">").p(html).p("</a>");
		return this;
	}
	public xwriter axjs(final String id,final String func,final String param){
		p("$x('").p(id);
		if(!b.isempty(func))p(" ").p(func);
		if(!b.isempty(param))p(" ").p(param);
		return p("')");
	}
//	public xwriter axBgn(final a a,final String args){return tago("a").attrdef(a).attr("href","javascript:$x('"+a.id()+" "+args+"')").tagoe();}
//	public xwriter axBgn(final a a){return tago("a").attrdef(a).attr("href","javascript:$x('"+a.id()+"')").tagoe();}
	public xwriter axBgn(final a e,final String args){return tago("a").attr("href","javascript:$x('"+e.id()+" "+args+"')").tagoe();}
	public xwriter axBgn(final a e){return tago("a").attr("href","javascript:$x('"+e.id()+"')").tagoe();}
	public xwriter axEnd(){return tage("a");}
	public xwriter br(){return tag("br");}
	public xwriter div(final String cls){return tago("div").attr("class",cls).tagoe();}
	public xwriter div(){return tag("div");}
	public xwriter div(final a e){return tago("div").attr("id",e.id()).tagoe();}
	public xwriter divEnd(){return tage("div");}
	public xwriter focus(final a e){return script().p("$f('").p(e.id()).p("')").scriptEnd();}
	public xwriter inputInt(final a e){return tago("input").attr("value",e.toString()).attrdef(e).attr("type","text").attr("class","nbr").attr("size",5).attr("onchange","$b(this)").tagoe();}
	public xwriter inputFlt(final a e){return tago("input").attr("value",e.toString()).attrdef(e).attr("type","text").attr("class","nbr").attr("size",5).attr("onchange","$b(this)").tagoe();}
	public xwriter inputLng(final a e){return inputInt(e);}
	public xwriter pre(){return tag("pre");}
	public xwriter pre(final String cls){return tago("pre").attr("class",cls).tagoe();}
	public xwriter preEnd(){return tage("pre");}
	public xwriter script(){return tag("script");}
	public xwriter scriptEnd(){return tage("script");}
	public xwriter spano(final a e){return spano(e,null);}
	public xwriter spano(final a e,final String style){
		tago("span").attr("id",e.id());
		if(style!=null)attr("style",style);
		return tagoe();
	}
	public xwriter span(final a e){return span(e,null);}
	public xwriter span(final a e,final String style){
		tago("span").attr("id",e.id());
		if(style!=null)attr("style",style);
		tagoe();
		try{e.to(new osltgt(os));}catch(Throwable t){throw new Error(t);}
		return spanEnd();
	}
	public xwriter spanh(final a e){tago("span").attr("id",e.id()).tagoe();try{e.to(os);}catch(Throwable t){throw new Error(t);}return spanEnd();}
	public xwriter spanx(final a e)throws Throwable{tago("span").attr("id",e.id()).tagoe();e.to(this);return spanEnd();}
	public xwriter spanEnd(){return tage("span");}
	public xwriter table(){return tag("table");}
	public xwriter table(final String cls){return tago("table").attr("class",cls).tagoe();}
	public xwriter tableEnd(){return tage("table");}
	public xwriter style(){return p("<style>");}
	public xwriter styleEnd(){return tage("style");}
	public xwriter td(){return tag("td");}
	public xwriter td(final String cls){if(cls==null||cls.length()==0) return td();return tago("td").attr("class",cls).tagoe();}
	public xwriter tdEnd(){return tage("td");}
	public xwriter th(){return tag("th");}
	public xwriter th(final int colspan){return tago("th").attr("colspan",colspan).tagoe();}
	public xwriter th(final String cls){return tago("th").attr("class",cls).tagoe();}
	public xwriter thEnd(){return tage("th");}
	public xwriter tr(){return tag("tr");}
	public xwriter tr(final String cls){return tago("tr").attr("class",cls).tagoe();}
	public xwriter trEnd(){return tage("tr");}
	public xwriter ul(){return tag("ul");}
	public xwriter ulEnd(){return tage("ul");}
	public xwriter li(){return tag("li");}
	public xwriter li(final String cls){if(cls==null)return li();return tago("li").attr("class",cls).tagoe();}
	public xwriter code(){return tag("code");}
	public xwriter codeEnd(){return tage("code");}
	public xwriter rend(final a e)throws Throwable{if(e==null)return this;e.to(this);return this;}
	public xwriter inputText(final a e){return input(e,"text",null,null,null,null,null,null,null);}
	public xwriter inputText(final a e,final String stylecls,final a axonreturn,final String axp){return input(e,"text",null,stylecls,axonreturn,axp,null,null,null);}
	public xwriter inputText(final a e,final a axonreturn,final String axp){return input(e,"text",null,null,axonreturn,axp,null,null,null);}
	public xwriter inputText(final a e,final String stylecls,final a axonreturn,final String axp,final String txt){return input(e,"text",null,stylecls,axonreturn,axp,txt,null,null);}
	public xwriter inputColor(final a e){return input(e,"color",null,null,null,null,null,null,null);}
	public xwriter input(final a e,final String type,final String style,final String stylecls,final a axonreturn,final String axp,final String txt,final a on_change_ajax_elem,final String on_change_ajax_param){
		final String value=txt==null?e.toString():txt;
		tago("input").attr("value",value).attrdef(e).attr("type",type);
		if(style!=null)
			attr("style",style);
		if(stylecls!=null)
			attr("class",stylecls);
		if(axonreturn!=null){
			final String ax=axonreturn.id()+(axp!=null?(" "+axp):"");
			attr("onkeypress","return $r(event,this,'"+ax+"')");
		}
		final StringBuilder sb=new StringBuilder();
		if("checkbox".equals(type)){
			if(value.equals(Boolean.TRUE.toString()))
				attr("checked","checked");
			sb.append("this.value=this.checked?'1':'0';$b(this)");
			if(on_change_ajax_elem!=null){
				final String ax=axonreturn.id()+(axp!=null?(" "+on_change_ajax_param):"");
				sb.append(";$x('"+ax+"')");
			}		
		}else{
			sb.append("$b(this)");
			if(on_change_ajax_elem!=null){
				final String ax=axonreturn.id()+(axp!=null?(" "+on_change_ajax_param):"");
				sb.append(";$x('"+ax+"')");
			}
		}
		attr("onchange",sb.toString());
		return tagoe();//? <input hidden "">
	}
	public xwriter inputTextArea(final a e){return inputTextArea(e,null);}
	public xwriter inputTextArea(final a e,final String cls){
		tago("textarea").attrdef(e).attr("onchange","$b(this);return true;");
		if(cls!=null)attr("class",cls);
		attr("wrap","off").attr("spellcheck","false").tagoe();
		try{e.to(new osltgt(outputstream()));}catch(final Throwable t){b.log(t);p(b.stacktrace(t));}
		//? fld.to(new osltgt(x.outputStream()))
//		String s=a.toString();
//		s=s.replaceAll("\\<","&lt;");//?
//		s=s.replaceAll("\\>","&gt;");
//		return p(s).tage("textarea");
		return tage("textarea");
	}
	public xwriter flush(){try{os.flush();}catch (IOException e){throw new Error(e);}return this;}
	public String toString(){return os.toString();}
	public xwriter hr(){return tag("hr");}
	public xwriter spc(){return p(' ');}
	public xwriter tab(){return p('\t');}
	public xwriter enter(){return p('\r');}
	public xwriter bell(){return p('\07');}
	public xwriter p(final CharSequence cs){return p(cs.toString());}
	public xwriter title(final String s){return script().xtitle(s).scriptEnd();}
	public xwriter css(final String cls,final String stl){return p(cls).p("{").p(stl).p("}");}
	public xwriter css(final a e,final String stl){return p("#").p(e.id()).p("{").p(stl).p("}");}
//	public xwriter cssln(final a e,final String stl){return style().p("#").p(e.id()).p("{").p(stl).p("}").styleEnd();}
	public xwriter inputax(final a e,final String stylecls,final a ax,final String axp){
		tago("input").attr("value",e.toString()).attrdef(e).attr("type","text");
//		tago("x").attrdef(a).attr("contenteditable","true");
		if(stylecls!=null)
			attr("class",stylecls);
//		final String onfocus="$d(this+' onfocus');var r=document.createRange();r.selectNodeContents(this);r.collapse(false);var s=window.getSelection();s.removeAllRanges();s.addRange(r);";
//		attr("onfocus",onfocus);
		attr("onfocus","this.setSelectionRange(this.value.length,this.value.length)");
		final StringBuilder sb=new StringBuilder();
		sb.append((ax==null?e:ax).id());
		if(!b.isempty(axp))
			sb.append(" ").append(axp);
		final String sbs=sb.toString();
//		attr("oninput","this._changed=true;if(this.innerHTML=='<br>')this.innerHTML='';$x('"+sbs+"')");
		attr("oninput","$b(this);$x('"+sbs+"');return true;");
		attr("onkeypress","return $r(event,this,'"+ax.id()+" sel')");
//		attr("onblur","if(!this._changed)return true;$x('"+sbs+"')");
//		return tagoe().p(a.toString()).tagEnd("x");
		return tagoe();
	}
	public xwriter inputax(final a e,final String stylecls,final a ax,final String onchangeaxp,final String onselectaxp){
		tago("input").attr("value",e.toString()).attrdef(e).attr("type","text");
		if(stylecls!=null)
			attr("class",stylecls);
		final String eid=ax.id();
		attr("onfocus","this.setSelectionRange(this.value.length,this.value.length)");
		attr("oninput","$b(this);$x('"+eid+" "+onchangeaxp+"');return true;");
		attr("onkeypress","if(!event)event=window.event;if(event.keyCode!=13)return true;$x('"+eid+" "+onselectaxp+"');return false;");
		return tagoe();
	}
	public xwriter inputax(final a e){return inputax(e,null,e.pt(),null);}

	public xwriter output(final a e){return tago("output").attr("id",e.id()).tagoe().tage("output");}//? value
	public xwriter spc(final int n){for(int i=0;i<n;i++)spc();return this;}
	public xwriter ul(final String cls){return tago("ul").attr("class",cls).tagoe();}
	public xwriter cssfont(final String name,final String url){return p("@font-face{font-family:").p(name).p(";src:url(").p(url).p(");}");}
	private xwriter jsstr(final String s){try{new osjsstr(os).write(b.tobytes(s));}catch(final IOException e){throw new Error(e);}return this;}
	public xwriter xu(final String id,final String s){return p("$s('").p(id).p("','").jsstr(s).pl("');");}
	public xwriter xu(final a e,final String s){return xu(e.id(),s);}
	public xwriter xub(final a e,final boolean inner,final boolean escltgt){p("$").p(inner?"s":"o").p("('").p(e.id()).p("','");return new xwriter(new osjsstr(escltgt?new osltgt(os):os));}
	public xwriter xube(){return pl("');");}
	public xwriter xu(final a e)throws Throwable{e.to(xub(e,true,false));return xube();}
	public xwriter xu(final a e,final boolean escltgt)throws Throwable{e.to(xub(e,true,escltgt));return xube();}
	public xwriter xuo(final a e)throws Throwable{e.to(xub(e,false,false));return xube();}

	public xwriter xinterval(final a e,final String ax,final int ms){return p("setInterval(\"$x('").p(e.id()).p(" ").p(ax).p("')\",").p(ms).pl(");");}
	private xwriter xhide(final a e,final boolean hide){
		//? bug style block  display:inherit
		return p("$('").p(e.id()).p("').style.display='").p(hide?"none":"inline").pl("';");
	}
	public xwriter xhide(final a e){return xhide(e,true);}
	public xwriter xshow(final a e){return xhide(e,false);}
	public xwriter xalert(final String s){return p("ui.alert('").jsstr(s).pl("');");}
	public xwriter xreload(){return pl("location.reload(true);");}
	public xwriter xfocus(final a e){return p("$f('").p(e.id()).pl("');");}
	public xwriter xtitle(final String s){return p("$t('").jsstr(b.isempty(s,"")).pl("');");}
	public xwriter xp(final a e,final String s){return p("$p('").p(e.id()).p("','").jsstr(s).pl("');");}
	public xwriter tag(final String name,final a e){return tag(name,e.id());}
	public xwriter el(final a e){return tag("el",e);}
	public xwriter el(final a e,final String style){return p("<div id=").p(e.id()).p(" style=\"").p(style).p("\">");}
	public xwriter elend(){return tage("el");}
	public xwriter p(final a e)throws Throwable{if(e==null)return this;e.to(this);return this;}
	public xwriter td(final int colspan){return p("<td colspan=").p(colspan).p(">");}
	public xwriter style(final a e,final String style){return style().css(e,style).styleEnd();}
	public xwriter style(final a e,final String selector,final String style){return style().css(e,selector,style).styleEnd();}
	public xwriter style(final String selector,final String style){return style().css(selector,style).styleEnd();}
	public xwriter el(final String style){return p("<el style=\"").p(style).p("\">");}
	public xwriter td(String style,String disrecarded_param_to_overcome_overloading){return tago("td").attr("style",style).tagoe();}
	public xwriter table(final String style,final String disrecarded_param_to_overcome_overloading){
		return tago("table").attr("style",style).tagoe();
	}
	public xwriter el(final String cls,final String style){
		tago("el");
		if(cls!=null&&cls.length()>0)
			attr("class",cls);
		if(style!=null&&style.length()>0)
			attr("style",style);
		tagoe();
		return this;
	}
	public xwriter el(){return tag("el");}
	public xwriter ax(final a e){return ax(e,"","::");}
	public xwriter xed(final a e,final a axe,final String axp,final String style)throws Throwable{
		p("<el id=").p(e.id()).p(" contenteditable=true spellcheck=false ");
		if(style!=null&&style.length()>0)
			p("style=\"").p(style).p("\" ");
		p("onkeypress=\"");
		if(axe!=null){
			p("if(event.charCode==13){$x('");
			p(axe.id());
			if(axp!=null&&axp.length()>0){
				p(" ");
				p(axp);
			}
			p("');return false;}");
		}
		p("$b(this)\">");
		e.to(os);
		return p("</el>");
	}
	public xwriter xed(final a e,final a axe,final String axp)throws Throwable{return xed(e,axe,axp,null);}
	public xwriter xed(final a e)throws Throwable{return xed(e,null,null,null);}
	public xwriter css(final a e,final String selector,final String style){
		return css("#"+e.id()+" "+selector,style);
	}
	
	
	public xwriter ol(){return tag("ol");}
	public xwriter ol_(){return tage("ol");}
	public xwriter xlocation(final String uri){//? std+
		p("location='").p(uri).pl("';");
		return this;
	}
	public xwriter span(final String style){return tago("span").attr("style",style).tagoe();}
	public xwriter nbsp(){return p("&nbsp;");}
}
