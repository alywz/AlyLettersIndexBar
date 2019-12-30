package com.demo.alylettersindexbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Aly on 2019/12/30.
 * description:自定义字母索引
 */
public class LettersIndexBar extends View {
    private Paint mLetterPaint;//未选中字母的画笔
    private int mLetterColor;//未选中字母的画笔颜色
    private Paint mLetterFocusPaint;//选中字母的画笔
    private int mLetterFocusColor;//选中字母的画笔颜色
    private int letterTextSize = 14;
    private static String[] letters = {"↑","*","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private Paint.FontMetrics fontMetrics;
    private int itemheight;//单个字母的高度
    private int currentPosition = -1;//当前选中的字母位置


    public LettersIndexBar(Context context) {
        this(context, null);
    }

    public LettersIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LettersIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LettersIndexBar);
        mLetterColor = array.getColor(R.styleable.LettersIndexBar_mLetterColor, mLetterColor);
        mLetterFocusColor = array.getColor(R.styleable.LettersIndexBar_mLetterFocusColor, mLetterFocusColor);
        letterTextSize = array.getDimensionPixelSize(R.styleable.LettersIndexBar_letterTextSize, letterTextSize);
        //初始化画笔
        mLetterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLetterPaint.setColor(mLetterColor);
        mLetterPaint.setTextSize(letterTextSize);
        //初始化选中画笔
        mLetterFocusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLetterFocusPaint.setColor(mLetterFocusColor);
        mLetterFocusPaint.setTextSize(letterTextSize);
    }

    private float sp2px(int textsize) {
        float pxtv = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textsize, getResources().getDisplayMetrics());
        return pxtv;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int with = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (withMode == MeasureSpec.AT_MOST) {
            with = (int) mLetterPaint.measureText("A") + getPaddingLeft() + getPaddingRight();
        }
        fontMetrics = mLetterPaint.getFontMetrics();
        itemheight = (int) (fontMetrics.bottom - fontMetrics.top);
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (int) (getPaddingTop() + getPaddingBottom() + itemheight * letters.length);
        }
        setMeasuredDimension(with, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < letters.length; i++) {
            int with = (int) mLetterPaint.measureText(letters[i]);
            int x = getWidth() / 2 - with / 2;//字母居中
            if (currentPosition == i) {
                //画选中的字母
                canvas.drawText(letters[i], x, (i + 1) * itemheight, mLetterFocusPaint);
            } else {
                //画未选中的字母
                canvas.drawText(letters[i], x, (i + 1) * itemheight, mLetterPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //手指触摸,滑动
                int y = (int) event.getY();
                int position = y / itemheight;
                if (position == currentPosition) {
                    return false;
                } else {
                    currentPosition = position;
                }
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                if (currentPosition > (letters.length - 1)) {
                    currentPosition = letters.length - 1;
                }
                invalidate();
                Log.d("TAG", "y----->" + y + " itemheight--->" + itemheight + " currentPosition--->" + currentPosition);
                if (lettersListener != null && currentPosition != -1) {
                    lettersListener.touchLetter(letters[currentPosition], true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (lettersListener != null) {
                    lettersListener.touchLetter(letters[currentPosition], false);
                }
                currentPosition = -1;
                invalidate();
                break;
        }
        return true;
    }

    private LettersListener lettersListener;

    public void setLettersListener(LettersListener lettersListener) {
        this.lettersListener = lettersListener;
    }

    public interface LettersListener {
        void touchLetter(CharSequence charSequence, boolean isTouch);
    }
}
