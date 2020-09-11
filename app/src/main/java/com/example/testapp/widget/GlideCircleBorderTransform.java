package com.example.testapp.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;


import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 文 件 名：GlideCircleBorderTransform
 * 描   述：可以图片增加圆角带边框的transform
 */
public class GlideCircleBorderTransform extends BitmapTransformation {
    private final String ID = getClass().getName();
    private Paint mBorderPaint;//边框画笔
    private float borderWidth;//边框宽度 不想要直接设置为0
    private int borderColor;//边框颜色
    private int cornerRadius;//角度大小

    private boolean topLeft;
    private boolean topRight;
    private boolean bottomRight;
    private boolean bottomLeft;


    //展示圆角的类型   0 全部显示、1左上角、2右上角、3左下角、4右下角   12左上和右上角  13左上和左下   24右上和右下角   34左下和右下角  其他组合可自己定义
    private int showCornerType;


    public GlideCircleBorderTransform(float borderWidth, int borderColor, int cornerRadius, int showCornerType) {
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.cornerRadius = cornerRadius;
        this.showCornerType = showCornerType;

        mBorderPaint = new Paint();
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setDither(true);
    }


    private Bitmap circleCrop(BitmapPool bitmapPool, Bitmap source) {

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap square = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        //画图
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        //设置 Shader
        paint.setShader(new BitmapShader(square, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float radius = size / 2f;

        //绘制圆角矩形外加边框
        drawCornerAndBoder(showCornerType, canvas, paint, result.getWidth(), result.getHeight());

        //绘制一个圆s
//        canvas.drawCircle(radius, radius, radius, paint);


        //注意：避免出现描边被屏幕边缘裁掉
        float borderRadius = radius - (borderWidth / 2);
        //画边框
        //canvas.drawCircle(radius, radius, borderRadius, mBorderPaint);

        return result;
    }


    private Path getPath(float radius, boolean topLeft, boolean topRight,
                         boolean bottomRight, boolean bottomLeft, int width, int height) {

        final Path path = new Path();
        final float[] radii = new float[8];

        if (topLeft) {
            radii[0] = radius;
            radii[1] = radius;
        }

        if (topRight) {
            radii[2] = radius;
            radii[3] = radius;
        }

        if (bottomRight) {
            radii[4] = radius;
            radii[5] = radius;
        }

        if (bottomLeft) {
            radii[6] = radius;
            radii[7] = radius;
        }
        //https://blog.csdn.net/kuaiguixs/article/details/78753149?utm_medium=distribute.pc_relevant.none-task-blog-title-4&spm=1001.2101.3001.4242
        path.addRoundRect(new RectF(borderWidth/2, borderWidth/2, width-borderWidth/2, height-borderWidth/2),
                radii, Path.Direction.CW);

        return path;
    }

    private void drawCornerAndBoder(int showCornerType, Canvas canvas, Paint paint, int width, int height) {

        RectF rectFs = new RectF(0, 0, width, height); //显示矩形时 具体的宽高 大小数值需要自己调
        canvas.drawRoundRect(rectFs, cornerRadius, cornerRadius, paint);
        switch (showCornerType) {
            //----绘制圆角矩形
            case 0:
                setShowCorner(true, true, true, true);
                break;
            case 1://左上角 这种操作其实就是覆盖了一部分之前的操作
                canvas.drawRect(0, cornerRadius, width, height, paint);
                canvas.drawRect(cornerRadius, 0, width, height, paint);
                setShowCorner(true, false, false, false);
                break;
            case 2://右上角
                canvas.drawRect(0, cornerRadius, width, height, paint);
                canvas.drawRect(0, 0, width - cornerRadius, height, paint);
                setShowCorner(false, true, false, false);
                break;
            case 3://左下角
                canvas.drawRect(cornerRadius, 0, width, height, paint);
                canvas.drawRect(0, 0, width, height - cornerRadius, paint);
                setShowCorner(false, false, false, true);
                break;
            case 4://右下角
                canvas.drawRect(0, 0, width - cornerRadius, height, paint);
                canvas.drawRect(0, 0, width, height - cornerRadius, paint);
                setShowCorner(false, false, true, false);
                break;
            case 12://左上角和右上角
                canvas.drawRect(0, cornerRadius, width, height, paint);
                setShowCorner(true, true, false, false);
                break;
            case 13://左上角和左下角
                canvas.drawRect(cornerRadius, 0, width, height, paint);
                setShowCorner(true, false, false, true);
                break;
            case 24://24右上角和右下角
                canvas.drawRect(0, 0, width - cornerRadius, height, paint);
                setShowCorner(false, true, true, false);
                break;
            case 34://34左下角和右下角
                canvas.drawRect(0, 0, width, height - cornerRadius, paint);
                setShowCorner(false, false, true, true);
                break;
            default:
                break;
        }
        Path path = getPath(cornerRadius, topLeft, topRight, bottomRight, bottomLeft, width, height);
        canvas.drawPath(path, mBorderPaint);
    }

    private void setShowCorner(boolean topLeft, boolean topRight, boolean bottomRight, boolean bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }


    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes(CHARSET));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideCircleBorderTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
//        return null;
        return circleCrop(pool, toTransform);

    }

}
