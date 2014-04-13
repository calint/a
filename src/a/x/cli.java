package a.x;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import b.b;
public final class cli implements Runnable{
	public static int stats_cli_actv=1;
	private Process prc;
	private PrintStream con;
	private InputStream is;
	private Thread thread=new Thread(this,cli.class.getName()+"-"+stats_cli_actv);
	private int exitValue;
	private OutputStream os;
	private String cmd;
	private StringBuilder sb=new StringBuilder();
	public cli(final String cmd,final OutputStream os) throws IOException{
		this.cmd=cmd;
		this.os=os;
		final ProcessBuilder pb=new ProcessBuilder(cmd);
		pb.redirectErrorStream(true);
//		prc=Runtime.getRuntime().exec(cmd);
		prc=pb.start();
		//? merge err and out
		is=prc.getInputStream();
		con=new PrintStream(prc.getOutputStream(),true);
		thread.start();
	}
	public final void run(){
		stats_cli_actv++;
		try{b.cp(is,os);}catch(Throwable t){b.log(t);}//?
		try{exitValue=prc.waitFor();}catch(InterruptedException ok){}
		stats_cli_actv--;
	}
	public final int getExitValue(){return exitValue;}
	public final cli wait_for_cli(){try{thread.join();}catch(InterruptedException ok){}return this;}
	public final cli p(final String sh){sb.append(sh);return this;}
	public final cli nl(){con.println(sb);b.out.print(b.q);b.out.println(sb);sb.setLength(0);return this;}
	public cli exit(){return p(";exit").nl().wait_for_cli();}
	public String toString(){return cmd;}
}
