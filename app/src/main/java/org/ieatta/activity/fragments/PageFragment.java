package org.ieatta.activity.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.R;
import org.wikipedia.BackPressedHandler;

public class PageFragment extends Fragment implements BackPressedHandler {
    public static PageFragment newInstance() {
        return new PageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
