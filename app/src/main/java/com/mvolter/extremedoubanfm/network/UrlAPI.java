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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.mvolter.extremedoubanfm.models.PlayList;
import com.mvolter.extremedoubanfm.models.SongInfo;
import com.mvolter.extremedoubanfm.utils.http.OkHttpUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okio.BufferedSink;

public class UrlAPI implements Runnable{
    private static final String TAG = "UrlAPI";

    private static final String URL_GET_CAPTCHA_ID = "http://douban.fm/j/new_captcha";
    private static final String URL_GET_CAPTCHA = "http://douban.fm/misc/captcha";
    private static final String URL_LOGIN = "http://douban.fm/j/login";

    private String domain;
    private Handler h;

    public UrlAPI(Handler h) {
        domain = new String("http://douban.fm/");
        this.h = h;
    }

    @Override
    public void run() {
        SongInfo info = getNextSong();
        Log.i(TAG, "get new song:" + info);

        Bundle data = new Bundle();
        data.putParcelable(PlayList.NEW_SONG, info);

        Message msg = new Message();
        msg.what = PlayList.ADD;
        msg.setData(data);
        msg.setTarget(h);
        msg.sendToTarget();
    }

    private SongInfo getNextSong() {
        String nextUrl = null;
        String nextTitle = null;
        String nextArtist =null;
        Bitmap nextSurface = null;
        SongInfo newInfo = null;

        StringBuilder sb = new StringBuilder(domain);
        sb.append("j/mine/playlist?");
        sb.append("type=n");
        sb.append("&sid=1990509&pt=0.0&channel=0&pb=128&from=mainsite&r=f50ea97d4c");

        try {
            URL url = new URL(sb.toString());
            JSONObject obj = request(url);
            nextUrl = parseUrl(obj);
            nextTitle = parseTitle(obj);
            nextArtist = parseArtist(obj);
            nextSurface = getSurface(obj);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        newInfo = new SongInfo(nextUrl, nextTitle, nextArtist, nextSurface);

        return newInfo;
    }

    private JSONObject request(URL url) {
        if(url == null) {
            throw new NullPointerException("request url is null");
        }

        JSONObject rlt = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.connect();

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            in.close();

            rlt = new JSONObject(response.toString());
            Log.i(TAG, "request JSONObject=" + rlt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rlt;
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

    private Bitmap getSurface(JSONObject obj) {
        String surfUrl = null;
        Bitmap surface = null;

        try {
            surfUrl = (obj.getJSONArray("song")).getJSONObject(0).getString("picture");
            Log.i(TAG, "next song surfUrl = " + surfUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(surfUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.connect();

            InputStream in = conn.getInputStream();
            surface = BitmapFactory.decodeStream(in);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return surface;
    }

    /**
     * Get captcha ID
     * @param
     * @see OkHttpUtil
     * @return String captcha ID
     */
    public static String getCaptchaId() {

        String id = null;

        Request request = new Request.Builder()
                .url(URL_GET_CAPTCHA_ID).build();

        try {
            Response response = OkHttpUtil.execute(request);
            if(response.isSuccessful()) {
                id = response.body().toString().trim();
            } else {
                //TODO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     *
     * @param
     * @see
     * @return
     */
    public static Bitmap getCaptcha(String captchaId) {

        Bitmap pic = null;

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(URL_GET_CAPTCHA);
        urlBuilder.append("?id=");
        urlBuilder.append(captchaId);
        urlBuilder.append("&size=s");

        final Request request = new Request.Builder()
                .url(urlBuilder.toString()).build();

        try {
            Response response = OkHttpUtil.execute(request);

            if(response.isSuccessful()) {
                InputStream in = response.body().byteStream();
                pic = BitmapFactory.decodeStream(in);
            } else {
                //TODO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pic;
    }

    public static String loginWithCaptcha(String username,
                                          String password,
                                          String captchaId,
                                          String captchaSolution) {

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("source=radio");
        bodyBuilder.append("&alias=");
        bodyBuilder.append(username);
        bodyBuilder.append("&form_password=");
        bodyBuilder.append(password);
        bodyBuilder.append("&captcha_solution=");
        bodyBuilder.append(captchaId);
        bodyBuilder.append("&captcha_id=");
        bodyBuilder.append(captchaSolution);
        bodyBuilder.append("&captcha_id=");
        bodyBuilder.append(captchaId);
        //RequestBody requestBody = RequestBody.
        //Request request = new Request.Builder().url(URL_LOGIN).build();
        //request.method();

        return null;
    }
}
