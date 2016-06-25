package com.example.mynews.activity;

import com.example.mynews.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
//import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class EntertainDetail extends Activity implements OnClickListener {
	private WebView detailView;
	private ProgressBar progressBar;
	private ImageView backImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.entertain_detail_view);

		backImageView = (ImageView) findViewById(R.id.news_detail_back_iv);
		backImageView.setOnClickListener(this);

		detailView = (WebView) findViewById(R.id.entertain_detail_webiew);
		// 设置可以支持缩放
		detailView.getSettings().setSupportZoom(true);
		// 设置出现缩放工具
		detailView.getSettings().setBuiltInZoomControls(true);
		// 扩大比例的缩放
		detailView.getSettings().setUseWideViewPort(true);
		// 自适应屏幕
		detailView.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		detailView.getSettings().setLoadWithOverviewMode(true);
		// 支持js
		detailView.getSettings().setJavaScriptEnabled(true);
		progressBar = (ProgressBar) findViewById(R.id.entertain_detail_webview_pb);
		progressBar.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		String uri = intent.getStringExtra("uri");

		detailView.setWebViewClient(new MyWebViewClient());
		// detailView.setWebChromeClient(new MyWebChromeClient());
		detailView.loadUrl(uri);
		uri = null;
		intent = null;
	}

	/*
	 * private class MyWebChromeClient extends WebChromeClient {
	 * 
	 * @Override public void onProgressChanged(WebView view, int newProgress) {
	 * super.onProgressChanged(view, newProgress);
	 * progressBar.setProgress(newProgress); if (newProgress == 100) {
	 * progressBar.setVisibility(View.GONE); progressBar = null; } }
	 * 
	 * }
	 */

	private class MyWebViewClient extends WebViewClient {
		private ProgressDialog progressDialog;
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progressDialog = new ProgressDialog(EntertainDetail.this);
			progressDialog.setMessage("加载中...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			
			progressDialog.dismiss();
			
			super.onPageFinished(view, url);
		}
	}

	@Override
	public void onBackPressed() {
		detailView.destroy();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_detail_back_iv:
			onBackPressed();
			break;
		default:
			break;
		}
	}
}
