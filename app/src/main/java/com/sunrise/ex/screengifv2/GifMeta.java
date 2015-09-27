package com.sunrise.ex.screengifv2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class GifMeta {

    private int mDelay;
    private int mFrames;
    private int mOriginalSize;
    private String mGifPath;
    private int mHeight;
    private int mWidth;
    private UUID mUUID;
    private boolean isActive;
    private boolean isActive2;
    private boolean isActive3;

    private final static String JSON_DELAY = "delay";
    private final static String JSON_FRAMES = "frames";
    private final static String JSON_SIZE = "size";
    private final static String JSON_PATH = "path";
    private final static String JSON_HEIGHT = "height";
    private final static String JSON_WIDTH = "width";
    private final static String JSON_ID = "id";
    private final static String JSON_ACTIVE = "active";
    private final static String JSON_ACTIVE2 = "active2";
    private final static String JSON_ACTIVE3 = "active3";

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_DELAY,mDelay);
        json.put(JSON_FRAMES,mFrames);
        json.put(JSON_SIZE,mOriginalSize);
        json.put(JSON_PATH,mGifPath);
        json.put(JSON_HEIGHT,mHeight);
        json.put(JSON_WIDTH,mWidth);
        json.put(JSON_ID,mUUID);
        json.put(JSON_ACTIVE,isActive);
        json.put(JSON_ACTIVE2,isActive2);
        json.put(JSON_ACTIVE3,isActive3);

        return json;
    }

    public GifMeta(JSONObject json) throws JSONException{

        mUUID = UUID.fromString(json.getString(JSON_ID));
        mDelay = json.getInt(JSON_DELAY);
        mFrames = json.getInt(JSON_FRAMES);
        mOriginalSize = json.getInt(JSON_SIZE);
        mGifPath = json.getString(JSON_PATH);
        mHeight = json.getInt(JSON_HEIGHT);
        mWidth = json.getInt(JSON_WIDTH);
        isActive = json.getBoolean(JSON_ACTIVE);
        isActive2 = json.getBoolean(JSON_ACTIVE2);
        isActive3 = json.getBoolean(JSON_ACTIVE3);

    }

    public GifMeta(){
        mUUID = UUID.randomUUID();
    }

    public boolean isActive2() {
        return isActive2;
    }

    public void setActive2(boolean isActive2) {
        this.isActive2 = isActive2;
    }

    public boolean isActive3() {
        return isActive3;
    }

    public void setActive3(boolean isActive3) {
        this.isActive3 = isActive3;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getDelay() {
        return mDelay;
    }

    public void setDelay(int delay) {
        mDelay = delay;
    }

    public int getFrames() {
        return mFrames;
    }

    public void setFrames(int frames) {
        mFrames = frames;
    }

    public int getOriginalSize() {
        return mOriginalSize;
    }

    public void setOriginalSize(int originalSize) {
        mOriginalSize = originalSize;
    }

    public String getGifPath() {
        return mGifPath;
    }

    public void setGifPath(String gifPath) {
        mGifPath = gifPath;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

}
