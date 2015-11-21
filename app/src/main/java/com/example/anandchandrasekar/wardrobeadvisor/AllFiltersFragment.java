package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by anandchandrasekar on 11/18/15.
 */
public class AllFiltersFragment extends Fragment {

    private static final int NUM_PAGES = 3;
    private FilterKindFragment.FilterSelectedListener filterSelectedListener;

    private ViewPager filterPager;
    private PagerAdapter filterPagerAdapter;
    private ImageButton prevFilterPageButton;
    private ImageButton nextFilterPageButton;
    private ArrayList<FilterKindFragment> filterFragments;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static AllFiltersFragment create() {
        return new AllFiltersFragment();
    }

    public interface SelectedFiltersFragmentInteractionListener {
        public void removeSelectedFilter(ItemFilter filter);
        public ArrayList<ItemFilter> getCurrentlySelectedFilters();
    }

    public AllFiltersFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterKindFragment.FilterSelectedListener){
            filterSelectedListener=(FilterKindFragment.FilterSelectedListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterFragments = new ArrayList<FilterKindFragment>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_all_filters, container, false);

        filterPager = (ViewPager) rootView.findViewById(R.id.filterViewPager);
        filterPagerAdapter = new FilterPagerAdapter(getChildFragmentManager(), filterSelectedListener);
        filterPager.setAdapter(filterPagerAdapter);
        filterPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                updatePagerNavigationButtons(position);
            }
        });

        prevFilterPageButton = (ImageButton) rootView.findViewById(R.id.prevFilterButton);
//        prevFilterPageButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });
        prevFilterPageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().setColorFilter(0x11000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        filterPager.setCurrentItem(filterPager.getCurrentItem() - 1, true);
                        updatePagerNavigationButtons(filterPager.getCurrentItem());
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
        nextFilterPageButton = (ImageButton) rootView.findViewById(R.id.nextFilterButton);
        nextFilterPageButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                filterPager.setCurrentItem(filterPager.getCurrentItem() + 1, true);
                updatePagerNavigationButtons(filterPager.getCurrentItem());
            }
        });

        updatePagerNavigationButtons(0);

        return rootView;
    }

    private void updatePagerNavigationButtons(int currentPageNumber) {
        nextFilterPageButton.setEnabled(true);
        prevFilterPageButton.setEnabled(true);
        if(currentPageNumber == NUM_PAGES-1) {
            nextFilterPageButton.setEnabled(false);
        } else if (currentPageNumber == 0) {
            prevFilterPageButton.setEnabled(false);
        }
    }

    public void updateSelectedFilters() {
        for(int i=0; i<filterFragments.size(); i++) {
            filterFragments.get(i).updateSelectedFilters();
        }
    }

    private class FilterPagerAdapter extends FragmentStatePagerAdapter {

        public FilterKindFragment.FilterSelectedListener filterSelectedListener;
        public FilterPagerAdapter(FragmentManager fm, FilterKindFragment.FilterSelectedListener filterSelectedListener) {
            super(fm);
            this.filterSelectedListener = filterSelectedListener;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    FilterKindFragment fragment1 = FilterKindFragment.create(getString(R.string.filter_kind_color), this.filterSelectedListener);
                    filterFragments.add(fragment1);
                    return fragment1;
                case 1:
                    FilterKindFragment fragment2 = FilterKindFragment.create(getString(R.string.filter_kind_weather), this.filterSelectedListener);
                    filterFragments.add(fragment2);
                    return fragment2;
                case 2:
                    FilterKindFragment fragment3 = FilterKindFragment.create(getString(R.string.filter_kind_type), this.filterSelectedListener);
                    filterFragments.add(fragment3);
                    return fragment3;
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

}
