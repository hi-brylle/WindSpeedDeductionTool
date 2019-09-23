package activities.activity_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activities.activity_add_new_entry.AddNewEntryActivity;
import helper_classes.LocationService;
import helper_classes.db_helper.DBHelper;

public class MainActivity extends AppCompatActivity implements IMainActivityMVP.IMainActivityView {

    private static final String[] REQUIRED_PERMISSIONS = new String[] {
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
        //add more permissions here when needed
    };
    private static final int ALL_PERMISSIONS_REQUEST_CODE = 17;

    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        mPresenter = new MainActivityPresenter(this, new DBHelper(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ImageButton imageButtonNew = findViewById(R.id.image_button_new);
        ListView listViewDBEntries = findViewById(R.id.list_view_entries);

        DBEntriesAdapter dbEntriesAdapter = new DBEntriesAdapter(this, mPresenter.getDBHelper().getAllEntries());
        listViewDBEntries.setAdapter(dbEntriesAdapter);

        imageButtonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.handleNewButtonClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void openAddNewEntryActivity() {
        Intent intent = new Intent(MainActivity.this, AddNewEntryActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS_REQUEST_CODE);
        } else {
            final int[] grantResults = new int[REQUIRED_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(ALL_PERMISSIONS_REQUEST_CODE, REQUIRED_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ALL_PERMISSIONS_REQUEST_CODE) {
            for (int index = permissions.length - 1; index >= 0; --index) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    // exit the app if at least one permission is not granted
                    Toast.makeText(this, "Required permission '" + permissions[index]
                            + "' not granted, exiting", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                startService(intent);
            }
        }
    }


}
