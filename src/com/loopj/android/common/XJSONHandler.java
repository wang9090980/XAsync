package com.loopj.android.common;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * HTTP代理--返回JSON
 */
public class XJSONHandler extends XBaseHandler {

	public XJSONHandler(Context context, boolean showDialog) {
		super(context, showDialog);

	}

	protected static final int SUCCESS_JSON_MESSAGE = 100;

	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		onSuccess(statusCode, response);
	}

	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		onSuccess(statusCode, response);
	}

	public void onSuccess(int statusCode, JSONArray response) {
		onSuccess(response);
	}
	
	public void onSuccess(int statusCode, JSONObject response) {
		onSuccess(response);
	}
	
	public void onSuccess(JSONObject response) {
	}

	public void onSuccess(JSONArray response) {
	}

	public void onFailure(Throwable e, JSONObject errorResponse) {
	}

	public void onFailure(Throwable e, JSONArray errorResponse) {
	}

	@Override
	protected void sendSuccessMessage(int statusCode, Header[] headers,
			String responseBody) {
		if (statusCode != HttpStatus.SC_NO_CONTENT) {
			try {
				Object jsonResponse = parseResponse(responseBody);
				sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
						statusCode, headers, jsonResponse }));
			} catch (JSONException e) {
				sendFailureMessage(e, responseBody);
			}
		} else {
			sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
					statusCode, new JSONObject() }));
		}
	}

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_JSON_MESSAGE:
			Object[] response = (Object[]) msg.obj;
			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
					(Header[]) response[1], response[2]);
			break;
		default:
			super.handleMessage(msg);
		}
	}

	protected void handleSuccessJsonMessage(int statusCode, Header[] headers,
			Object jsonResponse) {
		if (jsonResponse instanceof JSONObject) {
			onSuccess(statusCode, headers, (JSONObject) jsonResponse);
		} else if (jsonResponse instanceof JSONArray) {
			onSuccess(statusCode, headers, (JSONArray) jsonResponse);
		} else {
			onFailure(new JSONException("Unexpected type "
					+ jsonResponse.getClass().getName()), (JSONObject) null);
		}
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		try {
			if (responseBody != null) {
				Object jsonResponse = parseResponse(responseBody);
				if (jsonResponse instanceof JSONObject) {
					onFailure(e, (JSONObject) jsonResponse);
				} else if (jsonResponse instanceof JSONArray) {
					onFailure(e, (JSONArray) jsonResponse);
				} else {
					onFailure(e, responseBody);
				}
			} else {
				onFailure(e, "");
			}
		} catch (JSONException ex) {
			onFailure(e, responseBody);
		}
	}
	
	/**
	 * 
	 * @param responseBody
	 * @return
	 * @throws JSONException
	 */
	private Object parseResponse(String responseBody) throws JSONException {
		Object result = null;

		responseBody = responseBody.trim();
		if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
			result = JSON.parse(responseBody);
		}
		if (result == null) {
			result = responseBody;
		}
		return result;
	}

}
