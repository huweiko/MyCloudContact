package com.my.cloudcontact.bean;

import android.content.Context;
import android.content.SharedPreferences;

public class Constant {

	public static final long MIN_LOGINTIME = 0;
	public static class Preference {

		public static final String UNAME = "UNAME";
		public static final String PWD = "PWD";

		public static SharedPreferences getSharedPreferences(Context context) {
			return context.getSharedPreferences("CloudContactSharePref", Context.MODE_PRIVATE);
		}
	}
	public static final String PgyerAPPID="70eb1fa8158a4bb188be2264418339cc";// 集成蒲公英sdk应用的appId
}
