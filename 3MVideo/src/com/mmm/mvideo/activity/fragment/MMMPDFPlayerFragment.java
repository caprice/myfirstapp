package com.mmm.mvideo.activity.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.webtrends.WebtrendsDataCollectionHelper;
import com.webtrends.mobile.analytics.WebtrendsDataCollector;

/**
 * @author Eric Liu
 * 
 */
public class MMMPDFPlayerFragment extends MMMBasePlayerFragment {
	private WebView webView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmm.mvideo.activity.fragment.MMMBaseFragment#onCreateView(android
	 * .view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pdf_fragment, container, false);
		webView = (WebView) view.findViewById(R.id.webView);
		this.loadPDF();
		return view;
	}

	private void loadPDF() {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.requestFocus();
		Bundle args = getArguments();
		position = args.getInt(FragmentParameterKey.CUR_ITEM_POSITION);
		playList = (ArrayList<MMMVideoItem>) args.get(FragmentParameterKey.CUR_ITEM_LIST);
		MMMVideoItem curItem = playList.get(position);
		webView.setWebViewClient(new MyWebViewClient());
		String pdfUrl = curItem.getUrl();
		webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
		// webView.loadUrl("file:///android_asset/temp.html");
		WebtrendsDataCollectionHelper.getInstance().onScreenView(getTag() + "/MMMPDFPlayerFragment", "read PDF : " + "http://docs.google.com/gview?embedded=true&url=" + pdfUrl, "View", null, "Read PDF");
		webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}

	final class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("WebView", "onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url) {
			Log.d("WebView", "onPageFinished ");
			super.onPageFinished(view, url);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		WebtrendsDataCollectionHelper.getInstance().onViewStart("HomeActivity/MMMPDFPlayerFragment", null);
	}

	@Override
	public void onStop() {
		super.onStop();
		WebtrendsDataCollectionHelper.getInstance().onViewEnd("HomeActivity/MMMPDFPlayerFragment", null);
	}
}
