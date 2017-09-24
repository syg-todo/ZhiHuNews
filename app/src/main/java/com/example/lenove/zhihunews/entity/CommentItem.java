package com.example.lenove.zhihunews.entity;

/**
 * Created by lenove on 2017/5/8.
 */

public class CommentItem {
    private String itemIcon;
    private String itemName;
    private String itemContent;
    private int time;
    private int likes;

    public CommentItem() {

    }

    public String getItemIcon() {
        return itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
