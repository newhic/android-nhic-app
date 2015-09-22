package apps4christ.android.nhicapp.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

import android.widget.TextView;
import org.jsoup.nodes.Element;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import apps4christ.android.nhicapp.R;

/**
 * Created by mjmerin on 3/12/15.
 */
public class JSoupActivity extends Activity {
    private String url;
    private Tracker dbgTracker;

    public void onCreate(Bundle savedInstanceState) {

        Intent intent;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcontent_main);

        intent = getIntent();
        url = intent.getDataString();

        ConnectionDetector cd = new ConnectionDetector(
                this);

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent)
            new ParseContent().execute();
        else
            cd.showAlertDialog();

        String trackerId =  getResources().getString(R.string.trackingId);

        dbgTracker = GoogleAnalytics.getInstance(this)
                .newTracker(trackerId);
    }

    @Override
    public void onResume() {
        super.onResume();

        dbgTracker.setScreenName("HTML Content");
        dbgTracker.send(new HitBuilders.AppViewBuilder().build());

    }

    // Title AsyncTask
    private class ParseContent extends AsyncTask<Void, Void, Void> {
        String title;
        String content;

        private View progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar = findViewById(R.id.jSoupLoadingSpinner);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Elements links;

            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                content = "";
                title = document.body().getElementsByTag("h1").text();

                links = document.select("p");
                for(Element link : links) {
                    content = content + System.getProperty("line.separator") + System.getProperty("line.separator") + link.text();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            progressBar.setVisibility(View.GONE);

            TextView txtTitle = (TextView) findViewById(R.id.webTitle);
            txtTitle.setText(title);
            txtTitle.setTypeface(null, Typeface.BOLD);
            txtTitle.setTextSize(20);

            TextView txtContent = (TextView) findViewById(R.id.webContent);
            txtContent.setMovementMethod(new ScrollingMovementMethod());
            txtContent.setText(content);
            txtContent.setTextSize(15);
            //mProgressDialog.dismiss();
        }
    }
}
