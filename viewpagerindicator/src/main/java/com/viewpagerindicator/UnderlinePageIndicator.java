package com.viewpagerindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws a line for each page. The current page line is colored differently
 * than the unselected page lines.
 */
public class UnderlinePageIndicator extends View implements PageIndicator {
    private static final int FADE_FRAME_MS = 30;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ViewPager mViewPager;
    private int mScrollState;
    private int mCurrentPage;
    private float mPositionOffset;

    public UnderlinePageIndicator(Context context) {
        this(context, null);
    }

    public UnderlinePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiUnderlinePageIndicatorStyle);
    }

    public UnderlinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final int paddingLeft = getPaddingLeft();
        final float pageWidth = (getWidth() - paddingLeft - getPaddingRight()) / (1f * count);
        final float left = paddingLeft + pageWidth * (mCurrentPage + mPositionOffset);
        final float right = left + pageWidth;
        final float top = getPaddingTop();
        final float bottom = getHeight() - getPaddingBottom();
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        mPositionOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mPositionOffset = 0;
            invalidate();
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
    }
}