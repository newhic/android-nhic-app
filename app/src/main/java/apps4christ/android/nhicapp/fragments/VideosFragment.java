package apps4christ.android.nhicapp.fragments;


import apps4christ.android.nhicapp.main.ConnectionDetector;
import apps4christ.android.nhicapp.R;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


public class VideosFragment extends Fragment {
	public VideosFragment() {
	}

	private View videosFragView;
	private WebView webView;
	private String url;
	private Tracker dbgTracker;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.videos_main, container, false);
		videosFragView = rootView;

		/* Check for internet connectivity to avoid exceptions */
		ConnectionDetector cd = new ConnectionDetector(
				videosFragView.getContext());

		Boolean isInternetPresent = cd.isConnectingToInternet(); // true or
																	// false

		if (isInternetPresent) {
            webView = (WebView) videosFragView.findViewById(R.id.webView1);
            customViewContainer = (FrameLayout) videosFragView.findViewById(R.id.customViewContainer);

            mWebViewClient = new myWebViewClient();
            mWebChromeClient = new myWebChromeClient();

            webView.setWebViewClient(mWebViewClient);
            webView.setWebChromeClient(mWebChromeClient);

			webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSaveFormData(true);

			url = "http://www.youtube.com/user/NewHopeIC";
			webView.loadUrl(url);

		} else {
			cd.showAlertDialog();
		}

        String trackerId =  getResources().getString(R.string.trackingId);

		dbgTracker = GoogleAnalytics.getInstance(videosFragView.getContext())
				.newTracker(trackerId);

		return rootView;
	}

    public boolean inCustomView() {
        return (mCustomView != null);
    }

    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        webView.onPause();
    }

	@Override
	public void onResume() {
		super.onResume();

        webView.onResume();

		dbgTracker.setScreenName("Videos");
		dbgTracker.send(new HitBuilders.AppViewBuilder().build());
	}

    @Override
    public void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        if (inCustomView()) {
            hideCustomView();
        }
    }



    class myWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view,CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            webView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            webView.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }


}
