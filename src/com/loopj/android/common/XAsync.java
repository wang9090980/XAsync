package com.loopj.android.common;

import android.content.Context;

import com.loopj.android.common.XBaseHandler.OnCancelAsyncListener;
import com.loopj.android.http.AsyncHttpClient;

/**
 * XAsync
 */
public class XAsync implements OnCancelAsyncListener {

	public static final String TAG = "XAsync";

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

	public void getString(String url, XBaseHandler responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	public void getJSONObject(String url, XBaseHandler responseHandler) {
		bindEvent(responseHandler);
		getHttpClient().get(responseHandler.mContext, url, responseHandler);
	}

	public void getJSONArray(XBaseHandler responseHandler) {
		bindEvent(responseHandler);
	}

	/**
	 * 事件绑定
	 * 
	 * @param responseHandler
	 */
	private void bindEvent(XBaseHandler responseHandler) {
		responseHandler.setOnCancelListener(this);
	}

	@Override
	public void onAsyncCancel(Context context) {
		mHttpClient.cancelRequests(context, true);
	}

}
