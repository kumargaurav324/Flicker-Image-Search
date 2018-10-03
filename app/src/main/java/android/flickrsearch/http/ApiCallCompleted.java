package android.flickrsearch.http;

import android.flickrsearch.models.PhotoModel;

import java.util.ArrayList;

public interface ApiCallCompleted {

    void onSuccess(ArrayList<PhotoModel> photoModelArrayList, int curPage, int totPage);

    void onFailure();
}
