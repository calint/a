package a.pz;
final public class core{
	final public int nregs=16,ncalls=8,nloops=8;

	public boolean running,stopped,wait,notify,last_instruction_was_end_of_frame;
	public int pc,ir,zn,loadreg=-1;
	public int[]regs=new int[nregs];
	public int[]calls=new int[ncalls];
	public int call_i;
	public int[]loops=new int[nloops*2];
	public int loops_i;
}