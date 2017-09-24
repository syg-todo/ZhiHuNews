package com.example.lenove.zhihunews.home;

/**
 * Created by lenove on 2017/5/9.
 */

public class BannerModel {

    private String imageUrl;
    private String tips;

    public BannerModel(String imageUrl, String tips) {
        this.imageUrl = imageUrl;
        this.tips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTips() {
        return tips;
    }
}
