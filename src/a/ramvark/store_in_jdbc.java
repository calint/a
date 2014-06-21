package a.ramvark;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
			final PreparedStatement s=c.prepareStatement("update b set d=? where i=?");
			final ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
			e.save(baos);
			s.setBytes(1,baos.toByteArray()); 
			s.setString(2,e.did.toString());
			s.executeUpdate();
			c.commit();
		}
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		try(final Connection c=getConnection()){
			final String q="select d from b where i='"+did+"'";//? sqlinjection
			final Statement s=c.createStatement();//? preparedquery
			final ResultSet r=s.executeQuery(q);
			itm e;
			if(r.next()){
				final InputStream is=r.getBinaryStream(1);
				e=cls.newInstance();
				e.load(is);
	//			final byte[]bytes=r.getBytes(1);
	//			e=null;
			}else{
				e=null;
			}
			return e;
		}
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
	}
	
	  private static Connection getConnection() throws Exception {
		  return getHSQLConnection();
	  }
  private static Connection getHSQLConnection() throws Exception {
    Class.forName("org.hsqldb.jdbcDriver");
    System.out.println("Driver Loaded.");
    String url = "jdbc:hsqldb:data/tutorial";
    return DriverManager.getConnection(url, "sa", "");
  }

  public static Connection getMySqlConnection() throws Exception {
    String driver = "org.gjt.mm.mysql.Driver";
    String url = "jdbc:mysql://localhost/demo2s";
    String username = "oost";
    String password = "oost";

    Class.forName(driver);
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }

  public static Connection getOracleConnection() throws Exception {
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@localhost:1521:databaseName";
    String username = "username";
    String password = "password";

    Class.forName(driver); // load Oracle driver
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }

}
