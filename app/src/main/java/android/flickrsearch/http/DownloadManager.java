package android.flickrsearch.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadManager {
    private final ThreadPoolExecutor threadPoolExecutor;

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_TIME = 50;

    private static DownloadManager downloadManager;
    private static MainThreadExecutor mainThreadExecutor;

    static {
        downloadManager = new DownloadManager();
        mainThreadExecutor = new MainThreadExecutor();
    }

    private DownloadManager() {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();

        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, blockingQueue);
    }

    public static DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public void runDownload(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    MainThreadExecutor getMainThreadExecutor() {
        return mainThreadExecutor;
    }
}
