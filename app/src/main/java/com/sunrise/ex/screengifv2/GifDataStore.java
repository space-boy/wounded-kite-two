package com.sunrise.ex.screengifv2;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class GifDataStore {

    private static GifDataStore sGifDataStore;
    private Context mAppContext;
    private ArrayList<GifMeta> mGifMetaArrayList;
    private static final String TAG = "GifDataStore";
    private static final String FILENAME = "GifMetaData.json";
    private GifWidgetJSONSerializer mSerializer;

    public ArrayList<GifMeta> getGifMetaArrayList() {
        return mGifMetaArrayList;
    }

    private GifDataStore(Context appcontext){
        mAppContext = appcontext;
        mSerializer = new GifWidgetJSONSerializer(mAppContext,FILENAME);

        try{
            mGifMetaArrayList = mSerializer.loadGifMetas();
        }catch (Exception e){
            mGifMetaArrayList = new ArrayList<GifMeta>();

        }

        mSerializer = new GifWidgetJSONSerializer(mAppContext,FILENAME);

    }

    public boolean saveGifMetaData(){
        try{
            mSerializer.saveGifMetas(mGifMetaArrayList);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public static GifDataStore get(Context c){
        if(sGifDataStore == null){
            sGifDataStore = new GifDataStore(c.getApplicationContext());
        }
        return sGifDataStore;
    }
}
