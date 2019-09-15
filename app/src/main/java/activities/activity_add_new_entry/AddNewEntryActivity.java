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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import activities.activity_gallery.GalleryActivity;
import helper_classes.db_helper.DBHelper;
import helper_classes.photo_manager.IPhotoManagerBitmapListener;
import helper_classes.photo_manager.PhotoManager;

public class AddNewEntryActivity extends AppCompatActivity implements IAddNewEntryActivityMVP.IAddNewEntryActivityView {

    private AddNewEntryActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private RadioGroup radioGroupRoofDamage;
    private RadioGroup radioGroupWindowsDamage;
    private RadioGroup radioGroupWallsDamage;
    private Button buttonAddNewEntry;

    private BroadcastReceiver locationBroadcastReceiver;

    PhotoManager photoManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        mPresenter = new AddNewEntryActivityPresenter(this, new DBHelper(this));
        photoManager = new PhotoManager(this);

        appRootDirInit();
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
                //TODO: set image view here
                setMiniGalleryButtonResource(photoManager.getBitmaps().get(photoManager.getBitmaps().size() - 1));
            }
        });

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

        buttonAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectRadioGroups();
                toggleAddNewButtonOnOff();

                ArrayList<Bitmap> photoBitmaps = photoManager.getBitmaps();

                byte[][] byteArrayArray = null;
                if (photoBitmaps != null && photoBitmaps.size() > 0) {
                    byteArrayArray = convertBitmapsToByteArrayArray(photoBitmaps);
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

                //kinda dangerous, but im hoping what i read somewhere, sometime, about
                //how SQLite operations are actually single-threaded, internally, is true
                String potentialFilename = getString(R.string.photoReferencePrefix) + mPresenter.getLatestRowID();
                Log.d("MY TAG", "Potential Filename: " + potentialFilename);

                if (isMyAppDirRootInThere()) {
                    savePhotos(photoBitmaps, potentialFilename);
                }

                /*photoBitmaps = null;
                final ImageButton imageButtonMiniGallery = findViewById(R.id.image_button_MiniGallery);
                imageButtonMiniGallery.setVisibility(View.GONE);
                imageButtonMiniGallery.setImageBitmap(null);*/
            }
        });

        imageButtonAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoManager.takeSinglePhoto();
            }
        });


    }

    void appRootDirInit() {
        if (!isMyAppDirRootInThere()) {
            boolean makeDir = makeAppRootDir();
            if (makeDir) {
                Log.d("MY TAG", "App root directory made");
            } else {
                Log.d("MY TAG", "Failed to make App root directory");
            }
        } else {
            Log.d("MY TAG", "Folder already exists");
        }
    }

    boolean isMyAppDirRootInThere() {
        File myAppRootDirProbably = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.appRootDir));
        return myAppRootDirProbably.exists();
    }

    private boolean makeAppRootDir() {
        File myAppRootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.appRootDir));
        return myAppRootDir.mkdir();
    }

    void savePhotos(ArrayList<Bitmap> photoBitmaps, String folderName) {
        File myAppRootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getString(R.string.appRootDir));

        File currentPhotoSetDir = new File(myAppRootDir, folderName);
        if (currentPhotoSetDir.exists()) {
            currentPhotoSetDir.delete();
        }

        boolean folderCreated = currentPhotoSetDir.mkdir();
        if (folderCreated) {
            Log.d("MY TAG", "folder " + folderName + " created");
        } else {
            Toast.makeText(this, "Failed to save photos in local storage", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < photoBitmaps.size(); i++) {
            String filename = folderName + "_#" + (i + 1) + ".jpg";
            File photoFile = new File(currentPhotoSetDir, filename);
            if (photoFile.exists()) {
                photoFile.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(photoFile);
                photoBitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                Log.d("MY TAG", "Saved " + filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

    byte[][] convertBitmapsToByteArrayArray(ArrayList<Bitmap> photoBitmaps) {
        byte[][] byteArrayArray = null;
        byteArrayArray = new byte[photoBitmaps.size()][];
        for (int i = 0; i < photoBitmaps.size(); i++) {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == photoManager.CAMERA_REQUEST) {
            assert data != null;
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            photoManager.addBitmap(bmp);
        }
    }


}
