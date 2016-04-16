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

package com.mvolter.extremedoubanfm.interactors;

import android.media.MediaPlayer;
import android.util.Log;

import com.mvolter.extremedoubanfm.interfaces.PlayerInteractor;
import com.mvolter.extremedoubanfm.models.SongInfo;

import java.io.IOException;

public class PlayerInteractorImpl implements PlayerInteractor, MediaPlayer.OnCompletionListener {

    private static final String TAG = "PlayerInteractorImpl";

    public enum PlayState {
        START, PAUSE, RESUME, STOP, COMPLETED
    }

    private MediaPlayer mPlayer;
    private OnSongChangedListener mListener;

    public PlayerInteractorImpl(OnSongChangedListener listener) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mListener = listener;
    }

    @Override
    public void clickPlayeButton() {
        if (mPlayer.isPlaying()) {
            Log.i(TAG, "player is playing, reset it, play next song");
            mPlayer.reset();
        }
    }

    @Override
    public void doubleClickSurface() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                mPlayer.start();
            }
            mListener.onPlayStateChanged(mPlayer.isPlaying() ? PlayState.START : PlayState.PAUSE);
        }
    }

    @Override
    public void playNext(SongInfo info) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(info.getUrl());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mListener.onPlayStateChanged(PlayState.COMPLETED);
    }
}
