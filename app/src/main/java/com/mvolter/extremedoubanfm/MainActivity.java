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

package com.mvolter.extremedoubanfm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mvolter.extremedoubanfm.models.PlayList;
import com.mvolter.extremedoubanfm.models.SongInfo;
import com.mvolter.extremedoubanfm.utils.AccountLocalStoreUtil;
import com.mvolter.extremedoubanfm.utils.PlayListMonitor;
import com.mvolter.extremedoubanfm.utils.colorfinder.ColorScheme;
import com.mvolter.extremedoubanfm.utils.colorfinder.DominantColorCalculator;
import com.mvolter.extremedoubanfm.utils.database.SQLiteDatabaseAdapter;
import com.mvolter.extremedoubanfm.views.listeners.SurfaceTouchListener;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "MainActivity";
    private MediaPlayer player;
    private PlayList list;
    private PlayListMonitor monitor;
    private SongInfo currentSong;
    private SQLiteDatabaseAdapter mDbAdapter;

    private AccountLocalStoreUtil accountLocalStore;

    private FrameLayout colorBarFrameLayout;
    private ImageView surfaceImg;
    private TextView titleTxt;
    private TextView artistTxt;
    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colorBarFrameLayout = (FrameLayout) findViewById(R.id.frame_layout_color_bar);

        surfaceImg = (ImageView) findViewById(R.id.image_view_surface);
        surfaceImg.setLongClickable(true);

        titleTxt = (TextView) findViewById(R.id.image_view_song_title);
        artistTxt = (TextView) findViewById(R.id.image_view_song_artist);

        fabBtn = (FloatingActionButton) findViewById(R.id.fab);
        fabBtn.setOnClickListener(this);

        surfaceImg.setOnTouchListener(new SurfaceTouchListener(player, fabBtn));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        player = new MediaPlayer();
        list = new PlayList();
        monitor = new PlayListMonitor();
        list.setOnPlayListListener(monitor);

        player.setOnCompletionListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        try {
            if (player.isPlaying()) {
                Log.i(TAG, "player is playing, reset it, play next song");
                player.reset();
            }

            SongInfo info = list.getNext();
            currentSong = info;
            player.setDataSource(info.getUrl());
            player.prepare();
            player.start();

            Bitmap bit = info.getSurface();

            surfaceImg.setImageBitmap(info.getSurface());
            titleTxt.setText(info.getTitle());
            artistTxt.setText(info.getArtist());

            DominantColorCalculator calculator = new DominantColorCalculator(bit);
            ColorScheme scheme = calculator.getColorScheme();

            colorBarFrameLayout.setBackgroundColor(scheme.primaryAccent);
            titleTxt.setTextColor(scheme.secondaryAccent);
            artistTxt.setTextColor(scheme.secondaryAccent);
        } catch (IllegalStateException e) {
            System.out.println(player.getCurrentPosition());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            Log.i(TAG, "player onCompletion, play next");
            SongInfo info = list.getNext();
            player.reset();
            player.setDataSource(info.getUrl());
            player.prepare();
            player.start();

            Bitmap bit = info.getSurface();

            surfaceImg.setImageBitmap(info.getSurface());
            titleTxt.setText(info.getTitle());
            artistTxt.setText(info.getArtist());

            DominantColorCalculator calculator = new DominantColorCalculator(bit);
            ColorScheme scheme = calculator.getColorScheme();

            colorBarFrameLayout.setBackgroundColor(scheme.primaryAccent);
            fabBtn.setBackgroundColor(scheme.secondaryAccent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
