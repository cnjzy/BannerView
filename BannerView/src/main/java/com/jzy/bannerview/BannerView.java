package com.jzy.bannerview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.jzy.bannerview.adapter.BannerAdapter;
import com.jzy.bannerview.transformer.ScaleInTransformer;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author jzy
 * created at 2018/8/31
 */
public class BannerView extends LinearLayout {
    private NoScrollViewPager mBannerVp;
    private LinearLayout mBannerDotLl;

    private BannerAdapter mBannerAdapter;
    private int prePosition = 0;

    private int mDotResId = R.drawable.banner_dot;

    private OnBannerItemClickListener mOnBannerItemClickListener = null;

    private boolean mIsDestroy = false;
    private boolean mIsAutoLoop = true;
    private long mAutoLoopDelay = 5 * 1000;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mIsAutoLoop && !mIsDestroy) {
                mBannerVp.setCurrentItem(mBannerVp.getCurrentItem() + 1, true);
                mHandler.sendEmptyMessageDelayed(1, mAutoLoopDelay);
            }
        }
    };

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initListener();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_banner_viewpager, this);

        mBannerVp = findViewById(R.id.banner_vp);
        mBannerDotLl = findViewById(R.id.banner_dot_ll);

        mBannerVp.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getContext().getResources().getDisplayMetrics()));
        mBannerVp.setOffscreenPageLimit(3);
        // 可以组合使用 new RotateDownPageTransformer(new AlphaPageTransformer(new ScaleInTransformer()));
        mBannerVp.setPageTransformer(true, new ScaleInTransformer());

        // 默认滑动速度
        setScrollerSpeed(1000);
    }

    private void initListener() {
        mBannerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mBannerDotLl.getChildCount() > 0) {
                    mBannerDotLl.getChildAt(prePosition).setEnabled(false);
                    mBannerDotLl.getChildAt(position % mBannerAdapter.getItemCount()).setEnabled(true);
                    prePosition = position % mBannerAdapter.getItemCount();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    mHandler.sendEmptyMessageDelayed(1, mAutoLoopDelay);
                } else {
                    mHandler.removeCallbacksAndMessages(null);
                }
            }
        });
    }

    private void initDots() {
        if (null != mBannerDotLl) {
            mBannerDotLl.removeAllViews();
        }
        for (int i = 0; i < mBannerAdapter.getItemCount(); i++) {
            ImageView dot = new ImageView(getContext());
            dot.setEnabled(false);
            dot.setImageResource(mDotResId);
            LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            dot.setLayoutParams(params);
            mBannerDotLl.addView(dot);
        }
        mBannerDotLl.getChildAt(0).setEnabled(true);
    }

    public BannerView bindData(List<String> imageList) {
        if (imageList == null || imageList.size() == 0) {
            return this;
        }
        mBannerAdapter = new BannerAdapter(getContext(), imageList, new OnBannerItemClickListener() {
            @Override
            public void onBannerItemClick(View view, int position) {
                if (mOnBannerItemClickListener != null) {
                    mOnBannerItemClickListener.onBannerItemClick(view, position);
                }
            }
        });
        mBannerVp.setAdapter(mBannerAdapter);
        mBannerVp.setCurrentItem(1000 - 1000 % mBannerAdapter.getItemCount(), false);
        initDots();
        mHandler.sendEmptyMessageDelayed(1, mAutoLoopDelay);
        return this;
    }

    public BannerView setDotDrawable(int dotResId) {
        if (dotResId > 0) {
            mDotResId = dotResId;
        }
        initDots();
        return this;
    }

    public BannerView setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        mBannerVp.setPageTransformer(true, pageTransformer);
        return this;
    }

    public BannerView setPageMargin(int margin) {
        mBannerVp.setPageMargin(margin);
        return this;
    }

    public BannerView setPagePadding(int pagePadding) {
        mBannerVp.setPadding(pagePadding, 0, pagePadding, 0);
        return this;
    }

    public BannerView setNoScroll(boolean scroll) {
        mBannerVp.setScanScroll(scroll);
        return this;
    }

    public BannerView setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.mOnBannerItemClickListener = onBannerItemClickListener;
        return this;
    }

    public BannerView setDotVisibility(int visibility) {
        if (mBannerDotLl != null) {
            mBannerDotLl.setVisibility(visibility);
        }
        return this;
    }

    public BannerView setIsAutoLoop(boolean isAutoLoop) {
        this.mIsAutoLoop = isAutoLoop;
        return this;
    }

    public BannerView setIsInfinite(boolean isInfinite) {
        mBannerAdapter.setIsInfinite(isInfinite);
        mBannerVp.setCurrentItem(0);
        return this;
    }

    public BannerView setScrollerSpeed(int duration) {
        /**
         * 通过反射来修改 ViewPager的mScroller属性
         */
        try {
            Class clazz = Class.forName("android.support.v4.view.ViewPager");
            Field f = clazz.getDeclaredField("mScroller");
            FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(getContext(), new LinearOutSlowInInterpolator());
            fixedSpeedScroller.setmDuration(duration);
            f.setAccessible(true);
            f.set(mBannerVp, fixedSpeedScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 必须调用，否则内存泄漏
     */
    public void onDestroy() {
        mBannerAdapter.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mIsDestroy = true;
    }

    public ViewPager getViewPager() {
        return mBannerVp;
    }


    public interface OnBannerItemClickListener {
        void onBannerItemClick(View view, int position);
    }
}
