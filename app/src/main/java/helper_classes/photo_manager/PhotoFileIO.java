package helper_classes.photo_manager;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.windspeeddeductiontool.R;

import java.io.File;
import java.util.ArrayList;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class PhotoFileIO {
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;

    private File appRootDirectory;
    private File currentPhotoSetFoldername;
    private ArrayList<File> currentPhotoSetFilenames;

    public PhotoFileIO(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView){
        this.mvpView = mvpView;
        initAppDir();
    }

    public boolean doesAppRootDirExist(){
        File myAppRootDirProbably = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mvpView.getStringFromRes(R.string.appRootDir));
        return myAppRootDirProbably.exists();
    }

    private boolean makeAppRootDir(){
        appRootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mvpView.getStringFromRes(R.string.appRootDir));
        return appRootDirectory.mkdir();
    }

    public void initAppDir(){
        if(!doesAppRootDirExist()){
            if(makeAppRootDir()){
                mvpView.logSomething("MY TAG", "App root directory made");
            } else{
                mvpView.logSomething("MY TAG", "Failed to make App root directory");
            }
        } else{
            mvpView.logSomething("MY TAG", "App root directory already exists");
        }
    }

    public void savePhotoSet(ArrayList<Bitmap> photoBitmaps, String folderName){

    }

    public void dumpVars(){
        if(currentPhotoSetFoldername != null){
            currentPhotoSetFoldername = null;
        }
        if(currentPhotoSetFilenames != null){
            currentPhotoSetFilenames.clear();
            currentPhotoSetFilenames = null;
        }
    }
}
