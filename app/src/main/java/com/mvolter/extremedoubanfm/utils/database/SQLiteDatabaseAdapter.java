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

package com.mvolter.extremedoubanfm.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mvolter.extremedoubanfm.models.SongInfo;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import static com.mvolter.extremedoubanfm.utils.database.FavouriteSong.SongEntry.*;


public class SQLiteDatabaseAdapter {

    public static FavouriteSongDbHelper mDbHelper;

    public static SQLiteDatabase mDb;

    private static SQLiteDatabaseAdapter sInstance;

    public static SQLiteDatabaseAdapter getsInstance(Context context) {

        if(sInstance == null) {
            sInstance = new SQLiteDatabaseAdapter(context);
        }

        return sInstance;
    }

    private SQLiteDatabaseAdapter(Context context) {

        mDbHelper = new FavouriteSongDbHelper(context);

        mDb = mDbHelper.getWritableDatabase();
    }

    /**
     * 将标记红心的歌曲信息插入数据库
     * @param info
     * @return row number after inserted
     */
    public long insert(SongInfo info) {

        long ret = 0;

        mDb.beginTransaction();

        try {
            //通过SongInfo构造ContentValues
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_TITLE, info.getTitle());
            values.put(COLUMN_NAME_ARTIST, info.getArtist());

            //将Bitmap转化成BLOB，放入values
            Bitmap bmp = info.getSurface();
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
            values.put(COLUMN_NAME_SURFACE, os.toByteArray());

            //将values插入数据库
            ret = mDb.insert(TABLE_NAME, null, values);

            //清空values
            values.clear();

            //标记事物成功
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.endTransaction();
        }

        return ret;
    }

    /**
     * 查询红心歌曲
     * @return 歌曲列表
     */
    public List<SongInfo> query() {
        Cursor cursor = null;
        List<SongInfo> list = new LinkedList<SongInfo>();

        String[] columns = new String[]{
                COLUMN_NAME_TITLE,
                COLUMN_NAME_ARTIST,
                COLUMN_NAME_SURFACE
        };

        mDb.beginTransaction();

        try {

            //查询数据库，返回cursor
            cursor = mDb.query(TABLE_NAME, columns, null, null, null, null, "_id asc");

            //获取各列的index
            int titleIndex = cursor.getColumnIndex(columns[0]);
            int artistIndex = cursor.getColumnIndex(columns[1]);
            int surfaceIndex = cursor.getColumnIndex(columns[2]);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                SongInfo si = new SongInfo();

                String title = cursor.getString(titleIndex);
                si.setTitle(title);
                String artist = cursor.getString(artistIndex);
                si.setArtist(artist);

                byte[] in = cursor.getBlob(surfaceIndex);
                Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
                si.setSurface(bmp);

                list.add(si);
            }

            cursor.close();

            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDb.endTransaction();

        return list;
    }
}
