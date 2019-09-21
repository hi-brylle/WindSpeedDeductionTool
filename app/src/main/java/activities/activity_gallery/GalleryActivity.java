package activities.activity_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridView;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    ArrayList<Uri> photoSetURIs;
    GridView gridViewGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        Bundle uriBundle = intent.getBundleExtra("bundle");
        if (uriBundle != null) {
            photoSetURIs = uriBundle.getParcelableArrayList("uriArrayList");
        }

        Uri[] uriArray = new Uri[photoSetURIs.size()];
        for(int i = 0; i < photoSetURIs.size(); i++){
            uriArray[i] = photoSetURIs.get(i);
            Log.d("MY TAG", "onCreate: " + uriArray[i]);
        }

        gridViewGallery = findViewById(R.id.grid_view_gallery);
        UriAdapter uriAdapter = new UriAdapter((LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE),
                                                uriArray,
                                                getResources().getDrawable(R.drawable.highlight));
        gridViewGallery.setAdapter(uriAdapter);

        //do your shit here
    }
}
