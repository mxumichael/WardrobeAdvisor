package com.example.anandchandrasekar.wardrobeadvisor;

import android.app.ActionBar;
import android.content.Intent;
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

public class ViewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("item_id", -1);

        DBHelper dbHelper = new DBHelper(this);
        Item item = dbHelper.getItemById(1);

        ImageView img  = (ImageView)findViewById(R.id.item_image);
        int imgId = getResources().getIdentifier(item.getImagePath(), "drawable", getApplicationContext().getPackageName());
        img.setImageResource(imgId);

        TextView textView = (TextView)findViewById(R.id.item_name_text);
        textView.setText(item.getName());

        textView = (TextView)findViewById(R.id.item_type_text);
        textView.setText(item.getType());

        textView = (TextView)findViewById(R.id.item_state_text);
        textView.setText(item.getState());

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
