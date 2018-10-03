package android.flickrsearch.http;

import android.flickrsearch.models.PhotoModel;
import android.flickrsearch.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.flickrsearch.utils.Constants.AND;
import static android.flickrsearch.utils.Constants.EQUALS;
import static android.flickrsearch.utils.Constants.FAILURE;
import static android.flickrsearch.utils.Constants.GET_REQUEST;
import static android.flickrsearch.utils.Constants.KEY_FARM;
import static android.flickrsearch.utils.Constants.KEY_ID;
import static android.flickrsearch.utils.Constants.KEY_ISFAMILY;
import static android.flickrsearch.utils.Constants.KEY_ISFRIEND;
import static android.flickrsearch.utils.Constants.KEY_ISPUBLIC;
import static android.flickrsearch.utils.Constants.KEY_OWNER;
import static android.flickrsearch.utils.Constants.KEY_PAGE;
import static android.flickrsearch.utils.Constants.KEY_PAGES;
import static android.flickrsearch.utils.Constants.KEY_PHOTO;
import static android.flickrsearch.utils.Constants.KEY_PHOTOS;
import static android.flickrsearch.utils.Constants.KEY_SECRET;
import static android.flickrsearch.utils.Constants.KEY_SERVER;
import static android.flickrsearch.utils.Constants.KEY_STATUS;
import static android.flickrsearch.utils.Constants.KEY_TITLE;
import static android.flickrsearch.utils.Constants.MAX_SIZE;
import static android.flickrsearch.utils.Constants.OK;
import static android.flickrsearch.utils.Constants.QUERY_PAGE;
import static android.flickrsearch.utils.Constants.QUERY_TEXT;
import static android.flickrsearch.utils.Constants.QUERY_URL;
import static android.flickrsearch.utils.Constants.SUCCESS;

public class SearchTask implements Runnable {
    private String query;
    private String page;
    private SearchResultTask searchResultTask;

    public SearchTask(String query, String page,
                      SearchResultTask resultTask) {
        this.query = query;
        this.page = page;
        searchResultTask = resultTask;
    }

    @Override
    public void run() {
        String url = QUERY_URL + AND + QUERY_TEXT + EQUALS + query + AND + QUERY_PAGE + EQUALS + page;

        String data = getData(url);
        int status = FAILURE;
        int currentPage = 0;
        int totalPage = 0;
        ArrayList<PhotoModel> photoModels = new ArrayList<>();
        try {
            JSONObject jsonObjectData = new JSONObject(data);

            if (JsonParser.getString(jsonObjectData, KEY_STATUS).equals(OK)) {
                status = SUCCESS;
                JSONObject jsonObject = jsonObjectData.getJSONObject(KEY_PHOTOS);
                currentPage = Integer.parseInt(JsonParser.getString(jsonObject, KEY_PAGE));
                totalPage = Integer.parseInt(JsonParser.getString(jsonObject, KEY_PAGES));
                JSONArray jsonArray = jsonObject.getJSONArray(KEY_PHOTO);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setId(JsonParser.getString(jsonObject1, KEY_ID));
                    photoModel.setOwner(JsonParser.getString(jsonObject1, KEY_OWNER));
                    photoModel.setSecret(JsonParser.getString(jsonObject1, KEY_SECRET));
                    photoModel.setServer(JsonParser.getString(jsonObject1, KEY_SERVER));
                    photoModel.setFarm(JsonParser.getString(jsonObject1, KEY_FARM));
                    photoModel.setTitle(JsonParser.getString(jsonObject1, KEY_TITLE));
                    photoModel.setIsPublic(JsonParser.getString(jsonObject1, KEY_ISPUBLIC));
                    photoModel.setIsFriend(JsonParser.getString(jsonObject1, KEY_ISFRIEND));
                    photoModel.setIsFamily(JsonParser.getString(jsonObject1, KEY_ISFAMILY));

                    photoModels.add(photoModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        searchResultTask.setResult(photoModels, status, currentPage, totalPage);
        DownloadManager.getDownloadManager().getMainThreadExecutor()
                .execute(searchResultTask);
    }

    private String getData(String strUrl) {
        StringBuilder builder = new StringBuilder();
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(strUrl);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod(GET_REQUEST);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.connect();

            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                return "";
            }

            InputStream stream = httpsURLConnection.getInputStream();
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] rawBuffer = new char[MAX_SIZE];
            int readSize;
            while (((readSize = reader.read(rawBuffer)) != -1)) {
                if (readSize > MAX_SIZE) {
                    readSize = MAX_SIZE;
                }
                builder.append(rawBuffer, 0, readSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return builder.toString();
    }
}
