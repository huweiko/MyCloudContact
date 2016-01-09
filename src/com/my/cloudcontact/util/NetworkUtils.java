package com.my.cloudcontact.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by admin on 15/12/1.
 */
public class NetworkUtils {

    public static boolean getNetworkEnable(Context context){

        try{
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }

        return false;
    }
}
