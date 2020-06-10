package com.example.android_newsky.navigation;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private int profile ;
    private String titleStr ;
    private String descStr ;

    public void setProfile(int prof) { profile = prof; }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public int getProfile() {
        return this.profile ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}