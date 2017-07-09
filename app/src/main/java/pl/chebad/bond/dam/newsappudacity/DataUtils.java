package pl.chebad.bond.dam.newsappudacity;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Damian Bondaruk on 2017-07-09.
 *
 */

final class DataUtils {
    private static final String LOG_TAG = DataUtils.class.getSimpleName();
    private static final String HTTP_BASE_REQUEST = "http://content.guardianapis.com/search?q=";
    private static final String API_KEY = "&api-key=test";
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String REQUEST_METHOD = "GET";
    private static final String JSON_RESPONSE = "response";
    private static final String JSON_RESULTS = "results";
    private static final String SECTION_TITTLE = "sectionName";
    private static final String NOT_FOUND = "Nothing here.";
    private static final String ARTICLE_TITTLE = "webTittle";
    private static final String URL_SITE = "webUrl";

    static ArrayList<Data> fetchData(String query) {
        URL url = createUrl(query);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with making HTTP request.", e);
        }

        return extractFromJSON(jsonResponse);
    }

    private static URL createUrl(String query) {
        URL url = null;
        try {
            url = new URL(HTTP_BASE_REQUEST + query + API_KEY);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with building URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with retrieving the JSON response.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }

    private static ArrayList<Data> extractFromJSON(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        ArrayList<Data> newsList = new ArrayList<>();

        try {
            JSONObject news = new JSONObject(json);

            if (news.has(JSON_RESPONSE)) {
                JSONObject response = news.getJSONObject(JSON_RESPONSE);

                if (response.has(JSON_RESULTS)) {
                    JSONArray results = response.getJSONArray(JSON_RESULTS);

                    for (int i = 0; i < results.length(); i++) {
                        String articleTittle, sectionTittle, urlSite;

                        //creating a single news instance
                        JSONObject singleNews = results.getJSONObject(i);
                        if (singleNews.has(SECTION_TITTLE)) {
                            sectionTittle = singleNews.getString(SECTION_TITTLE);
                        } else {
                            sectionTittle = NOT_FOUND;
                        }

                        if (singleNews.has(ARTICLE_TITTLE)){
                            articleTittle = singleNews.getString(ARTICLE_TITTLE);
                        } else {
                            articleTittle = NOT_FOUND;
                        }

                        if (singleNews.has(URL_SITE)){
                            urlSite = singleNews.getString(URL_SITE);
                        } else {
                            urlSite = NOT_FOUND;
                        }

                        newsList.add(new Data(articleTittle,sectionTittle,urlSite));
                    }
                }

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing json file.", e);
        }

        return newsList;
    }
}
