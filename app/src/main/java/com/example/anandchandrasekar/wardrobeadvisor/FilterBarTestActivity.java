package com.example.anandchandrasekar.wardrobeadvisor;


/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 */
public class FilterBarTestActivity extends FragmentActivity implements FilterKindFragment.FilterSelectedListener, SelectedFiltersFragment.SelectedFiltersFragmentInteractionListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */


    private ArrayList<Filter> currentlySelectedFilters;


    private SelectedFiltersFragment selectedFiltersFragment;
    private AllFiltersFragment allFiltersFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_bar_test);

        selectedFiltersFragment = (SelectedFiltersFragment) getSupportFragmentManager().findFragmentById(R.id.selectedFiltersFragment);
        allFiltersFragment = (AllFiltersFragment) getSupportFragmentManager().findFragmentById(R.id.allFiltersFragment);

        currentlySelectedFilters = new ArrayList<Filter>();


    }


    //filter bar fragment listener methods
    @Override
    public void addFilter(Filter filter) {
        Log.d("", "ADDING FILTER: " + filter.getId());
        currentlySelectedFilters.add(filter);
        selectedFiltersFragment.layoutSelectedFilters();
        allFiltersFragment.updateSelectedFilters();
        printFilters();
    }

    @Override
    public void removeFilter(Filter filter) {
        Log.d("", "REMOVING FILTER: " + filter.getId());
        currentlySelectedFilters.remove(filter);
        selectedFiltersFragment.layoutSelectedFilters();
        allFiltersFragment.updateSelectedFilters();
        printFilters();
    }

    //selected filters bar fragment listener methods
    @Override
    public ArrayList<Filter> getCurrentlySelectedFilters() {
        return this.currentlySelectedFilters;
    }

    @Override
    public void removeSelectedFilter(Filter filter) {
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
