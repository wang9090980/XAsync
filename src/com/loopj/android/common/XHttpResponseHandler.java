package com.loopj.android.common;

import org.apache.http.Header;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class XHttpResponseHandler extends AsyncHttpResponseHandler implements OnCancelListener{

	private boolean mShowDialog;
	private LoadingDialog mDialog = null;
	public Context mContext;
	private OnCancelAsyncListener mListener;
	
	public XHttpResponseHandler(Context context, boolean showDialog) {
		super();
		mShowDialog = showDialog;
		mContext = context;
	}
	
	public XHttpResponseHandler(Context context, boolean showDialog,OnCancelAsyncListener listener) {
		super();
		mShowDialog = showDialog;
		mContext = context;
		mListener = listener;
	}
	
	public void setOnCancelListener(OnCancelAsyncListener listener){
		this.mListener = listener;
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
	public void onSuccess(int statusCode, Header[] headers, String content) {
		LoadingUtils.dismissDialog(mDialog);
		super.onSuccess(statusCode, headers, content);
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
	public void onSuccess(String content) {
		super.onSuccess(content);
	}

	@Override
	public void onSuccess(int statusCode, String content) {
		super.onSuccess(statusCode, content);
	}
	
	public interface OnCancelAsyncListener{
		void onAsyncCancel(Context context);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if(mListener != null){
			mListener.onAsyncCancel(mContext);
		}
	}
}
