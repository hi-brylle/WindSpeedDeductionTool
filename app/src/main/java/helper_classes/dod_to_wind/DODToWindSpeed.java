package helper_classes.dod_to_wind;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DODToWindSpeed implements IDODToWindSpeed{
    private Context context;
    private static final String jsonFilename = "dod_to_wind.json";

    public DODToWindSpeed(Context context){
        this.context = context;
    }

    private String loadJSONFromAssets(){
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(jsonFilename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    private HashMap<String, Double> returnWindSpeeds(int dod){
        ArrayList<Double> windSpeedsForInputDOD = new ArrayList<>();
        try {
            JSONArray dod_vs_windSpeeds = new JSONArray(loadJSONFromAssets());
            for(int i = 0; i < dod_vs_windSpeeds.length(); i++){
                JSONObject per_dod_vs_wind = dod_vs_windSpeeds.getJSONObject(i);
                if(dod == per_dod_vs_wind.getInt("dod")){
                    JSONArray winds_per_dod = per_dod_vs_wind.getJSONArray("wind");
                    for(int j = 0; j < winds_per_dod.length(); j++){
                        windSpeedsForInputDOD.add(winds_per_dod.getDouble(j));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Double lowerBound = Collections.min(windSpeedsForInputDOD);
        Double upperBound = Collections.max(windSpeedsForInputDOD);

        Double sum = 0.0;
        for(int i = 0; i < windSpeedsForInputDOD.size(); i++){
            sum += windSpeedsForInputDOD.get(i);
        }
        Double mean = sum / windSpeedsForInputDOD.size();

        HashMap<String, Double> windSpeeds = new HashMap<>();
        windSpeeds.put("lowerBound", lowerBound);
        windSpeeds.put("upperBound", upperBound);
        windSpeeds.put("mean", mean);

        return windSpeeds;
    }


    @Override
    public HashMap<String, Double> getWindSpeeds(int dod) {
        return returnWindSpeeds(dod);
    }
}
