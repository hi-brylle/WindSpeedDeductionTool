package activities.activity_add_new_entry;

import java.util.HashMap;

import helper_classes.degenerate_nn.IDegenerateANN;
import helper_classes.db_helper.IDBHelper;
import helper_classes.dod_to_wind.IDODToWindSpeed;

public class AddNewEntryActivityPresenter implements IAddNewEntryActivityMVP.IAddNewEntryActivityPresenter {
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;
    private IAddNewEntryActivityMVP.IAddNewEntryActivityModel mvpModel;
    private IDBHelper dbHelper;
    private IDegenerateANN degenerateANN;
    private IDODToWindSpeed dodToWindSpeed;

    private IGPSFixListener gpsFixListener;

    //"Double" is a non-primitive type and thus can be null, not 0
    //used to check if gps has gotten a fix yet
    private Double longitude;
    private Double latitude;

    AddNewEntryActivityPresenter(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView,
                                 IDBHelper dbHelper,
                                 IDegenerateANN degenerateANN,
                                 IDODToWindSpeed dodToWindSpeed){
        this.mvpView = mvpView;
        this.dbHelper = dbHelper;
        this.degenerateANN = degenerateANN;
        this.dodToWindSpeed = dodToWindSpeed;
    }

    @Override
    public void updateCurrentLongLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;

        //callback when long and lat are both non-null
        gpsFixListener.onGPSFix();
    }

    @Override
    public boolean passDataToDBHelper(HashMap<String, String> componentToDmgDescriptions) {
        int dod = degenerateANN.predictDOD(componentToDmgDescriptions);
        mvpView.logSomething("MY TAG", "DOD: " + dod);

        HashMap<String, Double> windSpeeds = dodToWindSpeed.getWindSpeeds(dod);
        mvpView.logSomething("MY TAG", "lower bound: " + windSpeeds.get("lowerBound"));
        mvpView.logSomething("MY TAG", "upper bound: " + windSpeeds.get("upperBound"));
        mvpView.logSomething("MY TAG", "mean: " + windSpeeds.get("mean"));

        return dbHelper.insertToDB(longitude, latitude, componentToDmgDescriptions, dod, windSpeeds);
    }

    @Override
    public String getCurrentFilepathsTableName() {
        return dbHelper.getCurrentFilepathsTableName();
    }

    @Override
    public String getLatestFilepathsTableName() {
        return dbHelper.getLatestFilepathsTableName();
    }

    public boolean passFilepathsToDBHelper(String folderName, String[] currentSetFilepaths) {
        return dbHelper.insertFilepaths(folderName, currentSetFilepaths);
    }

    void addGPSFixListener(IGPSFixListener listener){
        gpsFixListener = listener;
    }

    double getLongitude() {
        return longitude;
    }

    double getLatitude() {
        return latitude;
    }
}
