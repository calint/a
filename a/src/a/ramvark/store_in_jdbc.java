package a.ramvark;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import a.ramvark.cstore.meters;

public class store_in_jdbc implements store{
	@Override public itm create(final Class<? extends itm> cls,final itm owner)throws Throwable{
		meters.creates++;
		final itm e=cls.newInstance();
		if(owner!=null)e.pid.set(owner.did);
		final String docid=cstore.mkdocid();
		e.did.set(docid);
		return e;
	}
	@Override public void save(final itm e)throws Throwable{
		try(final Connection c=getConnection()){
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
		try(final Connection c=getConnection()){
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
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
		final StringBuilder sb=new StringBuilder();
		sb.append("select d from b where c=?");
		if(owner!=null)sb.append(" and o=?");
		if(q!=null&&q.length()>0)sb.append(" and q like ?");
		try(final Connection c=getConnection()){
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
		try(final Connection c=getConnection()){
			final PreparedStatement s=c.prepareStatement("delete from b where i=?");
			s.setString(1,did);
			s.execute();
		}
	}
	
	private boolean initiated_ensured;
	public void ensure_initiated(final Connection c)throws Throwable{
		if(initiated_ensured==true)return;
		//CREATE TABLE `b` (
//		  `c` char(64) NOT NULL DEFAULT '',
//		  `i` char(32) NOT NULL DEFAULT '',
//		  `o` char(32) NOT NULL DEFAULT '',
//		  `q` varchar(255) NOT NULL DEFAULT '',
//		  `d` blob NOT NULL
//		) ENGINE=MyISAM DEFAULT CHARSET=utf8;
//
//create user ramvark;
//grant all on b.* to 'ramvark'@'localhost' identified by 'ramvark';
		
//		final PreparedStatement s=c.prepareStatement("create table b(c char(128),i char(32),o char(32),q varchar(255),d blob)");
//		s.execute();
		initiated_ensured=true;
	}
	
	private Connection getConnection()throws Throwable{
		final Connection c=getMySqlConnection();
		ensure_initiated(c);
		return c;
	}
	public Connection getMySqlConnection() throws Exception {
		final String driver="org.gjt.mm.mysql.Driver";
		final String url="jdbc:mysql://localhost/b";
		final String username="ramvark";
		final String password="ramvark";
		Class.forName(driver);
		final Connection conn=DriverManager.getConnection(url,username,password);
		return conn;
	}
//	private Connection getHSQLConnection()throws Throwable{
//		Class.forName("org.hsqldb.jdbcDriver");
//		final String url="jdbc:hsqldb:data/ramvark";
//		return DriverManager.getConnection(url,"sa","");
//	}
//	public static Connection getOracleConnection() throws Exception {
//		final String driver="oracle.jdbc.driver.OracleDriver";
//		final String url="jdbc:oracle:thin:@localhost:1521:databaseName";
//		final String username="username";
//		final String password="password";
//		Class.forName(driver);
//		final Connection conn=DriverManager.getConnection(url,username,password);
//		return conn;
//	}
}


