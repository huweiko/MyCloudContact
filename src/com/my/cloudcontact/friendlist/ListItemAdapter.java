package com.my.cloudcontact.friendlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.my.cloudcontact.ImagePagerActivity;
import com.my.cloudcontact.R;
import com.my.cloudcontact.util.TostHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ItemEntity> items;

	public ListItemAdapter(Context ctx, ArrayList<ItemEntity> items) {
		this.mContext = ctx;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_list, null);
			holder.tv_memo = (TextView) convertView.findViewById(R.id.tv_memo);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ItemEntity itemEntity = items.get(position);
		holder.tv_content.setText(itemEntity.getContent());
		holder.tv_memo.setText(itemEntity.getMemo());
		holder.tv_time.setText(itemEntity.getTime());
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();//
		final ArrayList<String> imageUrls = itemEntity.getImageUrls();
		if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
		} else {
			holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
		}
		// 点击回帖九宫格，查看大图
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrls);
			}
		});
		holder.tv_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				copy(itemEntity.getContent(), v.getContext());
				TostHelper.ToastSht("已经复制内容到剪切板", v.getContext());
				
				for(int i = 0;i < imageUrls.size();i++){
					ImageLoader.getInstance().loadImage(imageUrls.get(i), new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							saveBitmap(arg2,arg0.replace("/", "_"));
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				TostHelper.ToastSht("保存图片到DCIM文件夹中", v.getContext());
				  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					  MediaScannerConnection.scanFile(mContext, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/"}, null, null);
					/*  Intent mediaScanIntent = new Intent(
	                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	                    Uri contentUri = Uri.fromFile(out); \\out is your output file
	                    mediaScanIntent.setData(contentUri);
	                    this.sendBroadcast(mediaScanIntent); */
				  } else {
	                	mContext.sendBroadcast(new Intent(
	                            Intent.ACTION_MEDIA_MOUNTED,
	                            Uri.parse("file://"
	                                    + Environment.getExternalStorageDirectory())));
	                }
//				mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://" + Environment.getExternalStorageDirectory())));  
//				MediaScannerConnection.scanFile(mContext, new String[]{Environment.getExternalStorageDirectory()+""}, null, null);
			}
		});
		return convertView;
	}
	/** 
	* 实现文本复制功能 
	* add by wangqianzhou 
	* @param content 
	*/  
	public static void copy(String content, Context context)  
	{  
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setText(content.trim());  
	}  
	public void saveBitmap(Bitmap bm,String filename) { 
		Log.e(getClass().getSimpleName(), "保存图片"); 
		File f = new File(Environment.getExternalStorageDirectory()+"/DCIM/",filename); 
		if (f.exists()) { 
		f.delete(); 
		} 
		try { 
			FileOutputStream out = new FileOutputStream(f); 
			bm.compress(Bitmap.CompressFormat.PNG, 90, out); 
			out.flush(); 
			out.close(); 
			Log.i(getClass().getSimpleName(), "已经保存"); 
		} catch (FileNotFoundException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
	}
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author Administrator
	 * 
	 */
	class ViewHolder {
		
		private TextView tv_content;
		private TextView tv_memo;
		private TextView tv_time;
		private NoScrollGridView gridview;
	}
}
