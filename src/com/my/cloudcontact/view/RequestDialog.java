package com.my.cloudcontact.view;

import com.my.cloudcontact.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;


/**
 * 包含原型progressbar，可设置消息的对话框
 * @author lenovo
 */
public class RequestDialog extends Dialog{

	public RequestDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.request_dialog);
	}
	
	public void setMessage(String msg){
		TextView tvMsg = (TextView)findViewById(R.id.tvMsg);
		tvMsg.setText(msg);
	}

}
