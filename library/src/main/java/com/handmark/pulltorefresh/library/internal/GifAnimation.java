package com.handmark.pulltorefresh.library.internal;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * Created by ccheng on 8/4/14.
 */
public class GifAnimation implements Handler.Callback {

    public static final int DELAY_MILLIS = 200;
    private final ImageView mHeaderImage;
    private final int[] mGifReses;
    private final Handler mHandler;
    private int mIndex;
    private boolean mStart;

    public GifAnimation(ImageView headerImage, int[] gifRes) {
        mHeaderImage = headerImage;
        mGifReses = gifRes;

        mHandler = new Handler(headerImage.getContext().getMainLooper(), this);
    }

    public void start() {
        if (!mStart) {
            mStart = true;
            mIndex = 0;

            nextImage();
            mHandler.sendEmptyMessageDelayed(0, DELAY_MILLIS);
        }
    }

    private void nextImage() {
        mHeaderImage.setImageResource(mGifReses[mIndex]);
        mIndex = (mIndex + 1) % 3;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (!mStart) {
            return true;
        }

        nextImage();
        mHandler.sendEmptyMessageDelayed(0, DELAY_MILLIS);
        return true;
    }

    public void stop() {
        mStart = false;
    }
}
