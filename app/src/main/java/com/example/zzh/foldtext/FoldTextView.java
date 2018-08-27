package com.example.zzh.foldtext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;


/**
 * Created by zhangzhihao on 2018/6/28 10:26.
 * 折叠textView
 */

public class FoldTextView extends AppCompatTextView {
    private static final String TAG = "FoldTextView";
    private static final String ELLIPSIZE_END = "...";
    private static final int MAX_LINE = 4;
    private static final String EXPAND_TIP_TEXT = "  收起全文";
    private static final String FOLD_TIP_TEXT = "全文";
    private static final int TIP_COLOR = 0xFFFFFFFF;
    /**
     * 全文显示的位置
     */
    private static final int END = 0;
    private int mShowMaxLine;
    /**
     * 折叠文本
     */
    private String mFoldText;
    /**
     * 展开文本
     */
    private String mExpandText;
    /**
     * 原始文本
     */
    private CharSequence mOriginalText;
    /**
     * 是否展开
     */
    private boolean isExpand;
    /**
     * 全文显示的位置
     */
    private int mTipGravity;
    /**
     * 全文文字的颜色
     */
    private int mTipColor;
    /**
     * 全文是否可点击
     */
    private boolean mTipClickable;
    private boolean flag;
    private Paint mPaint;

    /**
     * 展开后是否显示文字提示
     */
    private boolean isShowTipAfterExpand;


    /**
     * 提示文字坐标
     */
    float minX;
    float maxX;
    float minY;
    float maxY;
    /**
     * 收起全文不在一行显示时
     */
    float middleY;
    /**
     * 原始文本的行数
     */
    int originalLineCount;

    /**
     * 点击时间
     */
    private long clickTime;
    /**
     * 是否超过最大行数
     */
    private boolean isOverMaxLine;
    private onTipClickListener onTipClickListener;


    public FoldTextView(Context context) {
        this(context, null);
    }


    public FoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShowMaxLine = MAX_LINE;
        if (attrs != null) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);
            mShowMaxLine = arr.getInt(R.styleable.FoldTextView_showMaxLine, MAX_LINE);
            mTipGravity = arr.getInt(R.styleable.FoldTextView_tipGravity, END);
            mTipColor = arr.getColor(R.styleable.FoldTextView_tipColor, TIP_COLOR);
            mTipClickable = arr.getBoolean(R.styleable.FoldTextView_tipClickable, false);
            mFoldText = arr.getString(R.styleable.FoldTextView_foldText);
            mExpandText = arr.getString(R.styleable.FoldTextView_expandText);
            isShowTipAfterExpand = arr.getBoolean(R.styleable.FoldTextView_showTipAfterExpand, false);
            arr.recycle();
        }
        if (TextUtils.isEmpty(mExpandText)) {
            mExpandText = EXPAND_TIP_TEXT;
        }
        if (TextUtils.isEmpty(mFoldText)) {
            mFoldText = FOLD_TIP_TEXT;
        }
        if (mTipGravity == END) {
            mFoldText = "  ".concat(mFoldText);
        }
        mPaint = new Paint();
        mPaint.setTextSize(getTextSize());
        mPaint.setColor(mTipColor);
    }

    @Override
    public void setText(final CharSequence text, final BufferType type) {
        mOriginalText = text;
        if (TextUtils.isEmpty(text) || mShowMaxLine == 0) {
            super.setText(text, type);
        } else if (isExpand) {
            //文字展开
            SpannableStringBuilder spannable = new SpannableStringBuilder(mOriginalText);
            if (isShowTipAfterExpand) {
                spannable.append(mExpandText);
                spannable.setSpan(new ForegroundColorSpan(mTipColor), spannable.length() - mExpandText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            super.setText(spannable, type);
            int mLineCount = getLineCount();
            Layout layout = getLayout();
            minX = getPaddingLeft() + layout.getPrimaryHorizontal(spannable.toString().lastIndexOf(mExpandText.charAt(0)) - 1);
            maxX = getPaddingLeft() + layout.getSecondaryHorizontal(spannable.toString().lastIndexOf(mExpandText.charAt(mExpandText.length() - 1)) + 1);
            Rect bound = new Rect();
            if (mLineCount > originalLineCount) {
                //不在同一行
                layout.getLineBounds(originalLineCount - 1, bound);
                minY = getPaddingTop() + bound.top;
                middleY = minY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
                maxY = middleY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
            } else {
                //同一行
                layout.getLineBounds(originalLineCount - 1, bound);
                minY = getPaddingTop() + bound.top;
                maxY = minY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
            }
        } else {
            if (!flag) {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        flag = true;
                        formatText(text, type);
                        return true;
                    }
                });
            } else {
                formatText(text, type);
            }
        }
    }

    private void formatText(CharSequence text, final BufferType type) {
        Layout layout = getLayout();
        if (layout == null || !layout.getText().equals(mOriginalText)) {
            super.setText(mOriginalText, type);
            layout = getLayout();
        }
        if (layout == null) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    translateText(getLayout(), type);
                }
            });
        } else {
            translateText(layout, type);
        }
    }

    private void translateText(Layout layout, BufferType type) {
        originalLineCount = layout.getLineCount();
        if (layout.getLineCount() > mShowMaxLine) {
            isOverMaxLine = true;
            SpannableStringBuilder span = new SpannableStringBuilder();
            int start = layout.getLineStart(mShowMaxLine - 1);
            int end = layout.getLineEnd(mShowMaxLine - 1);
            if (mTipGravity == END) {
                TextPaint paint = getPaint();
                StringBuilder builder = new StringBuilder(ELLIPSIZE_END).append(mFoldText);
                end -= paint.breakText(mOriginalText, start, end, false, paint.measureText(builder.toString()), null);
                float x = getWidth() - getPaddingLeft() - getPaddingRight() - getTextWidth(mFoldText);
                while (layout.getPrimaryHorizontal(end - 1) + getTextWidth(mOriginalText.subSequence(end - 1, end).toString()) < x) {
                    end++;
                }
                end--;
            } else {
                end--;
            }
            CharSequence ellipsize = mOriginalText.subSequence(0, end);
            span.append(ellipsize);
            span.append(ELLIPSIZE_END);
            if (mTipGravity != END) {
                span.append("\n");
            }
            super.setText(span, type);
        } else {
            isOverMaxLine = false;
        }
    }

    public void setShowMaxLine(int mShowMaxLine) {
        this.mShowMaxLine = mShowMaxLine;
    }

    public void setFoldText(String mFoldText) {
        this.mFoldText = mFoldText;
    }

    public void setExpandText(String mExpandText) {
        this.mExpandText = mExpandText;
    }

    public void setTipGravity(int mTipGravity) {
        this.mTipGravity = mTipGravity;
    }

    public void setTipColor(int mTipColor) {
        this.mTipColor = mTipColor;
    }

    public void setTipClickable(boolean mTipClickable) {
        this.mTipClickable = mTipClickable;
    }


    public void setShowTipAfterExpand(boolean showTipAfterExpand) {
        isShowTipAfterExpand = showTipAfterExpand;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isOverMaxLine && !isExpand) {
            //折叠
            if (mTipGravity == END) {
                minX = getWidth() - getPaddingLeft() - getPaddingRight() - getTextWidth(mFoldText);
                maxX = getWidth() - getPaddingLeft() - getPaddingRight();
                minY = getHeight() - (getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent) - getPaddingBottom();
                maxY = getHeight() - getPaddingBottom();
                canvas.drawText(mFoldText, minX,
                        getHeight() - getPaint().getFontMetrics().descent - getPaddingBottom(), mPaint);
            } else {
                minX = getPaddingLeft();
                maxX = minX + getTextWidth(mFoldText);
                minY = getHeight() - (getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent) - getPaddingBottom();
                maxY = getHeight() - getPaddingBottom();
                canvas.drawText(mFoldText, minX, getHeight() - getPaint().getFontMetrics().descent - getPaddingBottom(), mPaint);
            }
        }
    }

    private float getTextWidth(String text) {
        Paint paint = getPaint();
        return paint.measureText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTipClickable) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    clickTime = System.currentTimeMillis();
                    if (!isClickable()) {
                        if (isInRange(event.getX(), event.getY())) {
                            return true;
                        }
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    long delTime = System.currentTimeMillis() - clickTime;
                    clickTime = 0L;
                    if (delTime < ViewConfiguration.getTapTimeout() && isInRange(event.getX(), event.getY())) {
                        isExpand = !isExpand;
                        setText(mOriginalText);
                        if (onTipClickListener != null) {
                            onTipClickListener.onTipClick(isExpand);
                        }
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }


    private boolean isInRange(float x, float y) {
        if (minX < maxX) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY;
        } else {
            return x <= maxX && y >= middleY && y <= maxY || x >= minX && y >= minY && y <= middleY;
        }
    }

    public FoldTextView setExpand(boolean expand) {
        isExpand = expand;
        return this;
    }


    public FoldTextView setOnTipClickListener(FoldTextView.onTipClickListener onTipClickListener) {
        this.onTipClickListener = onTipClickListener;
        return this;
    }

    public interface onTipClickListener {
        void onTipClick(boolean flag);
    }
}
