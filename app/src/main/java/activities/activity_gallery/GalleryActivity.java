package activities.activity_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements IGalleryActivityMVP.IGalleryActivityView {

    private GalleryActivityPresenter mPresenter;

    GridView gridViewGallery;
    ImageButton imageButtonDeleteSelected;
    ImageButton imageButtonCancelSelection;
    TextView textViewSelectionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mPresenter = new GalleryActivityPresenter(this);

        Intent intent = getIntent();
        Bundle uriBundle = intent.getBundleExtra("bundle");
        ArrayList<Uri> photoSetURIs = new ArrayList<>();
        if (uriBundle != null) {
            photoSetURIs = uriBundle.getParcelableArrayList("uriArrayList");
        }

        mPresenter.initGalleryImages(photoSetURIs);
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageButtonDeleteSelected = findViewById(R.id.image_button_DeleteSelected);
        imageButtonCancelSelection = findViewById(R.id.image_button_CancelSelection);
        textViewSelectionCount = findViewById(R.id.text_view_SelectionCount);
        gridViewGallery = findViewById(R.id.grid_view_gallery);

        UriAdapter uriAdapter = new UriAdapter((LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE),
                mPresenter.getPhotoURIs(),
                getResources().getDrawable(R.drawable.highlight));
        gridViewGallery.setAdapter(uriAdapter);

        //do your shit here


    }

    @Override
    public void disableDelete() {

    }

    @Override
    public void hideCancel() {

    }

    @Override
    public void showCancel() {

    }

    @Override
    public void updateSelectCount() {

    }


}
