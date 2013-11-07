package d3;
public interface setup{
	public static class env{
		public world wld;
		public window win;
		public player[]plrs;
	}
	public void do_setup(applet app,env ret)throws Throwable;
}
