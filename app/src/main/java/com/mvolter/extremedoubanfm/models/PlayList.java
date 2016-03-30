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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mvolter.extremedoubanfm.network.UrlAPI;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayList {
    public interface OnPlayListListener {
        void onPlayListChanged(PlayList list);
    }

    private static final String TAG = "PlayList";

    public static final int ADD = 0;
    public static final String NEW_SONG = "NEW_SONG";

    OnPlayListListener listener;
    BlockingQueue<SongInfo> list;
    public Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD:
                    Bundle bundle = msg.getData();
                    SongInfo newInfo = bundle.getParcelable(NEW_SONG);
                    addPendingSong(newInfo);
                    break;
                default:
                    Log.e(TAG, "Unknown message " + msg.what);
            }
        }
    };

    public PlayList() {
        list = new LinkedBlockingQueue<SongInfo>();
        new Thread(new UrlAPI(this.h)).start();
    }

    public SongInfo getNext() {
        SongInfo next = null;

        try {
            next = list.remove();
            listener.onPlayListChanged(this);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return next;
    }

    public boolean addPendingSong(SongInfo info) {
        list.add(info);
        return true;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void setOnPlayListListener(OnPlayListListener listener) {
        this.listener = listener;
    }
}
