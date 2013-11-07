package d2;
import java.util.LinkedList;
final class grid{
	private LinkedList<object>objs=new LinkedList<object>();
	public void add(final object o){objs.add(o);}
	void clear(){objs.clear();}
	public void detectcollision(){
		for(int i=objs.size()-1;i>0;i--){
			final object o1=(object)objs.get(i);
			for(int j=i-1;j>=0;j--){
				final object o2=(object)objs.get(j);
				final boolean o1y=o1.interestedOfCollisionWith(o2);
				final boolean o2y=o2.interestedOfCollisionWith(o1);
				if(o1y||o2y){
					final boolean col=o1.collisioncheck(o2);
					if(col){
						if(o1y)o1.addCollision(o2);
						if(o2y)o2.addCollision(o1);
					}
				}
			}
		}
	}
}
