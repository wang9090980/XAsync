package com.loopj.android.common;

import org.apache.http.Header;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * HTTP代理基类
 */
public class XBaseHandler extends AsyncHttpResponseHandler implements OnCancelListener{

	private boolean mShowDialog;
	private LoadingDialog mDialog = null;
	public Context mContext;
	private OnCancelAsyncListener mListener;
	
	public XBaseHandler(Context context, boolean showDialog) {
		super();
		mShowDialog = showDialog;
		mContext = context;
	}
	
	public XBaseHandler(Context context, boolean showDialog,OnCancelAsyncListener listener) {
		super();
		mShowDialog = showDialog;
		mContext = context;
		mListener = listener;
	}
	
	@Override
	public void onStart() {
		mDialog = LoadingUtils.showLoading(mContext, mShowDialog);
		if (mDialog != null && mListener != null) {
			mDialog.setOnCancelListener(this);
		}
		super.onStart();
	}

	@Override
	public void onFinish() {
		LoadingUtils.dismissDialog(mDialog);
		super.onFinish();
	}

	@Override
	public void onFailure(Throwable error, String content) {
		LoadingUtils.dismissDialog(mDialog);
		super.onFailure(error, content);
	}
	
	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		LoadingUtils.dismissDialog(mDialog);
		super.onSuccess(statusCode, headers, content);
	}

	@Override
	public void onSuccess(String content) {
		super.onSuccess(content);
	}

	@Override
	public void onSuccess(int statusCode, String content) {
		super.onSuccess(statusCode, content);
	}
	
	/**
	 * 异步请求取消接口
	 */
	public interface OnCancelAsyncListener{
		void onAsyncCancel(Context context);
	}
	
	public void setOnCancelListener(OnCancelAsyncListener listener){
		this.mListener = listener;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if(mListener != null){
			mListener.onAsyncCancel(mContext);
		}
	}
}
