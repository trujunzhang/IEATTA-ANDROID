package org.ieatta.activity.editing;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import org.ieatta.R;
import org.json.JSONObject;
import org.wikipedia.analytics.ProtectedEditAttemptFunnel;
import org.wikipedia.analytics.SavedPagesFunnel;

public class EditHandler  {
    public static final int RESULT_REFRESH_PAGE = 1;

    private ProtectedEditAttemptFunnel funnel;

    public EditHandler() {
    }

    public void setPage() {
    }

    private void showUneditableDialog() {
//        new AlertDialog.Builder(fragment.getActivity())
//                .setCancelable(false)
//                .setTitle(R.string.page_protected_can_not_edit_title)
//                .setMessage(R.string.page_protected_can_not_edit)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .show();
//        funnel.log(currentPage.getPageProperties().getEditProtectionStatus());
    }

}
