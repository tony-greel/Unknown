package com.ljj.unknown;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.ljj.unknown.other.GlideImageLoader;
import com.zxy.tiny.Tiny;

import org.litepal.LitePal;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/7/30.
 */

public class UnknownApplication extends Application {

    public static Context context;
    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"fe911a5b766eb904c5c717612157bc8b");
        LitePal.initialize(this);
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new UnknownMessageHandler());
        //压缩图片框架初始化
        Tiny.getInstance().init(this);
        context = getApplicationContext();
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new GlideImageLoader())
                .setToolbaseColor(Color.parseColor("#108de8"))
                .build());
    }
}
