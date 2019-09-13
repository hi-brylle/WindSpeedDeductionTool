package activities.activity_add_new_entry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import activities.activity_gallery.GalleryActivity;
import helper_classes.db_helper.DBHelper;

public class AddNewEntryActivity extends AppCompatActivity implements IAddNewEntryActivityMVP.IAddNewEntryActivityView {

    private AddNewEntryActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private RadioGroup radioGroupRoofDamage;
    private RadioGroup radioGroupWindowsDamage;
    private RadioGroup radioGroupWallsDamage;
    private Button buttonAddNewEntry;

    private BroadcastReceiver locationBroadcastReceiver;

    private static final int CAMERA_REQUEST = 17;
    ArrayList<Bitmap> photoBitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        mPresenter = new AddNewEntryActivityPresenter(this, new DBHelper(this));

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
        ImageButton imageButtonAttachPhoto = findViewById(R.id.image_button_AttachPhoto);

        final HashMap<String, String> componentToDmgDescriptions = new HashMap<>();

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
        //and so, a null check is done first
        //smh Google, you're a shame
        radioGroupRoofDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null && rb.isChecked()){
                    componentToDmgDescriptions.put("roofDmg", rb.getText().toString());
                }
            }
        });
        radioGroupWindowsDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null && rb.isChecked()){
                    componentToDmgDescriptions.put("windowsDmg", rb.getText().toString());
                }
            }
        });
        radioGroupWallsDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null && rb.isChecked()){
                    componentToDmgDescriptions.put("wallsDmg", rb.getText().toString());
                }
            }
        });

        buttonAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectRadioGroups();
                toggleAddNewButtonOnOff();

                byte[][] byteArrayArray = null;
                if(photoBitmaps != null && photoBitmaps.size() > 0){
                    byteArrayArray = photoBitmapsToByteArrayArray(photoBitmaps);
                }

                final byte[][] finalByteArrayArray = byteArrayArray;
                class DBInsertTask extends AsyncTask<Void, Void, Boolean> {

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        return mPresenter.passDataToDBHelper(componentToDmgDescriptions, finalByteArrayArray);
                    }

                    @Override
                    protected void onPostExecute(Boolean insertSuccess) {
                        showToastOnDBInsert(insertSuccess);
                    }
                }

                DBInsertTask dbInsertTask = new DBInsertTask();
                dbInsertTask.execute();

                photoBitmaps = null;
            }
        });

        imageButtonAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
                //TODO: put into image view, i guess
            }
        });


    }

    //TODO: put these photo-taking shit in another class
    private void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == CAMERA_REQUEST){
            assert data != null;
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            if(photoBitmaps == null){
                photoBitmaps = new ArrayList<>();
            }
            photoBitmaps.add(bmp);

            setImageButtonResource(photoBitmaps.get(photoBitmaps.size() - 1));
        }
    }

    private void setImageButtonResource(final Bitmap latestBitmap) {
        ImageButton imageButtonMiniGallery = findViewById(R.id.image_view_MiniGallery);
        if(imageButtonMiniGallery.getVisibility() == View.GONE){
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

    byte[][] photoBitmapsToByteArrayArray(ArrayList<Bitmap> photoBitmaps){
        byte[][] byteArrayArray = null;
        byteArrayArray = new byte[photoBitmaps.size()][];
        for(int i = 0; i < photoBitmaps.size(); i++){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoBitmaps.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayArray[i] = stream.toByteArray();
        }

        return byteArrayArray;
    }

    private void deselectRadioGroups() {
        radioGroupRoofDamage.clearCheck();
        radioGroupWindowsDamage.clearCheck();
        radioGroupWallsDamage.clearCheck();
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
        if(success){
            Toast.makeText(AddNewEntryActivity.this, "New Entry Added", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(AddNewEntryActivity.this, "Failed to Add New Entry", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showCurrentLongLat() {
        String setLongitude = R.string.header_longitude + Double.toString(mPresenter.getLongitude());
        String setLatitude = R.string.header_latitude + Double.toString(mPresenter.getLatitude());
        textViewLongitude.setText(setLongitude);
        textViewLatitude.setText(setLatitude);
    }



}
