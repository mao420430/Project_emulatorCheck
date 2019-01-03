package com.xxx.mxh.emulatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		funcShow();

		Button btn_check = findViewById(R.id.main_button_check);

		btn_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				funcShow();
			}
		});



	}


	void funcShow()
	{
		Integer ret = Emulator.GetIsEmulator(this);

		Toast.makeText(this.getApplicationContext(), ret == 0 ? "不是模拟器" : "是模拟器:"+ret,Toast.LENGTH_LONG).show();

		TextView textView = findViewById(R.id.main_text_view1);

		textView.setMovementMethod(ScrollingMovementMethod.getInstance());

		textView.setText(Emulator.GetLog());
	}
}
