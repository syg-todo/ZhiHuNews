package com.example.lenove.zhihunews.entity;

import java.util.List;

/**
 * Created by lenove on 2017/4/25.
 */

public class NewsBean {
    private int date;

    public int getDate() {
        return date;
    }


    private List<StoryBean> stories;
    private List<TopBean> top_stories;

    public List<TopBean> getTop_stories() {
        return top_stories;
    }

    public List<StoryBean> getStories() {
        return stories;
    }



    public static class TopBean{
        private String image;
        private String title;
        private int id;

        public String getImages() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }
    }

    public static class StoryBean{
        private List<String> images;
        private String title;
        private int id;

        public List<String> getImages() {
            return images;
        }

        public String getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }

    }
}
