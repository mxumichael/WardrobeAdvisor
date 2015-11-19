package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity implements FilterKindFragment.FilterSelectedListener, SelectedFiltersFragment.SelectedFiltersFragmentInteractionListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Item>> listDataChild;
    private DBHelper dbHelper;

    //filter stuff
    private ArrayList<ItemFilter> currentlySelectedFilters;
    private SelectedFiltersFragment selectedFiltersFragment;
    private AllFiltersFragment allFiltersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        setupClothsList();

        selectedFiltersFragment = (SelectedFiltersFragment) getSupportFragmentManager().findFragmentById(R.id.selectedFiltersFragment);
        allFiltersFragment = (AllFiltersFragment) getSupportFragmentManager().findFragmentById(R.id.allFiltersFragment);
        currentlySelectedFilters = new ArrayList<ItemFilter>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Scan.class);
                startActivity(i);
            }
        });

//        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
//        fab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent i = new Intent(getApplicationContext(), FilterBarTestActivity.class);
//                startActivity(i);
//            }
//        });

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

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                long item_id = expListView.getExpandableListAdapter().getChildId(groupPosition, childPosition);
                displayItem(item_id);
                return false;
            }
        });
        // preparing list data
        prepareListData();

        listAdapter = new ClothsAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void displayItem(long value) {
        Intent intent = new Intent(this, ViewItemActivity.class);
        intent.putExtra("item_id", value);
        startActivity(intent);
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
        listDataChild = new HashMap<String, List<Item>>();

        // Adding child data
        listDataHeader.add(cleanStr);
        listDataHeader.add(dirtyStr);
        listDataHeader.add(inWashStr);

        listDataChild.put(listDataHeader.get(0), cleanItems); // Header, Child data
        listDataChild.put(listDataHeader.get(1), dirtyItems);
        listDataChild.put(listDataHeader.get(2), inWashItems);
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


    //filter bar fragment listener methods
    @Override
    public void addFilter(ItemFilter filter) {
        Log.d("", "ADDING FILTER: " + filter.getId());
        currentlySelectedFilters.add(filter);
        selectedFiltersFragment.layoutSelectedFilters();
        allFiltersFragment.updateSelectedFilters();
        printFilters();
    }

    @Override
    public void removeFilter(ItemFilter filter) {
        Log.d("", "REMOVING FILTER: " + filter.getId());
        currentlySelectedFilters.remove(filter);
        selectedFiltersFragment.layoutSelectedFilters();
        allFiltersFragment.updateSelectedFilters();
        printFilters();
    }

    @Override
    public ArrayList<ItemFilter> getFiltersOfKind(String kind) {
        return dbHelper.getFiltersOfKind(kind);
    }

    //selected filters bar fragment listener methods
    @Override
    public ArrayList<ItemFilter> getCurrentlySelectedFilters() {
        return this.currentlySelectedFilters;
    }

    @Override
    public void removeSelectedFilter(ItemFilter filter) {
        Log.d("", "REMOVING SELECTED FILTER: " + filter.getId());
        currentlySelectedFilters.remove(filter);
        selectedFiltersFragment.layoutSelectedFilters();
        allFiltersFragment.updateSelectedFilters();
        printFilters();
    }

    public void printFilters() {
        Log.d("sdf","PRINTING");
        for(int i=0; i<currentlySelectedFilters.size(); i++) {
            Log.d("sdf", currentlySelectedFilters.get(i).getFilterName() + " : " + currentlySelectedFilters.get(i).getId());
        }
    }
}
