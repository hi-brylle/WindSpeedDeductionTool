package activities.activity_add_new_entry.model;

import java.util.HashMap;

public class EntryModelFactory {

    public EntryModel entryModelFactory(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions, byte[][] byteArrayArray){
        if(byteArrayArray != null){
            return new EntryModelHasPhotos(longitude, latitude, componentToDmgDescriptions, byteArrayArray);
        } else {
            return new EntryModelNoPhotos(longitude, latitude, componentToDmgDescriptions);
        }
    }

}
