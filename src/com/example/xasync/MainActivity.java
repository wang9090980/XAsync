package com.example.xasync;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.common.XAsync;
import com.loopj.android.common.XParserHandler;

public class MainActivity extends Activity implements OnClickListener {

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

		XAsync.with().getParser(URL_JSON_OBJECT, new XParserHandler<UserInfoResponse>(this, UserInfoResponse.class, true){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					UserInfoResponse response) {
				super.onSuccess(statusCode, headers, response);
				Log.d("async", "执行了");
				tv.setText(response.getData().get(0).getUsername());
			}

			@Override
			public void onFailure(Throwable e, UserInfoResponse errorResponse) {
				super.onFailure(e, errorResponse);
				Log.d("async", "异常了");
			}
			
		});

	}

}
