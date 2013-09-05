package com.loopj.android.common;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

/**
 * HTTP代理--返回解析后对象
 * 
 * @param <T>
 */
public class XParserHandler<T> extends XBaseHandler {
	private final Class<T> mClazz;
	private static final int JSON_OBJECT = 0;
	private static final int JSON_ARRAY = 1;

	public XParserHandler(Context context, Class<T> clazz, boolean showDialog) {
		super(context, showDialog);
		mClazz = clazz;
	}

	protected static final int SUCCESS_PARSER_MESSAGE = 1000;
	protected static final int ERROR_PARSER_MESSAGE = SUCCESS_PARSER_MESSAGE + 1;

	public void onSuccess(T response) {

	}

	public void onSuccess(int statusCode, Header[] headers, T response) {
		onSuccess(statusCode, response);
	}

	public void onSuccess(int statusCode, T response) {
		onSuccess(response);
	}

	public void onSuccess(List<T> response) {

	}

	public void onSuccess(int statusCode, Header[] headers, List<T> response) {
		onSuccess(statusCode, response);
	}

	public void onSuccess(int statusCode, List<T> response) {
		onSuccess(response);
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
				if (responseBody.startsWith("{")) {
					T jsonResponse = parseResponseObject(responseBody);
					sendMessage(obtainMessage(SUCCESS_PARSER_MESSAGE,
							new Object[] { statusCode, headers, jsonResponse,
									JSON_OBJECT }));
				} else if (responseBody.startsWith("[")) {
					List<T> jsonResponse = parseResponseArray(responseBody);
					sendMessage(obtainMessage(SUCCESS_PARSER_MESSAGE,
							new Object[] { statusCode, headers, jsonResponse,
									JSON_ARRAY }));
				} else {
					sendMessage(obtainMessage(SUCCESS_PARSER_MESSAGE,
							new Object[] { statusCode,null,null,null}));
				}
			} catch (JSONException e) {
				sendFailureMessage(e, responseBody);
			}
		}
			break;
		case HttpStatus.SC_NO_CONTENT:
		default:
			sendMessage(obtainMessage(ERROR_PARSER_MESSAGE,
					new Object[] { statusCode }));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_PARSER_MESSAGE: {
			Object[] response = (Object[]) msg.obj;
			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
					(Header[]) response[1], response[2], response[3]);
		}
			break;
		case ERROR_PARSER_MESSAGE: {
			Object[] response = (Object[]) msg.obj;
			int status = ((Integer) response[0]).intValue();
			if (status == HttpStatus.SC_NO_CONTENT) {
				handleFailureMessage(XException.http(status), "服务器响应成功，但未返回数据");
			} else {
				handleFailureMessage(XException.http(status), null);
			}
		}
		default: {
			super.handleMessage(msg);
		}
		}
	}

	@SuppressWarnings("unchecked")
	protected void handleSuccessJsonMessage(int statusCode, Header[] headers,
			Object jsonResponse, Object flag) {
		if (jsonResponse != null) {
			switch (Integer.parseInt(flag.toString())) {
			case JSON_OBJECT:
				onSuccess(statusCode, headers, (T) jsonResponse);
				break;
			case JSON_ARRAY:
				onSuccess(statusCode, headers, (List<T>) jsonResponse);
				break;
			default:
				onFailure(XException.json(new Exception("数据解析异常")), null);
				break;
			}
		} else {
			onFailure(XException.json(new Exception("数据返回异常")), null);
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
	protected T parseResponseObject(String responseBody) throws JSONException {
		responseBody = responseBody.trim();
		return JSON.parseObject(responseBody, mClazz);
	}

	/**
	 * 解析数据
	 * 
	 * @param responseBody
	 * @return
	 * @throws JSONException
	 */
	protected List<T> parseResponseArray(String responseBody)
			throws JSONException {
		responseBody = responseBody.trim();
		return JSON.parseArray(responseBody, mClazz);
	}

}
