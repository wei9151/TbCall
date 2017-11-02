package com.tbcall;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.tbcall.bean.MainPhoneBean;
import com.tbcall.bean.SoundBean;
import com.tbcall.tool.FileTool;
import com.tbcall.tool.SoundTool;

import java.util.ArrayList;

import static com.tbcall.config.AppConfig.LOG_TAG;

/***
 * 打电话-界面
 */
public class DoCallActivity extends Activity {

    private TextView tv_top, tv_addr, tv_number, tv_hangup;

    private SoundTool soundTool;

    private ListView lv_sound;
    private ArrayList<SoundBean> soundBenaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_docall);
        soundTool = new SoundTool(this);
        soundBenaList = FileTool.getSoundList();
        Log.i(LOG_TAG, " 打电话界面 -- 录音的数量:" + soundBenaList.size());

        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_hangup = (TextView) findViewById(R.id.tv_hangup);

        tv_hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundTool.stopPlaying();
                finish();
            }
        });

        MainPhoneBean bean = (MainPhoneBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            tv_addr.setText(bean.numberAddr);
            tv_number.setText(bean.numberOrName);
        } else {
            tv_addr.setText("未知运营商");
            tv_number.setText("陌生电话");
        }

        delayShow();
        setClicks();
    }

    /**
     * 延迟显示顶部文字，并播放录音
     */
    public void delayShow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_top.setText("已接通");
                soundTool.startLoopPlaying(soundBenaList);
            }
        }, 3000);
    }

    /**
     * 设置点击事件
     */
    public void setClicks() {
        int[] txTemps = {R.id.tv_temp1, R.id.tv_temp2, R.id.tv_temp3, R.id.tv_temp4, R.id.tv_temp5, R.id.tv_temp6};
        for (int i = 0; i < txTemps.length; i++) {
            findViewById(txTemps[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            soundTool.stopPlaying();
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "打电话界面 --  onDestroy");
        soundTool.stopPlaying();
        if (soundTool != null) {
            soundTool.releaseAll();
            soundTool = null;
        }
    }
}
