package a.ramvark;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import static b.b.p;
import b.b;
public class store_in_jdbc_mysql extends store_in_jdbc{
	public int connection_close_intervall_in_ms=10_000;
	private DataSource ds;
	private boolean initiated;
	private static boolean use_connection_pool=true;
	@Override protected Connection connection()throws Throwable{
		final Connection cn;
		if(!initiated){
			final DataSource d;
			final String dbname="RAMVARK";
			final String url,username,password;
			if(b.cloud_bees==true){
				url="jdbc:"+System.getProperty("DATABASE_URL_"+dbname);
				username=System.getProperty("DATABASE_USERNAME_"+dbname);
				password=System.getProperty("DATABASE_PASSWORD_"+dbname);
			}else{
				url="jdbc:mysql://localhost/b";
				username="ramvark";
				password="ramvark";
			}
			p("jdbc info "+url+" "+username+" "+password);
			final MysqlDataSource mds=new MysqlDataSource();
			mds.setURL(url);
			mds.setUser(username);
			mds.setPassword(password);
			d=mds;
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
