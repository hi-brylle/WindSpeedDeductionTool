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
        ArrayList<Uri> getPhotoURIs();
        void setupSelectListeners();
        void toggleSelection(int index);
        boolean isCurrentImageSelected(int i);
        boolean noPhotosSelected();
    }
}
