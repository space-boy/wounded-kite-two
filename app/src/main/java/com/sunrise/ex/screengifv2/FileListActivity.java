package com.sunrise.ex.screengifv2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class FileListActivity extends ActionBarActivity implements FileListFragment.PrevBack {

    private String PrevFile;
    private String mCurFile;
    private Toolbar tbar;
    Fragment MainAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.file_list_activity);
        tbar = (Toolbar) findViewById(R.id.app_b);
        tbar.setTitle(Environment.getExternalStorageDirectory().toString());
        tbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tbar);
        FragmentManager fm = getFragmentManager();

        if(MainAct != null){
            fm.beginTransaction().remove(MainAct).commit();
        }

        if(savedInstanceState == null) {
            MainAct = new FileListFragment();
            fm.beginTransaction().add(R.id.list_fragment_container, MainAct).addToBackStack("init_list").commit();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        int lastIndx =  new StringBuilder(PrevFile).lastIndexOf("/");

        if(new StringBuilder(PrevFile).replace(lastIndx,PrevFile.length(),"").toString().equals("")){
            finish();
        }

        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        Fragment MainAct = FileListFragment.newInstance(new StringBuilder(PrevFile).replace(lastIndx,PrevFile.length(),"").toString());
        fm.beginTransaction().remove(MainAct).commit();
        fm.beginTransaction().add(R.id.list_fragment_container, MainAct).addToBackStack("init_back").commit();
    }

    @Override
    public void updatePrev(String PrevDir) {
        PrevFile = PrevDir;
        tbar.setTitle(PrevDir);
    }

    @Override
    public void updateCur(String CurrentDir) {
        mCurFile = CurrentDir;
        tbar.setTitle(CurrentDir);
    }
}