package activities.activity_add_new_entry.model;

import java.util.HashMap;

public class EntryModelFactory {

    public EntryModel getEntryModel(double longitude, double latitude, HashMap<String, String> componentToDmgDescriptions){
        return new EntryModelNoPhotos(longitude, latitude, componentToDmgDescriptions);
    }

}
