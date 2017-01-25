package com.example.viscuit_native_sdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.viscuit.sdk.ViscuitCategory;
import com.viscuit.sdk.ViscuitResult;
import com.viscuit.sdk.ViscuitSDK;
import com.viscuit.sdk.onViscuitListener;

public class MainActivity extends Activity implements OnClickListener{

	String[] categorys = {
			ViscuitCategory.ACTION_SPORT,
			ViscuitCategory.BUSINESS
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		/*최초 한번만 실행하면 된다.
		 * Application에서 한번만 실행해도 되고, 항상 살아있는 엑티비티에서 실행해주는 것이 좋다.
		 * 여러번 호출 되도 한번만 호출 된다, init시에 광고 리스트 및 소재를 다운받게 된다.
		 */
		
		ViscuitSDK.init(this, "viscuit", "test_ads", categorys);
		
		/*
		 * 개발용으로 테스트 광고를 내려받게 된다.
		 */
		ViscuitSDK.setDevType(true);
		
		
		/*
		 * 광고에 대한 결과값을 받는다.
		 */
		ViscuitSDK.setViscuitListener(new onViscuitListener() {
			
			@Override
			public void adcallbackmessage(ViscuitResult result) {
				switch(result) {
				case ERROR:
					//네트워크 에러등의 에러 발생
					break;
				case NOAD:
					// 광고 없음
					break;
				case SKIP:
					//스킵 상품의 광고를 스킵함.
					break;
				case SUCCESS:
					//정상적으로 광고 시청을 모두 완료함.
					//리워드 지급
					break;
				case ADREADY:
					// ViscuitSDK.checkAdStatus 을 했을 경우에 광고가 있다면 ADREADY가 호출 된다.
					break;
				}

				Toast.makeText(MainActivity.this, result.name(), Toast.LENGTH_LONG).show();
				
				Log.e("viscuit","[adcallbackmessage] " + result.name());

			}
		});
		
		((TextView)findViewById(R.id.version)).setText("비스킷 SDK : "  + ViscuitSDK.getSDKVersion(this));

	}

	
	@Override
	public void onClick(View v) {
		// 광고 요청
		switch(v.getId()) {
		case R.id.playad1:
			/*
			 * 동영상 광고를 재생합니다. 
			 * 해당 메소드 실행시 현재 진행 가능한 광고리스트를 받아오고 소재가 준비되어 있다면 
			 * 동영상이 재상 됩니다. 
			 */
			ViscuitSDK.viscuitStart();
			break;
		case R.id.playad2:
			// 현재 캠페인 진행 상태를 가져온다.
			// 광고가 있을 경우 이 경우에만 ADREADY가 호출 된다. 
			// 없을 경우 NOAD 호출
			ViscuitSDK.checkAdStatus(this);
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
