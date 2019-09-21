package activities.activity_gallery;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;

public class UriAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Uri> photoURIs;
    private Drawable highlight;

    private GalleryActivityPresenter mPresenter;

    UriAdapter(LayoutInflater inflater, Drawable highlight,
               ArrayList<Uri> uris, GalleryActivityPresenter presenter){
        this.inflater = inflater;
        this.highlight = highlight;
        photoURIs = uris;
        mPresenter = presenter;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.cell_gallery_photo, viewGroup, false);
        }

        final GalleryItemView imageViewPhoto = view.findViewById(R.id.image_view_cell_item);
        imageViewPhoto.setImageURI(photoURIs.get(i));

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPresenter.noPhotosSelected()){
                    Log.d("MY TAG","ZOOM IN NOW, NEW ACTIVITY");
                } else{
                    mPresenter.toggleSelectionAt(i);
                    if(mPresenter.isCurrentImageSelected(i)){
                        imageViewPhoto.setBackground(highlight);
                    } else{
                        imageViewPhoto.setBackground(null);
                    }
                }
            }
        });

        imageViewPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mPresenter.noPhotosSelected()){
                    mPresenter.toggleSelectionAt(i);

                    if(mPresenter.isCurrentImageSelected(i)){
                        imageViewPhoto.setBackground(highlight);
                    } else{
                        imageViewPhoto.setBackground(null);
                    }
                    return true;
                }

                return false;
            }
        });

        return view;
    }




}
