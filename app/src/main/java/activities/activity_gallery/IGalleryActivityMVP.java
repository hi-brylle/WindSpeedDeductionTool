package activities.activity_gallery;

import android.net.Uri;

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
        void initGalleryImages();
        Uri getPhotoURIAt(int index);
        int getUriListSize();
        void setupSelectListeners();
        void toggleSelectionAt(int index);
        boolean isImageSelectedAt(int index);
        boolean noPhotosSelected();
        void removeGalleryImageAt(int index);
        void forceDeselectAll();
    }
}
