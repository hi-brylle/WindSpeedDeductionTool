package activities.activity_add_new_entry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.util.HashMap;

import activities.activity_gallery.GalleryActivity;
import helper_classes.db_helper.DBHelper;
import helper_classes.photo_manager.IPhotoManagerBitmapListener;
import helper_classes.photo_manager.PhotoFileIO;
import helper_classes.photo_manager.PhotoManager;

public class AddNewEntryActivity extends AppCompatActivity implements IAddNewEntryActivityMVP.IAddNewEntryActivityView, View.OnClickListener {

    private AddNewEntryActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private RadioGroup radioGroupRoofDamage;
    private RadioGroup radioGroupWindowsDamage;
    private RadioGroup radioGroupWallsDamage;
    private Button buttonAddNewEntry;

    private BroadcastReceiver locationBroadcastReceiver;

    PhotoManager photoManager;
    final HashMap<String, String> componentToDmgDescriptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        mPresenter = new AddNewEntryActivityPresenter(this, new DBHelper(this));
        photoManager = new PhotoManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        textViewLongitude = findViewById(R.id.text_view_CurrentLongitude);
        textViewLatitude = findViewById(R.id.text_view_CurrentLatitude);
        radioGroupRoofDamage = findViewById(R.id.radio_group_RoofDamage);
        radioGroupWindowsDamage = findViewById(R.id.radio_group_WindowsDamage);
        radioGroupWallsDamage = findViewById(R.id.radio_group_WallsDamage);
        buttonAddNewEntry = findViewById(R.id.button_AddNewEntry);
        final ImageButton imageButtonAttachPhoto = findViewById(R.id.image_button_AttachPhoto);

        photoManager.addNonNullPhotoBitmapListener(new IPhotoManagerBitmapListener() {
            @Override
            public void onNonNullPhotoBitmap() {
                setMiniGalleryButtonResource(photoManager.getBitmaps().get(photoManager.getBitmaps().size() - 1));
            }
        });

        toggleAddNewButtonOnOff();
        //TODO: make the gps fix listener listen only on the first fix
        mPresenter.addGPSFixListener(new IGPSFixListener() {
            @Override
            public void onGPSFix() {
                toggleAddNewButtonOnOff();
            }
        });

        //there is a stupid bug in the framework in which when calling clearCheck(),
        //setOnCheckedChangeListener is called twice, but by the second time it is called,
        //rb loses reference to the checked button because its selection was just cleared,
        //and so, a null check is done first; smh Google, you're a shame
        radioGroupRoofDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if (rb != null && rb.isChecked()) {
                    componentToDmgDescriptions.put("roofDmg", rb.getText().toString());
                }
            }
        });
        radioGroupWindowsDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if (rb != null && rb.isChecked()) {
                    componentToDmgDescriptions.put("windowsDmg", rb.getText().toString());
                }
            }
        });
        radioGroupWallsDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if (rb != null && rb.isChecked()) {
                    componentToDmgDescriptions.put("wallsDmg", rb.getText().toString());
                }
            }
        });

        buttonAddNewEntry.setOnClickListener(this);

        imageButtonAttachPhoto.setOnClickListener(this);

    }

    private void setMiniGalleryButtonResource(final Bitmap latestBitmap) {
        final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
        if (imageButtonMiniGallery.getVisibility() == View.GONE) {
            imageButtonMiniGallery.setVisibility(View.VISIBLE);
        }
        imageButtonMiniGallery.setImageBitmap(latestBitmap);

        imageButtonMiniGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(AddNewEntryActivity.this, GalleryActivity.class);
                galleryIntent.putExtra("bitmap", latestBitmap);
                startActivity(galleryIntent);
            }
        });
    }

    private void deselectRadioGroups() {
        radioGroupRoofDamage.clearCheck();
        radioGroupWindowsDamage.clearCheck();
        radioGroupWallsDamage.clearCheck();
    }

    public boolean areAllRadioGroupsChecked() {
        return radioGroupRoofDamage.getCheckedRadioButtonId() != -1 &&
                radioGroupWindowsDamage.getCheckedRadioButtonId() != -1 &&
                radioGroupWallsDamage.getCheckedRadioButtonId() != -1;
    }

    public void toggleAddNewButtonOnOff() {
        if (areAllRadioGroupsChecked()) {
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
    public void takeSinglePhoto(int CAMERA_REQUEST) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == photoManager.CAMERA_REQUEST) {
            assert data != null;
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            photoManager.addBitmap(bmp);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_AddNewEntry) {
            addNewEntry();
        } else
        if(view.getId() == R.id.image_button_AttachPhoto){
            photoManager.takeSinglePhoto();
        }
    }

    private void addNewEntry(){
        deselectRadioGroups();
        toggleAddNewButtonOnOff();

        PhotoFileIO photoFileIO = new PhotoFileIO(this);

        //1. insert descriptions to DB
        boolean dataInsertSuccess = mPresenter.passDataToDBHelper(componentToDmgDescriptions);
        showToastOnDBInsert(dataInsertSuccess);

        if(photoManager.getBitmaps() != null && photoManager.getBitmaps().size() > 0){
            //2. get foldername
            String folderName = mPresenter.getLatestPhotosTableName();
            logSomething("MY TAG", "Foldername: " + folderName);

            //3. save photos
            photoFileIO.savePhotoSet(photoManager.getBitmaps(), folderName);

            //4. insert filepaths to DB
            String[] currentSetFilepaths = photoFileIO.getCurrentSetFilepaths();
            boolean areFilepathsInserted = mPresenter.passFilepathsToDBHelper(folderName, currentSetFilepaths);
            if(areFilepathsInserted){
                Log.d("MY TAG", "Filepaths inserted");
            } else{
                Log.d("MY TAG", "Failed to insert filepaths");
            }
        }

        photoManager.dumpBitmaps();


        final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
        imageButtonMiniGallery.setVisibility(View.GONE);
        imageButtonMiniGallery.setImageBitmap(null);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationBroadcastReceiver != null) {
            unregisterReceiver(locationBroadcastReceiver);
        }
    }

}
