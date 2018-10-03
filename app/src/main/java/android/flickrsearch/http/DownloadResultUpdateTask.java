package android.flickrsearch.http;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class DownloadResultUpdateTask implements Runnable {
    private ImageView imageView;
    private Bitmap bitmap;

    public DownloadResultUpdateTask(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp;
    }

    @Override
    public void run() {
        imageView.setImageBitmap(bitmap);
    }
}
