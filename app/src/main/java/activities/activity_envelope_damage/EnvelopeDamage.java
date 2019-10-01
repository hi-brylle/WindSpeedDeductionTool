package activities.activity_envelope_damage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.windspeeddeductiontool.R;

public class EnvelopeDamage extends AppCompatActivity {

    private RadioGroup radioGroupEnvelopeDmg;
    private Button buttonOkay;
    String envelopeComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envelope_damage);

        TextView textViewEnvelopeComponent = findViewById(R.id.text_view_EnvelopeComponent);

        Intent intent = getIntent();
        envelopeComponent = intent.getExtras().getString("envelopeComponent");
        textViewEnvelopeComponent.setText(envelopeComponent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final String[] envelopeComponentDamage = new String[1];

        radioGroupEnvelopeDmg = findViewById(R.id.radio_group_EnvelopeDamage);
        buttonOkay = findViewById(R.id.button_ConfirmDamage);

        toggleConfirmButtonOnOff();

        radioGroupEnvelopeDmg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if (rb != null && rb.isChecked()) {
                    envelopeComponentDamage[0] = rb.getText().toString();
                    toggleConfirmButtonOnOff();
                }
            }
        });

        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", envelopeComponentDamage[0]);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void toggleConfirmButtonOnOff() {
        if (isRadioGroupChecked()) {
            buttonOkay.setEnabled(true);
        } else {
            buttonOkay.setEnabled(false);
        }
    }

    private boolean isRadioGroupChecked() {
        return radioGroupEnvelopeDmg.getCheckedRadioButtonId() != -1;
    }
}
