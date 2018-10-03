package android.flickrsearch.http;

import android.content.Context;
import android.flickrsearch.models.PhotoModel;

import java.util.ArrayList;

import static android.flickrsearch.utils.Constants.SUCCESS;

public class SearchResultTask implements Runnable {
    private Context context;
    private ArrayList<PhotoModel> photoModels;
    private int status;
    private int currentPage;
    private int totalPage;

    public SearchResultTask(Context context) {
        this.context = context;
    }

    void setResult(ArrayList<PhotoModel> photoModels, int status, int currentPage, int totalPage) {
        this.photoModels = photoModels;
        this.status = status;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    @Override
    public void run() {
        ApiCallCompleted apiCallCompleted = (ApiCallCompleted) context;
        if (status == SUCCESS) {
            apiCallCompleted.onSuccess(photoModels, currentPage, totalPage);
            return;
        }
        apiCallCompleted.onFailure();
    }
}
