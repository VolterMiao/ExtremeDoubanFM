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

package com.mvolter.extremedoubanfm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mvolter.extremedoubanfm.models.Account;

public class AccountLocalStoreUtil {
    public static final String SP_NAME = "AccountDetails";
    SharedPreferences accountLocalDatabase;

    public AccountLocalStoreUtil(Context context) {
        accountLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeAccountData(Account account) {
        SharedPreferences.Editor spEditor = accountLocalDatabase.edit();
        spEditor.putString("username", account.username);
        spEditor.putString("password", account.password);
        spEditor.commit();
    }

    public Account getLoggedInAccount() {
        String name = accountLocalDatabase.getString("name", "");
        String pwd = accountLocalDatabase.getString("password", "");

        return new Account(name, pwd);
    }

    public void setAccountLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = accountLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }
    public boolean getAccountLoggedIn() {
        return accountLocalDatabase.getBoolean("loggedIn", false);
    }

    public void clearAccountData() {
        SharedPreferences.Editor spEditor = accountLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
