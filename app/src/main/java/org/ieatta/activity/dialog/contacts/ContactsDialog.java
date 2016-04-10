package org.ieatta.activity.dialog.contacts;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.activity.dialog.SwipeableBottomDialog;

public class ContactsDialog extends SwipeableBottomDialog implements DialogInterface.OnDismissListener {

    public static ContactsDialog newInstance(int entrySource) {
        ContactsDialog dialog = new ContactsDialog();
        Bundle args = new Bundle();
        // args.putParcelable("title", title);
        // args.putInt("entrySource", entrySource);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    protected View inflateDialogView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }
}
