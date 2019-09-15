package helper_classes.photo_manager;

import android.graphics.Bitmap;

import java.util.ArrayList;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class PhotoManager {
    private static final int CAMERA_REQUEST = 17;
    private ArrayList<Bitmap> photoBitmaps;
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;

    private IPhotoManagerBitmapListener iPhotoManagerBitmapListener;

    public PhotoManager(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView){
        this.mvpView = mvpView;
    }

    void takeSinglePhoto(){
        mvpView.takeSinglePhoto();
    }

    void addBitmap(Bitmap bitmap){
        if (photoBitmaps == null) {
            photoBitmaps = new ArrayList<>();
        }

        photoBitmaps.add(bitmap);
    }

    Bitmap getBitmap(int index){
        return photoBitmaps.get(index);
    }

    void dumpBitmaps(){
        photoBitmaps.clear();
        photoBitmaps = null;
    }

    void addNonNullPhotoBitmapListener(IPhotoManagerBitmapListener listener){
        iPhotoManagerBitmapListener = listener;
    }

}
