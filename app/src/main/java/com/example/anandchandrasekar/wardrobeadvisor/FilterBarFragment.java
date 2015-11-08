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


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class FilterBarFragment extends Fragment {

    public static final String ARG_FILTER_KIND = "ARG_FILTER_KIND";

    private String filterKind;
    private ArrayList<String> filterTextList;
    private ArrayList<String> filterImageList;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FilterBarFragment create(String filter_kind) {
        FilterBarFragment fragment = new FilterBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER_KIND, filter_kind);
        fragment.setArguments(args);
        return fragment;
    }

    public FilterBarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterKind = getArguments().getString(ARG_FILTER_KIND);
        populateFilterLabels(filterKind);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_filter_bar, container, false);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.hsv_ll);

        for(int i=0;i<filterTextList.size();i++){
            View view = inflater.inflate(R.layout.view_filter, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.filterViewImage);
            TextView textView = (TextView) view.findViewById(R.id.filterViewText);

            textView.setText(filterTextList.get(i));
            linearLayout.addView(view);
        }

        return rootView;
    }


    private void populateFilterLabels(String filterKind) {
        filterTextList = new ArrayList<String>();
        filterImageList = new ArrayList<String>();

        if(filterKind.equals(getString(R.string.filter_kind_color))) {
            filterTextList.add("Red");
            filterTextList.add("Blue");
        } else if(filterKind.equals(getString(R.string.filter_kind_weather))) {
            filterTextList.add("Sunny");
            filterTextList.add("Rainy");
        }
    }

}
