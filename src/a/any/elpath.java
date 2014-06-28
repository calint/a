package a.any;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import a.any.list.el;
import a.any.list.el.visitor;
import b.path;

final public class elpath implements el{
	private el pt;
	private path pth;
	private String name_override;
	public elpath(final el parent,final path p){pt=parent;pth=p;}
	public elpath(final el parent,final path p,final String name_override){pt=parent;pth=p;this.name_override=name_override;}
	@Override public String name(){return name_override!=null?name_override:pth.name();}
	@Override public boolean isdir(){return pth.isdir();}
	@Override public boolean isfile(){return pth.isfile();}
	@Override public List<String>list(){return Arrays.<String>asList(pth.list());}
	@Override public List<String>list(final String query){
		return Arrays.<String>asList(pth.list(new FilenameFilter(){@Override public boolean accept(File dir,String name){
				return name.startsWith(query);
		}}));
	}
	@Override public el get(String name){return new elpath(this,pth.get(name));}
	@Override public long size(){return pth.size();}
	@Override public long lastmod(){return pth.lastmod();}
	@Override public String uri(){return pth.uri();}
	@Override public boolean exists(){return pth.exists();}
	@Override public void append(String cs){try{pth.append(cs);}catch(Throwable t){throw new Error(t);}}
	@Override public String fullpath(){return pth.fullpath();}
	@Override public boolean rm(){return pth.rm();}
	@Override public el parent(){return pt;}
	@Override public OutputStream outputstream(){try{return pth.outputstream();}catch(Throwable t){throw new Error(t);}}
	@Override public InputStream inputstream(){{try{return pth.inputstream();}catch(Throwable t){throw new Error(t);}}}

	@Override public boolean ommit_column_edit(){return true;}
	@Override public boolean ommit_column_lastmod(){return false;}
	@Override public boolean ommit_column_size(){return false;}
	@Override public boolean ommit_column_icon(){return false;}
	
	
	@Override public void foreach(String query,visitor v) throws Throwable{throw new UnsupportedOperationException();}

	private static final long serialVersionUID=1;
}