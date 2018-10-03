package android.flickrsearch.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

public class ImageCache extends LruCache<String, Bitmap> {

    public ImageCache(int maxSize) {
        super(maxSize);
    }

    public static int getCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // use 1/8th of the available memory for this memory cache.
        return maxMemory / 8;
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
        return value.getByteCount() / 1024;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return get(key);
    }

}


