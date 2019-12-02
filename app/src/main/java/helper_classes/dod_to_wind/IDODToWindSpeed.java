package helper_classes.dod_to_wind;

import java.util.HashMap;

public interface IDODToWindSpeed {
    HashMap<String, Double> getWindSpeeds(int dod);
}
