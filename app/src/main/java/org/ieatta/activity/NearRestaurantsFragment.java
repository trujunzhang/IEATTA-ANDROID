package org.ieatta.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.R;
import org.wikipedia.activity.CallbackFragment;

public class NearRestaurantsFragment extends CallbackFragment {
    public static NearRestaurantsFragment newInstance() {
        return new NearRestaurantsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
