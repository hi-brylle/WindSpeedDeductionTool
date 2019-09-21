package activities.activity_gallery;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.windspeeddeductiontool.R;

public class UriAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Uri[] photoURIs;
    private Drawable highlight;

    UriAdapter(LayoutInflater inflater, Uri[] uris, Drawable highlight){
        this.inflater = inflater;
        photoURIs = uris;
        this.highlight = highlight;
    }

    @Override
    public int getCount() {
        return photoURIs.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = inflater.inflate(R.layout.cell_gallery_photo, viewGroup, false);
        final ImageView imageViewPhoto = itemView.findViewById(R.id.image_view_cell_item);
        imageViewPhoto.setImageURI(photoURIs[i]);
        imageViewPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageViewPhoto.setBackground(highlight);
                return true;
            }
        });

        return itemView;
    }


}
