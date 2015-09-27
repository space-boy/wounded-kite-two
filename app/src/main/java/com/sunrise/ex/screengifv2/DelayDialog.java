package com.sunrise.ex.screengifv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class DelayDialog extends DialogFragment {

    private static final String DELAY_KEY = "com.sunrise.ex.screengifv2.delaydialog";
    private NumberPicker mDelayPicker;
    private Button mOkButton;
    private int mInitialDelay;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.delay_dialog,null);

        mInitialDelay = getArguments().getInt(DELAY_KEY);

        mDelayPicker = (NumberPicker) v.findViewById(R.id.delay_num_picker);
        mDelayPicker.setMaxValue(500);
        mDelayPicker.setMinValue(0);
        mDelayPicker.setValue(mInitialDelay);

        mOkButton = (Button) v.findViewById(R.id.ok_delay);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewConfig nc = (NewConfig) getActivity();
                mDelayPicker.clearFocus();
                nc.onUserDelay(mDelayPicker.getValue());

                DelayDialog.this.dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Delay (ms)").create();
    }

    public static DelayDialog newInstance(int delay){

        Bundle args = new Bundle();
        args.putInt(DELAY_KEY, delay);
        DelayDialog ddf = new DelayDialog();
        ddf.setArguments(args);
        return ddf;

    }
}
