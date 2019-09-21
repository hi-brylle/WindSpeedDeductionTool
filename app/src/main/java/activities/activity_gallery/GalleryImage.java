package activities.activity_gallery;

import android.net.Uri;

public class GalleryImage {
    private Uri photoUri;
    boolean isSelected;

    public GalleryImage(Uri photoUri){
        this.photoUri = photoUri;
        isSelected = false;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }
}
