package a.ramvark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class store_in_jdbc_mysql extends store_in_jdbc{
	private DataSource ds;
	private boolean initiated;
	@Override protected Connection connection()throws Throwable{
		final Connection conn;
		if(!initiated){
			if(b.b.cloud_bees==true){
				final Context ctx=new InitialContext();
	//			NamingEnumeration<NameClassPair> list = ctx.list("");
	//			while (list.hasMore()) {
	//			  System.out.println(list.next().getName());
	//			}
				ds=(DataSource)ctx.lookup("java:comp/env/jdbc/ramvark");
			}else{
				final MysqlDataSource mds=new MysqlDataSource();
				mds.setURL("jdbc:mysql://localhost/b");
				mds.setUser("ramvark");
				mds.setPassword("ramvark");
				ds=mds;
			}
			conn=ds.getConnection();
			final PreparedStatement s=conn.prepareStatement("CREATE TABLE `b` (`c` char(64),`i` char(32),`o` char(32),`q` varchar(255),`d` blob) ENGINE=MyISAM DEFAULT CHARSET=utf8;");
			try{s.execute();}catch(Throwable ignored){}
			//create user ramvark;
			//grant all on b.* to 'ramvark'@'localhost' identified by 'ramvark';
			initiated=true;
		}else
			conn=ds.getConnection();
		return conn;
	}
}
