package com.example.lenove.zhihunews.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenove on 2017/5/16.
 */

public class ThemeBean {
    private ArrayList<StoriesBean> stories;
    private String image;
    private String description;
    private String background;

    public String getBackground() {
        return background;
    }

    private List<EditorBean> editors;
    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public static class EditorBean{
        private String url;
        private String bio;
        private int id;
        private String avatar;
        private String name;

        public String getUrl() {
            return url;
        }

        public String getBio() {
            return bio;
        }

        public int getId() {
            return id;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getName() {
            return name;
        }
    }

    public List<EditorBean> getEditors() {
        return editors;
    }

    public ArrayList<StoriesBean> getStories() {
        return stories;
    }

    public static class StoriesBean{
        private int type;
        private int id;
        private String title;
        private List<String> images;



        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getImages() {
            return images;
        }
    }
}
