package a.ca;

import b.a;
import b.xwriter;

public class $ extends a {
	public a cap;
	public a c;
	@Override public void to(xwriter x) throws Throwable {
		x.style("html","padding-left:4em;padding-top:1em");
		x.style(cap,"border:1px dotted green;width:40em;height:20em");
		x.style(c,"display:block;border:1px dotted blue;width:40em;height:20em");
		x.pl("cap source");
		x.inputTextArea(cap).nl().ax(this).pl(" compile to c").nl();
		x.el(c);
	}
	synchronized public void x_(xwriter y,String a){
		final xwriter x=y.xub(c,true,true);
		x.pl("#include\"cap.h\"");
		x.pl("int cap_main(int argc,const char**arg){");
		x.pl("	while(argc--)puts(*arg++);");
//
//		    file*f=file_new(rom,sizeof(rom)-1);
//		    pl("files %lu",file_count);
//		    file_info(f,stdout);nl();
//		    file_to(f,stdout);nl();
//		    __block char chh='.';
//		    file_foreach_char_write(f,^(char*ch){
//		        putchar(*ch);
//		        *ch='x';
//		        putchar(chh++);
//			});nl();
//		    file_to(f,stdout);nl();
//		    file_copy(f,"another",3);
//		    file_to(f,stdout);nl();
////		    vfunc(f,info, stdout);
//		    file_recycle(f);
//		    pl("files %lu",file_count);
//
//		    return 0;
		x.pl("}");
		y.xube();
	}
}
