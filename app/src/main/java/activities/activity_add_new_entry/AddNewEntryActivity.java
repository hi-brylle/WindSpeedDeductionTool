package activities.activity_add_new_entry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import activities.activity_gallery.GalleryActivity;
import helper_classes.db_helper.DBHelper;
import helper_classes.photo_manager.CameraRequest;
import helper_classes.photo_manager.PhotoFileIO;
import helper_classes.photo_manager.UriListSingleton;

public class AddNewEntryActivity extends AppCompatActivity implements IAddNewEntryActivityMVP.IAddNewEntryActivityView, View.OnClickListener {

    private AddNewEntryActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private Button buttonAddNewEntry;

    private BroadcastReceiver locationBroadcastReceiver;

    CameraRequest cameraRequest;
    PhotoFileIO photoFileIO;
    final HashMap<String, String> componentToDmgDescriptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        mPresenter = new AddNewEntryActivityPresenter(this, new DBHelper(this));
        cameraRequest = new CameraRequest(this);
        photoFileIO = new PhotoFileIO(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        textViewLongitude = findViewById(R.id.text_view_CurrentLongitude);
        textViewLatitude = findViewById(R.id.text_view_CurrentLatitude);
        buttonAddNewEntry = findViewById(R.id.button_AddNewEntry);
        final ImageButton imageButtonAttachPhoto = findViewById(R.id.image_button_AttachPhoto);

        toggleAddNewButtonOnOff();
        //TODO: make the gps fix listener listen only on the first fix
        mPresenter.addGPSFixListener(new IGPSFixListener() {
            @Override
            public void onGPSFix() {
                toggleAddNewButtonOnOff();
            }
        });

        buttonAddNewEntry.setOnClickListener(this);

        imageButtonAttachPhoto.setOnClickListener(this);

    }

    private void setMiniGalleryButtonResource(final Uri photoUri) {
        final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
        if (imageButtonMiniGallery.getVisibility() == View.GONE) {
            imageButtonMiniGallery.setVisibility(View.VISIBLE);
        }
        imageButtonMiniGallery.setImageURI(photoUri);

        imageButtonMiniGallery.setOnClickListener(this);
    }

    public void toggleAddNewButtonOnOff() {
        //TODO: the following logic is just a placeholder
        if (!buttonAddNewEntry.isEnabled()) {
            buttonAddNewEntry.setEnabled(true);
        } else {
            buttonAddNewEntry.setEnabled(false);
        }
    }

    @Override
    public void showToastOnDBInsert(boolean success) {
        if (success) {
            Toast.makeText(AddNewEntryActivity.this, "New Entry Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddNewEntryActivity.this, "Failed to Add New Entry", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showCurrentLongLat() {
        String setLongitude = getString(R.string.header_longitude) + mPresenter.getLongitude();
        String setLatitude = getString(R.string.header_latitude) + mPresenter.getLatitude();
        textViewLongitude.setText(setLongitude);
        textViewLatitude.setText(setLatitude);
    }

    @Override
    public String getStringFromRes(int resID) {
        return getString(resID);
    }

    @Override
    public void logSomething(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void toastSomething(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addNewEntry(){
        toggleAddNewButtonOnOff();

        boolean dataInsertSuccess = mPresenter.passDataToDBHelper(componentToDmgDescriptions);
        showToastOnDBInsert(dataInsertSuccess);

        if(photoFileIO.doImagesExist()){
            String folderName = mPresenter.getLatestFilepathsTableName();
            logSomething("MY TAG", "Foldername: " + folderName);

            String[] currentSetFilepaths = photoFileIO.getCurrentSetFilepaths();
            boolean areFilepathsInserted = mPresenter.passFilepathsToDBHelper(folderName, currentSetFilepaths);
            if(areFilepathsInserted){
                Log.d("MY TAG", "Filepaths inserted");
            } else{
                Log.d("MY TAG", "Failed to insert filepaths");
            }
        }

        photoFileIO.dumpURIs();

        final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
        imageButtonMiniGallery.setVisibility(View.GONE);
        imageButtonMiniGallery.setImageBitmap(null);
    }

    @Override
    public void takeAndSaveSinglePhoto(int CAMERA_REQUEST) {
        File file = null;
        try{
            file = photoFileIO.createImageFile(mPresenter.getCurrentFilepathsTableName());

            photoFileIO.addToCurrentPhotoFileset(FileProvider.getUriForFile(this, CameraRequest.CAMERA_IMAGE_FILE_PROVIDER, file));

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileIO.getLatestUri());

            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == CameraRequest.CAMERA_REQUEST){
            if(photoFileIO.getLatestUri() != null){
                final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
                if (imageButtonMiniGallery.getVisibility() == View.GONE) {
                    imageButtonMiniGallery.setVisibility(View.VISIBLE);
                }

                Log.d("path", photoFileIO.getLatestUri().getPath().replace("//", "/"));

                setMiniGalleryButtonResource(photoFileIO.getLatestUri());
            }
        }
    }

    private void goToGalleryActivity(){
        Intent galleryIntent = new Intent(AddNewEntryActivity.this, GalleryActivity.class);
        startActivity(galleryIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_AddNewEntry) {
            addNewEntry();
        } else
        if(view.getId() == R.id.image_button_AttachPhoto){
            cameraRequest.takeSinglePhoto();
        } else
        if(view.getId() == R.id.image_button_MiniGallery){
            goToGalleryActivity();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (locationBroadcastReceiver == null) {
            locationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mPresenter.updateCurrentLongLat((double) intent.getExtras().get("longitude"),
                            (double) intent.getExtras().get("latitude"));
                    showCurrentLongLat();
                }
            };
        }
        registerReceiver(locationBroadcastReceiver, new IntentFilter("location_updates"));

        toggleAddNewButtonOnOff();
        mPresenter.addGPSFixListener(new IGPSFixListener() {
            @Override
            public void onGPSFix() {
                toggleAddNewButtonOnOff();
            }
        });

        UriListSingleton uriListSingleton = UriListSingleton.getInstance();
        if(uriListSingleton.getUriListSize() == 0){
            final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
            imageButtonMiniGallery.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationBroadcastReceiver != null) {
            unregisterReceiver(locationBroadcastReceiver);
        }
    }

}
