package a.x;
public final class signal extends RuntimeException{
	static final long serialVersionUID=1;
	private int sig;
	signal(final int sig){this.sig=sig;}
	public boolean isEos(){return sig==1;}
	public static void eos(){throw new signal(1);}
	public String toString(){return "sig"+sig;}
}
