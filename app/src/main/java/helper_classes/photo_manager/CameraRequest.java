package helper_classes.photo_manager;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class CameraRequest {
    public static final int CAMERA_REQUEST = 17;
    public static final String CAMERA_IMAGE_FILE_PROVIDER = "com.example.windspeeddeductiontool.fileprovider";

    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;

    public CameraRequest(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView){
        this.mvpView = mvpView;
    }

    public void takeSinglePhoto(){
        mvpView.takeAndSaveSinglePhoto(CAMERA_REQUEST);
    }

}
