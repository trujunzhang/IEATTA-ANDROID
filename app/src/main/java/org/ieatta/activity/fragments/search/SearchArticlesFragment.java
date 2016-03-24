package org.ieatta.activity.fragments.search;

import org.ieatta.activity.fragments.PageFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.R;
import org.ieatta.activity.history.HistoryEntry;

import static org.wikipedia.util.DeviceUtil.hideSoftKeyboard;

public class SearchArticlesFragment extends PageFragment {

    public boolean isSearchActive() {
        return false;
    }

    @Override
    public void loadPage(HistoryEntry entry) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);

        return view;
    }

    @Override
    public void postLoadPage() {

    }
}
