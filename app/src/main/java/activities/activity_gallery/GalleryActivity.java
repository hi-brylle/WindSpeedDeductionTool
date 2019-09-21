package activities.activity_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements IGalleryActivityMVP.IGalleryActivityView, View.OnClickListener {

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
        mPresenter.setupSelectListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageButtonDeleteSelected = findViewById(R.id.image_button_DeleteSelected);
        imageButtonCancelSelection = findViewById(R.id.image_button_CancelSelection);
        textViewSelectionCount = findViewById(R.id.text_view_SelectionCount);
        gridViewGallery = findViewById(R.id.grid_view_gallery);

        UriAdapter uriAdapter = new UriAdapter((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE),
                getResources().getDrawable(R.drawable.highlight),
                mPresenter.getPhotoURIs(),
                mPresenter);
        gridViewGallery.setAdapter(uriAdapter);

        //do your shit here

        disableDelete();
        hideCancel();
        hideSelectCount();

        imageButtonDeleteSelected.setOnClickListener(this);
        imageButtonCancelSelection.setOnClickListener(this);

    }

    @Override
    public void enableDelete() {
        imageButtonDeleteSelected.setEnabled(true);
        imageButtonDeleteSelected.setAlpha(1.0f);
    }

    @Override
    public void disableDelete() {
        imageButtonDeleteSelected.setEnabled(false);
        imageButtonDeleteSelected.setAlpha(0.5f);
    }

    @Override
    public void showCancel() {
        imageButtonCancelSelection.setEnabled(true);
        imageButtonCancelSelection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCancel() {
        imageButtonCancelSelection.setEnabled(false);
        imageButtonCancelSelection.setVisibility(View.GONE);
    }

    @Override
    public void showAndUpdateSelectCount(int selectCount) {
        textViewSelectionCount.setVisibility(View.VISIBLE);
        textViewSelectionCount.setText(Integer.toString(selectCount));
    }

    @Override
    public void hideSelectCount() {
        textViewSelectionCount.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_button_DeleteSelected) {
            Toast.makeText(this, "DELETING SOMETHING", Toast.LENGTH_SHORT).show();
        } else
        if(view.getId() == R.id.image_button_CancelSelection){
            Toast.makeText(this, "CANCELING SELECTION", Toast.LENGTH_SHORT).show();
        }
    }




}
