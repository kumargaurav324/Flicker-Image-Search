package android.flickrsearch.ui;

import android.flickrsearch.R;
import android.flickrsearch.http.DownloadManager;
import android.flickrsearch.http.DownloadResultUpdateTask;
import android.flickrsearch.http.DownloadTask;
import android.flickrsearch.models.PhotoModel;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.flickrsearch.utils.Constants.FAMILY;
import static android.flickrsearch.utils.Constants.FRIEND;
import static android.flickrsearch.utils.Constants.PUBLIC;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<PhotoModel> photoModels;

    ImageAdapter(ArrayList<PhotoModel> photoModels) {
        this.photoModels = photoModels;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_items_layout, viewGroup, false);
        return new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        imageViewHolder.imageView.setImageBitmap(null);
        String imageUrl = "http://farm" + photoModels.get(i).getFarm() + ".static.flickr.com/" + photoModels.get(i).getServer()
                + "/" + photoModels.get(i).getId() + "_" + photoModels.get(i).getSecret() + ".jpg";
        loadImage(imageUrl, imageViewHolder.imageView, photoModels.get(i).getId());
        imageViewHolder.tVTitle.setText(photoModels.get(i).getTitle());
        imageViewHolder.tVOwner.setText("Owner : " + photoModels.get(i).getOwner());
        imageViewHolder.tVRelation.setText(getRelation(photoModels.get(i).getIsPublic(), photoModels.get(i).getIsFriend()
                , photoModels.get(i).getIsFamily()));
    }

    private void loadImage(String url, ImageView imageView, String key) {
        DownloadResultUpdateTask updateTask = new DownloadResultUpdateTask(imageView);

        DownloadTask downloadTask = new DownloadTask(url, key, updateTask);
        DownloadManager.getDownloadManager().runDownload(downloadTask);

    }

    @Override
    public int getItemCount() {
        return photoModels.size();
    }

    private String getRelation(String isPublic, String isFriend, String isFamily) {
        if (isPublic.equals("1")) {
            return PUBLIC;
        }
        if (isFriend.equals("1")) {
            return FRIEND;
        }
        if (isFamily.equals("1")) {
            return FAMILY;
        }
        return "";
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tVTitle;
        TextView tVOwner;
        TextView tVRelation;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            tVTitle = itemView.findViewById(R.id.title);
            tVOwner = itemView.findViewById(R.id.owner);
            tVRelation = itemView.findViewById(R.id.relation);
        }
    }
}
