package com.hex.express.iwant.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
/**
 * 自定义圆形头像
 * @author Eric
 *
 */
public class CircleImageView extends View {

    private Paint mPaint;
    private RectF mBound;
    private Bitmap mImageBitmap;
    private float mRadius;

    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBound = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int vw,vh;
        vw = vh =0;

        int iw,ih;
        if(mImageBitmap==null){
            iw = ih = 0;
        }else{
            iw = mImageBitmap.getWidth();
            ih = mImageBitmap.getHeight();
        }

        int size = Math.min(iw,ih);

        setMeasuredDimension(size,size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w!=oldw||h!=oldh){
            /**
             * 设置边界，剧中显示
             */
            int iw,ih;
            if(mImageBitmap==null){
                iw = ih = 0;
            }else{
                iw = mImageBitmap.getWidth();
                ih = mImageBitmap.getHeight();
            }

            int size = Math.min(getHeight(),getWidth());

            mBound.set(0,0,size,size);
            mRadius = size/2;

            if(mPaint.getShader()!=null){
                Matrix m = new Matrix();
                if(iw>ih){
                    m.setTranslate((iw-ih)/2,0);
                }else{
                    m.setTranslate(0,(ih-iw)/-2);
                }
                mPaint.getShader().setLocalMatrix(m);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mImageBitmap!=null) {
            canvas.drawRoundRect(mBound, mRadius, mRadius, mPaint);
        }
    }

    /**
     * 由图片决定View的大小
     * @param bitmap
     */
    public void setImageBitmap(Bitmap bitmap){
        if(bitmap!=mImageBitmap){
            mImageBitmap = bitmap;
            if(bitmap!=null) {
                BitmapShader bs = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                mPaint.setShader(bs);
            }else{
                mPaint.setShader(null);
            }

            requestLayout();
        }
    }
}
