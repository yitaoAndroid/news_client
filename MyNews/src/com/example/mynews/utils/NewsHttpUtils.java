package com.example.mynews.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mynews.R;
import com.example.mynews.beans.News;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class NewsHttpUtils {
	private static NewsHttpUtils newsHttpUtils;
	private LruCache<String, Bitmap> cache;

	private NewsHttpUtils() {
		int maxSize = (int) (Runtime.getRuntime().freeMemory() * 0.75);
		cache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getHeight() * value.getRowBytes();
			}
		};
	}

	public static NewsHttpUtils getInstance() {
		if (newsHttpUtils == null) {
			newsHttpUtils = new NewsHttpUtils();
		}
		return newsHttpUtils;
	}

	public void getNewsTextFromNet(final Handler handler, final int currPage) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String appId = "17590";
				String secr = "8b5a1519fec243f1bbbf8780114db20b";
				String URL_PATH = "https://route.showapi.com/198-1?num=20&page="
						+ currPage
						+ "&showapi_appid="
						+ appId
						+ "&showapi_timestamp="
						+ StringUtils.parseDate_phoneLocation_api(System
								.currentTimeMillis()) + "&showapi_sign=" + secr;
				appId = null;
				secr = null;
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(URL_PATH);
				HttpResponse response;
				String info = null;
				try {
					response = client.execute(get);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						info = EntityUtils.toString(response.getEntity());
						ArrayList<News> arrayList = parseNum(info);
						Message message = new Message();
						message.what = Constance.ENTERTAIN_TEXT_OK;
						Bundle data = new Bundle();
						data.putSerializable("entertain_text", arrayList);
						message.setData(data);
						handler.sendMessage(message);
						data = null;
						message = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private ArrayList<News> parseNum(String info) {
		News news = null;
		ArrayList<News> arrayList = new ArrayList<News>();
		try {
			JSONObject jsonObject = new JSONObject(info);
			JSONObject bodyJsonObject = jsonObject
					.getJSONObject("showapi_res_body");
			JSONArray newsArray = bodyJsonObject.getJSONArray("newslist");
			for (int i = 0; i < newsArray.length(); i++) {
				JSONObject newsObject = newsArray.getJSONObject(i);
				news = new News();
				news.setTitle(newsObject.getString("title"));
				news.setTime(newsObject.getString("ctime"));
				news.setImgUri(newsObject.getString("picUrl"));
				System.out.println(newsObject.getString("picUrl"));
				news.setDetailUri(newsObject.getString("url"));
				news.setDescription(newsObject.getString("description"));
				arrayList.add(news);
				news = null;
				newsObject = null;
			}
			jsonObject = null;
			bodyJsonObject = null;
			newsArray = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public class TaskForImage extends AsyncTask<Void, Void, ImageView> {
		private Context context;
		private ImageView imageView;
		private String imagePath;

		public TaskForImage(Context context, ImageView imageView) {
			this.context = context;
			this.imageView = imageView;
			imagePath = (String) imageView.getTag();
		}

		@Override
		protected ImageView doInBackground(Void... params) {
			return doBack(imageView, imagePath);
		}

		@Override
		protected void onPostExecute(ImageView result) {
			if (result == null) {
				return;
			}
			result.setImageBitmap((Bitmap) result.getTag());
			result = null;
		}

		private synchronized ImageView doBack(ImageView imageView,
				String imgPath) {
			Bitmap bitmap = null;
			String picName = imgPath.substring(imgPath.lastIndexOf("/") + 1,
					imgPath.length());
			// 如果数据中没有图片url
			if (imgPath == null || "".equals(imgPath.trim())) {
				bitmap = cache.get("default");
				if (bitmap == null) {
					bitmap = BitmapUtils.getInstance().decodeResource(context,
							R.drawable.news_default_2);
					cache.put("default", bitmap);
				}
				imageView.setTag(bitmap);
				bitmap = null;
				return imageView;
			}
			// 从缓存中获取
			bitmap = cache.get(picName);
			if (bitmap == null) {
				// 从文件中获取
				bitmap = getImageFromFile(picName);
			}
			if (bitmap == null) {
				// 从网络中获取
				bitmap = getImageFromNet(imgPath, picName);
			}
			imageView.setTag(bitmap);
			return imageView;
		}

		private Bitmap getImageFromFile(String picName) {
			Bitmap bitmap = null;
			File file = getFile(picName);
			bitmap = BitmapUtils.getInstance().decodeFile(
					file.getAbsolutePath());
			file = null;
			return bitmap;
		}

		private Bitmap getImageFromNet(String uriPath, String picName) {
			Bitmap bitmap = null;
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(uriPath);
			HttpResponse response;
			try {
				response = client.execute(get);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					byte[] data = EntityUtils.toByteArray(response.getEntity());
					bitmap = BitmapUtils.getInstance().decodeByteArray(data);
					if (bitmap != null) {
						cache.put(picName, bitmap);
						OutputStream stream = new FileOutputStream(
								getFile(picName));
						bitmap.compress(CompressFormat.JPEG, 100, stream);
						stream.close();
						stream = null;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		private File getFile(String picName) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/news");
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(file.getAbsolutePath() + "/img");
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(file.getAbsoluteFile() + "/" + picName);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}

	}
}
