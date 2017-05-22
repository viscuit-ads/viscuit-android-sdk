package com.example.viscuit_native_sdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.viscuit.sdk.ViscuitResult;
import com.viscuit.sdk.ViscuitSDK;
import com.viscuit.sdk.onReloadListener;
import com.viscuit.sdk.onViscuitListener;

public class MainActivity extends Activity implements OnClickListener {

    private TimerButton mTimerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerButton = (TimerButton) findViewById(R.id.main_show);
        mTimerButton.setCountDownTime(10);
        mTimerButton.setDefaultText("권장 요청 샘플");
		
		/*최초 한번만 실행하면 된다.
         * Application에서 한번만 실행해도 되고, 항상 살아있는 엑티비티에서 실행해주는 것이 좋다.
		 * 여러번 호출 되도 한번만 호출 된다, init시에 광고 리스트 및 소재를 다운받게 된다.
		 */

        ViscuitSDK.init(this, "viscuit", "test_ads");

		/*
		 * 개발용으로 테스트 광고를 내려받게 된다.
		 */
        ViscuitSDK.setDevType(true);


		/*
		 * 광고에 대한 결과값을 받는다.
		 */
        ViscuitSDK.setViscuitListener(new onViscuitListener() {

            @Override
            public void adcallbackmessage(final ViscuitResult result) {
                switch (result) {
                    case ERROR:
                        //네트워크 에러등의 에러 발생
                        break;
                    case NOAD:
                        // 광고 없음
                        break;
                    case SKIP:
                        //스킵 상품의 광고를 스킵함.
                        mTimerButton.start();
                        break;
                    case SUCCESS:
                        //정상적으로 광고 시청을 모두 완료함.
                        //리워드 지급
                        mTimerButton.start();
                        break;
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result.name(), Toast.LENGTH_LONG).show();
                    }
                });

                Log.e("viscuit", "[adcallbackmessage] " + result.name());

            }
        });

        ((TextView) findViewById(R.id.version)).setText("비스킷 SDK : " + ViscuitSDK.getSDKVersion(this));

    }


    @Override
    public void onClick(View v) {
        // 광고 요청
        switch (v.getId()) {
            case R.id.main_init:
//                ViscuitSDK.init(this, "이곳에 MediaCode를 넣어주세요", "이곳에 SlotCode를 넣어주세요.");
                break;
            case R.id.main_reload:
                // 현재 캠페인 진행 상태를 가져온다.
                // 광고가 있을 경우 이 경우에만 ADREADY가 호출 된다.
                // 없을 경우 NOAD 호출

//                ViscuitSDK.reloadAdStatus(this);

                ViscuitSDK.reloadAdStatus(this, new onReloadListener() {
                    @Override
                    public void onReloadFinish() {
                        Toast.makeText(MainActivity.this, ViscuitSDK.isAdReady(MainActivity.this) ? "광고 있음" : "광고 없음", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.main_check:
                // NonThread
                Toast.makeText(MainActivity.this, ViscuitSDK.isAdReady(MainActivity.this) ? "광고 있음" : "광고 없음", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_show:
                /*
                 * 동영상 광고를 재생합니다.
                 * 해당 메소드 실행시 현재 진행 가능한 광고리스트를 받아오고 소재가 준비되어 있다면
                 * 동영상이 재생 됩니다.
                 */
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
