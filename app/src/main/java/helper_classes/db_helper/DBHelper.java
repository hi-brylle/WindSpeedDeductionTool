package helper_classes.db_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper implements IDBHelper{
    private static final String DATABASE_NAME = "entries.db";
    private static final String TABLE_NAME_INPUTS = "entries";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_UNIX_TIME = "unix_time";
    private static final String COLUMN_ROOF_DAMAGE = "roof_damage";
    private static final String COLUMN_WINDOWS_DAMAGE = "windows_damage";
    private static final String COLUMN_WALLS_DAMAGE = "wall_damage";
    private static final String COLUMN_PHOTOS_TABLE_NAME = "photos_table_name";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BASE_TABLE = "create table " +
                TABLE_NAME_INPUTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_UNIX_TIME + " INTEGER," +
                COLUMN_ROOF_DAMAGE + " TEXT," +
                COLUMN_WINDOWS_DAMAGE + " TEXT," +
                COLUMN_WALLS_DAMAGE + " TEXT," +
                COLUMN_PHOTOS_TABLE_NAME + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_BASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INPUTS);
        onCreate(db);
    }

    private boolean insert(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg){
        SQLiteDatabase db = this.getWritableDatabase();

        long currentTime = System.currentTimeMillis();
        ContentValues cv = fillContentValues(currentTime, longitude, latitude, roofDmg, windowsDmg, wallsDmg);

        long result = db.insert(TABLE_NAME_INPUTS, null, cv);

        return result != -1;
    }

    private boolean insert(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg, byte[][] byteArrayArray){
        SQLiteDatabase db = this.getWritableDatabase();

        long currentTime = System.currentTimeMillis();
        ContentValues cv = fillContentValues(currentTime, longitude, latitude, roofDmg, windowsDmg, wallsDmg);

        String uniquePhotosTableName = "photos_table_" + currentTime;
        cv.put(COLUMN_PHOTOS_TABLE_NAME, uniquePhotosTableName);
        //TODO: trim this function by changing dependency upon current time
        long result1 = db.insert(TABLE_NAME_INPUTS, null, cv);

        long result2 = insertPhotoBLOB(byteArrayArray , uniquePhotosTableName);

        return result1 != -1 && result2 != -1;
    }

    private long insertPhotoBLOB(byte [][] byteArrayArray, String uniquePhotosTableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        final String column_id = "_id";
        final String column_photos = "photos";

        String SQL_CREATE_PHOTOS_TABLE = "create table " +
                uniquePhotosTableName + " (" +
                column_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                column_photos + " BLOB" +
                ")";

        db.execSQL(SQL_CREATE_PHOTOS_TABLE);

        long[] results = new long[byteArrayArray.length];
        Log.d("MY TAG (DB)", "bAA size: " + byteArrayArray.length);
        for(int i = 0; i < byteArrayArray.length; i++){
            cv.put(column_photos, byteArrayArray[i]);
            results[i] = db.insert(uniquePhotosTableName, null, cv);
        }

        for(int i = 0; i < byteArrayArray.length; i++){
            if(results[i] == -1){
                return -1;
            }
        }

        return results[0];
    }

    private ContentValues fillContentValues(long currentTime, double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_UNIX_TIME, currentTime);
        cv.put(COLUMN_ROOF_DAMAGE, roofDmg);
        cv.put(COLUMN_WINDOWS_DAMAGE, windowsDmg);
        cv.put(COLUMN_WALLS_DAMAGE, wallsDmg);

        return cv;
    }

    @Override
    public boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg, byte[][] byteArrayArray) {
        return insert(longitude, latitude, roofDmg, windowsDmg, wallsDmg, byteArrayArray);
    }

    @Override
    public boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg) {
        return insert(longitude, latitude, roofDmg, windowsDmg, wallsDmg);
    }
}
