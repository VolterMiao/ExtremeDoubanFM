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

package com.mvolter.extremedoubanfm.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mvolter.extremedoubanfm.R;
import com.mvolter.extremedoubanfm.models.Account;
import com.mvolter.extremedoubanfm.network.UrlAPI;
import com.mvolter.extremedoubanfm.utils.AccountLocalStoreUtil;
import com.mvolter.extremedoubanfm.utils.http.OkHttpUtil;

public class LoginActivity extends Activity
        implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    EditText userNameEdt;
    EditText passwordEdt;
    EditText captchaSolutionEdt;
    ImageView captchaImg;

    Button loginBtn;

    AccountLocalStoreUtil accountLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountLocalStore = new AccountLocalStoreUtil(this);

        userNameEdt = (EditText) findViewById(R.id.edit_text_username);
        passwordEdt = (EditText) findViewById(R.id.edit_text_password);
        captchaSolutionEdt = (EditText) findViewById(R.id.edit_text_captcha_solution);

        captchaImg = (ImageView) findViewById(R.id.image_view_captcha);

        loginBtn = (Button) findViewById(R.id.button_login);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_login:

                String username = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                boolean canLogin = checkAccountValid(username, password);

                if(canLogin) {

                    if(captchaImg.getVisibility() == ImageView.GONE) {

                        String captchaId = UrlAPI.getCaptchaId();
                        Bitmap captchaBmp = UrlAPI.getCaptcha(captchaId);

                        captchaImg.setImageBitmap(captchaBmp);
                        captchaImg.setVisibility(ImageView.VISIBLE);
                        captchaSolutionEdt.setVisibility(EditText.VISIBLE);
                    } else {

                        String captchaSolution = captchaSolutionEdt.getText().toString();

                        boolean captchaOk = checkCaptchaSolutionValid(captchaSolution);

                        if(captchaOk) {

                        }


                    }
                } else {

                }





                Account account = new Account(username, password);
                accountLocalStore.storeAccountData(account);
                accountLocalStore.setAccountLoggedIn(true);


                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean checkAccountValid(String username, String password) {
        return true;
    }

    private boolean checkCaptchaSolutionValid(String captchaSolution) {
        return true;
    }
}
