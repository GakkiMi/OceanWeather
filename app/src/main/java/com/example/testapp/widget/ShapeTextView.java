package com.example.testapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.testapp.R;


/**
 * Created by Ocean on 2019/3/29.
 */

public class ShapeTextView extends AppCompatTextView {


    GradientDrawable gd;

    private Paint mBgPaint;
    private Paint mStokePaint;

    private int mShape;


    //圆角大小
    private float cornerRadius;
    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;
    //背景颜色
    private int normalBgColor;
    private int pressBgColor;
    //字体颜色
    private int normalTextColor;
    private int pressTextColor;
    //描边颜色及宽度
    private int strokeNormalColor;
    private int strokePressColor;
    private int strokeWidth;
    //虚线空隙及宽度
    private float dashGap;
    private float dashWidth;


    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);

        mShape = typedArray.getInt(R.styleable.ShapeTextView_shapeType, 0);//默认为矩形

        cornerRadius = typedArray.getDimension(R.styleable.ShapeTextView_cornerRadius, 0);
        topLeftRadius = typedArray.getDimension(R.styleable.ShapeTextView_topLeftCornerRadius, 0);
        topRightRadius = typedArray.getDimension(R.styleable.ShapeTextView_topRightCornerRadius, 0);
        bottomLeftRadius = typedArray.getDimension(R.styleable.ShapeTextView_bottomLeftCornerRadius, 0);
        bottomRightRadius = typedArray.getDimension(R.styleable.ShapeTextView_bottomRightCornerRadius, 0);

        pressBgColor = typedArray.getColor(R.styleable.ShapeTextView_PressBgColor, 0);
        normalBgColor = typedArray.getColor(R.styleable.ShapeTextView_NormalBgColor, 0);

        pressTextColor = typedArray.getColor(R.styleable.ShapeTextView_PressTextColor, 0);
        normalTextColor = typedArray.getColor(R.styleable.ShapeTextView_NormalTextColor, 0);

        strokeNormalColor = typedArray.getColor(R.styleable.ShapeTextView_StrokeNormalColor, 0);
        strokePressColor = typedArray.getColor(R.styleable.ShapeTextView_StrokePressColor, 0);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_StrokeWidth, 0);

        dashGap = typedArray.getDimension(R.styleable.ShapeTextView_dashGap, 0);
        dashWidth = typedArray.getDimension(R.styleable.ShapeTextView_dashWidth, 0);

        typedArray.recycle();

        init();
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        gd = new GradientDrawable();
        gd.setShape(mShape);
        if (mShape == 0) {
            if (cornerRadius == 0) {//四个圆角不一致
                //分别表示 左上 右上 右下 左下
                gd.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
            } else {
                //四个圆角一致
                gd.setCornerRadius(cornerRadius);
            }
        }


        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);


        mStokePaint = new Paint();
        mStokePaint.setAntiAlias(true);
        mStokePaint.setStrokeWidth(strokeWidth);
        mStokePaint.setStyle(Paint.Style.STROKE);

    }




    @Override
    protected void onDraw(Canvas canvas) {
//        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
//        if (bgColor != 0) {
//            mBgPaint.setColor(switchBgColor());
//            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, mBgPaint);
//        }
//        if (strokeWidth != 0) {
//            mStokePaint.setColor(switchStrokeColor());
//            RectF rectStroke = new RectF(0, 0, getWidth(), getHeight());
//            canvas.drawRoundRect(rectStroke, cornerRadius, cornerRadius, mStokePaint);
//        }
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }else if(drawableRight != null){
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableRight.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate(-(getWidth() - bodyWidth) / 2, 0);
            }
        }


        int bgColor = switchBgColor();
        int strokeColor = switchStrokeColor();
        if (bgColor != 0) {
            gd.setColor(bgColor);
        }
        if (strokeWidth != 0) {
            if (dashWidth != 0) {//画虚线
                gd.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
            } else {//画实线
                gd.setStroke(strokeWidth, strokeColor);
            }
        } else {
            gd.setStroke(0, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(gd);
        }

        super.onDraw(canvas);
        //设置字体颜色
        int textColor = switchTextColor();
        if (textColor != 0) {
            setTextColor(switchTextColor());
        }
    }

    /**
     * 判断背景颜色
     *
     * @return
     */
    private int switchBgColor() {
        // 按下状态
        if (isPressed()) {
            return pressBgColor == 0 ? normalBgColor : pressBgColor;
        }
        return normalBgColor;
    }


    /**
     * 判断背景颜色
     *
     * @return
     */
    private int switchStrokeColor() {
        // 按下状态
        if (isPressed()) {
            return strokePressColor == 0 ? strokeNormalColor : strokePressColor;
        }
        return strokeNormalColor;
    }


    /**
     * 判断字体颜色
     *
     * @return
     */
    private int switchTextColor() {
        // 按下状态
        if (isPressed()) {
            return pressTextColor == 0 ? normalTextColor : pressTextColor;
        }
        return normalTextColor;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸的时候重绘
        postInvalidate();
        return super.onTouchEvent(event);
    }


    public void setStrokeNormalColor(int color) {
        strokeNormalColor = color;
        postInvalidate();
    }


    public void setBgNormalColor(int color) {
        normalBgColor = color;
        postInvalidate();
    }

}
