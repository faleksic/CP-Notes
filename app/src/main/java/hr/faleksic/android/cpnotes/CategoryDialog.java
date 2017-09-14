package hr.faleksic.android.cpnotes;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.cpnotes.R;

import java.util.Objects;

public class CategoryDialog extends DialogFragment {
    private MyDBHelper myDBHelper;

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        myDBHelper = new MyDBHelper(getActivity());
        final View view = inflater.inflate(R.layout.dialog_category, new LinearLayout(getActivity()), false);

        int title, positive;

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            ((EditText) view.findViewById(R.id.category_edittext)).setText(name);
            title = R.string.dialog_title_rename;
            positive = R.string.rename;
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.category_delete_button);
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDBHelper.deleteCategory(getArguments().getInt("id"));
                    dismiss();
                }
            });
        } else {
            title = R.string.dialog_title_new;
            positive = R.string.add;
            view.findViewById(R.id.category_delete_button).setVisibility(View.GONE);
        }

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ((EditText) view.findViewById(R.id.category_edittext)).getText().toString();
                        if(!Objects.equals(name, "")) {
                            if (getArguments() != null) {
                                myDBHelper.updateCategory(getArguments().getInt("id"), name);
                            } else {
                                myDBHelper.insertCategory(name);
                            }
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.category_name_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CategoryDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
}
