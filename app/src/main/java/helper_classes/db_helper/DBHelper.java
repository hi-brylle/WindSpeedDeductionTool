package helper_classes.db_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.HashMap;

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
    private static final String COLUMN_FILEPATHS_TABLE_NAME = "filepaths_table_name";

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
                COLUMN_FILEPATHS_TABLE_NAME + " TEXT" +
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

        ContentValues cv = fillContentValues(longitude, latitude, roofDmg, windowsDmg, wallsDmg);

        long result = db.insert(TABLE_NAME_INPUTS, null, cv);

        return result != -1;
    }

    private boolean insert(String targetFolder, String[] filepaths){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        final String column_id = "_id";
        final String column_filepaths = "filepaths";

        String SQL_CREATE_FILEPATHS_TABLE = "create table " +
                getCurrentPhotosTableName() + " (" +
                column_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                column_filepaths + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_FILEPATHS_TABLE);

        long[] results = new long[filepaths.length];
        for(int i = 0; i < filepaths.length; i++){
            cv.put(column_filepaths, filepaths[i]);
            results[i] = db.insert(targetFolder, null, cv);
        }

        for(int i = 0; i < filepaths.length; i++){
            if(results[i] == -1){
                return false;
            }
        }

        return true;
    }

    private ContentValues fillContentValues(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg){
        ContentValues cv = new ContentValues();

        long currentTime = System.currentTimeMillis();

        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_UNIX_TIME, currentTime);
        cv.put(COLUMN_ROOF_DAMAGE, roofDmg);
        cv.put(COLUMN_WINDOWS_DAMAGE, windowsDmg);
        cv.put(COLUMN_WALLS_DAMAGE, wallsDmg);

        if(isEntriesTableEmpty()){
            cv.put(COLUMN_FILEPATHS_TABLE_NAME, "photos_for_id_1");
        } else{
            cv.put(COLUMN_FILEPATHS_TABLE_NAME, "photos_for_id_" + getCurrentRowID());
        }

        return cv;
    }

    private boolean isEntriesTableEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME_INPUTS);
        return count <= 0;
    }

    private int getCurrentRowID(){
        if(isEntriesTableEmpty()){
            //haxxx, shhhhh
            return 1;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            String SQL_GET_LAST_ROW_ID = "select ROWID from " + TABLE_NAME_INPUTS + " order by ROWID DESC limit 2";
            Cursor c = db.rawQuery(SQL_GET_LAST_ROW_ID, null);
            long lastID = 0;
            if(c != null && c.moveToFirst()){
                lastID = c.getLong(0);
            }

            c.close();

            int currentID = (int) lastID;
            currentID++;
            return currentID;
        }
    }

    private String getCurrentPhotosTableName(){
        if(isEntriesTableEmpty()){
            return "photos_for_id_1";
        } else{
            /*SQLiteDatabase db = this.getWritableDatabase();
            String SQL_GET_TABLE_NAMES = "SELECT " + COLUMN_FILEPATHS_TABLE_NAME + " FROM " + TABLE_NAME_INPUTS;
            Cursor c = db.rawQuery(SQL_GET_TABLE_NAMES, null);

            String columnName;
            assert c != null;
            c.moveToLast();
            columnName = c.getString(c.getColumnIndex(COLUMN_FILEPATHS_TABLE_NAME));

            c.close();

            return columnName;*/
            return "photos_for_id_" + getCurrentRowID();
        }
    }

    @Override
    public boolean insertToDB(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions) {
        String roofDmg = componentToDmgDescriptions.get("roofDmg");
        String windowsDmg = componentToDmgDescriptions.get("windowsDmg");
        String wallsDmg = componentToDmgDescriptions.get("wallsDmg");

        return insert(longitude, latitude, roofDmg, windowsDmg, wallsDmg);
    }

    @Override
    public String getLatestPhotosTableName() {
        return getCurrentPhotosTableName();
    }

    @Override
    public boolean insertFilepaths(String folderName, String[] currentSetFilepaths) {
        return insert(folderName, currentSetFilepaths);
    }
}
