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

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mvolter.extremedoubanfm.R;
import com.mvolter.extremedoubanfm.interfaces.MainPresenter;
import com.mvolter.extremedoubanfm.interfaces.MainView;
import com.mvolter.extremedoubanfm.presenters.MainPresenterImpl;

public class MainActivity extends AppCompatActivity
        implements MainView, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ImageView.OnTouchListener{

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;

    private FrameLayout mColorBarFrameLayout;
    private ImageView mSurfaceImg;
    private TextView mTitleTxt;
    private TextView mArtistTxt;
    private FloatingActionButton mFabBtn;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mColorBarFrameLayout = (FrameLayout) findViewById(R.id.frame_layout_color_bar);

        mSurfaceImg = (ImageView) findViewById(R.id.image_view_surface);
        mSurfaceImg.setLongClickable(true);

        mTitleTxt = (TextView) findViewById(R.id.image_view_song_title);
        mArtistTxt = (TextView) findViewById(R.id.image_view_song_artist);

        mFabBtn = (FloatingActionButton) findViewById(R.id.fab);
        mFabBtn.setOnClickListener(this);

        mSurfaceImg.setOnTouchListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mMainPresenter = new MainPresenterImpl(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_channel_selector) {
            Toast.makeText(this, "MHz", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_share) {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

        
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                mMainPresenter.clickPlayButton();
                break;
            case R.id.image_view_surface:
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mMainPresenter.touchSurface(event);
    }

    @Override
    public void setSurfaceImage(Bitmap image) {
        mSurfaceImg.setImageBitmap(image);
    }

    @Override
    public void setTitleText(String title) {
        mTitleTxt.setText(title);
    }

    @Override
    public void setArtistText(String artist) {
        mArtistTxt.setText(artist);
    }

    @Override
    public void setColorBarColor(int color) {
        mColorBarFrameLayout.setBackgroundColor(color);
    }

    @Override
    public void setTitleColorScheme(int color) {
        mTitleTxt.setTextColor(color);
    }

    @Override
    public void setArtistColorScheme(int color) {
        mArtistTxt.setTextColor(color);
    }

    @Override
    public void setFloatingActionButtonColor(int color) {
        mFabBtn.setBackgroundColor(color);
    }

    @Override
    public void setFloatingActionButtonClickable(boolean clickable) {
        mFabBtn.setClickable(clickable);
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.onDestroy();
        super.onDestroy();
    }
}


