package a.ramvark;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class store_in_jdbc implements store{
	@Override public itm create(final Class<? extends itm> cls,final itm owner)throws Throwable{
		return null;
	}
	@Override public void save(final itm e)throws Throwable{
		final Connection conn=getHSQLConnection();
		PreparedStatement pstmt = conn.prepareStatement("update blob_table set blob = ? where id = ?");
		File blob = new File("/path/to/picture.png");
		FileInputStream in = new FileInputStream(blob);

		// the cast to int is necessary because with JDBC 4 there is 
		// also a version of this method with a (int, long) 
		// but that is not implemented by Oracle
		pstmt.setBinaryStream(1, in, (int)blob.length()); 

		pstmt.setInt(2, 42);  // set the PK value
		pstmt.executeUpdate();
		conn.commit();
	}
	@Override public itm load(final Class<? extends itm>cls,final String did)throws Throwable{
		final Connection conn=getHSQLConnection();
		final String query="select d from b where i='"+did+"'";//? sqlinjection
		final Statement stmt=conn.createStatement();//? preparedquery
		final ResultSet rs=stmt.executeQuery(query);
		itm e;
		if(rs.next()){
			final byte[]bytes=rs.getBytes(1);
			e=null;
		}else{
			e=null;
		}
		rs.close();
		stmt.close();
		conn.close();
		return e;
	}
	@Override public void foreach(final Class<? extends itm>cls,final itm owner,final String q,final store.visitor v)throws Throwable{
	}
	@Override public void delete(final Class<? extends itm>cls,final String did)throws Throwable{
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
