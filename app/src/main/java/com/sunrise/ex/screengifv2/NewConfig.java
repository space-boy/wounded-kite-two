package com.sunrise.ex.screengifv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class NewConfig extends ActionBarActivity {

    private ImageView mGifImg;
    private TextView mGifPath;
    private TextView mTxtFrame;
    private TextView mTxtDelay;
    private TextView mTxtDimenson;
    private TextView mTxtSize;
    private Toolbar tbar;
    private Button mAdd;
    private Button mDelete;
    private CheckBox mCheckActive;
    private CheckBox mCheckActive2;
    private CheckBox mCheckActive3;
    private Bitmap mGifBitDec;
    private GifView mAsyncGifView;
    private int mDelayMs;
    private int mFrameCount;
    private int mBHeight;
    private int mBWidth;
    private int mIndexLoc;
    private int mExists;
    private int mDecoded;
    private long mOriginSize;
    public String mPath;
    public String mOperation;
    private ArrayList<GifMeta> mGifMetaArrayList;
    private static final String DELAY_AMOUNT = "Delay_amount";
    private static final String DELAY_SESSION_KEY = "com.sunrise.ex.screengifv2.delay key";
    private int mPreRotateDelay = 0;

    public static final String PATH_KEY  = "com.sunrise.ex.screengifv2.PATH_KEY";
    public static final String WIDTH_KEY  = "com.sunrise.ex.screengifv2.WIDTH_KEY";
    public static final String HIGH_KEY  = "com.sunrise.ex.screengifv2.HIGH_KEY";
    public static final String DELAY_KEY  = "com.sunrise.ex.screengifv2.DELAY_KEY";

    public static final String PATH_KEY2  = "com.sunrise.ex.screengifv2.PATH_KEY2";
    public static final String WIDTH_KEY2  = "com.sunrise.ex.screengifv2.WIDTH_KEY2";
    public static final String HIGH_KEY2  = "com.sunrise.ex.screengifv2.HIGH_KEY2";
    public static final String DELAY_KEY2  = "com.sunrise.ex.screengifv2.DELAY_KEY2";

    public static final String PATH_KEY3  = "com.sunrise.ex.screengifv2.PATH_KEY3";
    public static final String WIDTH_KEY3  = "com.sunrise.ex.screengifv2.WIDTH_KEY3";
    public static final String HIGH_KEY3  = "com.sunrise.ex.screengifv2.HIGH_KEY3";
    public static final String DELAY_KEY3 = "com.sunrise.ex.screengifv2.DELAY_KEY3";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        Intent data = getIntent();

        if(savedInstanceState != null) {
            mPreRotateDelay = savedInstanceState.getInt(DELAY_SESSION_KEY);
        }
        tbar = (Toolbar) findViewById(R.id.app_b);
        tbar.setTitle("Gif Setup");
        tbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tbar);

        mGifPath = (TextView) findViewById(R.id.gif_path_txt);
        mGifImg = (ImageView) findViewById(R.id.img_main);
        mTxtDelay = (TextView) findViewById(R.id.text_delay);
        mTxtFrame = (TextView) findViewById(R.id.text_frames);
        mTxtDimenson = (TextView) findViewById(R.id.text_dimensions);
        mTxtSize = (TextView) findViewById(R.id.text_decoded_size);
        mAdd = (Button) findViewById(R.id.add_btn);
        mDelete = (Button) findViewById(R.id.del_btn);
        mCheckActive = (CheckBox) findViewById(R.id.active_check);
        mCheckActive2 = (CheckBox) findViewById(R.id.active_check2);
        mCheckActive3 = (CheckBox) findViewById(R.id.active_check3);


        if(data.hasExtra(SearchListActivity.GIF_PATH))
            if(!data.getExtras().get(SearchListActivity.GIF_PATH).equals("")) {
                mOperation = data.getExtras().get(SearchListActivity.GIF_PATH).toString();

                mGifPath.setText(mOperation.substring(mOperation.lastIndexOf('/')).replace("/", ""));

                if(!data.hasExtra(GifListFragment.LOCATION_INDEX)) {
                    new QuickDecodeGif().execute(data.getExtras().get(SearchListActivity.GIF_PATH).toString());
               }else{

                    mGifMetaArrayList = GifDataStore.get(getApplicationContext()).getGifMetaArrayList();
                    GifMeta gim = mGifMetaArrayList.get(data.getExtras().getInt(GifListFragment.LOCATION_INDEX));
                    mDecoded = 1;
                    mGifBitDec = BitmapFactory.decodeFile(gim.getGifPath());
                    mGifImg.setImageBitmap(mGifBitDec);

                    if(mPreRotateDelay == 0) {
                        mTxtDelay.setText("Delay:  " + gim.getDelay() + " ms ");
                    } else {
                        mTxtDelay.setText("Delay:  " + mPreRotateDelay + " ms ");
                    }

                    mTxtFrame.setText("Frames: " + gim.getFrames() + " ");
                    mTxtDimenson.setText("Dimensions: " + gim.getWidth() + " x " + gim.getHeight());
                    mTxtSize.setText("Original Image Size: " + String.valueOf(humanReadableByteCount(gim.getOriginalSize(),true)));
                    mExists = 1;
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    mIndexLoc = data.getExtras().getInt(GifListFragment.LOCATION_INDEX);
                }
        }

        if(data.hasExtra(FileListFragment.IMG_PATH)){
                if(!data.getExtras().get(FileListFragment.IMG_PATH).equals("")) {
                    mOperation = data.getExtras().get(FileAlertDialog.IMG_PATH).toString();

                    mGifPath.setText(mOperation.substring(mOperation.lastIndexOf('/')).replace("/", ""));
                    if(!data.hasExtra(GifListFragment.LOCATION_INDEX)) {
                        new QuickDecodeGif().execute(data.getExtras().get(FileListFragment.IMG_PATH).toString());
                    }else{

                        mGifMetaArrayList = GifDataStore.get(getApplicationContext()).getGifMetaArrayList();
                        GifMeta gim = mGifMetaArrayList.get(data.getExtras().getInt(GifListFragment.LOCATION_INDEX));
                        mDecoded = 1;
                        mGifBitDec = BitmapFactory.decodeFile(gim.getGifPath());
                        mGifImg.setImageBitmap(mGifBitDec);

                        if(mPreRotateDelay == 0) {
                            mTxtDelay.setText("Delay:  " + gim.getDelay() + " ms ");
                        } else {
                            mTxtDelay.setText("Delay:  " + mPreRotateDelay + " ms ");
                        }

                        mTxtFrame.setText("Frames: " + gim.getFrames() + " ");
                        mTxtDimenson.setText("Dimensions: " + gim.getWidth() + " x " + gim.getHeight());
                        mTxtSize.setText("Original Image Size: " + String.valueOf(humanReadableByteCount(gim.getOriginalSize(),true)));
                        mExists = 1;
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        mIndexLoc = data.getExtras().getInt(GifListFragment.LOCATION_INDEX);
                    }
                }
        }

        if(data.hasExtra(GifListFragment.ACTIVE))
            if(data.getExtras().getBoolean(GifListFragment.ACTIVE)){
                mCheckActive.setChecked(true);
            }

        if(data.hasExtra(GifListFragment.ACTIVE2))
            if(data.getExtras().getBoolean(GifListFragment.ACTIVE2)){
                mCheckActive2.setChecked(true);
            }

        if(data.hasExtra(GifListFragment.ACTIVE3))
            if(data.getExtras().getBoolean(GifListFragment.ACTIVE3)){
                mCheckActive3.setChecked(true);
            }
                
        if(data.hasExtra(GifListFragment.LOCATION_INDEX)){
            mIndexLoc = data.getExtras().getInt(GifListFragment.LOCATION_INDEX);

            mAdd.setText("Save");
            mDelete.setVisibility(View.VISIBLE);
            mExists = 1;

        }

        mTxtDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDecoded != 1){
                    Toast.makeText(getApplicationContext(), "Gif Decoding", Toast.LENGTH_SHORT).show();
                    return;
                }

                String DelayInt = mTxtDelay.getText().toString();

                FragmentManager fm = getSupportFragmentManager();


                DelayDialog ddf = DelayDialog.newInstance(Integer.valueOf(DelayInt.replaceAll("\\D+", "")));

                ddf.show(fm, DELAY_AMOUNT);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGifMetaArrayList = GifDataStore.get(getApplicationContext()).getGifMetaArrayList();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();

                if(mGifMetaArrayList.get(mIndexLoc).isActive()) {
                    editor.remove(PATH_KEY);
                    editor.remove(WIDTH_KEY);
                    editor.remove(HIGH_KEY);
                    editor.remove(DELAY_KEY);
                    editor.commit();
                }
                if(mGifMetaArrayList.get(mIndexLoc).isActive2()) {
                    editor.remove(PATH_KEY2);
                    editor.remove(WIDTH_KEY2);
                    editor.remove(HIGH_KEY2);
                    editor.remove(DELAY_KEY2);
                    editor.commit();
                }
                if(mGifMetaArrayList.get(mIndexLoc).isActive3()) {
                    editor.remove(PATH_KEY3);
                    editor.remove(WIDTH_KEY3);
                    editor.remove(HIGH_KEY3);
                    editor.remove(DELAY_KEY3);
                    editor.commit();
                }
                mGifMetaArrayList.remove(mIndexLoc);
                finish();
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGifMetaArrayList = GifDataStore.get(getApplicationContext()).getGifMetaArrayList();

                //update mode...
                if(mExists > 0){

                    GifMeta gm = mGifMetaArrayList.get(mIndexLoc);

                    if(mCheckActive.isChecked())
                        for(GifMeta g : mGifMetaArrayList){
                            if(g.isActive()){
                                g.setActive(false);
                            }
                        }

                    if(mCheckActive2.isChecked())
                        for(GifMeta g : mGifMetaArrayList){
                            if(g.isActive2()){
                                g.setActive2(false);
                            }
                        }

                    if(mCheckActive3.isChecked())
                        for(GifMeta g : mGifMetaArrayList){
                            if(g.isActive3()){
                                g.setActive3(false);
                            }
                        }

                    gm.setActive(mCheckActive.isChecked());
                    gm.setActive2(mCheckActive2.isChecked());
                    gm.setActive3(mCheckActive3.isChecked());

                    if(mPreRotateDelay == 0 && mDelayMs == 0) {
                        gm.setDelay(gm.getDelay());
                    } else {
                        gm.setDelay(mDelayMs);
                    }
                    mGifMetaArrayList.remove(mIndexLoc);

                    setActiveGif(gm);
                    mGifMetaArrayList.add(gm);

                    finish();
                    return;
                }

                if(mCheckActive.isChecked())
                    for(GifMeta g : mGifMetaArrayList){
                        if(g.isActive()){
                            g.setActive(false);
                        }
                    }

                if(mDecoded != 1){
                    Toast.makeText(getApplicationContext(), "Gif Decoding", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mBHeight == 0 || mBWidth == 0){
                    Toast.makeText(getApplicationContext(),"Gif can't be 0 Height/Width",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mFrameCount > 130){
                    Toast.makeText(getApplicationContext(),"Gif must be 130 frames or less",Toast.LENGTH_LONG).show();
                    return;
                }

                if(mCheckActive2.isChecked())
                    for(GifMeta g : mGifMetaArrayList){
                        if(g.isActive2()){
                            g.setActive2(false);
                        }
                    }

                if(mCheckActive3.isChecked())
                    for(GifMeta g : mGifMetaArrayList){
                        if(g.isActive3()){
                            g.setActive3(false);
                        }
                    }

                GifMeta g = new GifMeta();
                g.setFrames(mFrameCount);
                g.setDelay(mDelayMs);
                g.setGifPath(mPath);
                g.setHeight(mBHeight);
                g.setWidth(mBWidth);
                g.setActive(mCheckActive.isChecked());
                g.setActive2(mCheckActive2.isChecked());
                g.setActive3(mCheckActive3.isChecked());
                g.setOriginalSize(((int) mOriginSize));

                mGifMetaArrayList.add(g);
                setActiveGif(g);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DELAY_SESSION_KEY,mPreRotateDelay);

    }

    public void onUserDelay(int delay){
        mTxtDelay.setText("Delay: " + delay + " ms");
        mDelayMs = delay;
        mPreRotateDelay = delay;
    }

    private void setActiveGif(GifMeta gm){

        if(mCheckActive.isChecked()){

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = pref.edit();

            editor.putString(PATH_KEY,gm.getGifPath());
            editor.putInt(WIDTH_KEY,gm.getWidth());
            editor.putInt(HIGH_KEY,gm.getHeight());
            editor.putInt(DELAY_KEY,gm.getDelay());
            editor.commit();
        }
        if(mCheckActive2.isChecked()){

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(PATH_KEY2,gm.getGifPath());
            editor.putInt(WIDTH_KEY2,gm.getWidth());
            editor.putInt(HIGH_KEY2,gm.getHeight());
            editor.putInt(DELAY_KEY2,gm.getDelay());
            editor.commit();


        }
        if(mCheckActive3.isChecked()){

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(PATH_KEY3,gm.getGifPath());
            editor.putInt(WIDTH_KEY3,gm.getWidth());
            editor.putInt(HIGH_KEY3,gm.getHeight());
            editor.putInt(DELAY_KEY3,gm.getDelay());
            editor.commit();

        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private class QuickDecodeGif extends AsyncTask<String,Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mAsyncGifView = null;
            String path = params[0];
            mPath = path;
            mAsyncGifView = new GifView(NewConfig.this);
            mAsyncGifView.setGif(path,BitmapFactory.decodeFile(path));
            mAsyncGifView.decode();

            while (mAsyncGifView.decodeStatus != 2) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            File fi = new File(path);

            mDelayMs = mAsyncGifView.getDelayTime(mAsyncGifView.getFrameNum() - 1);
            mFrameCount = mAsyncGifView.getFrameNum();

            mBHeight = BitmapFactory.decodeFile(path).getHeight();
            mBWidth =  BitmapFactory.decodeFile(path).getWidth();
            mOriginSize = fi.length();

            mAsyncGifView.destroyDrawingCache();
            mAsyncGifView.setDrawingCacheEnabled(true);

            mAsyncGifView.invalidate();
            mAsyncGifView.buildDrawingCache(true);

            Bitmap bit = Bitmap.createBitmap(BitmapFactory.decodeFile(path).getWidth(),
                    BitmapFactory.decodeFile(path).getHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(bit);
            mAsyncGifView.layout(0, 0, mAsyncGifView.getMeasuredWidth(), mAsyncGifView.getMeasuredHeight());
            mAsyncGifView.draw(c);
            mGifBitDec = bit;

            mAsyncGifView = null;
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mDecoded = 1;

            mGifImg.setImageBitmap(mGifBitDec);
            mTxtDelay.setText("Delay:  " + String.valueOf(mDelayMs) + " ms ");
            mTxtFrame.setText("Frames: " + String.valueOf(mFrameCount) + " ");
            mTxtDimenson.setText("Dimensions: " + String.valueOf(mBWidth) + " x " + String.valueOf(mBHeight));
            mTxtSize.setText("Original Image Size: " + String.valueOf(humanReadableByteCount(mOriginSize,true)));
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            ScreenGifWidgetProvider.setters(mPath, mDelayMs);
            ScreenGifWidgetProvider2.setters(mPath, mDelayMs);
            ScreenGifWidgetProvider3.setters(mPath, mDelayMs);

        }
    }
}
