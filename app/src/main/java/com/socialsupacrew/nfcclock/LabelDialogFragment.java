package com.socialsupacrew.nfcclock;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by SocialSupaCrew on 27/08/2015.
 */
public class LabelDialogFragment extends DialogFragment {

    private EditText mLabelBox;

    private SimpleCursorRecyclerAdapter mCursorAdapter;
    private int mPosition;
    private Alarm mAlarm;

    public LabelDialogFragment(SimpleCursorRecyclerAdapter cursorAdapter, int position, Alarm alarm) {
        this.mCursorAdapter = cursorAdapter;
        this.mPosition = position;
        this.mAlarm = alarm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.label_layout, container, false);
        mLabelBox = (EditText) view.findViewById(R.id.labelBox);
        mLabelBox.setText(mAlarm.label);

        mLabelBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    set(mLabelBox.getText().toString());
                    return true;
                }
                return false;
            }
        });

        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final Button setButton = (Button) view.findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set(mLabelBox.getText().toString());
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    private void set(String label) {
        if (label.trim().length() == 0) {
            label = "";
        }
        Alarm a = new Alarm(mAlarm.id, mAlarm.time, mAlarm.active, mAlarm.repeat, mAlarm.repeatDays, mAlarm.ringtoneUri, mAlarm.ringtoneTitle, mAlarm.vibrate, label);
        mCursorAdapter.updateAlarm(a, mPosition, false);

        dismiss();
    }
}
