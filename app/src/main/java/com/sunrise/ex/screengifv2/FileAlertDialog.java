package com.sunrise.ex.screengifv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FileAlertDialog extends DialogFragment {

    private Button mAutomatic;
    private Button mManual;
    public static final String GIF_PATH = "com.sun.ex.screengifv2.GIF_PATH";
    private final String FILE_PATH = "com.sun.ex.screengifv2.FILE_PATH";
    public static String IMG_PATH = "com.sunrise.ex.screengifv2.IMG_PATH";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.file_choice_dialog,null);

        mAutomatic = (Button) v.findViewById(R.id.automatic);
        mManual = (Button) v.findViewById(R.id.manual);

        mAutomatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), SearchListActivity.class);
                i.putExtra(ConfigActivity.FILE_PATH2, 1);
                startActivityForResult(i, 1);

            }
        });

        mManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), FileListActivity.class);
                i.putExtra(FILE_PATH, 0);
                startActivityForResult(i, 0);

            }
        });

        return new AlertDialog.Builder(getActivity()).setView(v)
                .setTitle("Select a File").create();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data == null){
            return;
        }

        Intent in = new Intent(getActivity().getApplicationContext(), NewConfig.class);

        if(data.getExtras().getString(GIF_PATH) != null) {
            in.putExtra(GIF_PATH, (data.getExtras().getString(GIF_PATH)));
            startActivity(in);
            FileAlertDialog.this.dismiss();
            return;
        }

        if(data.getExtras().getString(FileListFragment.IMG_PATH) != null){

            in.putExtra(FileListFragment.IMG_PATH,(data.getExtras().getString(FileListFragment.IMG_PATH)));
            startActivity(in);
            FileAlertDialog.this.dismiss();

        }
    }
}
