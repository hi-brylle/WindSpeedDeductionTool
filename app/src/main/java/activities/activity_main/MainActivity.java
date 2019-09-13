package activities.activity_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windspeeddeductiontool.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activities.activity_add_new_entry.AddNewEntryActivity;
import helper_classes.LocationService;

public class MainActivity extends AppCompatActivity implements IMainActivityMVP.IMainActivityView {

    private static final String[] REQUIRED_PERMISSIONS = new String[] {
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA
        //add more permissions here when needed
    };
    private static final int ALL_PERMISSIONS_REQUEST_CODE = 17;

    private MainActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        mPresenter = new MainActivityPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button buttonNew = findViewById(R.id.button_New);
        textViewLongitude = findViewById(R.id.text_view_CurrentLongitude);
        textViewLatitude = findViewById(R.id.text_view_CurrentLatitude);

        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.handleNewButtonClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    double longitude = (double) intent.getExtras().get("longitude");
                    double latitude = (double) intent.getExtras().get("latitude");
                    String setLongitude = R.string.header_longitude + Double.toString(longitude);
                    String setLatitude = R.string.header_latitude + Double.toString(latitude);
                    textViewLongitude.setText(setLongitude);
                    textViewLatitude.setText(setLatitude);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_updates"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
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
