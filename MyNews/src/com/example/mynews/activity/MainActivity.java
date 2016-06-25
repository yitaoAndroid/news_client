package com.example.mynews.activity;

import java.util.ArrayList;

import com.example.mynews.R;
import com.example.mynews.adapter.NewsAdapter;
import com.example.mynews.beans.News;
import com.example.mynews.utils.AppUtils;
import com.example.mynews.utils.Constance;
import com.example.mynews.utils.NewsHttpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class MainActivity extends Activity implements OnItemClickListener,
		OnScrollListener, OnClickListener {
	private ListView listView;
	private NewsAdapter adapter;
	private MyHandler handler;
	private int currPage = 1;
	private boolean isBottom;
	private ProgressDialog dialog;
	private View footerView;
	private ViewFlipper flipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		showInfoDialog();
		listView = (ListView) findViewById(R.id.news_listview);
		adapter = new NewsAdapter(this);
		listView.setAdapter(adapter);
		footerView = getLayoutInflater().inflate(
				R.layout.entertain_listview_footer, null);
		listView.addFooterView(footerView);
		handler = new MyHandler();
		NewsHttpUtils.getInstance().getNewsTextFromNet(handler, currPage);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
	}

	private void initFilper() {
		ImageView fliperImageViewFirst, fliperImageViewSec, fliperImageViewThird, fliperImageViewFourth, fliperImageViewFifth;
		TextView fliperTextViewFirst, fliperTextViewSec, fliperTextViewThird, fliperTextViewFourth, fliperTextViewFifth;
		fliperImageViewFirst = (ImageView) findViewById(R.id.entertain_filper_first_iv);
		fliperImageViewSec = (ImageView) findViewById(R.id.entertain_filper_sec_iv);
		fliperImageViewThird = (ImageView) findViewById(R.id.entertain_filper_third_iv);
		fliperImageViewFourth = (ImageView) findViewById(R.id.entertain_filper_fourth_iv);
		fliperImageViewFifth = (ImageView) findViewById(R.id.entertain_filper_fifth_iv);

		fliperTextViewFirst = (TextView) findViewById(R.id.entertain_filper_first_tv);
		fliperTextViewSec = (TextView) findViewById(R.id.entertain_filper_sec_tv);
		fliperTextViewThird = (TextView) findViewById(R.id.entertain_filper_third_tv);
		fliperTextViewFourth = (TextView) findViewById(R.id.entertain_filper_fourth_tv);
		fliperTextViewFifth = (TextView) findViewById(R.id.entertain_filper_fifth_tv);
			
		News news = adapter.getItem(0);
		fliperTextViewFirst.setText(news.getTitle());
		NewsHttpUtils httpUtils = NewsHttpUtils.getInstance();
		fliperImageViewFirst.setTag(news.getImgUri());
		httpUtils.new TaskForImage(this, fliperImageViewFirst).execute();

		news = adapter.getItem(1);
		fliperTextViewSec.setText(news.getTitle());
		fliperImageViewSec.setTag(news.getImgUri());
		httpUtils.new TaskForImage(this, fliperImageViewSec).execute();

		news = adapter.getItem(2);
		fliperTextViewThird.setText(news.getTitle());
		fliperImageViewThird.setTag(news.getImgUri());
		httpUtils.new TaskForImage(this, fliperImageViewThird).execute();

		news = adapter.getItem(3);
		fliperTextViewFourth.setText(news.getTitle());
		fliperImageViewFourth.setTag(news.getImgUri());
		httpUtils.new TaskForImage(this, fliperImageViewFourth).execute();

		news = adapter.getItem(4);
		fliperTextViewFifth.setText(news.getTitle());
		fliperImageViewFifth.setTag(news.getImgUri());
		httpUtils.new TaskForImage(this, fliperImageViewFifth).execute();
		
		news = null;
		httpUtils = null;

		flipper = (ViewFlipper) findViewById(R.id.entertain_fliper);
		flipper.setInAnimation(this, R.anim.right_in);
		flipper.setOutAnimation(this, R.anim.left_out);
		flipper.setOnClickListener(this);
		flipper.startFlipping();
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constance.ENTERTAIN_TEXT_OK:
				System.out.println("HAN");
				ArrayList<News> newses = (ArrayList<News>) msg.getData()
						.getSerializable("entertain_text");
				adapter.addAll(newses);
				adapter.notifyDataSetChanged();
				newses = null;
				initFilper();
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				footerView.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		gotoDetail(position);
	}

	private void gotoDetail(int position) {
		News news = adapter.getItem(position);
		String uriPath = news.getDetailUri();
		news = null;
		Intent intent = new Intent(this, EntertainDetail.class);
		intent.putExtra("uri", uriPath);
		startActivity(intent);
		uriPath = null;
		intent = null;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_IDLE:
			if (isBottom) {
				footerView.setVisibility(View.VISIBLE);
				++currPage;
				NewsHttpUtils.getInstance().getNewsTextFromNet(handler,
						currPage);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		isBottom = (firstVisibleItem + visibleItemCount == totalItemCount);
	}

	@SuppressLint("InlinedApi")
	private void showInfoDialog() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		dialog.setMessage("正在努力加载...");
		dialog.show();
		dialog.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.entertain_fliper:
			switch (flipper.getCurrentView().getId()) {
			case R.id.entertain_filper_first:
				gotoDetail(0);
				break;
			case R.id.entertain_filper_sec:
				gotoDetail(1);
				break;
			case R.id.entertain_filper_third:
				gotoDetail(2);
				break;

			case R.id.entertain_filper_fourth:
				gotoDetail(3);
				break;
			case R.id.entertain_filper_fifth:
				gotoDetail(4);
				break;
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		AppUtils.toAnotherApp(this, "com.example.mycontacts");
		super.onBackPressed();
	}
}
