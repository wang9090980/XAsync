package com.loopj.android.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.http.HttpException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 异常管理
 */
public class XException extends Exception {

	public static final int SHOW_LOG = 0;
	public static final int SHOW_TOAST = 1;
	public static final int SHOW_PRINTLN = 2;
	private static final int DEFAULT_SHOW = SHOW_TOAST;

	private static final String TAG = "XException";

	private static final long serialVersionUID = 1L;

	private final static boolean Debug = true;// 是否保存错误日志

	/** 定义异常类型 */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_XML = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_RUN = 0x07;
	public final static byte TYPE_OO = 0x08;
	public final static byte TYPE_NULL = 0x09;
	public final static byte TYPE_INDEX = 0x10;
	public final static byte TYPE_FORMATE = 0x11;

	private byte type;
	private int code;

	private XException() {
	}

	private XException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		if (Debug) {
			//this.saveErrorLog(excp);
		}
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * 普通异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException normal(Exception e) {
		if (e instanceof NullPointerException) {
			return new XException(TYPE_NULL, 0, e);
		} else if (e instanceof IndexOutOfBoundsException) {
			return new XException(TYPE_INDEX, 0, e);
		} else if (e instanceof NumberFormatException) {
			return new XException(TYPE_FORMATE, 0, e);
		} else if (e instanceof ArrayIndexOutOfBoundsException) {
			return new XException(TYPE_FORMATE, 0, e);
		}
		return new XException();
	}

	/**
	 * IO异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new XException(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new XException(TYPE_IO, 0, e);
		}
		return runTime(e);
	}

	/**
	 * XML异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException xml(Exception e) {
		return new XException(TYPE_XML, 0, e);
	}
	
	/**
	 * JSON异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException json(Exception e) {
		return new XException(TYPE_XML, 0, e);
	}

	/**
	 * 网络相关的异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new XException(TYPE_NETWORK, 0, e);
		} else if (e instanceof HttpException) {
			return http(e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	/**
	 * HTTP异常
	 * 
	 * @param code
	 *            http返回码
	 * @return
	 */
	public static XException http(int code) {
		return new XException(TYPE_HTTP_CODE, code, null);
	}

	/**
	 * HTTP异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException http(Exception e) {
		return new XException(TYPE_HTTP_ERROR, 0, e);
	}

	/**
	 * SOCKET异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException socket(Exception e) {
		return new XException(TYPE_SOCKET, 0, e);
	}

	/**
	 * 运行时异常
	 * 
	 * @param e
	 * @return
	 */
	public static XException runTime(Exception e) {
		return new XException(TYPE_RUN, 0, e);
	}

	// ------------------消息展示--------------------

	/**
	 * show error message friendly
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		makeToast(ctx, DEFAULT_SHOW);
	}

	/**
	 * show error message friendly by type
	 * 
	 * @param ctx
	 * @param type
	 */
	public void makeToast(Context ctx, int type) {
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			String err = String.format("网络异常，错误码：%d", this.getCode());
			showErrorMessage(ctx, err, type);
			break;
		case TYPE_HTTP_ERROR:
			showErrorMessage(ctx, "网络异常，请求超时", type);
			break;
		case TYPE_SOCKET:
			showErrorMessage(ctx, "网络异常，读取数据超时", type);
			break;
		case TYPE_NETWORK:
			showErrorMessage(ctx, "网络连接失败，请检查网络设置", type);
			break;
		case TYPE_XML:
			showErrorMessage(ctx, "数据解析异常", type);
			break;
		case TYPE_IO:
			showErrorMessage(ctx, "文件流异常", type);
			break;
		case TYPE_RUN:
			showErrorMessage(ctx, "应用程序运行时异常", type);
			break;
		case TYPE_OO:
			showErrorMessage(ctx, "内存溢出", type);
			break;
		case TYPE_NULL:
			showErrorMessage(ctx, "空指针异常", type);
			break;
		case TYPE_INDEX:
			showErrorMessage(ctx, "数组越界", type);
			break;
		case TYPE_FORMATE:
			showErrorMessage(ctx, "数据类型转换异常", type);
			break;
		}
	}

	/**
	 * show error message by type
	 * 
	 * @param message
	 */
	private void showErrorMessage(Context context, String message,
			int message_show_type) {
		Log.d("exception", message);
		switch (message_show_type) {
		case SHOW_LOG:
			Log.d(TAG, message);
			break;
		case SHOW_PRINTLN:
			System.out.println(String.format("%s-%s", TAG, message));
			break;
		case SHOW_TOAST:
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			break;
		default:
			Log.d(TAG, message);
			break;
		}
	}

	// ------------------保存异常日志--------------------

	/**
	 * save error log
	 * 
	 * @param excp
	 */
	private void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/GomeLog/Log/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + (new Date().toLocaleString())
					+ "---------------------");
			excp.printStackTrace(pw);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
