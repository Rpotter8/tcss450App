package group4.tcss450.uw.edu.challengeapp.wikipedia;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import group4.tcss450.uw.edu.challengeapp.R;

public class WikipediaResult extends AppCompatActivity {

    private static final String PARTIAL_URL =
            "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exintro&titles=";

    private TextView mTitle;
    private TextView mContent;
    private TextView mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wikipedia_result);

        mTitle = (TextView) findViewById(R.id.textViewTitle);
        mContent = (TextView) findViewById(R.id.textViewContent);
        mUrl = (TextView) findViewById(R.id.textViewUrl);

        String title, url;
        Bundle data = getIntent().getExtras();

        if (data != null) {
            title = data.getString("title");
            url = data.getString("url");

            AsyncTask<String, Void, String> task = new WikiContentServiceTask();
            task.execute(PARTIAL_URL, title);

            mTitle.setText(title);
            mUrl.setText(url);
        }
    }


    private void parseJSON(final String json) {
        try {
            JSONObject object = new JSONObject(json);
            JSONObject query = object.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");

            Iterator iterator = pages.keys();
            String key = null;

            while (iterator.hasNext()) {
                key = (String) iterator.next();
            }

            JSONObject pageid = pages.getJSONObject(key);
            String extract = pageid.getString("extract");

            mContent.setText(Html.fromHtml(extract));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class WikiContentServiceTask extends AsyncTask<String, Void, String> {

        private static final String RETURN_FORMAT = "&format=json";

        @Override
        protected String doInBackground(String... params) {
            if (params.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }

            String response = "";
            HttpURLConnection urlConnection = null;
            String url = params[0];     // partial URL
            String query = params[1];   // query to find with wikipedia, example: Rose Flower

            try {
                URL urlObject = new URL(url + query + RETURN_FORMAT);
                urlConnection = (HttpURLConnection) urlObject.openConnection();

                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            }
            catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // mTextView.setText(result);
            parseJSON(result);

        }
    }
}
