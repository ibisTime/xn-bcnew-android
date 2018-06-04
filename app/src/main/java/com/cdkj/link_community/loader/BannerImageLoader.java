package com.cdkj.link_community.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        if (path.toString().indexOf("http") != -1) {
            Glide.with(context)
                    .load(path.toString())
                    .into(imageView);
        } else {

            Glide.with(context)
                    .load(MyCdConfig.QINIUURL + path.toString())
                    .into(imageView);
        }

    }
}
