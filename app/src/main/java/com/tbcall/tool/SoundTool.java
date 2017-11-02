package com.tbcall.tool;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import com.tbcall.config.AppConfig;

import java.io.IOException;

import static com.tbcall.config.AppConfig.LOG_TAG;

/**
 * 录音与播放--工具类
 */
public class SoundTool {
    public MediaRecorder mRecorder;
    public MediaPlayer mPlayer;
    public boolean isRecording;
    public Activity context;
    public String newFileIndex;

    public SoundTool(Activity context) {
        this.context = context;
    }

    /**
     * 开始录音
     */
    public void startRecording() {
        Log.i(LOG_TAG, "-----开始录音");
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        try {
            newFileIndex = FileTool.getFileIndex(context);
            mRecorder.reset();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(FileTool.getNewFileName(newFileIndex));
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.i(LOG_TAG, "录音--失败");
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecording() {
        Log.i(LOG_TAG, "-------停止录音");
        if (mRecorder == null) {
            return;
        }
        try {
            mRecorder.stop();
            FileTool.setSharedData(context, AppConfig.FILE_NAME_MAX_INDEX, newFileIndex);
        } catch (Exception e) {
            Log.i(LOG_TAG, "停止录音---失败");
            e.printStackTrace();
        }

    }

    /**
     * 播放录音
     */
    public void startPlaying(String fileName) {
        Log.i(LOG_TAG, "-------开始播放录音: " + (FileTool.getAppAudioPath() + fileName));
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        try {
            mPlayer.reset();
            mPlayer.setDataSource(FileTool.getAppAudioPath() + fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.i(LOG_TAG, "播放录音---失败");
            e.printStackTrace();
        }
    }

    /**
     * 停止播放录音
     */
    public void stopPlaying() {
        Log.i(LOG_TAG, "---停止播放");
        if (mPlayer == null) {
            return;
        }
        try {
            mPlayer.stop();
        } catch (Exception e) {
            Log.i(LOG_TAG, "停止播放录音---失败");
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void releaseAll() {
        Log.i(LOG_TAG, "---释放 releaseAll");
        try {
            if (mRecorder != null) {
//            mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
            if (mPlayer != null) {
//            mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
