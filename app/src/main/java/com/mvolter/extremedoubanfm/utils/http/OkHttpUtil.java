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

package com.mvolter.extremedoubanfm.utils.http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static final OkHttpClient mClient = new OkHttpClient();

    static {
        mClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }
    /**
     * Execute a request synchronized
     * @param
     * @see
     * @return
     */
    public static Response execute(Request request) throws IOException {
        return mClient.newCall(request).execute();
    }

    /**
     * Execute a request by starting an async thread
     * @param
     * @see
     * @return
     */
    public static void enqueue(Request request, Callback callback) {
        mClient.newCall(request).enqueue(callback);
    }

    /**
     * Execute a request by starting an async thread and ignore the result
     * @param
     * @see
     * @return
     */
    public static void enqueue(Request request) {
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    private static final String CHARSET_NAME = "UTF-8";


}
