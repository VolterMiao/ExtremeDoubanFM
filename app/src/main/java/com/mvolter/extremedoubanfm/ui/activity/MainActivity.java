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

package com.mvolter.extremedoubanfm.ui.activity;

import android.app.Application;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mvolter.extremedoubanfm.ApplicationComponent;
import com.mvolter.extremedoubanfm.ApplicationModule;
import com.mvolter.extremedoubanfm.DaggerApplicationComponent;
import com.mvolter.extremedoubanfm.R;
import com.mvolter.extremedoubanfm.ui.activity.component.DaggerMainActivityComponent;
import com.mvolter.extremedoubanfm.ui.activity.component.MainActivityComponent;
import com.mvolter.extremedoubanfm.ui.activity.module.MainActivityModule;
import com.mvolter.extremedoubanfm.ui.adapter.MusicListAdapter;
import com.mvolter.extremedoubanfm.interfaces.MainPresenter;
import com.mvolter.extremedoubanfm.interfaces.MainView;
import com.mvolter.extremedoubanfm.ui.activity.presenter.MainPresenterImpl;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class MainActivity extends AppCompatActivity
        implements MainView, NavigationView.OnNavigationItemSelectedListener,
            ImageView.OnTouchListener, AppBarLayout.OnOffsetChangedListener{

    private static final String TAG = "MainActivity";

    private MainActivityComponent mMainActivityComponent;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mSongNameInAppBar;

    @Bind(R.id.app_bar) AppBarLayout mAppBarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.frame_layout_color_bar) FrameLayout mColorBarFrameLayout;
    @Bind(R.id.image_view_surface) ImageView mSurfaceImg;
    @Bind(R.id.text_view_song_title) TextView mTitleTxt;
    @Bind(R.id.text_view_song_artist) TextView mArtistTxt;
    @Bind(R.id.recycler_view_play_history) RecyclerView mRecyclerView;
    @Bind(R.id.fab) FloatingActionButton mFabBtn;

    @Inject MainPresenterImpl mMainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mSurfaceImg.setOnTouchListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new MusicListAdapter());

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        initInject();
    }

    private void initInject() {
        getMainActivityComponent().inject(this);
    }

    protected MainActivityComponent getMainActivityComponent() {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .mainActivityModule(getMainActivityModule())
                .build();
    }

    protected MainActivityModule getMainActivityModule() {
        return new MainActivityModule(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .build();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(verticalOffset == 0 ){
            //Expanded
            mCollapsingToolbarLayout.setTitle(null);
        }else if(Math.abs(verticalOffset) >= mAppBarLayout.getTotalScrollRange()){
            //Collapsed
            mCollapsingToolbarLayout.setTitle(mSongNameInAppBar);
        }else {
            //Other
            mCollapsingToolbarLayout.setTitle(null);
        }
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

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager =
                (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if(searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if(searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(
                    MainActivity.this.getComponentName()
            ));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
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

    @OnClick(R.id.fab) void clickFloatingActionButton() {
        mMainPresenter.clickPlayButton();
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
        mSongNameInAppBar = title;
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


