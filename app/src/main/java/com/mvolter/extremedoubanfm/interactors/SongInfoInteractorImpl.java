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

import com.mvolter.extremedoubanfm.interfaces.SongInfoInteractor;
import com.mvolter.extremedoubanfm.models.SongInfo;
import com.mvolter.extremedoubanfm.network.DoubanFmApi;

public class SongInfoInteractorImpl implements SongInfoInteractor{

    private DoubanFmApi mDoubanFmApi;
    private SongInfoInteractorResponse mCallback;

    public SongInfoInteractorImpl(SongInfoInteractorResponse callback) {
        mCallback = callback;
        mDoubanFmApi = new DoubanFmApi(mCallback);
    }

    @Override
    public void getNextSongInfo() {
        mDoubanFmApi.getNextSong();
    }

    @Override
    public void getNextSongSurface(SongInfo info) {
        mDoubanFmApi.getAlbumSurface(info.getSurfaceUrl());
    }
}
