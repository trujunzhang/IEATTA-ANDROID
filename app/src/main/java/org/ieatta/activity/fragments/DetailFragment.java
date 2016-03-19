package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.R;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.wikipedia.BackPressedHandler;


public abstract class DetailFragment extends PageFragment implements BackPressedHandler {

    private PageViewModel model;

    @Nullable public Page getPage() {
        return model.getPage();
    }

    public PageTitle getTitle() {
        return model.getTitle();
    }

    public PageTitle getTitleOriginal() {
        return model.getTitleOriginal();
    }

}
