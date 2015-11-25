package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectedFiltersFragment extends Fragment {

    private SelectedFiltersFragmentInteractionListener selectedFiltersFragmentInteractionListener;

    private ArrayList<ItemFilter> selectedFiltersList;
    private HashMap<ItemFilter, View> selectedFilterViewMap;
    private LinearLayout linearLayout;
    private HorizontalScrollView horizontalScrollView;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static SelectedFiltersFragment create() {
        return new SelectedFiltersFragment();
    }

    public interface SelectedFiltersFragmentInteractionListener {
        public void removeSelectedFilter(ItemFilter filter);
        public void clearAllFilters();
        public ArrayList<ItemFilter> getCurrentlySelectedFilters();
    }

    public SelectedFiltersFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SelectedFiltersFragmentInteractionListener){
            selectedFiltersFragmentInteractionListener=(SelectedFiltersFragmentInteractionListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedFilterViewMap = new HashMap<ItemFilter, View>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_selected_filters, container, false);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.selectedFiltersLinearLayout);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.selectedFiltersHSV);

        ImageButton clearFiltersButton = (ImageButton) rootView.findViewById(R.id.clearFiltersButton);
        clearFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFiltersFragmentInteractionListener.clearAllFilters();
            }
        });

        clearFiltersButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.setAlpha(0.6f);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ImageButton view = (ImageButton) v;
                        view.setAlpha(1.0f);
                        selectedFiltersFragmentInteractionListener.clearAllFilters();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setAlpha(1.0f);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        return rootView;
    }

    private void removeFilter(ItemFilter filter) {
        selectedFiltersFragmentInteractionListener.removeSelectedFilter(filter);
    }

    public void layoutSelectedFilters() {
        this.selectedFiltersList = selectedFiltersFragmentInteractionListener.getCurrentlySelectedFilters();
        selectedFilterViewMap.clear();
        linearLayout.removeAllViewsInLayout();

        if(selectedFiltersList.size() > 0) {
            horizontalScrollView.setHorizontalScrollBarEnabled(true);
        } else {
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
        }

        Log.d("", "LAYING OUT SELECTED FILTERS- num: "+selectedFiltersList.size());
        for(int i=0;i<selectedFiltersList.size();i++){
            final ItemFilter currFilter = selectedFiltersList.get(i);
            final Integer filterId = currFilter.getId();

            View view = FilterView.createNew(getActivity(), currFilter);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedFiltersList.contains(currFilter)) {
                        removeFilter(currFilter);
                    }
                }
            });

            selectedFilterViewMap.put(currFilter, view);
            linearLayout.addView(view);
        }
    }

}
