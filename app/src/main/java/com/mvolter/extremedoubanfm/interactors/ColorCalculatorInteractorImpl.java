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

import android.graphics.Bitmap;

import com.mvolter.extremedoubanfm.interfaces.ColorCalculatorInteractor;
import com.mvolter.extremedoubanfm.utils.colorfinder.ColorScheme;
import com.mvolter.extremedoubanfm.utils.colorfinder.DominantColorCalculator;

public class ColorCalculatorInteractorImpl implements ColorCalculatorInteractor {
    @Override
    public ColorScheme getColorScheme(Bitmap image) {
        DominantColorCalculator calculator = new DominantColorCalculator(image);
        return calculator.getColorScheme();
    }
}
