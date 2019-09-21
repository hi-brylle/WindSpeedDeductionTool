package activities.activity_gallery.gallery_image;

import android.net.Uri;

public class GalleryImage implements IGalleryImage{
    private Uri photoUri;
    boolean isSelected;
    private IImageSelectListener imageSelectListener;

    public GalleryImage(Uri photoUri){
        this.photoUri = photoUri;
        isSelected = false;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    void setAsSelected(){
        isSelected = true;

        imageSelectListener.onImageSelected();
    }

    void addImageSelectListener(IImageSelectListener listener){
        imageSelectListener = listener;
    }
}
