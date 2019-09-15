package helper_classes.db_helper;

public interface IDBHelper {
    boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg, byte[][] byteArrayArray);
    boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg);
    int getLatestRowID();
}
