package com.example.viscuit_native_sdk;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Administrator on 2017-05-16.
 */

public class TimerButton extends Button {
    private int mCountDownTime;
    private MyCountDownTimer myCountDownTimer;
    private String mDefaultText;

    public TimerButton(Context context) {
        super(context);
    }

    public TimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start () {
        setEnabled(false);

        if(myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }

        myCountDownTimer = new MyCountDownTimer(mCountDownTime, 1000);
        myCountDownTimer.start();
    }

    public void setCountDownTime(int sec) {
        mCountDownTime = sec * 1000;
    }

    public void setDefaultText(String text) {
        mDefaultText = text;
        setText(text);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(secondsToString((int)(millisUntilFinished/ 1000) ));
        }

        @Override
        public void onFinish() {
            setEnabled(true);
            setText(mDefaultText);
        }
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

}
