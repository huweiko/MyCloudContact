package com.my.cloudcontact;

import android.app.Application;

import com.my.cloudcontact.bean.Constant;
import com.my.cloudcontact.http.FinalHttp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

public class MyCloudContactApplication extends Application {
	private FinalHttp mFinalHttp;
	private static MyCloudContactApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 初始化事件分发器
		mFinalHttp = new FinalHttp();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
			.showImageForEmptyUri(R.drawable.ic_launcher) //
			.showImageOnFail(R.drawable.ic_launcher) //
			.cacheInMemory(true) //
			.cacheOnDisk(true) //
			.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
			.Builder(getApplicationContext())//
			.defaultDisplayImageOptions(defaultOptions)//
			.discCacheSize(50 * 1024 * 1024)//
			.discCacheFileCount(100)// 缓存一百张图片
			.writeDebugLogs()//
			.build();//
		ImageLoader.getInstance().init(config);
		PgyCrashManager.register(this,Constant.PgyerAPPID);// 集成蒲公英sdk应用的appId
		// mFinalHttp.setReqeustHeader(new BaseReqeustHeader(instance));
		// mFinalHttp.setCheckResponseInterface(new BaseCheckResponse());
	}
	
	public static MyCloudContactApplication instance(){
		if(instance == null){
			instance = new MyCloudContactApplication();
		}
		return instance;
	}

	public FinalHttp getFinalHttp() {
		if(mFinalHttp == null){
			mFinalHttp = new FinalHttp();
		}
		return mFinalHttp;
	}
}
