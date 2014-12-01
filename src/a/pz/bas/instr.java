package a.pz.bas;

import b.xwriter;

public class instr extends stmt{
	int znxr;
	int op;
	String ra;
	String rd;
	int rai;
	int rdi;
	//		int imm;
	public instr(program p){
		super(p);
	}
	public instr(final program p,final int znxr,final int op,final String rd){
		this(p,znxr,op,null,rd);
	}
	public instr(final program p,final int znxr,final int op,final String ra,final String rd){
		super(p);
		this.znxr=znxr;
		this.op=op;
		this.ra=ra;
		this.rd=rd;
		rai=ra==null?0:p.register_index_from_string(ra);
		rdi=rd==null?0:p.register_index_from_string(rd);
	}
	public instr(final program p,final int znxr,final int op,final String ra,final String rd,boolean fliprdra){
		this(p,znxr,op,ra,rd);
		if(fliprdra){
			final int i=rai;
			rai=rdi;
			rdi=i;
		}
	}
	void mkstr(){
		final xwriter x=new xwriter();
		if((znxr&3)==3)
			x.p("ifp ");
		else if((znxr&1)==1)
			x.p("ifz ");
		else if((znxr&2)==2)
			x.p("ifn ");
		x.p(txt);
		if((znxr&4)==4)
			x.p(" nxt");
		if((znxr&8)==8)
			x.p(" ret");
		txt=x.toString();
	}
	public int znxr_ci__ra__rd__(){
		return znxr|op|((rai&15)<<8)|((rdi&15)<<12);
	}
	public void compile(program r){
		bin=new int[]{znxr_ci__ra__rd__()};
	}
	private static final long serialVersionUID=1;
}