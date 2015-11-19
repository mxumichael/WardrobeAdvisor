package com.example.anandchandrasekar.wardrobeadvisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by anandchandrasekar on 11/14/15.
 */
public class FilterView {
    public static View createNew(Context context, ItemFilter filter) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null){
            View view = inflater.inflate(R.layout.view_filter, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.filterViewImage);
            TextView textView = (TextView) view.findViewById(R.id.filterViewText);

            final Integer filterId = filter.getId();
            textView.setText(filter.getFilterName());

            return view;
        }

        return null;
    }
}
