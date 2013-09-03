package com.example.xasync;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.common.XHttp;
import com.loopj.android.common.XJSONHandler;
import com.loopj.android.common.XStringHanler;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "async";
	
	private Button btn;
	private TextView tv;

	private static final String URL_JSON_ARRAY = "http://10.57.40.64:8080/Async/servlet/Api";
	private static final String URL_JSON_OBJECT = "http://10.57.40.64:8080/Async/servlet/Api1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(this);
		tv = (TextView) findViewById(R.id.textView1);
	}

	@Override
	public void onClick(View v) {
		JSONObject json = new JSONObject();
		json.put("index", "3");
		json.put("pagesize", "10");
		json.put("keyword", "西游");
		XHttp.with().getJSON(URL_JSON_ARRAY, new XJSONHandler(this, true){

			@Override
			public void onSuccess(JSONObject response) {
				Log.d(TAG, "onSuccess-JSONObject");
			}

			@Override
			public void onSuccess(JSONArray response) {
				Log.d(TAG, "onSuccess-JSONArray");
			}
			
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStart() {
				Log.d(TAG, "onStart");
			}

			@Override
			public void onFinish() {
				Log.d(TAG, "onFinish");
			}
			
		});
	}

}
