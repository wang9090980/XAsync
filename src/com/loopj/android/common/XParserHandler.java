package com.loopj.android.common;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

/**
 * HTTP代理--返回解析后对象
 * 
 * @param <T>
 */
public class XParserHandler<T> extends XBaseHandler {
	private final Class<T> mClazz;

	public XParserHandler(Context context, Class<T> clazz, boolean showDialog) {
		super(context, showDialog);
		mClazz = clazz;
	}

	protected static final int SUCCESS_PARSER_MESSAGE = 100;

	public void onSuccess(T response) {
	}

	public void onSuccess(int statusCode, Header[] headers, T response) {
		onSuccess(statusCode, response);
	}

	public void onSuccess(int statusCode, T response) {
		onSuccess(response);
	}
	
	public void onFailure(Throwable error, String content) {
		
	}

	@Override
	protected void sendSuccessMessage(int statusCode, Header[] headers,
			String responseBody) {
		if (statusCode != HttpStatus.SC_NO_CONTENT) {
			try {
				T jsonResponse = parseResponse(responseBody);
				sendMessage(obtainMessage(SUCCESS_PARSER_MESSAGE, new Object[] {
						statusCode, headers, jsonResponse }));
			} catch (JSONException e) {
				sendFailureMessage(e, responseBody);
			}
		} else {
			sendMessage(obtainMessage(SUCCESS_PARSER_MESSAGE, new Object[] {
					statusCode, null }));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_PARSER_MESSAGE:
			Object[] response = (Object[]) msg.obj;
			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
					(Header[]) response[1], (T) response[2]);
			break;
		default:
			super.handleMessage(msg);
		}
	}

	@SuppressWarnings("null")
	protected void handleSuccessJsonMessage(int statusCode, Header[] headers,
			T jsonResponse) {
		if (jsonResponse != null) {
			onSuccess(statusCode, headers, jsonResponse);
		} else {
			onFailure(new JSONException("Unexpected type "
					+ jsonResponse.getClass().getName()), null);
		}
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		onFailure(e, responseBody);
	}

	/**
	 * 解析数据
	 * 
	 * @param responseBody
	 * @return
	 * @throws JSONException
	 */
	protected T parseResponse(String responseBody) throws JSONException {
		responseBody = responseBody.trim();
		return JSON.parseObject(responseBody, mClazz);
	}

}
