package activities.activity_add_new_entry;

import java.util.HashMap;

import activities.activity_add_new_entry.model.EntryModelFactory;
import helper_classes.db_helper.IDBHelper;

public class AddNewEntryActivityPresenter implements IAddNewEntryActivityMVP.IAddNewEntryActivityPresenter {
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;
    private IAddNewEntryActivityMVP.IAddNewEntryActivityModel mvpModel;
    private IDBHelper dbHelper;

    private IGPSFixListener gpsFixListener;

    //"Double" is a non-primitive type and thus can be null, not 0
    //used to check if gps has gotten a fix yet
    private Double longitude;
    private Double latitude;

    AddNewEntryActivityPresenter(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView,
                                 IDBHelper dbHelper){
        this.mvpView = mvpView;
        this.dbHelper = dbHelper;
    }

    @Override
    public void updateCurrentLongLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;

        //callback when long and lat are both non-null
        gpsFixListener.onGPSFix();
    }

    @Override
    public boolean passDataToDBHelper(HashMap<String, String> componentToDmgDescriptions, byte[][] byteArrayArray) {
        createEntryModel(componentToDmgDescriptions, byteArrayArray);
        return mvpModel.insertDataToDB(dbHelper);
    }

    @Override
    public void createEntryModel(HashMap<String, String> componentToDmgDescriptions, byte[][] byteArrayArray) {
        EntryModelFactory modelFactory = new EntryModelFactory();
        setMvpModel(modelFactory.getEntryModel(longitude, latitude, componentToDmgDescriptions, byteArrayArray));
    }

    @Override
    public String getLatestPhotosTableName() {
        return dbHelper.getLatestPhotosTableName();
    }

    public boolean passFilepathsToDBHelper(String folderName, String[] currentSetFilepaths) {
        return dbHelper.insertFilepaths(folderName, currentSetFilepaths);
    }

    void addGPSFixListener(IGPSFixListener listener){
        gpsFixListener = listener;
    }

    private void setMvpModel(IAddNewEntryActivityMVP.IAddNewEntryActivityModel mvpModel) {
        this.mvpModel = mvpModel;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
