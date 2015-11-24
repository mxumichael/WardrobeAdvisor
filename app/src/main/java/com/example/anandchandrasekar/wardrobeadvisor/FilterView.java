package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by anandchandrasekar on 11/14/15.
 */
public class FilterView {
    public static View createNew(Context context, ItemFilter filter) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null){
            View view = inflater.inflate(R.layout.view_filter, null);
            TextView tv = (TextView) view.findViewById(R.id.filterViewText);
            ImageView iv = (ImageView) view.findViewById(R.id.filterViewImage);

            final Integer filterId = filter.getId();
            tv.setText(filter.getFilterName());
            if(filter.getFilterDrawableId() != -1) {
                //iv.setImageDrawable(context.getResources().getDrawable(filter.getFilterDrawableId()));
                iv.setImageResource(filter.getFilterDrawableId());

                String name = "filter_" + filter.getFilterName().toLowerCase();
                int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
                iv.setImageResource(id);
            }

            view.setLayoutParams(new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.MATCH_PARENT));

            return view;
        }

        return null;
    }
}
