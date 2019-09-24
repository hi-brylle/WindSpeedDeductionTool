package activities.activity_main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.windspeeddeductiontool.R;

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
        TextView textViewID = view.findViewById(R.id.text_view_EntryID);
        TextView textViewDate = view.findViewById(R.id.text_view_EntryDate);
        TextView textViewDOD = view.findViewById(R.id.text_view_EntryDOD);

        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        long unixTime = cursor.getLong(cursor.getColumnIndex("unix_time"));
        int dod = 3; //TODO: do your research dude

        Date mePlease = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
        //TODO: make the format pretty, please, pretty please
        String readableDate = sdf.format(mePlease);

        textViewID.setText(String.valueOf(id));
        textViewDate.setText(readableDate);
        textViewDOD.setText(String.valueOf(dod));
    }
}