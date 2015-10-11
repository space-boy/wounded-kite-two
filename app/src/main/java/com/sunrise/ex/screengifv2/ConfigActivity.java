package com.sunrise.ex.screengifv2;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigActivity extends ActionBarActivity {

    public static final String FILE_PATH2 = "com.sun.ex.screengifv2.FILE_PATH2";
    public static final String FILE_C_TYPE = "FILE_TYPE";
    private ArrayList<GifMeta> mGifMetaArrayList;
    private Toolbar tbar;
    private ImageView mNoWidget;

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        tbar = (Toolbar) findViewById(R.id.app_b);
        tbar.setTitle("Screen Gif Widget");
        tbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tbar);

        mGifMetaArrayList = GifDataStore.get(getApplicationContext()).getGifMetaArrayList();

        if(mGifMetaArrayList.size() == 0){

            mNoWidget = (ImageView) findViewById(R.id.no_widget_img);
            mNoWidget.setVisibility(View.VISIBLE);
        } else {

           for(Iterator<GifMeta> it = mGifMetaArrayList.iterator(); it.hasNext(); ){
               GifMeta metagif = it.next();
               if(!new File(metagif.getGifPath()).exists()){
                   cleanUpList(metagif);
                   it.remove();
               }
           }
        }

        FragmentManager fm = getFragmentManager();
        ListFragment Ls = (ListFragment) fm.findFragmentById(R.id.fragmentContainer);

        if(Ls == null) {
            Ls = new GifListFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,Ls).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if(itemid == R.id.menu_add){
            FragmentManager fm = getFragmentManager();
            DialogFragment fad = new FileAlertDialog();
            fad.show(fm,FILE_C_TYPE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if(mGifMetaArrayList.size() > 0) {

            mNoWidget = (ImageView) findViewById(R.id.no_widget_img);
            mNoWidget.setVisibility(View.GONE);

            for(Iterator<GifMeta> it = mGifMetaArrayList.iterator(); it.hasNext(); ){
                GifMeta metagif = it.next();
                if(!new File(metagif.getGifPath()).exists()){
                    cleanUpList(metagif);
                    it.remove();
                }
            }

        }else{
            mNoWidget = (ImageView) findViewById(R.id.no_widget_img);
            mNoWidget.setVisibility(View.VISIBLE);
        }
        FragmentManager fm = getFragmentManager();
        ListFragment Ls = (ListFragment) fm.findFragmentById(R.id.fragmentContainer);
        fm.beginTransaction().remove(Ls).commit();

            Ls = new GifListFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,Ls).commit();

        super.onResume();
    }

    private void cleanUpList(GifMeta gifmeta){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();

        if(gifmeta.isActive()) {
            editor.remove(NewConfig.PATH_KEY);
            editor.remove(NewConfig.WIDTH_KEY);
            editor.remove(NewConfig.HIGH_KEY);
            editor.remove(NewConfig.DELAY_KEY);
            editor.commit();
        }
        if(gifmeta.isActive2()) {
            editor.remove(NewConfig.PATH_KEY2);
            editor.remove(NewConfig.WIDTH_KEY2);
            editor.remove(NewConfig.HIGH_KEY2);
            editor.remove(NewConfig.DELAY_KEY2);
            editor.commit();
        }
        if(gifmeta.isActive3()) {
            editor.remove(NewConfig.PATH_KEY3);
            editor.remove(NewConfig.WIDTH_KEY3);
            editor.remove(NewConfig.HIGH_KEY3);
            editor.remove(NewConfig.DELAY_KEY3);
            editor.commit();
        }

    }
}