package a.t3;
import java.util.HashMap;
import java.util.Map;

import b.a;
import b.xwriter;
public class drive extends a{
	static final long serialVersionUID=1;
	public Map<String,byte[]>files=new HashMap<>();
	{files.put("index",new byte[10]);}
	{files.put("/keyword1",new byte[20]);}
	public drive(system c,String nm){
		super(c,nm);
	}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null)x.title("virtual 128 terra byte file");
		x.p("[");
		files.entrySet().forEach((Map.Entry me)->{x.p(me.getKey().toString()).spc().p(((byte[])me.getValue()).length).spc(2);});
		x.p("]");
	}
}
