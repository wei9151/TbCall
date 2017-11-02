package com.tbcall.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.tbcall.bean.SoundBean;
import com.tbcall.config.AppConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.tbcall.config.AppConfig.LOG_TAG;

/**
 * 文件--工具类
 */
public class FileTool {
    public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * sd卡上的应用目录
     */
    public static String getAppPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppConfig.FILE_APP + "/";
    }

    /**
     * 录音文件目录
     */
    public static String getAppAudioPath() {
        return getAppPath() + AppConfig.FILE_NAME_AUDIO + "/";
    }

    /**
     * 获取新建文件名
     */
    public static String getNewFileName(String index) {
        String name = getAppAudioPath() + "audio_" + index + ".3gp";
        return name;
    }

    /**初始化文件目录*/
    public static void initFile() {
        try {
            File parent = new File(getAppAudioPath());
            Log.i(LOG_TAG, " 1 新建目录: " + parent.getAbsolutePath());
            if (!parent.exists()) {
                parent.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取新建文件名 索引
     */
    public static String getFileIndex(Context context) {
        String maxIndex = getSharedData(context, AppConfig.FILE_NAME_MAX_INDEX);
        Log.i(LOG_TAG, "-----getSharedData 索引maxIndex: " + maxIndex);
        if (!isNotNull(maxIndex)) {
            ArrayList<SoundBean> soundBenaList = getSoundList();
            if (soundBenaList.isEmpty()) {
                maxIndex = "0";
            } else {
                maxIndex = soundBenaList.get(soundBenaList.size() - 1).index + "";
            }
        }
        try {
            maxIndex = (Integer.parseInt(maxIndex) + 1) + "";   //新文件索引需要加1
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxIndex;
    }

    /**
     * 获取--录音文件列表
     */
    public static ArrayList<SoundBean> getSoundList() {
        Log.i(LOG_TAG, "-----获取录音文件列表");
        ArrayList<SoundBean> soundBenaList = new ArrayList<>();
        File parent = new File(getAppAudioPath());
        File[] files = parent.listFiles();
        int len = files.length;
        SoundBean bean = null;
        for (int i = 0; i < len; i++) {
            bean = new SoundBean();
            bean.fileName = files[i].getName();
            bean.lastModified = files[i].lastModified();
            bean.fileTime = sdf.format(new Date(files[i].lastModified()));
            Log.i(LOG_TAG, (i+1) + " 文件名：" + bean.fileName + "  时间: " + bean.fileTime);
            soundBenaList.add(bean);
        }
        Collections.sort(soundBenaList, new FileComparator());  //排序
        int size = soundBenaList.size();
        for (int i = 0; i < size; i++) {
            soundBenaList.get(i).index = i + 1;                 //设置bean索引
        }
        return soundBenaList;
    }

    static class FileComparator implements Comparator<SoundBean> {
        @Override
        public int compare(SoundBean lhs, SoundBean rhs) {
            if (lhs.lastModified > rhs.lastModified) {
                return 1;
            } else if (lhs.lastModified > rhs.lastModified) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String fileName) {
        try {
            File delFile = new File(fileName);
            Log.i(LOG_TAG, " 删除文件: " + delFile.getAbsolutePath());
            if (delFile.exists()) {
                delFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 弹出toast
     */
    public static void showToast(Context context, String text) {
        if (context != null && isNotNull(text)) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNotNull(String str) {
        return str != null && !"".equals(str.trim());
    }

    /**
     * 获取保存的字符串
     */
    public static String getSharedData(Context context, String key) {
        if (context == null || !isNotNull(key))
            return null;
        Log.i(LOG_TAG, " getSharedData：key:" + key);
        SharedPreferences pre = context.getSharedPreferences(AppConfig.FILE_SHARE_NAME, Context.MODE_PRIVATE);
        return pre.getString(key, "");
    }

    /**
     * 保存字符串
     */
    public static boolean setSharedData(Context context, String key, String value) {
        if (context == null || !isNotNull(key))
            return false;
        Log.i(LOG_TAG, " setSharedData：key:" + key + " value:" + value);
        SharedPreferences pre = context.getSharedPreferences(AppConfig.FILE_SHARE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pre.edit();
        edit.putString(key, value);
        return edit.commit();
    }

}
