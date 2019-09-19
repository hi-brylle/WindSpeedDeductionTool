package activities.activity_gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.windspeeddeductiontool.R;

public class UriAdapter extends BaseAdapter {
    private Context context;
    private Uri[] photoURIs;
    private LayoutInflater inflater;

    UriAdapter(Context context, Uri[] uris){
        this.context = context;
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
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.cell_gallery_photo, viewGroup, false);
        ImageView imageView = itemView.findViewById(R.id.image_view_cell_item);
        imageView.setImageURI(photoURIs[i]);

        return itemView;
    }


}
