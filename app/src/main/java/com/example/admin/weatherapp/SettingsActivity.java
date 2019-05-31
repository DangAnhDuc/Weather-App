package com.example.admin.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private final String PREFS_NAME = "PREFS_FILE";

    //Views variables
    private SwitchCompat enlargedText;
    private SwitchCompat getFahrenheit;

    //Global variables
    private boolean enlargedBoolean;
    private boolean fahrenheitBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        enlargedText = findViewById(R.id.enlarged_text);
        getFahrenheit = findViewById(R.id.get_fahrenheit);

        enlargedText.setOnCheckedChangeListener(this);
        getFahrenheit.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStart() {
        //Restore instance state from last session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean enlargedBoolean = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        boolean fahrenheitBoolean = sharedPreferences.getBoolean("FAHRENHEIT_SWITCH", false);

        enlargedText.setChecked(enlargedBoolean);
        getFahrenheit.setChecked(fahrenheitBoolean);

        super.onStart();
    }

    @Override
    public void onPause() {
        //Save instance state of current session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ENLARGED_SWITCH", enlargedBoolean);
        editor.putBoolean("FAHRENHEIT_SWITCH", fahrenheitBoolean);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case (R.id.enlarged_text):  //"Enlarged Text" SwitchCompat
                if (!isChecked) {
                    enlargedBoolean = false;
                    Toast.makeText(SettingsActivity.this, "Enlarged Text is off", Toast.LENGTH_SHORT).show();
                } else {
                    enlargedBoolean = true;
                    Toast.makeText(SettingsActivity.this, "Enlarged Text is on", Toast.LENGTH_SHORT).show();
                }
                break;

            case (R.id.get_fahrenheit): //"Display Fahrenheit" Switch Compat
                if (!isChecked) {
                    fahrenheitBoolean = false;
                    Toast.makeText(SettingsActivity.this, "Temperature is set  to Celcius", Toast.LENGTH_SHORT).show();
                } else {
                    fahrenheitBoolean = true;
                    Toast.makeText(SettingsActivity.this, "Temperature is set  to Fahrenheit", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
