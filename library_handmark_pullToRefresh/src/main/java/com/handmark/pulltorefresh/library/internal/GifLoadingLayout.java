/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class GifLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	private final Matrix mHeaderImageMatrix;

    private final boolean mRotateDrawableWhilePulling;
    private GifAnimation mGifAnimation;

    private int[] mGifRes = {
            R.drawable.dropdown_loading_00,
            R.drawable.dropdown_loading_01,
            R.drawable.dropdown_loading_02,
    };

    private GifDrawable mGifDrawable;

    public GifLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
        try {
            mGifDrawable = new GifDrawable(context.getAssets(), "squirrel.gif");
            mHeaderImage.setImageDrawable(mGifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
	}

    int mPrevIndex = -1;
	protected void onPullImpl(float scaleOfLayout) {
        if (mPrevIndex == -1 && scaleOfLayout > 0.3) {
            mGifDrawable.pause();
            mPrevIndex = 0;
        }
    }

    @Override
	protected void refreshingImpl() {
        if (!mGifDrawable.isPlaying()) {
            mGifDrawable.start();
        }
    }

	@Override
	protected void resetImpl() {
        pauseGif();
    }

    private void pauseGif() {
        if (mGifDrawable != null && mGifDrawable.isPlaying()) {
            mGifDrawable.pause();
        }
    }

    @Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}
}
