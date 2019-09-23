package activities.activity_gallery;

import android.net.Uri;

import java.util.ArrayList;

public interface IGalleryActivityMVP {
    interface IGalleryActivityView{
        void enableDelete();
        void disableDelete();
        void showCancel();
        void hideCancel();
        void showAndUpdateSelectCount(int selectCount);
        void hideSelectCount();
    }

    interface IGalleryActivityPresenter{
        void initGalleryImages(ArrayList<Uri> photoSetURIs);
        Uri getPhotoURIAt(int index);
        int getURIsSize();
        void setupSelectListeners();
        void toggleSelectionAt(int index);
        boolean isImageSelected(int index);
        boolean noPhotosSelected();
        void removeGalleryImageAt(int index);
        void forceDeselectAll();
    }
}
