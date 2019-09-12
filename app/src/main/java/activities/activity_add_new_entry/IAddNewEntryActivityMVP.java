package activities.activity_add_new_entry;

import java.util.HashMap;

import helper_classes.db_helper.IDBHelper;

public interface IAddNewEntryActivityMVP {
    interface IAddNewEntryActivityView{
        void showToastOnDBInsert(boolean success);
    }

    interface IAddNewEntryActivityPresenter{
        void updateCurrentLongLat(double longitude, double latitude);
        boolean passDataToDBHelper(HashMap<String, String> componentToDmgDescriptions, byte[][] byteArrayArray);
        void createEntryModel(HashMap<String, String> componentToDmgDescriptions, byte[][] byteArrayArray);
    }

    interface IAddNewEntryActivityModel{
        boolean insertDataToDB(IDBHelper dbHelper);
    }
}
