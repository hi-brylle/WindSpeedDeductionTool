package helper_classes.photo_manager;

import android.net.Uri;
import android.os.Environment;

import com.example.windspeeddeductiontool.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class PhotoFileIO {
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;

    private File appRootDirectory;
    private File currentPhotoSetDir;
    private ArrayList<Uri> currentPhotoSetURIs;

    private int setCounter = 0;

    public PhotoFileIO(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView) {
        this.mvpView = mvpView;

        initAppDir();
    }

    private boolean doesAppRootDirExist() {
        File myAppRootDirProbably = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mvpView.getStringFromRes(R.string.appRootDir));
        return myAppRootDirProbably.exists();
    }

    private boolean makeAppRootDir() {
        appRootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mvpView.getStringFromRes(R.string.appRootDir));
        return appRootDirectory.mkdir();
    }

    private void initAppDir() {
        if (!doesAppRootDirExist()) {
            if (makeAppRootDir()) {
                mvpView.logSomething("MY TAG", "App root directory made");
            } else {
                mvpView.logSomething("MY TAG", "Failed to make App root directory");
            }
        } else {
            appRootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mvpView.getStringFromRes(R.string.appRootDir));
            mvpView.logSomething("MY TAG", "App root directory already exists");
        }
    }

    private boolean doesCurrentFolderDirExist(String folderName) {
        File currentDirProbably = new File(appRootDirectory.toString(), folderName);
        return currentDirProbably.exists();
    }

    private boolean makeCurrentFolderDir(String folderName) {
        currentPhotoSetDir = new File(appRootDirectory.toString(), folderName);
        return currentPhotoSetDir.mkdirs();
    }

    private void initCurrentFolderDir(String folderName) {
        if (!doesCurrentFolderDirExist(folderName)) {
            if (makeCurrentFolderDir(folderName)) {
                mvpView.logSomething("MY TAG", "Current directory made");
            } else {
                mvpView.logSomething("MY TAG", "Failed to make current directory");
            }
        } else {
            currentPhotoSetDir = new File(appRootDirectory.toString(), folderName);
            mvpView.logSomething("MY TAG", "Current directory already exists");
        }
    }

    public String[] getCurrentSetFilepaths() {
        String[] currentSetFilepaths = new String[currentPhotoSetURIs.size()];

        for (int i = 0; i < currentSetFilepaths.length; i++) {
            currentSetFilepaths[i] = currentPhotoSetURIs.get(i).getPath().replace("//", "/");
        }

        return currentSetFilepaths;
    }

    public File createImageFile(String folderName) throws IOException {
        initCurrentFolderDir(folderName);
        String filename = folderName + "_" + (setCounter + 1) + ".jpg";
        setCounter++;
        return new File(currentPhotoSetDir, filename);
    }

    public void addToCurrentPhotoFileset(Uri uri) {
        if (currentPhotoSetURIs == null || currentPhotoSetURIs.size() == 0) {
            currentPhotoSetURIs = new ArrayList<>();
        }
        currentPhotoSetURIs.add(uri);
    }

    public ArrayList<Uri> getPhotoSetURIs(){
        return currentPhotoSetURIs;
    }

    public Uri getLatestUri() {
        return currentPhotoSetURIs.get(currentPhotoSetURIs.size() - 1);
    }

    public boolean doImagesExist() {
        return currentPhotoSetURIs != null && currentPhotoSetURIs.size() > 0;
    }

    public void dumpURIs() {
        if (currentPhotoSetDir != null) {
            currentPhotoSetDir = null;
        }

        if (currentPhotoSetURIs != null) {
            currentPhotoSetURIs.clear();
            currentPhotoSetURIs = null;
        }

        setCounter = 0;
    }

}
