package activities.activity_gallery.gallery_image;

import android.net.Uri;

public class GalleryImage implements IGalleryImage{
    private Uri photoUri;
    private boolean isSelected;
    private IImageSelectListeners imageSelectListener;

    public GalleryImage(Uri photoUri){
        this.photoUri = photoUri;
        isSelected = false;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void toggleSelection(){
        isSelected = !isSelected;

        if(isSelected) {
            imageSelectListener.onImageSelected();
        } else{
            imageSelectListener.onImageNotSelected();
        }

    }

    public boolean getSelectStatus(){
        return isSelected;
    }

    public void addImageSelectListeners(IImageSelectListeners listener){
        imageSelectListener = listener;
    }
}
