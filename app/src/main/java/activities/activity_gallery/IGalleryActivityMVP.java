package activities.activity_gallery;

import android.net.Uri;

import java.util.ArrayList;

public interface IGalleryActivityMVP {
    interface IGalleryActivityView{
        void disableDelete();
        void hideCancel();
        void showCancel();
        void updateSelectCount();
    }

    interface IGalleryActivityAdapterView{

    }

    interface IGalleryActivityPresenter{
        void initGalleryImages(ArrayList<Uri> photoSetURIs);
        ArrayList<Uri> getPhotoURIs();
    }
}
