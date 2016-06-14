package com.guang.client.protocol;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.guang.client.mode.GUser;

/**
 * �Զ���Э�� ����json
 * 
 * @author guang
 * 
 */

public class GProtocol {
	public static final String TAG = "GProtocol";
	public static final int MODE_TEST = -1;
	public static final String PROTOCOL_CLASS_HEAD = "com.guang.client.protocol.";
	public static final String PROTOCOL_CLASS_SESSION = "org.apache.mina.core.session.IoSession";
	public static final String PROTOCOL_CLASS_STRING = "java.lang.String";

	public static final String MODE_USER_VALIDATE = "GModeUser_validate";
	public static final String MODE_USER_LOGIN = "GModeUser_login";
	public static final String MODE_USER_REGISTER = "GModeUser_register";
	public static final String MODE_USER_HEART_BEAT = "GModeUser_heartBeat";

	public static void parse(IoSession session, Object message) {
		try {			
			JSONObject data = new JSONObject(message.toString());
			String[] mode = data.getString("mode").split("_");
			String className = mode[0];
			String methodName = mode[1];
			Class<?> c = Class.forName(PROTOCOL_CLASS_HEAD + className);
			Class<?> args[] = new Class[] {
					Class.forName(PROTOCOL_CLASS_SESSION),
					Class.forName(PROTOCOL_CLASS_STRING) };
			Method m = c.getMethod(methodName, args);
			m.invoke(c, session, data.getString("body"));
		} catch (Exception e) {
			Log.e(TAG,"���ݽ���ʧ�ܣ�" + e.getMessage());
		}
	}

	
	
}