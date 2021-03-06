package com.example.anandchandrasekar.wardrobeadvisor;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewItemActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long id = intent.getLongExtra("item_id", -1);

        dbHelper = new DBHelper(this);
        Item item = dbHelper.getItemById((int)id);

        ImageView img  = (ImageView)findViewById(R.id.item_image);
        int imgId = getResources().getIdentifier(item.getImagePath(), "drawable", getApplicationContext().getPackageName());
        img.setImageResource(imgId);

        TextView textView = (TextView)findViewById(R.id.item_name_text);
        textView.setText(item.getName());

        textView = (TextView)findViewById(R.id.item_type_text);
        textView.setText(item.getType());

        textView = (TextView)findViewById(R.id.item_state_text);
        textView.setText(item.getStateName());

        textView = (TextView)findViewById(R.id.item_color_text);
        textView.setText(item.getColor());

        textView = (TextView)findViewById(R.id.item_size_text);
        textView.setText(item.getSize());

        textView = (TextView)findViewById(R.id.item_brand_text);
        textView.setText(item.getBrand());

        textView = (TextView)findViewById(R.id.item_weather_text);
        textView.setText(item.getWeather());

    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        long id = intent.getLongExtra("item_id", -1);

        Item item = dbHelper.getItemById((int) id);

        ImageView img  = (ImageView)findViewById(R.id.item_image);
        int imgId = getResources().getIdentifier(item.getImagePath(), "drawable", getApplicationContext().getPackageName());
        img.setImageResource(imgId);

        img = (ImageView)findViewById(R.id.item_state_icon);
        imgId = getResources().getIdentifier(item.getStateImage(item.getState()), "drawable", getApplicationContext().getPackageName());
        img.setImageResource(imgId);

        TextView textView = (TextView)findViewById(R.id.item_name_text);
        textView.setText(item.getName());

        textView = (TextView)findViewById(R.id.item_type_text);
        textView.setText(item.getType());

        textView = (TextView)findViewById(R.id.item_state_text);
        textView.setText(item.getStateName());

        textView = (TextView)findViewById(R.id.item_color_text);
        textView.setText(item.getColor());

        textView = (TextView)findViewById(R.id.item_size_text);
        textView.setText(item.getSize());

        textView = (TextView)findViewById(R.id.item_brand_text);
        textView.setText(item.getBrand());

        textView = (TextView)findViewById(R.id.item_weather_text);
        textView.setText(item.getWeather());
    }
}
