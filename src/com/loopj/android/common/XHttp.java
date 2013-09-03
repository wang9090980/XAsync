package com.loopj.android.common;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.common.XBaseHandler.OnCancelAsyncListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * XHttp
 */
public class XHttp implements OnCancelAsyncListener {

	public static final String TAG = "XAsync";

	private static final String JSON_BODY = "body";

	private static final int TIME_OUT = 30 * 1000;

	private static final String HTTP_HEADER_USER_AGENT_MESSAGE = "android GomeShopApp %s;";

	private static AsyncHttpClient mHttpClient;

	private static XHttp sInstance = null;

	private XHttp() {
		this.init();
	}

	/**
	 * 获取 XAsync
	 * 
	 * @param context
	 * @return
	 */
	public static XHttp with() {
		if (sInstance == null) {
			sInstance = new XHttp();
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
		mHttpClient.setTimeout(TIME_OUT);
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

	/**
	 * GET请求返回 String
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public void getString(String url, XStringHanler responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	/**
	 * GET请求返回 JSON对象
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public void getJSON(String url, XJSONHandler responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	/**
	 * GET请求返回 实体对象
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public <T> void getParser(String url, XParserHandler<T> responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	// ..........................................................................

	/**
	 * POST请求返回 String
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param json
	 *            请求参数【String】
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public void postString(String url, String json,
			XStringHanler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json);
		getHttpClient().post(url, params, responseHandler);
	}

	/**
	 * POST请求返回 JSON对象
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param json
	 *            请求参数【String】
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public void postJSON(String url, String json, XJSONHandler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json);
		getHttpClient().post(url, params, responseHandler);
	}

	/**
	 * POST请求返回 实体对象
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param json
	 *            请求参数【String】
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public <T> void postParser(String url, String json,
			XParserHandler<T> responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json);
		getHttpClient().post(url, params, responseHandler);
	}

	/**
	 * POST请求返回 String
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param json
	 *            请求参数【JSONObject】
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public void postString(String url, JSONObject json,
			XStringHanler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json.toJSONString());
		getHttpClient().post(url, params, responseHandler);
	}

	/**
	 * POST请求返回 JSON对象
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param json
	 *            请求参数【JSONObject】
	 * @param responseHandler
	 *            服务器端response代理
	 */
	public void postJSON(String url, JSONObject json,
			XJSONHandler responseHandler) {
		bindEvent(responseHandler);
		RequestParams params = new RequestParams(JSON_BODY, json.toJSONString());
		getHttpClient().post(url, params, responseHandler);
	}

	/**
	 * POST请求返回 实体对象
	 * 
	 * @param url
	 *            服务器端接口URL
	 * @param json
	 *            请求参数【JSONObject】
	 * @param responseHandler
	 *            服务器端response代理
	 */
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
