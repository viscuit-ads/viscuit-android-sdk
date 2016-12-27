package com.example.viscuit_native_sdk;

import com.viscuit.sdk.ViscuitCategory;
import com.viscuit.sdk.ViscuitResult;
import com.viscuit.sdk.ViscuitSDK;
import com.viscuit.sdk.onViscuitListner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	String[] categorys = {
			ViscuitCategory.ACTION_SPORT,
			ViscuitCategory.BUSINESS
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		/*최초 한번만 실행하면 된다.*/

		ViscuitSDK.init(this, "viscuit", "test_ads", categorys);
		ViscuitSDK.setDevType(true);

		ViscuitSDK.setViscuitListener(new onViscuitListner() {

			@Override
			public void adcallbackmessage(ViscuitResult result) {

				switch(result) {
				case ERROR:
					break;
				case NOAD:
					break;
				case SKIP:
					break;
				case SUCCESS:
					break;
	}

				Toast.makeText(MainActivity.this, result.name(), Toast.LENGTH_LONG).show();		

				}
		});

		/*  */
			}


	@Override
	public void onClick(View v) {
		// 광고 요청
		switch(v.getId()) {
		case R.id.playad1:
			ViscuitSDK.viscuitStart();
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//라이프사이클 등록
		ViscuitSDK.onPause(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		//라이프사이클 등록
		ViscuitSDK.onResume(this);
	}


}
