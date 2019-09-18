package helper_classes.photo_manager;

import android.graphics.Bitmap;

import java.util.ArrayList;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class PhotoManager {
    public static final int CAMERA_REQUEST = 17;
    public static final String CAMERA_IMAGE_FILE_PROVIDER = "com.example.windspeeddeductiontool.fileprovider";
    private ArrayList<Bitmap> photoBitmaps;
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;

    private IPhotoManagerBitmapListener iPhotoManagerBitmapListener;

    public PhotoManager(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView){
        this.mvpView = mvpView;
        photoBitmaps = null;
    }

    public void takeSinglePhoto(){
        mvpView.takeAndSaveSinglePhoto(CAMERA_REQUEST);
    }

    public void addBitmap(Bitmap bitmap){
        if (photoBitmaps == null) {
            photoBitmaps = new ArrayList<>();
        }

        photoBitmaps.add(bitmap);
        mvpView.logSomething("MY TAG", "BITMAP ADDED");

        iPhotoManagerBitmapListener.onNonNullPhotoBitmap();
    }

    public ArrayList<Bitmap> getBitmaps(){
        return photoBitmaps;
    }

    public void dumpBitmaps(){
        if(photoBitmaps != null){
            photoBitmaps.clear();
            photoBitmaps = null;
        }
    }

    public void addNonNullPhotoBitmapListener(IPhotoManagerBitmapListener listener){
        iPhotoManagerBitmapListener = listener;
    }

}
