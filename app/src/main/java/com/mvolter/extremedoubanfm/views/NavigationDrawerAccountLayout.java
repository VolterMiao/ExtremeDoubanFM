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

package com.mvolter.extremedoubanfm.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mvolter.extremedoubanfm.ui.adapter.NavigationDrawerAccountsMenuAdapter;
import com.mvolter.extremedoubanfm.models.Account;

public class NavigationDrawerAccountLayout extends LinearLayout{

    private Account mAccount;
    private ImageView mAccountsMenuSwitch;
    private ImageView mBackground;
    private TextView mTitle;
    private TextView mDescription;
    private RoundedImageView mPicture;
    private NavigationDrawerAccountsMenuAdapter mAccountsMenuAdapter;


    public NavigationDrawerAccountLayout(Context context) {
        super(context);
    }

    public NavigationDrawerAccountLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationDrawerAccountLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
