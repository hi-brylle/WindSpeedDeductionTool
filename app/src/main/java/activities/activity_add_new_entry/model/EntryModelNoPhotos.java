package activities.activity_add_new_entry.model;

import java.util.HashMap;

import helper_classes.db_helper.IDBHelper;

public class EntryModelNoPhotos extends EntryModel {
    private double longitude;
    private double latitude;

    private String roofDamage;
    private String windowsDamage;
    private String wallsDamage;

    EntryModelNoPhotos(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions) {
        this.longitude = longitude;
        this.latitude = latitude;

        roofDamage = componentToDmgDescriptions.get("roofDmg");
        windowsDamage = componentToDmgDescriptions.get("windowsDmg");
        wallsDamage = componentToDmgDescriptions.get("wallsDmg");
    }


    @Override
    public boolean insertDataToDB(IDBHelper dbHelper) {
        return dbHelper.insertToDB(longitude, latitude, roofDamage, windowsDamage, wallsDamage);
    }
}
