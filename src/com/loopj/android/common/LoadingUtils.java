package com.loopj.android.common;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

/**
 * 数据加载工具类
 */
public class LoadingUtils {

    /**
     * 显示进度框
     * 
     * @param context
     * @param message
     * @param cancelable
     * @param cancelListener
     * @return
     */
    public static LoadingDialog showLoadingDialog(Context context, String message, boolean cancelable,
            OnCancelListener cancelListener) {
        return LoadingDialog.show(context, message, cancelable, cancelListener);
    }
    
	/**
	 * 是否显示加载进度
	 * 
	 * @param isNeedLoading
	 * @return
	 */
	public static LoadingDialog showLoading(Context context , boolean isNeedLoading) {
		LoadingDialog progressDialog = null;
		if (isNeedLoading) {
			progressDialog = LoadingUtils.showLoadingDialog(context, "加载中",
					true, null);
		}
		return progressDialog;
	}
	
	/**
	 * 取消进度框
	 */
	public static void dismissDialog(LoadingDialog mDialog){
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

}
