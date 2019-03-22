package com.jzy.bannerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jzy.bannerview.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jzy
 * created at 2018/8/31
 */
public class BannerAdapter extends PagerAdapter {
    private List<String> mImageUrlList = new ArrayList<>();
    private Context mContext;
    private BannerView.OnBannerItemClickListener mOnBannerItemClickListener;
    private boolean mIsInfinite = true;

    private List<ImageView> mImageViewList = new ArrayList<>();

    public BannerAdapter(Context context, List<String> imageUrlList, BannerView.OnBannerItemClickListener onBannerItemClickListener) {
        this.mContext = context;
        this.mImageUrlList.addAll(imageUrlList);
        this.mOnBannerItemClickListener = onBannerItemClickListener;
        initViews();
    }

    private void initViews() {
        for (int i = 0; i < 10; i++) {
            mImageViewList.add(new ImageView(mContext));
        }
    }

    @Override
    public int getCount() {
        return mIsInfinite ? Integer.MAX_VALUE : getItemCount();
    }

    public int getItemCount() {
        return mImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView view = mImageViewList.get(position % mImageViewList.size());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(view.getContext()).load(mImageUrlList.get(position % mImageUrlList.size())).into(view);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBannerItemClickListener != null) {
                    mOnBannerItemClickListener.onBannerItemClick(v, position % getItemCount());
                }
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViewList.get(position % mImageViewList.size()));
    }

    public void setIsInfinite(boolean isInfinite) {
        this.mIsInfinite = isInfinite;
        notifyDataSetChanged();
    }

    public void onDestroy() {
        mImageViewList.clear();
    }
}
