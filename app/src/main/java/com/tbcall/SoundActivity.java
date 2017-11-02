package com.tbcall;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tbcall.bean.SoundBean;
import com.tbcall.tool.FileTool;
import com.tbcall.tool.SoundTool;

import java.util.ArrayList;

import static com.tbcall.config.AppConfig.LOG_TAG;

/***
 * 录音-界面 (已录音列表)
 */
public class SoundActivity extends Activity {

    private TextView tv_start1, tv_stop1, tv_stop2;
    private SoundTool soundTool;

    private ListView lv_sound;
    private ArrayList<SoundBean> soundBenaList;
    private SoundListAdapter soundListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sound);
        soundTool = new SoundTool(this);
        soundBenaList = FileTool.getSoundList();
        Log.i(LOG_TAG, "-------获取到的录音列表 size:" + soundBenaList.size());

        tv_start1 = (TextView) findViewById(R.id.tv_start1);
        tv_stop1 = (TextView) findViewById(R.id.tv_stop1);
        tv_stop2 = (TextView) findViewById(R.id.tv_stop2);
        lv_sound = (ListView) findViewById(R.id.lv_sound);

        tv_start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundTool.startRecording();
            }
        });

        tv_stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundTool.stopRecording();
                updateList();
            }
        });

        tv_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundTool.stopPlaying();
            }
        });

        soundListAdapter = new SoundListAdapter();
        lv_sound.setAdapter(soundListAdapter);
    }

    public void updateList(){
        soundBenaList = FileTool.getSoundList();
        soundListAdapter.notifyDataSetChanged();
    }

    /**
     * 列表适配器
     */
    class SoundListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public SoundListAdapter() {
            inflater = LayoutInflater.from(SoundActivity.this);
        }

        @Override
        public int getCount() {
            return soundBenaList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.sound_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
                viewHolder.tv_play = (TextView)convertView.findViewById(R.id.tv_play);
                viewHolder.tv_delete = (TextView)convertView.findViewById(R.id.tv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            SoundBean bean = soundBenaList.get(position);
            viewHolder.tv_name.setText(bean.fileName);
            viewHolder.tv_time.setText(bean.fileTime);
            viewHolder.tv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soundTool.startPlaying(soundBenaList.get(position).fileName);
                }
            });
            viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FileTool.deleteFile(FileTool.getAppAudioPath() +  soundBenaList.get(position).fileName)){
                        FileTool.showToast(SoundActivity.this, "删除成功");
                    }else{
                        FileTool.showToast(SoundActivity.this, "删除失败，有异常");
                    }
                    updateList();
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_name, tv_time;
        public TextView tv_play, tv_delete;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(soundTool != null){
            soundTool.releaseAll();
            soundTool = null;
        }
    }
}
