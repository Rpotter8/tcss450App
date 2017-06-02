package group4.tcss450.uw.edu.challengeapp.wikipedia;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import group4.tcss450.uw.edu.challengeapp.R;

/**
 * Recycle view activity that contains a list of most appropriate
 * results from wikipedia.
 */
public class ResultListActivity extends AppCompatActivity {

    private static final String PARTIAL_URL =
            "https://en.wikipedia.org/w/api.php?action=opensearch&search=";

    private String query;

    private List<Article> articleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticlesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        query = getIntent().getStringExtra("query");
        Log.v("EXTRAS", query);

        mAdapter = new ArticlesAdapter(articleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Article article = articleList.get(position);
                // Toast.makeText(getApplicationContext(), article.getTitle() + " is selected!",
                //         Toast.LENGTH_SHORT).show();


                Intent wikipediaIntent = new Intent(ResultListActivity.this, WikipediaResult.class);
                Bundle data = new Bundle();
                data.putString("title", article.getTitle());
                data.putString("url", article.getUrl());
                wikipediaIntent.putExtras(data);
                startActivity(wikipediaIntent);
                // finish();
            }

            // @Override
            // public void onLongClick(View view, int position) {
            //
            // }
        }));

        AsyncTask<String, Void, String> task = new WikiListServiceTask();
        task.execute(PARTIAL_URL, query);
    }

    /**
     * Parse json returned from wikipedia API
     * @param json - json to parse
     */
    private void parseJson(final String json) {
        JSONArray names, headers, urls;
        String title, name, header, url;

        try {
            JSONArray array = new JSONArray(json);
            // title = array.getString(0);
            names = array.getJSONArray(1);      // array of wiki titles
            headers = array.getJSONArray(2);    // array of short wiki description
            urls = array.getJSONArray(3);       // array of urls

            for (int i = 0; i < names.length(); i++) {

                name = names.getString(i);
                header = headers.getString(i);
                url = urls.getString(i);

                Article article = new Article(name, header, url);
                articleList.add(article);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper Service Task to obtain json from wikipedia API.
     * Json format is an array of 5 most appropriate articles.
     */
    private class WikiListServiceTask extends AsyncTask<String, Void, String> {

        private static final String RETURN_FORMAT = "&limit=5&namespace=0&format=json";

        @Override
        protected String doInBackground(String... params) {

            if (params.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }

            String response = "";
            HttpURLConnection urlConnection = null;
            String url = params[0];     // partial URL
            String query = params[1];   // query to get an array of results in json format from wikipedia

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
            parseJson(result);
            mAdapter.notifyDataSetChanged();
        }
    }

}
