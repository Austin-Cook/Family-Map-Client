package com.example.familymapclient.feature.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

import com.example.familymapclient.R;
import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.data.Settings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // FIXME FINISH SWITCH LOGIC

        // Get all switches
        Settings settings = DataCache.getInstance().getSettings();
        Switch lifeStoryLinesSwitch = findViewById(R.id.lifeStoryLinesSwitch);
        Switch familyTreeLinesSwitch = findViewById(R.id.familyTreeLinesSwitch);
        Switch spouseLinesSwitch = findViewById(R.id.spouseLinesSwitch);
        Switch fathersSideSwitch = findViewById(R.id.fathersSideSwitch);
        Switch mothersSideSwitch = findViewById(R.id.mothersSideSwitch);
        Switch maleEventsSwitch = findViewById(R.id.maleEventsSwitch);
        Switch femaleEventsSwitch = findViewById(R.id.femaleEventsSwitch);

        // Set switches to correct values
        lifeStoryLinesSwitch.setChecked(settings.isLifeStoryLines());
        familyTreeLinesSwitch.setChecked(settings.isFamilyTreeLines());
        spouseLinesSwitch.setChecked(settings.isSpouseLines());
        fathersSideSwitch.setChecked(settings.isFathersSide());
        mothersSideSwitch.setChecked(settings.isMotherSide());
        maleEventsSwitch.setChecked(settings.isMaleEvents());
        femaleEventsSwitch.setChecked(settings.isFemaleEvents());
    }
}