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

package com.mvolter.extremedoubanfm.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvolter.extremedoubanfm.R;
import com.mvolter.extremedoubanfm.models.SongInfo;

import butterknife.Bind;

public class MusicListAdapter extends RecyclerView.Adapter {

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_view_surface) ImageView surface;
        @Bind(R.id.text_view_song_title)private TextView title;
        @Bind(R.id.text_view_song_artist) TextView artist;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ImageView getSurface() {
            return surface;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getArtist() {
            return artist;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.music_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        SongInfo songInfo = data[position];

        vh.getSurface().setImageBitmap(songInfo.getSurface());
        vh.getTitle().setText(songInfo.getTitle());
        vh.getArtist().setText(songInfo.getArtist());
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    private SongInfo[] data = new SongInfo[]{
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
            new SongInfo("xxx","a","123","http://"),
    };
}
