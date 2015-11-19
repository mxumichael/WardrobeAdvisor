package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * TODO: document your custom view class.
 */
public class SettingsView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_settings_view);
        RadioButton radio_button_nfc = (RadioButton) findViewById(R.id.radio_NFC);
        RadioButton radio_button_qr = (RadioButton) findViewById(R.id.radio_QR);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String scanMode = sharedPref.getString("ScanMode", "NFC");
        if (scanMode.equals("NFC")) {
            radio_button_nfc.setChecked(true);
        } else if (scanMode.equals("QR")) {
            radio_button_qr.setChecked(true);
        } else {
            Toast.makeText(getApplicationContext(), "un-anticipated scanmode:" + scanMode, Toast.LENGTH_LONG).show();
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String scanMode = sharedPref.getString("ScanMode", "NFC");
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_NFC:
                if (checked) {
                    //Toast.makeText(getApplicationContext(), "radio_NFC checked", Toast.LENGTH_LONG).show();
                    editor.putString("ScanMode", "NFC");
                    editor.commit();
                }
                break;
            case R.id.radio_QR:
                if (checked) {
                    //Toast.makeText(getApplicationContext(), "radio_QR checked", Toast.LENGTH_LONG).show();
                    editor.putString("ScanMode", "QR");
                    editor.commit();
                }
                break;
        }
    }


}
