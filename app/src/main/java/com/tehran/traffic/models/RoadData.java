package com.tehran.traffic.models;

import android.content.Context;

import com.mohsenoid.tehran.traffic.R;


/**
 * Created by Mohsen on 11/3/14.
 */

public class RoadData {
    String[] imageUrl;
    String[] state;

    public RoadData(Context context) {
        this.state = context.getResources().getStringArray(R.array.states);
        this.imageUrl = context.getResources().getStringArray(R.array.roads);
    }

    public String getImageUrl(int id) {
        return imageUrl[id];
    }

    public String getState(int id) {
        return state[id];
    }
}
