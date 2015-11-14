package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupClothsList();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), nfc_test_page.class);
                startActivity(i);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), FilterBarTestActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupClothsList() {
        String[] codeLearnChapters = new String[]{"Android Introduction", "Android Setup/Installation", "Android Hello World",
                "Android Layouts/Viewgroups", "Android Activity & Lifecycle", "Intents in Android"};

        ArrayAdapter<String> codeLearnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, codeLearnChapters);

        ListView codeLearnLessons = (ListView) findViewById(R.id.listView1);
        codeLearnLessons.setAdapter(codeLearnArrayAdapter);
    }

    private void nfcTest() {
        NfcAdapter mNfcAdapter;
        String MIME_TEXT_PLAIN = "text/plain";
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        String is_nfc_ok = nfcSupportCheck();
        if (is_nfc_ok.equals("FAIL")){
            return;
        }
        setContentView(R.layout.nfc_test_page);
        getIntent();

    }

    private String nfcSupportCheck() {
        NfcAdapter mNfcAdapter;
        String MIME_TEXT_PLAIN = "text/plain";
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //copied from http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            //finish();
            return "FAIL";
        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC is disabled.", Toast.LENGTH_LONG).show();
            return "FAIL";
        } else {
            Toast.makeText(this,"NFC is OK.", Toast.LENGTH_LONG).show();
        }
        return "OK";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
