package activities.activity_add_new_entry.model;

import java.util.HashMap;

import helper_classes.db_helper.IDBHelper;

public class EntryModelHasPhotos extends EntryModel {
    private double longitude;
    private double latitude;

    private String roofDamage;
    private String windowsDamage;
    private String wallsDamage;

    private byte[][] byteArrayArray;

    EntryModelHasPhotos(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions, byte[][] byteArrayArray) {
        this.longitude = longitude;
        this.latitude = latitude;

        roofDamage = componentToDmgDescriptions.get("roofDmg");
        windowsDamage = componentToDmgDescriptions.get("windowsDmg");
        wallsDamage = componentToDmgDescriptions.get("wallsDmg");

        this.byteArrayArray = byteArrayArray;
    }

    @Override
    public boolean insertDataToDB(IDBHelper dbHelper) {
        return dbHelper.insertToDB(longitude, latitude, roofDamage, windowsDamage, wallsDamage, byteArrayArray);
    }
}
