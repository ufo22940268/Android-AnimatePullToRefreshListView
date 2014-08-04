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

public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	private final Matrix mHeaderImageMatrix;

	private float mRotationPivotX, mRotationPivotY;

    private final boolean mRotateDrawableWhilePulling;
    private GifAnimation mGifAnimation;

    private int[] mGifRes = {
            R.drawable.dropdown_loading_00,
            R.drawable.dropdown_loading_01,
            R.drawable.dropdown_loading_02,
    };

    public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
        System.out.println("RotateLoadingLayout.onLoadingDrawableSet");
        if (null != imageDrawable) {
			mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
			mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
		}
	}

    int mPrevIndex = -1;
	protected void onPullImpl(float scaleOfLayout) {
		float angle;
		if (mRotateDrawableWhilePulling) {
			angle = scaleOfLayout * 90f;
		} else {
			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
		}

        float max = 1.7f;
        int index = (int) (scaleOfLayout / 1f * 10);
        if (index == mPrevIndex) {
            return;
        } else {
            if (index > 10) {
                index = 10;
            }
            int res = getResources().getIdentifier(String.format("dropdown_anim_%02d",
                    index), "drawable", getContext().getPackageName());
//            Bitmap scaledBitmap = getScaledBitmap(res, index);
//            mHeaderImage.setImageBitmap(scaledBitmap);
            mHeaderImage.setImageResource(res);
            mPrevIndex = index;
        }
    }

    private Bitmap getScaledBitmap(int res, int index) {
        float p = ((float) index/10*7 + 3)/10;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
        return Bitmap.createScaledBitmap(bitmap, (int)(mHeaderImage.getWidth()*p), (int)(mHeaderImage.getHeight()*p), false);
    }

    @Override
	protected void refreshingImpl() {
        mGifAnimation = new GifAnimation(mHeaderImage, mGifRes);
        mGifAnimation.start();
    }

	@Override
	protected void resetImpl() {
        mHeaderImage.clearAnimation();
        if (mGifAnimation != null) {
            mGifAnimation.stop();
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
