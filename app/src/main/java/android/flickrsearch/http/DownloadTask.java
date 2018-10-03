package android.flickrsearch.http;

import android.flickrsearch.utils.ImageCache;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.flickrsearch.utils.Constants.GET_REQUEST;

public class DownloadTask implements Runnable {
    private String url;
    private String key;
    private DownloadResultUpdateTask resultUpdateTask;

    public DownloadTask(String url, String key,
                        DownloadResultUpdateTask updateTask) {
        this.url = url;
        this.key = key;
        resultUpdateTask = updateTask;
    }

    @Override
    public void run() {
        ImageCache imageCache = new ImageCache(ImageCache.getCacheSize());
        Bitmap bitmap = imageCache.getBitmapFromMemCache(key);
        if (bitmap == null) {
            bitmap = downloadImage(url);
            imageCache.addBitmapToMemoryCache(key, bitmap);
        }

        resultUpdateTask.setBitmap(bitmap);
        DownloadManager.getDownloadManager().getMainThreadExecutor()
                .execute(resultUpdateTask);
    }

    private Bitmap downloadImage(String strUrl) {
        Bitmap bitmap = null;
        HttpURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(strUrl);
            httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod(GET_REQUEST);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.connect();

            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream = httpsURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }

        }
        return bitmap;
    }
}
