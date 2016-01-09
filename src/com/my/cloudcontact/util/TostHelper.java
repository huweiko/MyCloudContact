package com.my.cloudcontact.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tom on 2015/11/19.
 * descripton :
 */
public class TostHelper {
	public static void ToastLg(String string, Context activity) {
		Toast.makeText(activity, string, Toast.LENGTH_LONG).show();
	}
	
	public static void ToastSht(String string, Context activity) {
		Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
	}
	
	public static void ToastLg(int strId, Context activity) {
		Toast.makeText(activity, strId, Toast.LENGTH_LONG).show();
	}
	
	public static void ToastSht(int strId, Context activity) {
		Toast.makeText(activity, strId, Toast.LENGTH_SHORT).show();
	}
}
