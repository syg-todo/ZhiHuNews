package com.example.lenove.zhihunews.entity;

import java.util.List;

/**
 * Created by lenove on 2017/5/7.
 */

public class CommentBean {
    private List<EachBean> comments;

    public List<EachBean> getComments() {
        return comments;
    }

    public static class EachBean{
        private String author;
        private String content;
        private int time;
        private String avatar;
        private int id;
        private int likes;

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public int getTime() {
            return time;
        }

        public int getId() {
            return id;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getLikes() {
            return likes;
        }
    }


}
