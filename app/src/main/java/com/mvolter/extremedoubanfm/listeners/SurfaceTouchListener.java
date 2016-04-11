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

package com.mvolter.extremedoubanfm.listeners;

import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;


public class SurfaceTouchListener implements View.OnTouchListener{

    private MediaPlayer mMediaPlayer;
    private FloatingActionButton mFloatingActionButton;
    private int count;
    private long firClick;
    private long secClick;

    public SurfaceTouchListener(MediaPlayer player, FloatingActionButton btn) {
        mMediaPlayer = player;
        mFloatingActionButton = btn;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (firClick != 0 && System.currentTimeMillis() - firClick > 1000) {
                count = 0;
            }

            count++;

            if (count == 1) {
                firClick = System.currentTimeMillis();
            } else if (count == 2) {
                secClick = System.currentTimeMillis();

                if (secClick - firClick < 1000) {
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                            mFloatingActionButton.setClickable(false);
                            //TODO set another image for unclickable
                        } else {
                            mMediaPlayer.start();
                            mFloatingActionButton.setClickable(true);
                            //TODO set another image for unclickable
                        }
                    }
                }

                clear();
            }
        }

        return false;
    }

    private void clear() {
        count = 0;
        firClick = 0;
        secClick = 0;
    }
}
