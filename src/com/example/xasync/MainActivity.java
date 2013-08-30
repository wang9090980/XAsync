package com.example.xasync;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.common.XAsync;
import com.loopj.android.common.XStringHanler;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn;
	private TextView tv;
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
		final String url = "http://hyxfblog.duapp.com/";
		
		XAsync.with().getString(url, new XStringHanler(this, true){

			@Override
			public void onSuccess(String response) {
				tv.setText(response.trim());
			}
		});
		
	}

}
