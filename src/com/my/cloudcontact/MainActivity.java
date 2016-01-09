package com.my.cloudcontact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.cloudcontact.bean.AccountInfo;
import com.my.cloudcontact.bean.Response;
import com.my.cloudcontact.bean.Urls;
import com.my.cloudcontact.bean.UserDownloadHistory;
import com.my.cloudcontact.bean.UserInfo;
import com.my.cloudcontact.bean.Constant.Preference;
import com.my.cloudcontact.http.AjaxCallBack;
import com.my.cloudcontact.http.AjaxParams;
import com.my.cloudcontact.util.TostHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity  extends BaseActivity {
	Button mButtonFrind;
	private Context context;
	Button mBtnImport;
	ProgressDialog dialog;
	ListView mListViewLog;
	TextView mTextViewContractNum;//未下载人数
	TextView mTextViewZuijinGenXin;//上一次下载人数
	TextView mTextViewTotalNum;//总下载人数
	TextView mTextViewServerStatus;//连接服务器状态
	
	private final int UPDATE_DIAL = 100;
	public static final int have_net = 101;
	public static  final int no_net = 102;
	public static  final int update_ui = 103;
	
	private ArrayAdapter<String> historyAdapter;
	String uname, pwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		uname = preferences.getString(Preference.UNAME, null);
		pwd = preferences.getString(Preference.PWD, null);
		this.context = this;
		mButtonFrind = (Button) findViewById(R.id.ButtonFrind);
		mBtnImport = (Button) findViewById(R.id.btnImport);
		mListViewLog = (ListView) findViewById(R.id.ListViewLog);
		mTextViewContractNum = (TextView) findViewById(R.id.TextViewContractNum);
		mTextViewZuijinGenXin = (TextView) findViewById(R.id.TextViewZuijinGenXin);
		mTextViewTotalNum = (TextView) findViewById(R.id.TextViewTotalNum);
		mTextViewServerStatus = (TextView) findViewById(R.id.TextViewServerStatus);
		historyAdapter = new ArrayAdapter<String>(context, R.layout.item_user_download_history);
		mListViewLog.setAdapter(historyAdapter);
		mButtonFrind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), FriendListActivity.class);
				startActivity(intent);
			}
		});
		mBtnImport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				importContact();
			}
		});
		
		NetCheckThread mNetCheckThread = new NetCheckThread(context,mHandler, Urls.IP);
		mNetCheckThread.start();
		
	}
	void updateUI(){
		login(uname, pwd);
		getHistory();
	}
	/* 获取历史记录接口
	 * 
	 * */
	protected void getHistory(){
		 // 请求联系人数据
		dialog = new ProgressDialog(this);
		dialog.setMessage("获取下载历史...");
		dialog.show();
		AjaxParams param = new AjaxParams();
		param.put("buss", Urls.gethistory);
		param.put("userid", LoginActivity.mUserInfo.getUserid());
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>(){

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Response<List<UserDownloadHistory>> response = new Gson().fromJson(t, 
						new TypeToken<Response<ArrayList<UserDownloadHistory>>>(){}.getType());
				if(response.getResult()){
					List<UserDownloadHistory> mListUserDownloadHistory = response.getResponse();
					if(mListUserDownloadHistory.size() > 0){
						historyAdapter.clear();
						for(int i = 0;i<mListUserDownloadHistory.size();i++){
							
							historyAdapter.add(mListUserDownloadHistory.get(i).getCurrtime()+" 增加了"+mListUserDownloadHistory.get(i).getAddfans()+"个粉丝");
						}
						historyAdapter.notifyDataSetChanged();
					}
				}
				else{
					TostHelper.ToastLg(response.getMessage(), getActivity());
				}
				dialog.cancel();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.cancel();
			}
		});
		
	}
	/* 获取联系人接口
	 * */
	protected void importContact() {
		 // 请求联系人数据
		dialog = new ProgressDialog(this);
		dialog.setMessage("获取联系人信息中...");
		dialog.show();
		AjaxParams param = new AjaxParams();
		param.put("buss", Urls.getMobile);
		param.put("userid", LoginActivity.mUserInfo.getUserid());
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>(){

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					Log.i("--tom", "ready ----------11111111111111111111111");
					dialog.setMessage("导入联系人中...");
					dialog.setProgress(0);
					dialog.setMax(100);
					MyImportAsycTask task = new MyImportAsycTask(dialog);
					task.execute(t);
//					importContactToGroup(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.cancel();
			}
		});
		
	}
	
	class MyImportAsycTask extends AsyncTask<String, Integer, List<ContactsInfo>>{
		
		
		private MyImportAsycTask(ProgressDialog dial){
 			Log.i("--tom", "ready ---------- 2222222222222222222222222222222");
		}

		@Override
		protected List<ContactsInfo> doInBackground(String... params) {
			 
			try {
				Response<List<ContactsInfo>> response = new Gson().fromJson(params[0], 
						new TypeToken<Response<ArrayList<ContactsInfo>>>(){}.getType());
					List<ContactsInfo> data;
					if(response.getResult()){
						
						data = response.getResponse();
						if(data.size()>0){
							ContactsUtils.deleteAllPhone(context, 1);
							ContactsUtils.deleteAllPhone(context, 0);
						}
					Log.i("--tom", "ready import contacts -->" + params[0] );
					Log.i("--tom", "ready import contacts size()-->" + data.size() );

//					String acountName = data.get(0).getAccountInfo()
//							.getAccount_name();
//					long groupId = ContactsUtils.getGroupIdByGroupName(
//							acountName, context);
//					if (groupId == -1) {
//						// 创建群组之后，再添加
//						groupId = ContactsUtils.createGroup(acountName, context);
//					}
						for(int i = 0; i < data.size(); i += 500) {
							ArrayList<ContactsInfo> list = new ArrayList<ContactsInfo>(); 
							for(int j = i; j < i+500 && j < data.size(); j++){
								list.add(data.get(j));
							}
							ContactsUtils.addContacts(list, getActivity(), i);
							list.clear();
							Message msg = mHandler.obtainMessage();
							msg.what = UPDATE_DIAL;
							int progress = i / data.size() * 100; 
							Log.i("--tom", "progress ->" + progress);
							msg.obj = progress;
							mHandler.sendMessage(msg);
						}
						updateUI();
					}else{
						TostHelper.ToastLg(response.getMessage(), getActivity());
					}
				} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
		
		
		@Override
		protected void onPostExecute(List<ContactsInfo> result) {
			super.onPostExecute(result);
			if(dialog != null){
				dialog.cancel();
			}
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		updateUI();
	}
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_DIAL:
				int progress = (Integer) msg.obj; 
//				dialog.setProgress(progress);
				dialog.setMessage("导入进度-->" + progress + "%");
				break;
			case have_net:
				mTextViewServerStatus.setText("连接服务器成功");
				break;
			case no_net:
				mTextViewServerStatus.setText("连接服务器失败");
				break;
			case update_ui:
				mTextViewContractNum.setText(LoginActivity.mUserInfo.getCurrnum()+"人");
				mTextViewZuijinGenXin.setText("最近已更新"+LoginActivity.mUserInfo.getStorynum()+"条通讯录");
				mTextViewTotalNum.setText("通讯录现在已有"+LoginActivity.mUserInfo.getTotalnum()+"人");
				break;
			default:
				break;
			}
		}
		
	};
	
	 /* 登录接口
	  * */
	private void login(String userName, String pwd) {
		AjaxParams param = new AjaxParams();
		param.put("buss", "login");
		param.put("username", userName);
		param.put("password", pwd);
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				parseData(t);
			}

			private void parseData(String t) {
				Response<List<UserInfo>> response = new Gson().fromJson(t, 
						new TypeToken<Response<List<UserInfo>>>(){}.getType());
				
				if(response.getResult()){
					List<UserInfo> listUserInfo = response.getResponse();
					LoginActivity.mUserInfo = listUserInfo.get(0);
					mHandler.sendEmptyMessage(update_ui);
				}else{
					TostHelper.ToastLg(response.getMessage(), getActivity());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo,
					String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(MainActivity.this, strMsg, Toast.LENGTH_SHORT).show();
			}
			
		});
	}
}
