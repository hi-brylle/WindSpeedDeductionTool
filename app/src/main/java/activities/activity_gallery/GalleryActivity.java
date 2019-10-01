package activities.activity_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.windspeeddeductiontool.R;

import helper_classes.photo_manager.UriListSingleton;

public class GalleryActivity extends AppCompatActivity implements IGalleryActivityMVP.IGalleryActivityView, View.OnClickListener {

    private GalleryActivityPresenter mPresenter;
    UriAdapter uriAdapter;

    GridView gridViewGallery;
    ImageButton imageButtonDeleteSelected;
    ImageButton imageButtonCancelSelection;
    TextView textViewSelectionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mPresenter = new GalleryActivityPresenter(this);

        mPresenter.initGalleryImages();
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageButtonDeleteSelected = findViewById(R.id.image_button_DeleteSelected);
        imageButtonCancelSelection = findViewById(R.id.image_button_CancelSelection);
        textViewSelectionCount = findViewById(R.id.text_view_SelectionCount);
        gridViewGallery = findViewById(R.id.grid_view_gallery);

        uriAdapter = new UriAdapter((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE),
                getResources().getDrawable(R.drawable.highlight), mPresenter);

        gridViewGallery.setAdapter(uriAdapter);

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
            deleteSelected();
        } else
        if(view.getId() == R.id.image_button_CancelSelection){
            cancelSelections();
        }
    }

    //TODO: make sure that on db insert of filepaths, no remnants of prematurely saved images remain
    private void deleteSelected() {
        int numIndicesToKill = 0;
        for(int i = 0; i < mPresenter.getUriListSize(); i++){
            if(mPresenter.isImageSelectedAt(i)){
                numIndicesToKill++;
            }
        }
        Log.d("MY TAG", "# indices to kill: " + numIndicesToKill);


        int[] indicesToKill = new int[numIndicesToKill];
        for(int i = mPresenter.getUriListSize() - 1, j = 0; j < numIndicesToKill; i--){
            if(mPresenter.isImageSelectedAt(i)){
                indicesToKill[j] = i;
                j++;
                Log.d("MY TAG", "Indices to kill: " + i);
                GalleryItemView galleryItemView = (GalleryItemView) gridViewGallery.getChildAt(i);
                galleryItemView.setBackground(null);
            }
        }

        UriListSingleton uriListSingleton = UriListSingleton.getInstance();
        for(int i = mPresenter.getUriListSize() - 1, j = 0; j < numIndicesToKill; i--){
            if(i == indicesToKill[j]){
                j++;
                this.getContentResolver().delete(uriListSingleton.getUriAt(i), null, null);
                uriListSingleton.removeUriAt(i);
                mPresenter.removeGalleryImageAt(i);
            }
        }

        if(uriListSingleton.getUriListSize() == 0){
            finish();
        } else{
            mPresenter.initGalleryImages();
            uriAdapter.updateAdapter();
        }
    }

    void cancelSelections(){
        for(int i = 0; i < mPresenter.getUriListSize(); i++){
            if(mPresenter.isImageSelectedAt(i)){
                GalleryItemView galleryItemView = (GalleryItemView) gridViewGallery.getChildAt(i);
                mPresenter.toggleSelectionAt(i);
                galleryItemView.setBackground(null);
            }
        }
    }


    //TODO: return correct remaining URIs back to AddNewActivity after deletion

}
