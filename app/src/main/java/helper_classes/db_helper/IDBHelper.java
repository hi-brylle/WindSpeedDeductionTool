package helper_classes.db_helper;

import android.database.Cursor;

import java.util.HashMap;

public interface IDBHelper {
    boolean insertToDB(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions, int dod);
    String getLatestFilepathsTableName();
    String getCurrentFilepathsTableName();
    boolean insertFilepaths(String folderName, String[] currentSetFilepaths);
    Cursor getAllEntries();
    String[] getFilepathsForID(int id);
}
