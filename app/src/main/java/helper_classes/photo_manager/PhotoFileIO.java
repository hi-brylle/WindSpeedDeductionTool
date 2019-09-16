package helper_classes.photo_manager;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.windspeeddeductiontool.R;

import java.io.File;
import java.io.FileOutputStream;
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
//        File myAppRootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                getString(R.string.appRootDir));

        currentPhotoSetFoldername = new File(appRootDirectory, folderName);
        if (currentPhotoSetFoldername.exists()) {
            currentPhotoSetFoldername.delete();
        }

        boolean folderCreated = currentPhotoSetFoldername.mkdir();
        if (folderCreated) {
            mvpView.logSomething("MY TAG", "folder " + folderName + " created");
        } else {
            mvpView.toastSomething("Failed to save photos in local storage");
            return;
        }

        for (int i = 0; i < photoBitmaps.size(); i++) {
            String filename = folderName + "_#" + (i + 1) + ".jpg";
            File photoFile = new File(currentPhotoSetFoldername, filename);
            if (photoFile.exists()) {
                photoFile.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(photoFile);
                photoBitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                mvpView.logSomething("MY TAG", "Saved" + filename);

                currentPhotoSetFilenames.add(photoFile.getAbsoluteFile());
                mvpView.logSomething("MY TAG", currentPhotoSetFilenames.get(i).toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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