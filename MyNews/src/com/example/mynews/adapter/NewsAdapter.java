package com.example.mynews.adapter;

import java.util.ArrayList;

import com.example.mynews.R;
import com.example.mynews.beans.News;
import com.example.mynews.utils.NewsHttpUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	private ArrayList<News> arrayList;
	private Context context;
	private NewsHttpUtils httpUtils;

	public NewsAdapter(Context context) {
		this.context = context;
		arrayList = new ArrayList<News>();
		httpUtils = NewsHttpUtils.getInstance();
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public News getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHelper helper = null;
		News news = arrayList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.entertain_listview_layout, null);
			helper = new ViewHelper();
			helper.imageView = (ImageView) convertView
					.findViewById(R.id.entertain_listview_iv);
			helper.titleView = (TextView) convertView
					.findViewById(R.id.entertain_listview_title_tv);
			helper.time_desc_View = (TextView) convertView
					.findViewById(R.id.entertain_listview_time_desc_tv);
			convertView.setTag(helper);
		} else {
			helper = (ViewHelper) convertView.getTag();
		}
		helper.imageView.setTag(news.getImgUri());
		httpUtils.new TaskForImage(context, helper.imageView).execute();
		helper.titleView.setText(news.getTitle());
		helper.time_desc_View.setText(news.getTime() + "     "
				+ news.getDescription());
		news = null;
		return convertView;
	}

	private class ViewHelper {
		ImageView imageView;
		TextView titleView;
		TextView time_desc_View;
	}

	public void addAll(ArrayList<News> newses) {
		arrayList.addAll(newses);
	}

	public void clear() {
		arrayList.clear();
	}

}
