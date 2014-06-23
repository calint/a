package a.ramvark;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;
import static b.b.p;
final class jdbc_connection_pool implements DataSource{
	final DataSource ds;
	final int close_intervall_in_ms;
//	final int max_connections;
	final static ConcurrentLinkedQueue<jdbc_connection_wrapper>cons=new ConcurrentLinkedQueue<jdbc_connection_wrapper>();
	jdbc_connection_pool(final DataSource d,final int close_intervall_in_ms){
		ds=d;
		this.close_intervall_in_ms=close_intervall_in_ms;
//		this.max_connections=max_connections;
	}
	@Override public Connection getConnection()throws SQLException{
		jdbc_connection_wrapper cn;
		while(true){
			cn=cons.poll();
			if(cn==null)break;
			final long t=System.currentTimeMillis();
			final long dt=t-cn.t0_in_ms;
			if(dt>close_intervall_in_ms){
				p("jdbc close ["+cons.size()+","+dt+"] "+cn);
				cn.close();
				continue;
			}
			return cn;
		}
		final Connection c1=ds.getConnection();
		final Connection c2=new jdbc_connection_wrapper(c1);
		return c2;
	}
	
	final private class jdbc_connection_wrapper implements Connection{
		private Connection cn;
		private long t0_in_ms=System.currentTimeMillis();
		jdbc_connection_wrapper(final Connection c){
			p("jdbc new "+c);
			cn=c;
		}
		@Override public PreparedStatement prepareStatement(final String sql)throws SQLException{
			return cn.prepareStatement(sql);
		}
		@Override public void close()throws SQLException{
			final long t=System.currentTimeMillis();
			final long dt=t-t0_in_ms;
			if(dt>close_intervall_in_ms){
//				System.out.println("close con "+cn+"   dt "+dt+"   conpoolsize "+cons.size());
				cn.close();
				return;
			}
			cons.add(this);
//			System.out.println("close con "+cn+"   dt "+dt+"   conpoolsize "+cons.size());
//			jdbc_connection_pool.this.cons.notify();
		}

		
		
		
		
		@Override public <T>T unwrap(Class<T>iface)throws SQLException{throw new UnsupportedOperationException();}
		@Override public boolean isWrapperFor(Class<?>iface)throws SQLException{throw new UnsupportedOperationException();}
		@Override public Statement createStatement()throws SQLException{throw new UnsupportedOperationException();}
		@Override public CallableStatement prepareCall(String sql)throws SQLException{throw new UnsupportedOperationException();}
		@Override public String nativeSQL(String sql) throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setAutoCommit(boolean autoCommit) throws SQLException {throw new UnsupportedOperationException();}
		@Override public boolean getAutoCommit() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void commit() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void rollback() throws SQLException {throw new UnsupportedOperationException();}
		@Override public boolean isClosed() throws SQLException {throw new UnsupportedOperationException();}
		@Override public DatabaseMetaData getMetaData() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setReadOnly(boolean readOnly) throws SQLException {throw new UnsupportedOperationException();}
		@Override public boolean isReadOnly() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setCatalog(String catalog) throws SQLException {throw new UnsupportedOperationException();}
		@Override public String getCatalog() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setTransactionIsolation(int level) throws SQLException {throw new UnsupportedOperationException();}
		@Override public int getTransactionIsolation() throws SQLException {throw new UnsupportedOperationException();}
		@Override public SQLWarning getWarnings() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void clearWarnings() throws SQLException {throw new UnsupportedOperationException();}
		@Override public Statement createStatement(int resultSetType,int resultSetConcurrency) throws SQLException {throw new UnsupportedOperationException();}
		@Override public PreparedStatement prepareStatement(String sql,int resultSetType, int resultSetConcurrency)throws SQLException {throw new UnsupportedOperationException();}
		@Override public CallableStatement prepareCall(String sql, int resultSetType,int resultSetConcurrency) throws SQLException {throw new UnsupportedOperationException();}
		@Override public Map<String, Class<?>> getTypeMap() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setTypeMap(Map<String, Class<?>> map) throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setHoldability(int holdability) throws SQLException {throw new UnsupportedOperationException();}
		@Override public int getHoldability() throws SQLException {throw new UnsupportedOperationException();}
		@Override public Savepoint setSavepoint() throws SQLException {throw new UnsupportedOperationException();}
		@Override public Savepoint setSavepoint(String name) throws SQLException {throw new UnsupportedOperationException();}
		@Override public void rollback(Savepoint savepoint) throws SQLException {throw new UnsupportedOperationException();}
		@Override public void releaseSavepoint(Savepoint savepoint) throws SQLException {throw new UnsupportedOperationException();}
		@Override public Statement createStatement(int resultSetType,int resultSetConcurrency, int resultSetHoldability)throws SQLException {throw new UnsupportedOperationException();}
		@Override public PreparedStatement prepareStatement(String sql,int resultSetType, int resultSetConcurrency,int resultSetHoldability)throws SQLException{throw new UnsupportedOperationException();}
		@Override public CallableStatement prepareCall(String sql, int resultSetType,int resultSetConcurrency, int resultSetHoldability)throws SQLException {throw new UnsupportedOperationException();}
		@Override public PreparedStatement prepareStatement(String sql,int autoGeneratedKeys) throws SQLException {throw new UnsupportedOperationException();}
		@Override public PreparedStatement prepareStatement(String sql,int[] columnIndexes) throws SQLException {throw new UnsupportedOperationException();}
		@Override public PreparedStatement prepareStatement(String sql,String[] columnNames) throws SQLException {throw new UnsupportedOperationException();}
		@Override public Clob createClob() throws SQLException {throw new UnsupportedOperationException();}
		@Override public Blob createBlob() throws SQLException {throw new UnsupportedOperationException();}
		@Override public NClob createNClob() throws SQLException {throw new UnsupportedOperationException();}
		@Override public SQLXML createSQLXML() throws SQLException {throw new UnsupportedOperationException();}
		@Override public boolean isValid(int timeout) throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setClientInfo(String name, String value)throws SQLClientInfoException {throw new UnsupportedOperationException();}
		@Override public void setClientInfo(Properties properties)throws SQLClientInfoException {throw new UnsupportedOperationException();}
		@Override public String getClientInfo(String name) throws SQLException {throw new UnsupportedOperationException();}
		@Override public Properties getClientInfo() throws SQLException {throw new UnsupportedOperationException();}
		@Override public Array createArrayOf(String typeName, Object[] elements)throws SQLException {throw new UnsupportedOperationException();}
		@Override public Struct createStruct(String typeName, Object[] attributes)throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setSchema(String schema) throws SQLException {throw new UnsupportedOperationException();}
		@Override public String getSchema() throws SQLException {throw new UnsupportedOperationException();}
		@Override public void abort(Executor executor) throws SQLException {throw new UnsupportedOperationException();}
		@Override public void setNetworkTimeout(Executor executor, int milliseconds)throws SQLException {throw new UnsupportedOperationException();}
		@Override public int getNetworkTimeout() throws SQLException {throw new UnsupportedOperationException();}
	}

	@Override public PrintWriter getLogWriter() throws SQLException {throw new UnsupportedOperationException();}
	@Override public void setLogWriter(PrintWriter out) throws SQLException {throw new UnsupportedOperationException();}
	@Override public void setLoginTimeout(int seconds) throws SQLException {throw new UnsupportedOperationException();}
	@Override public int getLoginTimeout() throws SQLException {throw new UnsupportedOperationException();}
	@Override public Logger getParentLogger() throws SQLFeatureNotSupportedException {throw new UnsupportedOperationException();}
	@Override public <T> T unwrap(Class<T> iface) throws SQLException {throw new UnsupportedOperationException();}
	@Override public boolean isWrapperFor(Class<?> iface) throws SQLException {throw new UnsupportedOperationException();}
	@Override public Connection getConnection(String username, String password)throws SQLException {throw new UnsupportedOperationException();}
}
