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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import activities.activity_envelope_damage.EnvelopeDamage;
import activities.activity_gallery.GalleryActivity;
import helper_classes.degenerate_nn.DegenerateANN;
import helper_classes.db_helper.DBHelper;
import helper_classes.dod_to_wind.DODToWindSpeed;
import helper_classes.photo_manager.CameraRequest;
import helper_classes.photo_manager.PhotoFileIO;
import helper_classes.photo_manager.UriListSingleton;

public class AddNewEntryActivity extends AppCompatActivity implements IAddNewEntryActivityMVP.IAddNewEntryActivityView, View.OnClickListener {

    private AddNewEntryActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private Button buttonAddNewEntry;
    private EditText editTextRoofDamage;
    private EditText editTextWindowsDamage;
    private EditText editTextWallsDamage;

    private BroadcastReceiver locationBroadcastReceiver;

    private boolean hasRoofDmgEntry;
    private boolean hasWindowsDmgEntry;
    private boolean hasWallsDmgEntry;

    CameraRequest cameraRequest;
    PhotoFileIO photoFileIO;
    final HashMap<String, String> componentToDmgDescriptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        mPresenter = new AddNewEntryActivityPresenter(this,
                                                        new DBHelper(this),
                                                        new DegenerateANN(this),
                                                        new DODToWindSpeed(this));
        cameraRequest = new CameraRequest(this);
        photoFileIO = new PhotoFileIO(this);

        hasRoofDmgEntry = false;
        hasWindowsDmgEntry = false;
        hasWallsDmgEntry = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        textViewLongitude = findViewById(R.id.text_view_CurrentLongitude);
        textViewLatitude = findViewById(R.id.text_view_CurrentLatitude);
        buttonAddNewEntry = findViewById(R.id.button_AddNewEntry);
        final ImageButton imageButtonAttachPhoto = findViewById(R.id.image_button_AttachPhoto);
        ImageButton imageButtonRoofDamageEdit = findViewById(R.id.image_button_RoofDamageEdit);
        ImageButton imageButtonWindowsDamageEdit = findViewById(R.id.image_button_WindowsDamageEdit);
        ImageButton imageButtonWallsDamageEdit = findViewById(R.id.image_button_WallsDamageEdit);
        editTextRoofDamage = findViewById(R.id.edit_text_RoofDamage);
        editTextWindowsDamage = findViewById(R.id.edit_text_WindowsDamage);
        editTextWallsDamage = findViewById(R.id.edit_text_WallsDamage);

        editTextRoofDamage.setEnabled(false);
        editTextWindowsDamage.setEnabled(false);
        editTextWallsDamage.setEnabled(false);

        addNewButtonAvailability();
        //TODO: make the gps fix listener listen only on the first fix
        mPresenter.addGPSFixListener(new IGPSFixListener() {
            @Override
            public void onGPSFix() {
                addNewButtonAvailability();
                logSomething("MY TAG","GPS fix ON");
            }
        });

        buttonAddNewEntry.setOnClickListener(this);

        imageButtonRoofDamageEdit.setOnClickListener(this);
        imageButtonWindowsDamageEdit.setOnClickListener(this);
        imageButtonWallsDamageEdit.setOnClickListener(this);

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

    public void addNewButtonAvailability() {
        logSomething("MY TAG", String.valueOf(hasRoofDmgEntry));
        logSomething("MY TAG", String.valueOf(hasWindowsDmgEntry));
        logSomething("MY TAG", String.valueOf(hasWallsDmgEntry));

        if(hasRoofDmgEntry && hasWindowsDmgEntry && hasWallsDmgEntry){
            buttonAddNewEntry.setEnabled(true);
            logSomething("MY TAG", "Add New Button ON");
        } else{
            buttonAddNewEntry.setEnabled(false);
            logSomething("MY TAG", "Add New Button OFF");
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

        hasRoofDmgEntry = false;
        hasWindowsDmgEntry = false;
        hasWallsDmgEntry = false;

        addNewButtonAvailability();

        editTextRoofDamage.setText(getResources().getString(R.string.roof_damage));
        editTextWindowsDamage.setText(getResources().getString(R.string.windows_damage));
        editTextWallsDamage.setText(getResources().getString(R.string.walls_damage));
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

        if(resultCode == RESULT_OK && requestCode == 7){
            assert data != null;
            componentToDmgDescriptions.put("roofDmg", data.getStringExtra("result"));
            String updateDmgText = getResources().getString(R.string.roof_damage) + ": " + componentToDmgDescriptions.get("roofDmg");
            editTextRoofDamage.setText(updateDmgText);
            hasRoofDmgEntry = true;
            Log.d("MY TAG", "roof " + componentToDmgDescriptions.get("roofDmg"));
        }
        if(resultCode == RESULT_OK && requestCode == 8){
            assert data != null;
            componentToDmgDescriptions.put("windowsDmg", data.getStringExtra("result"));
            String updateDmgText = getResources().getString(R.string.windows_damage) + ": " + componentToDmgDescriptions.get("windowsDmg");
            editTextWindowsDamage.setText(updateDmgText);
            hasWindowsDmgEntry = true;
            Log.d("MY TAG", "windows " + componentToDmgDescriptions.get("windowsDmg"));
        }
        if(resultCode == RESULT_OK && requestCode == 9){
            assert data != null;
            componentToDmgDescriptions.put("wallsDmg", data.getStringExtra("result"));
            String updateDmgText = getResources().getString(R.string.walls_damage) + ": " + componentToDmgDescriptions.get("wallsDmg");
            editTextWallsDamage.setText(updateDmgText);
            hasWallsDmgEntry = true;
            Log.d("MY TAG", "walls " + componentToDmgDescriptions.get("wallsDmg"));
        }
    }

    private void goToGalleryActivity(){
        Intent galleryIntent = new Intent(AddNewEntryActivity.this, GalleryActivity.class);
        startActivity(galleryIntent);
    }

    private void goToRoofDamageActivity() {
        Intent roofDamageIntent = new Intent(AddNewEntryActivity.this, EnvelopeDamage.class);
        roofDamageIntent.putExtra("envelopeComponent", getString(R.string.roof_damage));
        startActivityForResult(roofDamageIntent, 7);
    }

    private void goToWindowsDamageActivity() {
        Intent windowsDamageIntent = new Intent(AddNewEntryActivity.this, EnvelopeDamage.class);
        windowsDamageIntent.putExtra("envelopeComponent", getString(R.string.windows_damage));
        startActivityForResult(windowsDamageIntent, 8);
    }

    private void goToWallsDamageActivity() {
        Intent wallsDamageIntent = new Intent(AddNewEntryActivity.this, EnvelopeDamage.class);
        wallsDamageIntent.putExtra("envelopeComponent", getString(R.string.walls_damage));
        startActivityForResult(wallsDamageIntent, 9);
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
        } else
        if(view.getId() == R.id.image_button_RoofDamageEdit){
            goToRoofDamageActivity();
        } else
        if(view.getId() == R.id.image_button_WindowsDamageEdit){
            goToWindowsDamageActivity();
        } else
        if(view.getId() == R.id.image_button_WallsDamageEdit){
            goToWallsDamageActivity();
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

        mPresenter.addGPSFixListener(new IGPSFixListener() {
            @Override
            public void onGPSFix() {
                addNewButtonAvailability();
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
