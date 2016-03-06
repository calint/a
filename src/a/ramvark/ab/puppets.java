package a.ramvark.ab;
import a.ramvark.itm;
import a.ramvark.ls;
import a.ramvark.lst;
import b.xwriter;
public @ls(cls=puppet.class)class puppets extends lst{
	static final long serialVersionUID=1;
	protected void rendlisthead(final xwriter x){
		x.nl().style();
		x.css("table.tb","box-shadow:0 0 .5em rgba(0,0,0,1)");
		x.css("table.tb tr:hover,tr:active,tr:focus","background:#fff");
		x.css("table tr:nth-of-type(odd)","background-color:#fff");
		x.css("table.tb tr th","padding:.5em 1em .4em 1em;background:#fff;border:1px solid #ddd");//;white-space:nowrap
//		x.css("table.tb tr th:first-child","border-left:0");//;white-space:nowrap
		x.css("table.tb tr td","padding:.5em 1em .5em 1em;border:1px dotted #ddd");//;white-space:nowrap
//		x.css("table.tb tr td:first-child","");
//		x.css("table.tb tr td:last-child","background:yellow;width:100%");
		x.style_();
		x.nl().table("tb");
		x.nl().tr().th().p("name").th().p("email");
//		x.th().p("mother").th().p("father");
		x.th();
	}
	protected void rendrow(final xwriter x,final itm ee)throws Throwable{
		final puppet e=(puppet)ee;
		x.nl().tr();
		x.td();
		if(!e.colr.isempty())x.p("<span style=\"border-bottom:3px dotted "+e.colr+"\">");
		rendldax(x,ee);
		if(!e.colr.isempty())x.span_();
		x.td().r(e.email);
//		x.td().p(e.mother);
//		x.td().p(e.father);		
		x.td();
		renddelax(x,ee, "a/x");
	}
	protected void rendlistfoot(final xwriter x){
		x.nl().table_();
	}
}
