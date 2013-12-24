package a.sokio;
import static b.b.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
class splace extends an implements place{
		static final long serialVersionUID=1;
		private List<place>exits;
		private List<thing>things;
		transient private List<$>sokios;
//		public List<place>exits(){return exits;}
		public void places_foreach(final place.placevisitor v)throws Throwable{
			if(exits==null)return;
			for(final place p:exits)
				if(!v.visit(p))break;
		}
		public void things_foreach(final place.thingvisitor v)throws Throwable{
			if(things==null)return;
			for(final thing p:things)
				if(!v.visit(p))break;
		}
		public void sokios_foreach(final place.sokiovisitor v)throws Throwable{
			if(sokios==null)return;
			for(final $ s:sokios)
				if(!v.visit(s))break;
		}
		public int things_size(){if(things==null)return 0;return things.size();}
		public void things_remove(final thing o){if(things==null)return;things.remove(o);}
		public int sokios_size(){return sokios==null?0:sokios.size();}
		public void things_add(final thing o){
			if(o.place!=null)
				o.place.things.remove(o);
			o.place=this;
			if(things==null)things=Collections.synchronizedList(new LinkedList<thing>());
			things.add(o);
		}
		public void sokios_add(final $ s){
			if(sokios==null)sokios=Collections.synchronizedList(new LinkedList<$>());
			sokios.add(s);
		}
		public void sokios_recv(final String msg,final $ exclude){
			if(sokios==null)return;
			for(final $ e:sokios){
				if(e==exclude)continue;
				try{e.so_write(ByteBuffer.wrap(tobytes("\n"+msg+"\n")));}
				catch(final IOException ex){
					if("Broken pipe".equals(ex.getMessage())){
						sokios.remove(e);
						e.so_close();
						return;
					}
					if(ex instanceof ClosedChannelException){
						sokios.remove(e);
						e.so_close();
						return;
					}
					throw new Error(ex);
				}
				catch(Throwable t){throw new Error(t);}
			}		
		}
		public thing things_get(final String qry){
			if(things==null)return null;
			for(final thing e:things){
				if(e.toString().startsWith(qry)){
					return e;
				}
			}
			return null;
		}
		public place places_get(final String qry){
			if(exits==null)return null;
			for(final place e:exits){
				if(e.toString().startsWith(qry)){
					return e;
				}
			}
			return null;
		}
		public void places_add(final place o){
			if(exits==null)
				exits=Collections.synchronizedList(new LinkedList<place>());
			exits.add(o);
		}
		public void sokios_remove(final $ s){
			if(sokios==null)return;
			sokios.remove(s);
		}
		public boolean things_isempty(){
			if(things==null)return true;
			return things.isEmpty();
		}
		public place places_enter(final $ so,final String qry){
			place dest=places_get(qry);
			if(dest==null)dest=things_get(qry);
			if(dest==null)return null;
			so.moveto(dest);
//			so.place().sokios_remove(so);
//			so.place().sokios_recv(name+" departed to "+dest,so);
//			dest.sokios_recv(name+" arrived from "+so.place(),so);
//			dest.sokios_add(so);
//			so.path_push(dest);
			return dest;
		}
	}