package com.guang.client.handler;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.util.Log;

import com.guang.client.GuangClient;
import com.guang.client.controller.GUserController;
import com.guang.client.protocol.GProtocol;

public class GCoreHandler implements IoHandler{
	private static boolean isclosed = true;
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
	}

	@Override
	public void inputClosed(IoSession arg0) throws Exception {
		if(isclosed)
		{
			isclosed = false;
			Log.e("====reConnec===", "inputClosed");
			GuangClient.getSession().close(true);
		}
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		GProtocol.parse(session, message);
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Log.e("=======", "sessionClosed");
		GuangClient.reConnec();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus arg1) throws Exception {
		GUserController.getInstance().sendHeartBeat(session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		Log.e("=======", "sessionOpened");
		GUserController.getInstance().login(session);
		isclosed = true;		
	}

}
