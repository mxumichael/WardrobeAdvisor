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


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class FilterKindFragment extends Fragment {

    public static final String ARG_FILTER_KIND = "ARG_FILTER_KIND";
    public static final String FILTER_VIEW_CLICK_LISTENER = "FILTER_VIEW_CLICK_LISTENER";

    public FilterSelectedListener filterSelectedListener;
    private String filterKind;
    private DBHelper dbHelper;

    private ArrayList<ItemFilter> filtersList;
    private ArrayList<ItemFilter> selectedFiltersList;
    private HashMap<ItemFilter, View> filterViewMap;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FilterKindFragment create(String filter_kind, FilterSelectedListener filterSelectedListener) {
        FilterKindFragment fragment = new FilterKindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER_KIND, filter_kind);
        fragment.setArguments(args);
        fragment.setFilterSelectedListener(filterSelectedListener);
        return fragment;
    }

    public interface FilterSelectedListener {
        public void addFilter(ItemFilter filter);
        public void removeFilter(ItemFilter filter);
        public ArrayList<ItemFilter> getCurrentlySelectedFilters();
        public ArrayList<ItemFilter> getFiltersOfKind(String kind);
    }

    public FilterKindFragment() {
    }

    public void setFilterSelectedListener(FilterSelectedListener filterSelectedListener) {
        this.filterSelectedListener = filterSelectedListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d("", "ATTACHING FRAGMENT");
        if (context instanceof FilterSelectedListener){
            filterSelectedListener=(FilterSelectedListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filtersList = new ArrayList<ItemFilter>();
        selectedFiltersList = new ArrayList<ItemFilter>();
        filterViewMap = new HashMap<ItemFilter, View>();

        filterKind = getArguments().getString(ARG_FILTER_KIND);
        dbHelper = new DBHelper(getActivity());
        dbHelper.getWritableDatabase();
        populateFilterLabels(filterKind);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_filter_bar, container, false);
//        TextView filterKindTextView = (TextView) rootView.findViewById(R.id.filterKindTextView);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.hsv_ll);

//        filterKindTextView.setText(filterKind);
//        filterKindTextView.setRotation(-90);
        for(int i=0;i<filtersList.size();i++){
            final ItemFilter currFilter = filtersList.get(i);
            final Integer filterId = currFilter.getId();

            View view = FilterView.createNew(getActivity(), currFilter);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedFiltersList.contains(currFilter)) {
                        removeFilter(currFilter);
                    } else {
                        addFilter(currFilter);
                    }
                }
            });

            filterViewMap.put(currFilter, view);
            linearLayout.addView(view);
        }

        updateSelectedFilters();

        return rootView;
    }


    private void addFilter(ItemFilter filter) {
        filterSelectedListener.addFilter(filter);
    }

    private void removeFilter(ItemFilter filter) {
        filterSelectedListener.removeFilter(filter);
    }


    public void updateSelectedFilters()  {
        selectedFiltersList = filterSelectedListener.getCurrentlySelectedFilters();

        Iterator it = filterViewMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ItemFilter currFilter = (ItemFilter) pair.getKey();
            View currFilterView = (View) pair.getValue();

            if (selectedFiltersList.contains(currFilter)) {
                currFilterView.setBackgroundColor(Color.BLUE);
            } else {
                currFilterView.setBackgroundColor(Color.WHITE);
            }
        }
    }


    private void populateFilterLabels(String filterKind) {
        //load the filters from the database
//        ArrayList<Filter> filters = dbHelper.getFiltersOfKind(filterKind);
//        for(Filter currFilter: filters) {
//            filterTextList.add(currFilter.getFilterName());
//        }

        //right now using static entries
//        if(filterKind.equals(getString(R.string.filter_kind_color))) {
//            filtersList.add(new Filter(1, "Red", getString(R.string.filter_kind_color), null));
//            filtersList.add(new Filter(2, "Blue", getString(R.string.filter_kind_color), null));
//            filtersList.add(new Filter(3, "Green", getString(R.string.filter_kind_color), null));
//            for (int i=4; i<10; i++) {
//                filtersList.add(new Filter(i, "Orange", getString(R.string.filter_kind_color), null));
//            }
//        } else if(filterKind.equals(getString(R.string.filter_kind_type))) {
//            filtersList.add(new Filter(11, "Shirt", getString(R.string.filter_kind_type), null));
//            filtersList.add(new Filter(12, "Shorts", getString(R.string.filter_kind_type), null));
//            filtersList.add(new Filter(13, "Blazer", getString(R.string.filter_kind_type), null));
//            for (int i=14; i<20; i++) {
//                filtersList.add(new Filter(i, "Socks", getString(R.string.filter_kind_type), null));
//            }
//        } else if(filterKind.equals(getString(R.string.filter_kind_weather))) {
//            filtersList.add(new Filter(21, "Sunny", getString(R.string.filter_kind_weather), null));
//            filtersList.add(new Filter(22, "Rainy", getString(R.string.filter_kind_weather), null));
//            filtersList.add(new Filter(23, "Cold", getString(R.string.filter_kind_weather), null));
//        }

           filtersList.addAll(filterSelectedListener.getFiltersOfKind(filterKind));
    }

}
