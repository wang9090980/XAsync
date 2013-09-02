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
			this.saveErrorLog(excp);
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
		return run(e);
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
	public static XException run(Exception e) {
		return new XException(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param context
	 * @return
	 */
	public static XException getAppExceptionHandler() {
		return new XException();
	}

	// ------------------消息展示--------------------

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		makeToast(ctx, DEFAULT_SHOW);
	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 * @param type
	 */
	public void makeToast(Context ctx, int type) {
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			String err = String.format("网络异常，错误码：%d", this.getCode());
			makeMessage(ctx, err, type);
			break;
		case TYPE_HTTP_ERROR:
			makeMessage(ctx, "网络异常，请求超时", type);
			break;
		case TYPE_SOCKET:
			makeMessage(ctx, "网络异常，读取数据超时", type);
			break;
		case TYPE_NETWORK:
			makeMessage(ctx, "网络连接失败，请检查网络设置", type);
			break;
		case TYPE_XML:
			makeMessage(ctx, "数据解析异常", type);
			break;
		case TYPE_IO:
			makeMessage(ctx, "文件流异常", type);
			break;
		case TYPE_RUN:
			makeMessage(ctx, "应用程序运行时异常", type);
			break;
		case TYPE_OO:
			makeMessage(ctx, "内存溢出", type);
			break;
		case TYPE_NULL:
			makeMessage(ctx, "空指针异常", type);
			break;
		case TYPE_INDEX:
			makeMessage(ctx, "数组越界", type);
			break;
		case TYPE_FORMATE:
			makeMessage(ctx, "数据类型转换异常", type);
			break;
		}
	}

	/**
	 * 打印错误
	 * 
	 * @param message
	 */
	private void makeMessage(Context context, String message,
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
	 * 保存异常日志
	 * 
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
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
			// 没有挂载SD卡，无法写文件
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
			pw.close();
			fw.close();
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
