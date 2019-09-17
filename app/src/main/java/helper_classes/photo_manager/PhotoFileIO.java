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
    private File currentPhotoSetDir;
    private ArrayList<File> currentPhotoSetFiles;

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

    private boolean doesCurrentFolderDirExist(String folderName){
        File currentDirProbably = new File(appRootDirectory.toString(), folderName);
        return currentDirProbably.exists();
    }

    private boolean makeCurrentFolderDir(String folderName){
        currentPhotoSetDir = new File(appRootDirectory.toString(), folderName);
        return currentPhotoSetDir.mkdirs();
    }

    private void initCurrentFolderDir(String folderName){
        if(!doesCurrentFolderDirExist(folderName)){
            if(makeCurrentFolderDir(folderName)){
                mvpView.logSomething("MY TAG", "Current directory made");
            } else{
                mvpView.logSomething("MY TAG", "Failed to make current directory");
            }
        } else{
            currentPhotoSetDir = new File(appRootDirectory.toString(), folderName);
            mvpView.logSomething("MY TAG", "Current directory already exists");
        }
    }

    public void savePhotoSet(ArrayList<Bitmap> photoBitmaps, String folderName) {
        initCurrentFolderDir(folderName);

        for (int i = 0; i < photoBitmaps.size(); i++) {
            String filename = folderName + "_" + (i + 1) + ".jpg";
            File photoFile = new File(currentPhotoSetDir, filename);
            if (photoFile.exists()) {
                photoFile.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(photoFile);
                photoBitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                mvpView.logSomething("MY TAG", "Saved " + filename);

                addToCurrentPhotoFileSet(photoFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addToCurrentPhotoFileSet(File photoFile) {
        if (currentPhotoSetFiles == null || currentPhotoSetFiles.size() == 0) {
            currentPhotoSetFiles = new ArrayList<>();
        }
        currentPhotoSetFiles.add(photoFile/*.getAbsoluteFile()*/);
        int latestIndex = currentPhotoSetFiles.size() - 1;
        mvpView.logSomething("MY TAG", currentPhotoSetFiles.get(latestIndex).toString());
    }

    public String[] getCurrentSetFilepaths() {
        String[] currentSetFilepaths = new String[currentPhotoSetFiles.size()];

        for (int i = 0; i < currentSetFilepaths.length; i++) {
            currentSetFilepaths[i] = currentPhotoSetFiles.get(i).getAbsolutePath();
        }

        return currentSetFilepaths;
    }

    public void dumpVars() {
        if (currentPhotoSetDir != null) {
            currentPhotoSetDir = null;
        }
        if (currentPhotoSetFiles != null) {
            currentPhotoSetFiles.clear();
            currentPhotoSetFiles = null;
        }
    }
}
