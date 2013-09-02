package com.loopj.android.common;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.common.XBaseHandler.OnCancelAsyncListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * XAsync
 */
public class XAsync implements OnCancelAsyncListener {

	public static final String TAG = "XAsync";

	private static final String JSON_BODY = "body";

	private static final String HTTP_HEADER_USER_AGENT_MESSAGE = "android GomeShopApp %s;";

	private static AsyncHttpClient mHttpClient;

	private static XAsync sInstance = null;

	private XAsync() {
		this.init();
	}

	/**
	 * 获取 XAsync
	 * 
	 * @param context
	 * @return
	 */
	public static XAsync with() {
		if (sInstance == null) {
			sInstance = new XAsync();
		}
		return sInstance;
	}

	/**
	 * 初始化 RequestQueue
	 * 
	 * @param context
	 */
	private void init() {
		mHttpClient = new AsyncHttpClient();
		mHttpClient.setUserAgent(String.format(HTTP_HEADER_USER_AGENT_MESSAGE,
				"28.0.1"));
	}

	/**
	 * 获取 RequestQueue
	 * 
	 * @return RequestQueue
	 */
	private AsyncHttpClient getHttpClient() {
		if (mHttpClient != null) {
			this.init();
		}
		return mHttpClient;
	}

	public void getString(String url, XStringHanler responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	public void getJSON(String url, XJSONHandler responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	public <T> void getParser(String url, XParserHandler<T> responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	// ..........................................................................

	public void postString(String url, String json,
			XStringHanler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json);
		getHttpClient().post(url, params, responseHandler);
	}

	public void postJSON(String url, String json, XJSONHandler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json);
		getHttpClient().post(url, params, responseHandler);
	}

	public <T> void postParser(String url, String json,
			XParserHandler<T> responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json);
		getHttpClient().post(url, params, responseHandler);
	}

	public void postString(String url, JSONObject json,
			XStringHanler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json.toJSONString());
		getHttpClient().post(url, params, responseHandler);
	}

	public void postJSON(String url, JSONObject json,
			XJSONHandler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json.toJSONString());
		getHttpClient().post(url, params, responseHandler);
	}

	public <T> void postParser(String url, JSONObject json,
			XParserHandler<T> responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json.toJSONString());
		getHttpClient().post(url, params, responseHandler);
	}

	/**
	 * 事件绑定
	 * 
	 * @param responseHandler
	 */
	private void bindEvent(XBaseHandler responseHandler) {
		if (responseHandler != null) {
			responseHandler.setOnCancelListener(this);
		}
	}

	@Override
	public void onAsyncCancel(Context context) {
		mHttpClient.cancelRequests(context, true);
	}

}
