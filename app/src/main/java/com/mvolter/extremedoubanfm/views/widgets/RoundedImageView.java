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

package com.mvolter.extremedoubanfm.views.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        Bitmap bitmap = drawableToBitmap(drawable);
        Bitmap roundedBitmap = getCroppedBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, true),
                getWidth());

        canvas.drawBitmap(roundedBitmap, 0, 0, null);
    }

    /**
     * Convert any picture which we get from server to a bitmap
     *
     * @param drawable picture we get
     * @see
     * @return bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = null;

        // Drawable -> BitmapDrawable
        if(drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        //BitmapDrawable -> Bitmap (empty)
        if(drawable.getIntrinsicHeight() <= 0 || drawable.getIntrinsicWidth() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        }

        /**
         * How to draw an existed bitmap on an empty bitmap
         *
         * 1. make a canvas use an empty bitmap
         * 2. set bound for an existed bitmap with the former's width and height
         * 3. draw the 'drawable' on this canvas
         */
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Crop a bitmap to a circle bitmap
     *
     * @param bmp which is cropped
     * @see
     * @return newBmp which is a circle bitmap
     */
    public static  Bitmap getCroppedBitmap(Bitmap bmp, int size) {

        Bitmap scaleBmp;

        if(bmp.getWidth() != size || bmp.getHeight() != size) {
            //picture quality decreased when filter is false, try it
            scaleBmp = Bitmap.createScaledBitmap(bmp, size, size, false);
        } else {
            scaleBmp = bmp;
        }

        Rect rect = new Rect(0, 0, scaleBmp.getWidth(), scaleBmp.getHeight());
        Bitmap newBmp = Bitmap.createBitmap(scaleBmp.getWidth(), scaleBmp.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        //防止边缘锯齿
        paint.setAntiAlias(true);

        /**
         * 对位图进行滤波处理。如果该项设置为true，则图像在动画
         * 进行中会滤掉对Bitmap图像的优化操作，加快显示速度，
         * 本设置项依赖于dither和xfermode的设置。
         */

        paint.setFilterBitmap(true);

        //？
        paint.setDither(true);

        Canvas canvas = new Canvas(newBmp);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaleBmp.getWidth() / 2 + 0f,
                scaleBmp.getHeight() / 2 + 0f,
                scaleBmp.getWidth() / 2 + 0f, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(scaleBmp, rect, rect, paint);

        return newBmp;
    }
}
