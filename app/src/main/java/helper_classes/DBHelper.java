package helper_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class DBHelper extends SQLiteOpenHelper implements IAddNewEntryActivityMVP.IAddNewEntryActivityModel {
    private static final String DATABASE_NAME = "entries.db";
    private static final String TABLE_NAME_INPUTS = "entries";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_UNIX_TIME = "unix_time";
    private static final String COLUMN_ROOF_DAMAGE = "roof_damage";
    private static final String COLUMN_WINDOWS_DAMAGE = "windows_damage";
    private static final String COLUMN_WALLS_DAMAGE = "wall_damage";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INPUTS = "create table " +
                TABLE_NAME_INPUTS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_UNIX_TIME + " INTEGER," +
                COLUMN_ROOF_DAMAGE + " TEXT," +
                COLUMN_WINDOWS_DAMAGE + " TEXT," +
                COLUMN_WALLS_DAMAGE + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_INPUTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INPUTS);
        onCreate(db);
    }

    private boolean insert(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_UNIX_TIME, System.currentTimeMillis());
        cv.put(COLUMN_ROOF_DAMAGE, roofDmg);
        cv.put(COLUMN_WINDOWS_DAMAGE, windowsDmg);
        cv.put(COLUMN_WALLS_DAMAGE, wallsDmg);

        long result = db.insert(TABLE_NAME_INPUTS, null, cv);

        return result != -1;
    }

    @Override
    public boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg) {
        return insert(longitude, latitude, roofDmg, windowsDmg, wallsDmg);
    }
}
