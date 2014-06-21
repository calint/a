package a.ramvark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class store_in_jdbc_mysql extends store_in_jdbc{
	@Override protected Connection connection()throws Throwable{
		if(b.b.cloud_bees==true){
			final Context ctx=new InitialContext();
			final DataSource ds=(DataSource)ctx.lookup("java:comp/env/jdbc/ramvark");
			final Connection conn=ds.getConnection();
			ensure_initiated(conn);
			return conn;
		}

		final String driver="org.gjt.mm.mysql.Driver";
		final String url="jdbc:mysql://localhost/b";
		final String username="ramvark";
		final String password="ramvark";
		
		Class.forName(driver);
		final Connection conn=DriverManager.getConnection(url,username,password);
		ensure_initiated(conn);
		return conn;
	}
	private boolean initiated_ensured;
	private void ensure_initiated(final Connection c)throws Throwable{
		if(initiated_ensured==true)return;
//		final Context ctx=new InitialContext();
//		NamingEnumeration<NameClassPair> list = ctx.list("");
//		while (list.hasMore()) {
//		  System.out.println(list.next().getName());
//		}
		final PreparedStatement s=c.prepareStatement("CREATE TABLE `b` (`c` char(64),`i` char(32),`o` char(32),`q` varchar(255),`d` blob) ENGINE=MyISAM DEFAULT CHARSET=utf8;");
		try{s.execute();}catch(Throwable ignored){}
//create user ramvark;
//grant all on b.* to 'ramvark'@'localhost' identified by 'ramvark';
		initiated_ensured=true;
	}

}
