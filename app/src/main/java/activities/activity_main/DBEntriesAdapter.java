package activities.activity_main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.windspeeddeductiontool.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBEntriesAdapter extends CursorAdapter {
    DBEntriesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.row_entry, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewDate = view.findViewById(R.id.text_view_EntryDate);
        TextView textViewRoofDmg = view.findViewById(R.id.text_view_EntryRoofDmg);
        TextView textViewWindowsDmg = view.findViewById(R.id.text_view_EntryWindowsDmg);
        TextView textViewWallsDmg = view.findViewById(R.id.text_view_EntryWallsDmg);
        TextView textViewLowerBoundWind = view.findViewById(R.id.text_view_EntryLowerBoundWind);
        TextView textViewMeanWind = view.findViewById(R.id.text_view_EntryMeanWind);
        TextView textViewUpperBoundWind = view.findViewById(R.id.text_view_EntryUpperBoundWind);

        long unixTime = cursor.getLong(cursor.getColumnIndex("unix_time"));
        String roofDmg = cursor.getString(cursor.getColumnIndex("roof_damage"));
        String windowsDmg = cursor.getString(cursor.getColumnIndex("windows_damage"));
        String wallsDmg = cursor.getString(cursor.getColumnIndex("wall_damage"));
        double lowerBound = cursor.getDouble(cursor.getColumnIndex("lower_bound_speed"));
        double meanSpeed = cursor.getDouble(cursor.getColumnIndex("mean_speed"));
        double upperBound = cursor.getDouble(cursor.getColumnIndex("upper_bound_speed"));

        Date mePlease = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a d MMMM yyyy");

        String readableDate = sdf.format(mePlease);
        String setTextRoofDmg = "roof damage: " + roofDmg;
        String setTextWindowsDmg = "windows damage: " + windowsDmg;
        String setTextWallsDmg = "walls damage: " + wallsDmg;
        String setTextLowerBound = "lower bound: " + new DecimalFormat("#").format(lowerBound) + " kph";
        String setTextMeanWind = "mean wind: " + new DecimalFormat("#").format(meanSpeed) + " kph";
        String setTextUpperBound = "upper bound: " + new DecimalFormat("#").format(upperBound) + " kph";


        textViewDate.setText(readableDate);
        textViewRoofDmg.setText(setTextRoofDmg);
        textViewWindowsDmg.setText(setTextWindowsDmg);
        textViewWallsDmg.setText(setTextWallsDmg);
        textViewLowerBoundWind.setText(setTextLowerBound);
        textViewMeanWind.setText(setTextMeanWind);
        textViewUpperBoundWind.setText(setTextUpperBound);
    }
}