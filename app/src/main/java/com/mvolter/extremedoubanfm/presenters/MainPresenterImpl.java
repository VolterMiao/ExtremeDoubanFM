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

package com.mvolter.extremedoubanfm.presenters;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import com.mvolter.extremedoubanfm.interactors.ColorCalculatorInteractorImpl;
import com.mvolter.extremedoubanfm.interactors.PlayerInteractorImpl;
import com.mvolter.extremedoubanfm.interactors.SongInfoInteractorImpl;
import com.mvolter.extremedoubanfm.interfaces.ColorCalculatorInteractor;
import com.mvolter.extremedoubanfm.interfaces.MainPresenter;
import com.mvolter.extremedoubanfm.interfaces.MainView;
import com.mvolter.extremedoubanfm.interfaces.PlayerInteractor;
import com.mvolter.extremedoubanfm.interfaces.SongInfoInteractor;
import com.mvolter.extremedoubanfm.models.SongInfo;
import com.mvolter.extremedoubanfm.utils.colorfinder.ColorScheme;

import android.os.Handler;

public class MainPresenterImpl implements MainPresenter, PlayerInteractor.OnStateChangedListener,
                    SongInfoInteractor.SongInfoInteractorResponse{

    private MainView mMainView;
    private PlayerInteractor mPlayerInteractor;
    private SongInfoInteractor mSongInfoInteractor;
    private ColorCalculatorInteractor mColorCalculatorInteractor;

    private Handler mHandler = new Handler();

    public MainPresenterImpl(MainView mainView) {
        this.mMainView = mainView;
        this.mPlayerInteractor = new PlayerInteractorImpl(this);
        this.mSongInfoInteractor = new SongInfoInteractorImpl(this);
        this.mColorCalculatorInteractor = new ColorCalculatorInteractorImpl();
    }

    @Override
    public void clickPlayButton() {
        mPlayerInteractor.clickPlayeButton();
        mSongInfoInteractor.getNextSongInfo();
    }

    @Override
    public void onGetNextSongFinished(final SongInfo songInfo) {
        mSongInfoInteractor.getNextSongSurface(songInfo);
        mPlayerInteractor.playNext(songInfo);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUISongInfo(songInfo);
            }
        });
    }

    @Override
    public void onGetCaptchaIdFinished(String captchaId) {

    }

    @Override
    public void onGetCaptchaPicFinished(Bitmap captchaPic) {

    }

    @Override
    public void onGetSurfaceFinished(final Bitmap surface) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMainView.setSurfaceImage(surface);
                ColorScheme scheme = mColorCalculatorInteractor.getColorScheme(surface);
                mMainView.setColorBarColor(scheme.primaryAccent);
                mMainView.setTitleColorScheme(scheme.secondaryAccent);
                mMainView.setArtistColorScheme(scheme.secondaryAccent);
            }
        });
    }

    private int count;
    private long firClick;
    private long secClick;

    private void clear() {
        count = 0;
        firClick = 0;
        secClick = 0;
    }

    @Override
    public boolean touchSurface(MotionEvent event) {
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
                    mPlayerInteractor.doubleClickSurface();
                }

                clear();
            }
        }

        return false;
    }

    private void updateUISongInfo(SongInfo info) {
        mMainView.setTitleText(info.getTitle());
        mMainView.setArtistText(info.getArtist());
    }

    @Override
    public void onPlayStateChanged(PlayerInteractorImpl.PlayState state) {

        if(PlayerInteractorImpl.PlayState.COMPLETED == state) {
            mSongInfoInteractor.getNextSongInfo();
            return;
        }

        boolean clickable = false;

        if(PlayerInteractorImpl.PlayState.START == state) {
            clickable = true;
        }

        mMainView.setFloatingActionButtonClickable(clickable);
    }

    @Override
    public void onPlayProgressChanged(final int progress) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //mMainView.setProgressBarProgress(progress);
            }
        });
    }

    @Override
    public void onDestroy() {
        mMainView = null;
    }
}
