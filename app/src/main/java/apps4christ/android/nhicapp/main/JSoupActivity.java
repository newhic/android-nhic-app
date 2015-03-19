package apps4christ.android.nhicapp.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;

import android.widget.TextView;
import org.jsoup.nodes.Element;

import apps4christ.android.nhicapp.R;

/**
 * Created by mjmerin on 3/12/15.
 */
public class JSoupActivity extends Activity {
    private String url;
    private Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcontent_main);

        intent = getIntent();
        url = intent.getDataString();

        new ParseContent().execute();
    }

    // Title AsyncTask
    private class ParseContent extends AsyncTask<Void, Void, Void> {
        String title;
        String content;

        private View progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar = (View) findViewById(R.id.jSoupLoadingSpinner);
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
