package a.ramvark;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class store_in_jdbc_mysql extends store_in_jdbc{
	private DataSource ds;
	private boolean initiated;
	public static int connection_close_intervall_in_ms=10_000;
	public static boolean use_connection_pool=true;
	@Override protected Connection connection()throws Throwable{
		final Connection cn;
		if(!initiated){
			final DataSource d;
			if(b.b.cloud_bees==true){
				final Context ctx=new InitialContext();
	//			NamingEnumeration<NameClassPair> list = ctx.list("");
	//			while (list.hasMore()) {
	//			  System.out.println(list.next().getName());
	//			}
				d=(DataSource)ctx.lookup("java:comp/env/jdbc/ramvark");
			}else{
				final MysqlDataSource mds=new MysqlDataSource();
				mds.setURL("jdbc:mysql://localhost/b");
				mds.setUser("ramvark");
				mds.setPassword("ramvark");
				d=mds;
			}
			if(use_connection_pool){
				final jdbc_connection_pool jcp=new jdbc_connection_pool(d,connection_close_intervall_in_ms);
				ds=jcp;
			}else
				ds=d;
			cn=ds.getConnection();
			final PreparedStatement s=cn.prepareStatement("CREATE TABLE b(c char(64),i char(32),o char(32),q varchar(255),d blob)ENGINE=MyISAM DEFAULT CHARSET=utf8;");
			try{s.execute();}catch(Throwable ignored){}
			//create user ramvark;
			//grant all on b.* to 'ramvark'@'localhost' identified by 'ramvark';
			initiated=true;
		}else
			cn=ds.getConnection();
		return cn;
	}
}
