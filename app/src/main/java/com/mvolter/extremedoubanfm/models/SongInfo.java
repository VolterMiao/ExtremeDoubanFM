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

package com.mvolter.extremedoubanfm.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class SongInfo implements Parcelable{
    private String url;
    private String title;
    private String artist;

    private String surfaceUrl;
    private Bitmap surface;

    public SongInfo() {}

    public SongInfo(String url, String title, String artist, String surfUrl) {
        this.url = url;
        this.title = title;
        this.artist = artist;
        this.surfaceUrl = surfUrl;
    }

    private SongInfo(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.artist = in.readString();
        this.surface = in.readParcelable(null);
    }

    public String getSurfaceUrl() {
        return surfaceUrl;
    }

    public void setSurfaceUrl(String surfaceUrl) {
        this.surfaceUrl = surfaceUrl;
    }

    public Bitmap getSurface() {
        return surface;
    }

    public void setSurface(Bitmap surface) {
        this.surface = surface;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeParcelable(surface, 0);
    }

    public static final Creator<SongInfo> CREATOR
            = new Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel source) {
            return new SongInfo(source);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };
}
