package com.my.cloudcontact;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.cloudcontact.bean.Response;
import com.my.cloudcontact.bean.Urls;
import com.my.cloudcontact.friendlist.ItemEntity;
import com.my.cloudcontact.friendlist.ListItemAdapter;
import com.my.cloudcontact.http.AjaxCallBack;
import com.my.cloudcontact.http.AjaxParams;
import com.my.cloudcontact.struct.FriendList;
import com.my.cloudcontact.util.TostHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class FriendListActivity extends BaseActivity {

	/** Item数据实体集合 */
	private ArrayList<ItemEntity> itemEntities;
	/** ListView对象 */
	private ListView listview;
	private EditText mEditTextFunsNum;
	List<FriendList> mFriendList = new ArrayList<FriendList>();
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.activity_friend);
		setTitle("朋友圈");
		listview = (ListView) findViewById(R.id.ListViewFriend);
		mEditTextFunsNum = (EditText) findViewById(R.id.EditTextFunsNum);
		
		
		getFriend();
	}
	/* 获取朋友圈内容接口
	 * 
	 * */
	protected void getFriend() {
		 // 请求联系人数据
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("获取朋友圈...");
		dialog.show();
		AjaxParams param = new AjaxParams();
		param.put("buss", Urls.getfriend);
		param.put("userid", LoginActivity.mUserInfo.getUserid());
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>(){

			@Override
			public void onStart() {
				super.onStart();
				
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				parseData(t);
				dialog.cancel();
			}
			private void parseData(String t) {
				Response<List<FriendList>> response = new Gson().fromJson(t, 
						new TypeToken<Response<ArrayList<FriendList>>>(){}.getType());
				if(response.getResult()){
					mFriendList = response.getResponse();
					initData(mFriendList);
					listview.setAdapter(new ListItemAdapter(context, itemEntities));
				}else{
					TostHelper.ToastLg(response.getMessage(), getActivity());
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
	/* 设置粉丝数接口
	 * 
	 * */
	public void OnClickSubmitFuns(View v){
		 //设置粉丝数
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在设置粉丝数...");
		dialog.show();
		AjaxParams param = new AjaxParams();
		param.put("buss", Urls.setfans);
		param.put("userid", LoginActivity.mUserInfo.getUserid());
		param.put("addfans", mEditTextFunsNum.getText().toString());
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>(){

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				dialog.cancel();
				parseData(t);
			}
			private void parseData(String t) {
				Response<?> response = new Gson().fromJson(t, 
						new TypeToken<Response<?>>(){}.getType());
				if(response.getResult()){
					TostHelper.ToastLg(response.getMessage(), getActivity());
				}else{
					TostHelper.ToastLg(response.getMessage(), getActivity());
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
	/**
	 * 初始化数据
	 */
	private void initData(List<FriendList> friendList) {
		itemEntities = new ArrayList<ItemEntity>();
		for(int i = 0 ;i < friendList.size();i++){
			
			ArrayList<String> urls = new ArrayList<String>();
			String []value = friendList.get(i).getImgurl().split(";");
			for(int k = 0 ;k < value.length;k++){
				if(!value[k].equals(""))
				 urls.add(value[k]);
			}
			ItemEntity entity = new ItemEntity(friendList.get(i).getCurrtime(), friendList.get(i).getMemo(), friendList.get(i).getContent(), urls);
			itemEntities.add(entity);
		}
	}
}
