package com.tbcall;

import android.app.Activity;
import android.content.Intent;
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

import com.tbcall.bean.MainPhoneBean;
import com.tbcall.tool.FileTool;

import java.util.ArrayList;

import static com.tbcall.config.AppConfig.LOG_TAG;

/***
 * 主界面
 */
public class MainActivity extends Activity {

    private TextView tv_start1;
    private ListView lv_phone;
    private ArrayList<MainPhoneBean> phoneList;
    private ThisListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        FileTool.initFile();
        initPhoneList();

        tv_start1 = (TextView) findViewById(R.id.tv_start1);
        tv_start1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this, SoundActivity.class));      //跳转录音列表
                return true;
            }
        });
        findViewById(R.id.tv_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DoCallActivity.class));      //跳转-打电话
            }
        });


        lv_phone = (ListView) findViewById(R.id.lv_phone);
        adapter = new ThisListAdapter();
        lv_phone.setAdapter(adapter);
    }

    class ThisListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ThisListAdapter() {
            inflater = LayoutInflater.from(MainActivity.this);
        }

        @Override
        public int getCount() {
            return phoneList.size();
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
                convertView = inflater.inflate(R.layout.phone_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                viewHolder.tv_addr = (TextView)convertView.findViewById(R.id.tv_addr);
                viewHolder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MainPhoneBean bean = phoneList.get(position);
            viewHolder.tv_name.setText(bean.numberOrName);
            viewHolder.tv_addr.setText(bean.numberAddr);
            viewHolder.tv_date.setText(bean.date);
            convertView.findViewById(R.id.view_row).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(LOG_TAG, " 点击了第 " + (position+1) + " 行");
                    Intent intent = new Intent(MainActivity.this, DoCallActivity.class);
                    intent.putExtra("bean", phoneList.get(position));
                    startActivity(intent);                                               //跳转-打电话
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_name, tv_addr, tv_date;
    }

    public void initPhoneList(){
        phoneList = new ArrayList<>();
        phoneList.add(new MainPhoneBean("习大大", "中央移动", "刚刚"));
        phoneList.add(new MainPhoneBean("川普", "美国电信", "今天"));
        phoneList.add(new MainPhoneBean("普京", "俄罗斯电信", "昨天"));
        phoneList.add(new MainPhoneBean("李克强", "国务院移动", "前天"));
        phoneList.add(new MainPhoneBean("金三胖", "朝鲜2G", "前天"));
        phoneList.add(new MainPhoneBean("默克尔", "德国联通", "前天"));
        phoneList.add(new MainPhoneBean("马云", "浙江移动", "前天"));
        phoneList.add(new MainPhoneBean("许家印", "广东电信", "11/05"));
        phoneList.add(new MainPhoneBean("马化腾", "深圳联通", "11/03"));
        phoneList.add(new MainPhoneBean("刘强东", "北京移动", "11/02"));
        phoneList.add(new MainPhoneBean("李彦宏", "北京联通", "10/31"));
        phoneList.add(new MainPhoneBean("刘德华", "香港移动", "10/30"));
        phoneList.add(new MainPhoneBean("邓超", "上海移动", "10/28"));
        phoneList.add(new MainPhoneBean("何炅", "湖南联通", "10/27"));
        phoneList.add(new MainPhoneBean("汪涵", "长沙移动", "10/26"));
        phoneList.add(new MainPhoneBean("武莲村长", "岳阳移动", "10/25"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Log.i(LOG_TAG, "退出应用啦 ***---***");
            finish();
            System.exit(0);
        }
        return true;
    }


}
