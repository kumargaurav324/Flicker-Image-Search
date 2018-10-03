package android.flickrsearch.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.flickrsearch.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class AlertDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";

    public static AlertDialogFragment newInstance(String title) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(getResources().getText(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }
}
