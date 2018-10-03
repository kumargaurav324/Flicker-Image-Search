package android.flickrsearch.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.flickrsearch.R;
import android.flickrsearch.http.ApiCallCompleted;
import android.flickrsearch.http.DownloadManager;
import android.flickrsearch.http.SearchResultTask;
import android.flickrsearch.http.SearchTask;
import android.flickrsearch.models.PhotoModel;
import android.flickrsearch.utils.DetectInternet;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ApiCallCompleted {

    RecyclerView recyclerView;
    TextView tVDefault;
    String query;
    boolean isLoading = false;
    int currentPage = 1;
    int totalPage = 1;
    ArrayList<PhotoModel> photoModels = new ArrayList<>();
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tVDefault = findViewById(R.id.default_text);
        recyclerView = findViewById(R.id.recycler_view);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        imageAdapter = new ImageAdapter(photoModels);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                             @Override
                                             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                 super.onScrollStateChanged(recyclerView, newState);
                                             }

                                             @Override
                                             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                 super.onScrolled(recyclerView, dx, dy);

                                                 int visibleItemCount = gridLayoutManager.getChildCount();
                                                 int totalItemCount = gridLayoutManager.getItemCount();
                                                 int pastVisibleItems =
                                                         gridLayoutManager.findFirstVisibleItemPosition();

                                                 if (visibleItemCount + pastVisibleItems >= totalItemCount && !isLoading) {
                                                     isLoading = true;
                                                     if (++currentPage <= totalPage) {

                                                         SearchResultTask searchResultTask = new SearchResultTask(MainActivity.this);

                                                         SearchTask searchTask = new SearchTask(query, String.valueOf(currentPage), searchResultTask);
                                                         DownloadManager.getDownloadManager().runDownload(searchTask);
                                                         Toast.makeText(MainActivity.this, getResources().getString(R.string.loading), Toast.LENGTH_LONG).show();
                                                     }
                                                 }
                                             }
                                         }
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            currentPage = 1;
            totalPage = 1;
            photoModels.clear();
            tVDefault.setVisibility(View.VISIBLE);
            tVDefault.setText(getResources().getString(R.string.loading));
            isLoading = false;

            if (DetectInternet.isConnectedToInternet(MainActivity.this)) {
                SearchResultTask searchResultTask = new SearchResultTask(MainActivity.this);

                SearchTask searchTask = new SearchTask(query, String.valueOf(currentPage), searchResultTask);
                DownloadManager.getDownloadManager().runDownload(searchTask);

            } else {
                DialogFragment dialogFragment = AlertDialogFragment.newInstance(getResources().getString(R.string.no_internet));
                dialogFragment.show(getSupportFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<PhotoModel> photoModelArrayList, int curPage, int totPage) {

        currentPage = curPage;
        totalPage = totPage;
        photoModels.addAll(photoModelArrayList);
        imageAdapter.notifyDataSetChanged();
        tVDefault.setVisibility(View.GONE);
        isLoading = false;
    }

    @Override
    public void onFailure() {
        DialogFragment newFragment = AlertDialogFragment.newInstance(getResources().getString(R.string.error));
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

}
