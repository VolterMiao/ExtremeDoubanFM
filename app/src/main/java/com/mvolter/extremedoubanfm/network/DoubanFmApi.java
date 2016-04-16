/*
 *  Copyright (C) 2015 Frank, ExtremeDoubanFM (http://mvolter.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mvolter.extremedoubanfm.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mvolter.extremedoubanfm.interfaces.SongInfoInteractor;
import com.mvolter.extremedoubanfm.models.SongInfo;
import com.mvolter.extremedoubanfm.utils.http.OkHttpUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DoubanFmApi{

    private static final String TAG = "DoubanFmApi";

    private static final String URL_DOMAIN = "http://douban.fm/";
    private static final String URL_GET_CAPTCHA_ID = URL_DOMAIN + "j/new_captcha";
    private static final String URL_GET_CAPTCHA = URL_DOMAIN + "misc/captcha";
    private static final String URL_LOGIN = URL_DOMAIN + "j/login";
    private static final String URL_PLAY_OPERATION = URL_DOMAIN + "j/mine/playlist";
    private static final String URL_CHANGE_CHANNEL = URL_DOMAIN + "j/change_channel";
    private static final String URL_GET_LYRIC = "http://api.douban.com/v2/fm/lyric";

    private SongInfoInteractor.SongInfoInteractorResponse mCallback;

    public DoubanFmApi(SongInfoInteractor.SongInfoInteractorResponse callback) {
        mCallback = callback;
    }

    public void getNextSong() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_PLAY_OPERATION);
        stringBuilder.append("?type=n");
        stringBuilder.append("&sid=1990509&pt=0.0&channel=0&pb=128&from=mainsite&r=f50ea97d4c");

        Request request = new Request.Builder()
                .url(stringBuilder.toString())
                .build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "getNextSong failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                String nextUrl = parseUrl(jsonObject);
                String nextTitle = parseTitle(jsonObject);
                String nextArtist = parseArtist(jsonObject);
                String nextSurfaceUrl = parseSongSurfaceUrl(jsonObject);
                SongInfo newInfo = new SongInfo(nextUrl, nextTitle, nextArtist, nextSurfaceUrl);
                mCallback.onGetNextSongFinished(newInfo);
            }
        };

        OkHttpUtil.enqueue(request, callback);
    }

    public void getCaptchaId() {

        Request request = new Request.Builder().url(URL_GET_CAPTCHA_ID).build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "getCaptchaId failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String id = response.body().string().replace("\"", "");
                mCallback.onGetCaptchaIdFinished(id);
            }
        };

        OkHttpUtil.enqueue(request, callback);
    }

    public void getCaptchaPicById(String captchaId) {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(URL_GET_CAPTCHA);
        urlBuilder.append("?size=m&id=");
        urlBuilder.append(captchaId);

        Request request = new Request.Builder().url(urlBuilder.toString()).build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "getCaptchaPicById failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = response.body().byteStream();
                Bitmap pic = BitmapFactory.decodeStream(is);
                is.close();
                mCallback.onGetCaptchaPicFinished(pic);
            }
        };

        OkHttpUtil.enqueue(request, callback);
    }

    public void getAlbumSurface(String url) {
        Log.i(TAG, "next song surface url = " + url);
        Request request = new Request.Builder().url(url).build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "getAlbumSurface failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = response.body().byteStream();
                Bitmap pic = BitmapFactory.decodeStream(is);
                is.close();
                mCallback.onGetSurfaceFinished(pic);
            }
        };

        OkHttpUtil.enqueue(request, callback);
    }

    public String parseSongSurfaceUrl(JSONObject object) {
        if(object == null) {
            throw new NullPointerException("parseUrl json obj is null");
        }

        String url = null;

        try {
            url = (object.getJSONArray("song")).getJSONObject(0).getString("picture");
            Log.i(TAG, "parseSongSurfaceUrl next song surface url = " + url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }

    private String parseUrl(JSONObject obj) {
        if(obj == null) {
            throw new NullPointerException("parseUrl json obj is null");
        }

        String url = null;

        try {
            url = (obj.getJSONArray("song")).getJSONObject(0).getString("url");
            Log.i(TAG, "parseUrl next song url = " + url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }

    private String parseTitle(JSONObject obj) {
        if(obj == null) {
            throw new NullPointerException("parseTitle json obj is null");
        }

        String title = null;

        try {
            title = (obj.getJSONArray("song")).getJSONObject(0).getString("title");
            Log.i(TAG, "parseTitle next song title = " + title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return title;
    }

    private String parseArtist(JSONObject obj) {
        if(obj == null) {
            throw new NullPointerException("parseArtist json obj is null");
        }

        String title = null;

        try {
            title = (obj.getJSONArray("song")).getJSONObject(0).getString("artist");
            Log.i(TAG, "parseArtist next song artist = " + title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return title;
    }
}
