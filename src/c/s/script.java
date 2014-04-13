package c.s;import c.*;import static c.c.*;final public class script{{try{
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////









										final client c=new client(host,port);
										switch(run){
										case"get":
											if(pcontent)c.get(uri,System.out::write,c::close);
											else c.get(uri,null,c::close);
											break;
										case"websock":
											c.websock(uri,()->//connect to websock then
												/**/c.recv("hello",(len,bb)->{//send and process reply
												/**/	c.pl(tostr(bb,len));
												/**/	c.recv(".x",(len2,bb2)->{
												/**/		c.pl(tostr(bb2,len2));
												/**/		c.close();
												/**/	});
												/**/})
												);
											break;
										default:throw new Error();
										}














































}catch(final Throwable t){throw new Error(t);}}}
