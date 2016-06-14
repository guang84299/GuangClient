package com.guang.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.guang.client.handler.GCoreHandler;

import android.content.Context;
import android.util.Log;

public class GuangClient {
	private static final String LOG = "GuangClient";
	private final static String HOST = GCommon.SERVER_IP;
	private final static int PORT = 9123;
	private static IoConnector connector;
	private static IoSession session;
	private static Context context;

	public GuangClient() {
		connector = new NioSocketConnector();
	}

	public static IoConnector getConnector() {
		if (connector == null) {
			Log.e(LOG, "客户端还未启动...");
		}
		return connector;
	}
	
	public static IoSession getSession()
	{
		return session;
	}
	
	public static Context getContext()
	{
		return context;
	}
	
	public void setContext(Context context)
	{
		this.context = context;
	}

	public void start() {
		// 设置链接超时时间
		connector.setConnectTimeoutMillis(30000);
		// 添加过滤器
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		connector.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 15 );
		// 添加业务逻辑处理器类
		connector.setHandler(new GCoreHandler());
		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(
					HOST, PORT));// 创建连接
			future.awaitUninterruptibly();// 等待连接创建完成
			session = future.getSession();// 获得session
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(LOG, "客户端连接失败！"+e.getMessage());
			reConnec();
		}
	}
	
	public static void reConnec()
	{
		new Thread(){		
			public void run() {
				while(session == null || (session != null && !session.isConnected()))
				{
					try {
						ConnectFuture future = connector.connect(new InetSocketAddress(
								HOST, PORT));// 创建连接
						future.awaitUninterruptibly();// 等待连接创建完成
						session = future.getSession();// 获得session						
					} catch (Exception e) {
						Log.e(LOG, "客户端重连失败！"+e.getMessage());
					}	
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};			
		}.start();
		
	}
}
