package com.vs.giffy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by vikashign on 8/22/2018.
 */

public class Giffy extends View {

    private Context mContext;
    private InputStream mInputStream;
    private int mWidth, mHeight;
    private long start;
    private Movie gifMovie;

    public Giffy(Context context) {
        super(context);
        mContext = context;
    }

    public Giffy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Giffy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        if(attrs.getAttributeName(1).equals("background")){
            int id = Integer.parseInt(attrs.getAttributeValue(1).substring(1));
            setGiffyImageResource(id);
        }
    }


    private void init() {
        setFocusable(true);
        gifMovie = Movie.decodeStream(mInputStream);
        mHeight  = gifMovie.height();
        mWidth   = gifMovie.width();

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();

        if(start==0){
            start = now;
        }

        if(gifMovie !=null){
            int duration = gifMovie.duration();
            if(duration==0){
                duration = 5000;
            }
            int realTime = (int) ((now-start)%duration);
            gifMovie.setTime(realTime);
            gifMovie.draw(canvas,0,0);
            invalidate();
        }

    }

    public void setGiffyImageResource(int id) {
        mInputStream = mContext.getResources().openRawResource(id);
        init();
    }

    private void setGiffyImageResourceUri(Uri uri){
        try {
            mInputStream = mContext.getContentResolver().openInputStream(uri);
            init();
        } catch (FileNotFoundException e) {
            Log.e("Giffy", "File Not Found!!!");
        }
    }
}
