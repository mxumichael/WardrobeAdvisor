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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        setupClothsList();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), Scan.class);
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

        FloatingActionButton viewtest = (FloatingActionButton) findViewById(R.id.viewtest);
        viewtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), ViewItemActivity.class);
                i.putExtra("item_id", 1);
                startActivity(i);
            }
        });
    }

    private void setupClothsList() {
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ClothsAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
    * Preparing the list data
    */
    private void prepareListData() {

        ArrayList<Item> cleanItems = dbHelper.getItemsByState(Item.STATE_CLEAN);
        ArrayList<Item> dirtyItems = dbHelper.getItemsByState(Item.STATE_DIRTY);
        ArrayList<Item> inWashItems = dbHelper.getItemsByState(Item.STATE_INWASH);

        String cleanStr = "Clean (" + cleanItems.size() + ")";
        String dirtyStr = "Dirty (" + dirtyItems.size() + ")";
        String inWashStr = "In Wash (" + inWashItems.size() + ")";

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(cleanStr);
        listDataHeader.add(dirtyStr);
        listDataHeader.add(inWashStr);

        ArrayList<String> cleanClothes = new ArrayList<>();
        ArrayList<String> dirtyClothes = new ArrayList<>();
        ArrayList<String> inWashClothes = new ArrayList<>();

        for (int i = 0; i < cleanItems.size(); i++) {
            cleanClothes.add(cleanItems.get(i).getName());
        }

        for (int i = 0; i < dirtyItems.size(); i++) {
            dirtyClothes.add(dirtyItems.get(i).getName());
        }

        for (int i = 0; i < inWashItems.size(); i++) {
            inWashClothes.add(inWashItems.get(i).getName());
        }

        listDataChild.put(listDataHeader.get(0), cleanClothes); // Header, Child data
        listDataChild.put(listDataHeader.get(1), dirtyClothes);
        listDataChild.put(listDataHeader.get(2), inWashClothes);
    }

    private void nfcTest() {
        NfcAdapter mNfcAdapter;
        String MIME_TEXT_PLAIN = "text/plain";
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        String is_nfc_ok = nfcSupportCheck();
        if (is_nfc_ok.equals("FAIL")) {
            return;
        }
        setContentView(R.layout.scan_view);
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
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
            return "FAIL";
        } else {
            Toast.makeText(this, "NFC is OK.", Toast.LENGTH_LONG).show();
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
            Intent i = new Intent(getApplicationContext(), SettingsView.class);
            i.putExtra("option", R.id.action_settings);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
