package com.sunrise.ex.screengifv2;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class GifWidgetJSONSerializer {

    private Context mContext;
    private String mFilename;

    public GifWidgetJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    public ArrayList<GifMeta> loadGifMetas() throws JSONException, IOException{
        ArrayList<GifMeta> GifMetas = new ArrayList<GifMeta>();
        BufferedReader reader = null;

        try{
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for(int i = 0; i <array.length();i++){
                GifMetas.add(new GifMeta(array.getJSONObject(i)));
            }

            } catch (FileNotFoundException e){


            } finally {
            if(reader != null)
                reader.close();
        }
        return GifMetas;
    }

    public void saveGifMetas(ArrayList<GifMeta> GifMetas) throws JSONException, IOException{

        JSONArray array = new JSONArray();

        for(GifMeta g : GifMetas){
            array.put(g.toJSON());
        }

        Writer writer = null;

        try{
            OutputStream out = mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());

        } finally {
            if(writer != null){
                writer.close();
            }
        }

    }
}
