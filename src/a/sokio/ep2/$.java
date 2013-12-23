package a.sokio.ep2;
import a.sokio.*;
import b.*;
final public class ${
	public static class hallway extends anyplace{static final long serialVersionUID=1;{
		name="hallway";
		description="u r in the hallway of departments";
		places_add(new guns());
		places_add(new health());
		places_add(new treasury());
		places_add(new pathplace(b.path()));
	}}
	public static class guns extends anyplace{static final long serialVersionUID=1;{}}
	public static class health extends anyplace{static final long serialVersionUID=1;{}}
	public static class treasury extends anyplace{static final long serialVersionUID=1;{
		description="u r in the chamber of echos\n formerly known as treasury";
		things_add(new dust());
		things_add(new shoebox());
	}}
	public static class dust extends thing{static final long serialVersionUID=1;{
		things_add(new foot_steps());
	}}
	public static class foot_steps extends thing{static final long serialVersionUID=1;{}}
	public static class shoebox extends thing{static final long serialVersionUID=1;{
		aan="a";
		name="shoe box";
		description="u c numerous iou notes";
	}}
}
