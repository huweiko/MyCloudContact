package com.my.cloudcontact.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

public class Constant {

	public static final long MIN_LOGINTIME = 0;
	public static class Preference {

		public static final String UNAME = "UNAME";
		public static final String PWD = "PWD";

		public static SharedPreferences getSharedPreferences(Context context) {
			return context.getSharedPreferences("CloudContactSharePref", Context.MODE_PRIVATE);
		}
	}
	public static final String PgyerAPPID="3a05e1af690dbc648dd964b293ddc7a2";// 集成蒲公英sdk应用的appId

}
