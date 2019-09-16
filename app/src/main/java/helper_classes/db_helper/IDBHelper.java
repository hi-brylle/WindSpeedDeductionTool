package helper_classes.db_helper;

public interface IDBHelper {
    boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg);
    String getLatestPhotosTableName();
    boolean insertFilepaths(String folderName, String[] currentSetFilepaths);
}
