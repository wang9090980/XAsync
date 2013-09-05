package com.loopj.android.common;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * HTTP代理--返回JSON
 */
public class XJSONHandler extends XBaseHandler {

	public Context mContext;

	public XJSONHandler(Context context, boolean showDialog) {
		super(context, showDialog);
		mContext = context;
	}

	protected static final int SUCCESS_JSON_MESSAGE = 100;
	protected static final int ERROR_JSON_MESSAGE = SUCCESS_JSON_MESSAGE + 1;

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

	public void onFailure(Throwable e, String errorResponse) {
		if (!TextUtils.isEmpty(errorResponse)) {
			Toast.makeText(mContext, errorResponse, Toast.LENGTH_SHORT).show();
		} else {
			if (e != null && e instanceof XException) {
				((XException) e).makeToast(mContext);
			} else {
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void sendSuccessMessage(int statusCode, Header[] headers,
			String responseBody) {

		switch (statusCode) {
		case HttpStatus.SC_OK: {
			try {
				Object jsonResponse = parseResponse(responseBody);
				sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
						statusCode, headers, jsonResponse }));
			} catch (JSONException e) {
				sendFailureMessage(XException.json(e), responseBody);
			}
		}
			break;
		case HttpStatus.SC_NO_CONTENT:
		default:
			sendMessage(obtainMessage(ERROR_JSON_MESSAGE, new Object[] {
					statusCode}));
			break;
		}
	}

	@Override
	protected void handleMessage(Message msg) {

		switch (msg.what) {
		case SUCCESS_JSON_MESSAGE: {
			Object[] response = (Object[]) msg.obj;
			int status = ((Integer) response[0]).intValue();
			handleSuccessJsonMessage(status, (Header[]) response[1],
					response[2]);
		}
			break;
		case ERROR_JSON_MESSAGE: {
			Object[] response = (Object[]) msg.obj;
			int status = ((Integer) response[0]).intValue();
			if(status == HttpStatus.SC_NO_CONTENT){
				handleFailureMessage(XException.http(status), "服务器响应成功，但未返回数据");
			}else{
				handleFailureMessage(XException.http(status), null);
			}
		}
			break;
		default:
			super.handleMessage(msg);
		}
	}

	protected void handleSuccessJsonMessage(int statusCode, Header[] headers,
			Object jsonResponse) {
		if (statusCode == HttpStatus.SC_OK) {
			if (jsonResponse instanceof JSONObject) {
				onSuccess(statusCode, headers, (JSONObject) jsonResponse);
			} else if (jsonResponse instanceof JSONArray) {
				onSuccess(statusCode, headers, (JSONArray) jsonResponse);
			} else {
				onFailure(XException.json(new Exception("json parser error")),
						null);
			}
		} else {
			onFailure(XException.http(statusCode), null);
		}
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		onFailure(e, responseBody);
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
