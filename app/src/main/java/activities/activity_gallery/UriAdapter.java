package activities.activity_gallery;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;

public class UriAdapter extends BaseAdapter implements IGalleryActivityMVP.IGalleryActivityAdapterView{
    private LayoutInflater inflater;
    private ArrayList<Uri> photoURIs;
    private Drawable highlight;

    UriAdapter(LayoutInflater inflater, ArrayList<Uri> uris, Drawable highlight){
        this.inflater = inflater;
        photoURIs = uris;
        this.highlight = highlight;
    }

    @Override
    public int getCount() {
        return photoURIs.size();
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
        imageViewPhoto.setImageURI(photoURIs.get(i));
        imageViewPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO: do callback here indicating it got selected
                imageViewPhoto.setBackground(highlight);
                return true;
            }
        });

        return itemView;
    }


}
