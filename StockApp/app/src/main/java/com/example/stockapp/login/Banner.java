package com.example.stockapp.login;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stockapp.R;
import com.github.infinitebanner.AbsBannerAdapter;
import com.github.infinitebanner.InfiniteBannerView;

public class Banner extends AbsBannerAdapter {
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    protected View makeView(InfiniteBannerView parent) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    @Override
    protected void bind(View view, int position) {
        if (position == 0) {
            ((ImageView) view).setImageResource(R.drawable.img_0);
        } else if (position == 1) {
            ((ImageView) view).setImageResource(R.drawable.img_1);
        } else if (position == 2){
            ((ImageView) view).setImageResource(R.drawable.img_2);
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.img_3);
        }
    }
}