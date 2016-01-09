package com.my.cloudcontact;

import java.util.Date;

import com.my.cloudcontact.util.AssetUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class NetCheckThread extends Thread {
	String Tag = getClass().getSimpleName();
	boolean isRunning = true;
	String mServerIp;
	Handler mHandler;
	Context mContext;
	public NetCheckThread(Context context,Handler handler,String ServerIp) {
		mHandler = handler;
		mServerIp = ServerIp;
		mContext = context;
	}
	
	@Override
	public void run() {		
		while (isRunning) {
			if(AssetUtils.checkNetwork(mContext)){
				Log.i(Tag, "有网"+new Date().toGMTString());
				mHandler.sendEmptyMessage(MainActivity.have_net);
			}else{
				Log.i(Tag, "无网");
				mHandler.sendEmptyMessage(MainActivity.no_net);
			}
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void setRunning(boolean b) {
		isRunning = b;
	}
}
