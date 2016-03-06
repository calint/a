package a.ramvark;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import a.ramvark.cstore.meters;

abstract public class store_in_jdbc implements store{
	@Override public itm create(final Class<? extends itm>cls,final itm owner)throws Throwable{return cstore.mk(cls,owner);}
	@Override public void save(final itm e)throws Throwable{
		try(final Connection c=connection()){
			if(e.notnew==true){
				final PreparedStatement s=c.prepareStatement("update b set d=? where i=?");
				final ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
				e.save(baos);
				s.setBytes(1,baos.toByteArray()); 
				s.setString(2,e.did.toString());
				s.executeUpdate();
//				c.commit();
				return;
			}
			final PreparedStatement s=c.prepareStatement("insert into b(c,i,o,q,d) values(?,?,?,?,?)");				
			s.setString(1,e.getClass().getName());
			s.setString(2,e.did.toString());
			s.setString(3,e.pid.toString());
			s.setString(4,e.toString());
			final ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
			e.save(baos);
			s.setBytes(5,baos.toByteArray()); 
			s.executeUpdate();
//			c.commit();
			e.notnew=true;
		}
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		try(final Connection c=connection()){
			final PreparedStatement s=c.prepareStatement("select d from b where i=?");
			s.setString(1,did);
			final ResultSet r=s.executeQuery();
			itm e;
			if(r.next()){
				final InputStream is=r.getBinaryStream(1);
				e=cls.newInstance();
				e.load(is);
			}else
				e=null;
			return e;
		}
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final visitor v)throws Throwable{
		meters.foreaches++;
		final StringBuilder sb=new StringBuilder();
		sb.append("select d from b where c=?");
		if(owner!=null)sb.append(" and o=?");
		if(q!=null&&q.length()>0)sb.append(" and q like ?");
		try(final Connection c=connection()){
			final PreparedStatement s=c.prepareStatement(sb.toString());
			s.setString(1,cls.getName());
			int i=2;
			if(owner!=null)s.setString(i++,owner.did.toString());
			if(q!=null&&q.length()>0)s.setString(i,q+"%");
			final ResultSet r=s.executeQuery();
			itm e;
			while(r.next()){
				e=cls.newInstance();
				try(final InputStream is=r.getBinaryStream(1)){e.load(is);}
				v.visit(e);
			}
		}
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
		meters.deletes++;
		try(final Connection c=connection()){
			final PreparedStatement s=c.prepareStatement("delete from b where i=?");
			s.setString(1,did);
			s.execute();
		}
	}
	
	abstract protected Connection connection()throws Throwable;
}


