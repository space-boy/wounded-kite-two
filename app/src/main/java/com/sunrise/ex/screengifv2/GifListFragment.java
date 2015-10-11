package com.sunrise.ex.screengifv2;


import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GifListFragment extends ListFragment {

    public static final String ACTIVE = "com.sunrise.ex.screengifv2.Active";
    public static final String ACTIVE2 = "com.sunrise.ex.screengifv2.Active2";
    public static final String ACTIVE3 = "com.sunrise.ex.screengifv2.Active3";
    public static final String LOCATION_INDEX = "com.sunrise.ex.screengifv2.Location_Index";

    public  String PATH_KEY  = "com.sunrise.ex.screengifv2.PATH_KEY";
    public  String WIDTH_KEY  = "com.sunrise.ex.screengifv2.WIDTH_KEY";
    public  String HIGH_KEY  = "com.sunrise.ex.screengifv2.HIGH_KEY";
    public  String DELAY_KEY  = "com.sunrise.ex.screengifv2.DELAY_KEY";

    public  String PATH_KEY2  = "com.sunrise.ex.screengifv2.PATH_KEY2";
    public  String WIDTH_KEY2  = "com.sunrise.ex.screengifv2.WIDTH_KEY2";
    public  String HIGH_KEY2  = "com.sunrise.ex.screengifv2.HIGH_KEY2";
    public  String DELAY_KEY2  = "com.sunrise.ex.screengifv2.DELAY_KEY2";

    public  String PATH_KEY3  = "com.sunrise.ex.screengifv2.PATH_KEY3";
    public  String WIDTH_KEY3  = "com.sunrise.ex.screengifv2.WIDTH_KEY3";
    public  String HIGH_KEY3  = "com.sunrise.ex.screengifv2.HIGH_KEY3";
    public  String DELAY_KEY3 = "com.sunrise.ex.screengifv2.DELAY_KEY3";


    private ArrayList<GifMeta> mGifMetaArrayList;
    private TextView mGifPath;
    private ImageView mGifImage;
    private CheckBox mCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGifMetaArrayList = GifDataStore.get(getActivity()).getGifMetaArrayList();

        for(Iterator<GifMeta> it = mGifMetaArrayList.iterator(); it.hasNext(); ){
            GifMeta metagif = it.next();
            if(!new File(metagif.getGifPath()).exists()){

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(
                        getActivity().getApplicationContext());

                SharedPreferences.Editor editor = pref.edit();

                if(metagif.isActive()) {
                    editor.remove(PATH_KEY);
                    editor.remove(WIDTH_KEY);
                    editor.remove(HIGH_KEY);
                    editor.remove(DELAY_KEY);
                    editor.commit();
                }
                if(metagif.isActive2()) {
                    editor.remove(PATH_KEY2);
                    editor.remove(WIDTH_KEY2);
                    editor.remove(HIGH_KEY2);
                    editor.remove(DELAY_KEY2);
                    editor.commit();
                }
                if(metagif.isActive3()) {
                    editor.remove(PATH_KEY3);
                    editor.remove(WIDTH_KEY3);
                    editor.remove(HIGH_KEY3);
                    editor.remove(DELAY_KEY3);
                    editor.commit();
                }
                it.remove();

            }
        }

        GifMetaAdapter adapter = new GifMetaAdapter(mGifMetaArrayList);
        setListAdapter(adapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        GifDataStore.get(getActivity()).saveGifMetaData();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        GifMeta g = ((GifMetaAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity().getApplicationContext(),NewConfig.class);
        i.putExtra(SearchListActivity.GIF_PATH,g.getGifPath());
        i.putExtra(LOCATION_INDEX,mGifMetaArrayList.indexOf(g));
        i.putExtra(ACTIVE, g.isActive());
        i.putExtra(ACTIVE2, g.isActive2());
        i.putExtra(ACTIVE3, g.isActive3());

        startActivity(i);

    }

    private class GifMetaAdapter extends ArrayAdapter<GifMeta>{

        public GifMetaAdapter(ArrayList<GifMeta> GifMetas){
            super(getActivity(),0,GifMetas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gif_fragment,null);
            }

            GifMeta g = getItem(position);

            mCheckBox = (CheckBox) convertView.findViewById(R.id.active_chk);
            mGifPath = (TextView) convertView.findViewById(R.id.txt_path);
            mGifImage = (ImageView) convertView.findViewById(R.id.img_preview);

            if(g.isActive())
            mCheckBox.setChecked(g.isActive());

            if(g.isActive2())
                mCheckBox.setChecked(g.isActive2());

            if(g.isActive3())
                mCheckBox.setChecked(g.isActive3());

            mGifPath.setText(g.getGifPath().substring(g.getGifPath().lastIndexOf('/')).replace("/",""));
            mGifImage.setImageBitmap(BitmapFactory.decodeFile(g.getGifPath()));
            return convertView;

        }
    }
}
