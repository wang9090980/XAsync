package com.example.xasync;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.common.XHttp;
import com.loopj.android.common.XParserHandler;

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
	}

}
