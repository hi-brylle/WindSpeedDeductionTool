package activities.activity_add_new_entry;

import java.util.HashMap;

public interface IAddNewEntryActivityMVP {
    interface IAddNewEntryActivityView{
        void showToastOnDBInsert(boolean success);
        void showCurrentLongLat();
        void takeAndSaveSinglePhoto(final int CAMERA_REQUEST);
        String getStringFromRes(int resID);
        void logSomething(String tag, String message);
        void toastSomething(String message);
    }

    interface IAddNewEntryActivityPresenter{
        void updateCurrentLongLat(double longitude, double latitude);
        boolean passDataToDBHelper(HashMap<String, String> componentToDmgDescriptions);
        String getCurrentFilepathsTableName();
        String getLatestFilepathsTableName();
        boolean passFilepathsToDBHelper(String folderName, String[] currentSetFilepaths);
    }

    interface IAddNewEntryActivityModel{

    }
}
