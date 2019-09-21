package activities.activity_gallery;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.windspeeddeductiontool.R;

public class UriAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Uri[] photoURIs;

    UriAdapter(LayoutInflater inflater, Uri[] uris){
        this.inflater = inflater;
        photoURIs = uris;
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
        ImageButton imageButtonPhoto = itemView.findViewById(R.id.image_view_cell_item);
        imageButtonPhoto.setImageURI(photoURIs[i]);
        imageButtonPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("MY TAG", "LONG CLICKED");
                return true;
            }
        });

        return itemView;
    }


}
