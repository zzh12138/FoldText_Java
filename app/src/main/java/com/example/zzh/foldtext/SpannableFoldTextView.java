package com.example.zzh.foldtext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;


/**
 * Created by zhangzhihao on 2018/6/28 10:26.
 */

public class SpannableFoldTextView extends AppCompatTextView implements View.OnClickListener {
    private static final String TAG = "SpannableFoldTextView";
    private static final String ELLIPSIZE_END = "...";
    private static final int MAX_LINE = 4;
    private static final String EXPAND_TIP_TEXT = "收起全文";
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
    /**
     * 全文点击的span
     */
    private ExpandSpan mSpan;
    private boolean flag;
    /**
     * 展开后是否显示文字提示
     */
    private boolean isShowTipAfterExpand;

    /**
     * 是否是Span的点击
     */
    private boolean isExpandSpanClick;
    /**
     * 父view是否设置了点击事件
     */
    private boolean isParentClick;

    private OnClickListener listener;

    public SpannableFoldTextView(Context context) {
        this(context, null);
    }


    public SpannableFoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    public SpannableFoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShowMaxLine = MAX_LINE;
        mSpan = new ExpandSpan();
        if (attrs != null) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);
            mShowMaxLine = arr.getInt(R.styleable.FoldTextView_showMaxLine, MAX_LINE);
            mTipGravity = arr.getInt(R.styleable.FoldTextView_tipGravity, END);
            mTipColor = arr.getColor(R.styleable.FoldTextView_tipColor, TIP_COLOR);
            mTipClickable = arr.getBoolean(R.styleable.FoldTextView_tipClickable, false);
            mFoldText = arr.getString(R.styleable.FoldTextView_foldText);
            mExpandText = arr.getString(R.styleable.FoldTextView_expandText);
            isShowTipAfterExpand = arr.getBoolean(R.styleable.FoldTextView_showTipAfterExpand, false);
            isParentClick=arr.getBoolean(R.styleable.FoldTextView_isSetParentClick,false);
            arr.recycle();
        }
        if (TextUtils.isEmpty(mExpandText)) {
            mExpandText = EXPAND_TIP_TEXT;
        }
        if (TextUtils.isEmpty(mFoldText)) {
            mFoldText = FOLD_TIP_TEXT;
        }
    }

    @Override
    public void setText(final CharSequence text, final BufferType type) {
        if (TextUtils.isEmpty(text) || mShowMaxLine == 0) {
            super.setText(text, type);
        } else if (isExpand) {
            //文字展开
            SpannableStringBuilder spannable = new SpannableStringBuilder(mOriginalText);
            addTip(spannable, type);
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

    /**
     * 增加提示文字
     *
     * @param span
     * @param type
     */
    private void addTip(SpannableStringBuilder span, BufferType type) {
        if (!(isExpand && !isShowTipAfterExpand)) {
            //折叠或者展开并且展开后显示提示
            if (mTipGravity == END) {
                span.append("  ");
            } else {
                span.append("\n");
            }
            int length;
            if (isExpand) {
                span.append(mExpandText);
                length = mExpandText.length();
            } else {
                span.append(mFoldText);
                length = mFoldText.length();
            }
            if (mTipClickable) {
                span.setSpan(mSpan, span.length() - length, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (isParentClick) {
                    setMovementMethod(MyLinkMovementMethod.getInstance());
                    setClickable(false);
                    setFocusable(false);
                    setLongClickable(false);
                } else {
                    setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
            span.setSpan(new ForegroundColorSpan(mTipColor), span.length() - length, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        super.setText(span, type);
    }

    private void formatText(CharSequence text, final BufferType type) {
        mOriginalText = text;
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
        if (layout.getLineCount() > mShowMaxLine) {
            SpannableStringBuilder span = new SpannableStringBuilder();
            int start = layout.getLineStart(mShowMaxLine - 1);
            int end = layout.getLineVisibleEnd(mShowMaxLine - 1);
            TextPaint paint = getPaint();
            StringBuilder builder = new StringBuilder(ELLIPSIZE_END);
            if (mTipGravity == END) {
                builder.append("  ").append(mFoldText);
            }
            end -= paint.breakText(mOriginalText, start, end, false, paint.measureText(builder.toString()), null) + 1;
            CharSequence ellipsize = mOriginalText.subSequence(0, end);
            span.append(ellipsize);
            span.append(ELLIPSIZE_END);
            addTip(span, type);
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

    public void setParentClick(boolean parentClick) {
        isParentClick = parentClick;
    }

    @Override
    public void onClick(View v) {
        if (isExpandSpanClick) {
            isExpandSpanClick = false;
        } else {
            listener.onClick(v);
        }
    }

    private class ExpandSpan extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            if (mTipClickable) {
                isExpand = !isExpand;
                isExpandSpanClick = true;
                Log.d("emmm", "onClick: span click");
                setText(mOriginalText);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mTipColor);
            ds.setUnderlineText(false);
        }
    }

    /**
     * 重写，解决span跟view点击同时触发问题
     *
     * @param l
     */
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        listener = l;
        super.setOnClickListener(this);
    }
}
