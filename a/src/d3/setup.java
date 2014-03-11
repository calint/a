package d3;
public interface setup{
	final public static class env{
		public world wld;
		public window win;
		public player[]plrs;
	}
	public void do_setup(final applet app,final env ret)throws Throwable;
}
