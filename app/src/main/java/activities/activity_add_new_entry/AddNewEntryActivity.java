package activities.activity_add_new_entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import helper_classes.DBHelper;

public class AddNewEntryActivity extends AppCompatActivity implements IAddNewEntryActivityMVP.IAddNewEntryActivityView {

    private AddNewEntryActivityPresenter mPresenter;

    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private RadioGroup radioGroupRoofDamage;
    private RadioGroup radioGroupWindowsDamage;
    private RadioGroup radioGroupWallsDamage;
    private Button buttonAddNewEntry;
    private ImageButton imageButtonAttachPhoto;

    private BroadcastReceiver broadcastReceiver;

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
        imageButtonAttachPhoto = findViewById(R.id.image_button_AttachPhoto);

        final String[] roofDmg = new String[1];
        final String[] windowsDmg = new String[1];
        final String[] wallsDmg = new String[1];

        toggleAddNewButtonOnOff();

        mPresenter.addGPSFixListener(new IGPSFixListener() {
            @Override
            public void onGPSFix() {
                toggleAddNewButtonOnOff();
            }
        });

        //there is a stupid bug in the framework in which when calling clearCheck(),
        //setOnCheckedChangeListener is called twice, but by the second time it is called,
        //rb loses reference to the checked button because its selection was just cleared,
        //and so, a null check is done first to make sure the one-element String array gets the button's text
        //smh Google, you're a shame
        radioGroupRoofDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null && rb.isChecked()){
                    roofDmg[0] = rb.getText().toString();
                }
            }
        });
        radioGroupWindowsDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null && rb.isChecked()){
                    windowsDmg[0] = rb.getText().toString();
                }
            }
        });
        radioGroupWallsDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null && rb.isChecked()){
                    wallsDmg[0] = rb.getText().toString();
                }
            }
        });

        buttonAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.fetchDamageDescriptions(roofDmg[0], windowsDmg[0], wallsDmg[0]);

                //there's no need to inject coordinates since
                //the broadcast receiver continually updates the presenter

                mPresenter.logFetches();

                if(mPresenter.passDataToDBHelper()){
                    Toast.makeText(AddNewEntryActivity.this, "New Entry Added", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(AddNewEntryActivity.this, "Fail to Add New Entry", Toast.LENGTH_SHORT).show();
                }

                deselectRadioGroups();
            }
        });

        imageButtonAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddNewEntryActivity.this, "SHOULD BE OPENING NEW ACTIVITY NOW", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deselectRadioGroups() {
        radioGroupRoofDamage.clearCheck();
        radioGroupWindowsDamage.clearCheck();
        radioGroupWallsDamage.clearCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    double longitude = (double) intent.getExtras().get("longitude");
                    double latitude = (double) intent.getExtras().get("latitude");
                    Log.d("MY TAG (ADD_NEW)", String.valueOf(longitude));
                    Log.d("MY TAG (ADD_NEW)", String.valueOf(latitude));
                    textViewLongitude.setText("Longitude: " + longitude);
                    textViewLatitude.setText("Latitude: " + latitude);

                    mPresenter.updateCurrentLongLat(longitude, latitude);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_updates"));

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
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
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
}
