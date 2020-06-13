package com.example.android_newsky.navigation;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private String profile ;
    private String profId;
    private String titleStr ;
    private String descStr ;


    public void setProfile(String prof) { profile = prof; }
    public void setProfId(String Id) { profId = Id; }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getProfile() {
        return this.profile ;
    }
    public String getProfId() { return this.profId; }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}