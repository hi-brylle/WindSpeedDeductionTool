package helper_classes.db_helper;

import java.util.HashMap;

public interface IDBHelper {
    boolean insertToDB(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions);
    String getLatestFilepathsTableName();
    String getCurrentFilepathsTableName();
    boolean insertFilepaths(String folderName, String[] currentSetFilepaths);
    String[] getFilepathsForID(int id);
}
