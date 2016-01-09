package com.my.cloudcontact;

import java.util.List;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.cloudcontact.bean.Constant.Preference;
import com.my.cloudcontact.bean.Response;
import com.my.cloudcontact.bean.Urls;
import com.my.cloudcontact.bean.UserInfo;
import com.my.cloudcontact.dialog.InputDialog;
import com.my.cloudcontact.http.AjaxCallBack;
import com.my.cloudcontact.http.AjaxParams;
import com.my.cloudcontact.util.AssetUtils;
import com.my.cloudcontact.util.NetworkUtils;
import com.my.cloudcontact.util.TostHelper;

public class LoginActivity extends BaseActivity {

	EditText etUserName, etPwd;
	Button btnSubmit;
	public static UserInfo mUserInfo;
	private Context context;
	String uname, pwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.context = this;
		initView();
	}

	private void initView() {
		uname = preferences.getString(Preference.UNAME, null);
		pwd = preferences.getString(Preference.PWD, null);
		etUserName = (EditText)findViewById(R.id.etUserName);
		etPwd = (EditText)findViewById(R.id.etPwd);
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		 
		etUserName.setText(uname);
		etPwd.setText(pwd);
		setTitle(R.string.app_name);
		addRightBtn(R.string.register, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// not network
                if (!NetworkUtils.getNetworkEnable(etUserName.getContext())) {
                    TostHelper.ToastLg( "没有网络",getBaseContext());
                    return;
                }

                final InputDialog dialog = new InputDialog(getActivity(),R.style.common_dialog);
                dialog.getText1().setHint("请输入用户名");
                dialog.getText2().setHint("请输入密码");
                dialog.getConfirm().setText("注册");
                dialog.getText2().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                dialog.setConfirmListener(new InputDialog.InputDialogInterface() {
                    @Override
                    public void onBtnClick(String et1, String et2, String etUsernum) {
                        register(et1, et2, etUsernum,"0");
                       /* String t = AssetUtils.getDataFromAssets(context, "reg.txt");
    					parseData(t);*/
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("提示");
                dialog.show();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String userName = etUserName.getText().toString().replace(" ", "");
				String passwd = etPwd.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(passwd)){
					Toast.makeText(v.getContext(), "用户名或密码为空", Toast.LENGTH_SHORT).show();
					return;
				} else {
					saveUser(userName,passwd);
					login(userName, passwd);
//					String t = AssetUtils.getDataFromAssets(context, "login.txt");
//					parseData(t);
				}
				
			}
		});
	}
	public void saveUser(String userName, String passwd) {
		preferences.edit().putString(Preference.UNAME, userName).commit();
		preferences.edit().putString(Preference.PWD, passwd).commit();
	}
	/* 注册接口
	 * */
	 private void register(final String user, final String pwd,final String usernum,final String gjz) {

	        // 登陆
	        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
	            TostHelper.ToastLg( "用户名或密码为空",getBaseContext());
	            return;
	        }
	    	AjaxParams param = new AjaxParams();
	    	param.put("buss", "reg");
			param.put("username", user);
			param.put("password", pwd);
			param.put("usernum", usernum);
			param.put("gjz", gjz);
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("注册中...");
			dialog.show();
			getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>() {

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					parseData(t);
					dialog.cancel();
				}

				private void parseData(String t) {
					Response<?> response = new Gson().fromJson(t, 
							new TypeToken<Response<?>>(){}.getType());
					
					if(response.getResult()){//注册成功
						TostHelper.ToastLg(response.getMessage(), getActivity());
					}else if(response.getCode().equals("E01")){//该用户名已经注册
						TostHelper.ToastLg(response.getMessage(), getActivity());
					}else if(response.getCode().equals("E02")){//用户编号已存在
						
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("提示");
						builder.setMessage("该编号已存在，是否加入该组");
						builder.setNegativeButton(R.string.join, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
									
								register(user, pwd, usernum, "1");
								dialog.dismiss();
							}
						});
						builder.setPositiveButton(R.string.giveUp,new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.show();
						TostHelper.ToastLg(response.getMessage(), getActivity());
					}else{
						TostHelper.ToastLg(response.getMessage(), getActivity());
					}
				}

				@Override
				public void onFailure(Throwable t, int errorNo,
						String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
					dialog.cancel();
				}
				
			});
	    }
	 /* 登录接口
	  * */
	private void login(String userName, String pwd) {
		AjaxParams param = new AjaxParams();
		param.put("buss", "login");
		param.put("username", userName);
		param.put("password", pwd);
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("登录中...");
		dialog.show();
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				parseData(t);
				dialog.cancel();
			}

			private void parseData(String t) {
				Response<List<UserInfo>> response = new Gson().fromJson(t, 
						new TypeToken<Response<List<UserInfo>>>(){}.getType());
				
				if(response.getResult()){
					List<UserInfo> listUserInfo = response.getResponse();
					mUserInfo = listUserInfo.get(0);
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
				}else{
					TostHelper.ToastLg(response.getMessage(), getActivity());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo,
					String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
			
		});
	}
	
	
}
