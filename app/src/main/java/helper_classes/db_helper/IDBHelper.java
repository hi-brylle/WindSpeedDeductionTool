package helper_classes.db_helper;

import java.util.HashMap;

public interface IDBHelper {
    boolean insertToDB(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions);
    String getLatestPhotosTableName();
    boolean insertFilepaths(String folderName, String[] currentSetFilepaths);
}
