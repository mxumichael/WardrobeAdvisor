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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

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
public class FilterBarTestActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    private ViewPager filterPager;
    private PagerAdapter filterPagerAdapter;
    private Button prevFilterPageButton;
    private Button nextFilterPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_bar_test);

        // Instantiate a ViewPager and a PagerAdapter.
        filterPager = (ViewPager) findViewById(R.id.filterViewPager);
        filterPagerAdapter = new FilterPagerAdapter(getSupportFragmentManager());
        filterPager.setAdapter(filterPagerAdapter);
        filterPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
                updatePagerNavigationButtons(position);
            }
        });

        prevFilterPageButton = (Button) findViewById(R.id.prevFilterButton);
        prevFilterPageButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                filterPager.setCurrentItem(filterPager.getCurrentItem()-1, true);
                updatePagerNavigationButtons(filterPager.getCurrentItem());
            }
        });
        nextFilterPageButton = (Button) findViewById(R.id.nextFilterButton);
        nextFilterPageButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                filterPager.setCurrentItem(filterPager.getCurrentItem()+1, true);
                updatePagerNavigationButtons(filterPager.getCurrentItem());
            }
        });
        updatePagerNavigationButtons(0);
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

    private class FilterPagerAdapter extends FragmentStatePagerAdapter {
        public FilterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return FilterBarFragment.create(getString(R.string.filter_kind_color));
                case 1: return FilterBarFragment.create(getString(R.string.filter_kind_weather));
                case 2: return FilterBarFragment.create(getString(R.string.filter_kind_type));
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);
//
//        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);
//
//        // Add either a "next" or "finish" button to the action bar, depending on which page
//        // is currently selected.
//        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
//                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
//                        ? R.string.action_finish
//                        : R.string.action_next);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // Navigate "up" the demo structure to the launchpad activity.
//                // See http://developer.android.com/design/patterns/navigation.html for more.
//                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
//                return true;
//
//            case R.id.action_previous:
//                // Go to the previous step in the wizard. If there is no previous step,
//                // setCurrentItem will do nothing.
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//                return true;
//
//            case R.id.action_next:
//                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
//                // will do nothing.
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
