package com.sunrise.ex.screengifv2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SearchListActivity extends ListActivity {

    public ArrayList<File> mFiles;
    public ArrayList<File> mCopyFiles = new ArrayList<File>();
    private Toolbar tbar;
    public static final String TAG = "SearchListActivity ";
    public static final String GIF_PATH = "com.sun.ex.screengifv2.GIF_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview_ovr);

        tbar = (Toolbar) findViewById(R.id.app_b);
        tbar.setTitle("Search Results");
        tbar.setTitleTextColor(Color.WHITE);

        FileStructure fs = new FileStructure();
        File Fpass = new File(Environment.getExternalStorageDirectory().toString());
        mFiles = fs.getSearchFiles(Fpass);
        setTitle("Search Results");
        for(File inFile : mFiles){

            if(inFile.toString().contains(".gif")){
                    mCopyFiles.add(inFile);
            }
        }

        SIArrayAdapter adapter = new SIArrayAdapter(mCopyFiles);
        if(adapter.getCount() > 0){
            ImageView iv = (ImageView) findViewById(R.id.tuh);
            iv.setVisibility(View.GONE);
        }

        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView StringView = (TextView) v.findViewById(R.id.txt_path);

        File iFile = new File(StringView.getText().toString());

        if(iFile.isFile()){
            Intent i = new Intent();

            i.putExtra(GIF_PATH,StringView.getText().toString());
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }

    public class SIArrayAdapter extends ArrayAdapter<File>{

        public SIArrayAdapter(ArrayList<File> files){

            super(getApplicationContext(),0, files);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.gif_fragment,null);
            }

            File f = getItem(position);

            TextView mGifPath = (TextView) convertView.findViewById(R.id.txt_path);
            ImageView mGifImage = (ImageView) convertView.findViewById(R.id.img_preview);
            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.active_chk);
            mCheckBox.setVisibility(View.GONE);
            mGifPath.setTextSize(14);
            mGifPath.setText(f.toString());
            mGifImage.setImageBitmap(BitmapFactory.decodeFile(f.toString()));

            return convertView;
        }
    }
}

